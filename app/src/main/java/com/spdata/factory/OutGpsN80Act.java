package com.spdata.factory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.serialport.SerialPortSpd;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import common.base.act.FragActBase;
import common.utils.DataConversionUtils;
import common.utils.DeviceControl;

import static android.serialport.SerialPortSpd.SERIAL_TTYMT1;

/**
 * Created by lenovo-pc on 2017/6/14.
 */
public class OutGpsN80Act extends FragActBase implements View.OnClickListener {
    private Threads threads;
    private CustomTitlebar titlebar;
    /**
     * GPS数据:
     */
    private TextView textView2;
    private EditText edvInfors;
    private TextView tvGps;
    /**
     * 开始测试
     */
    private Button btnStart;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_expan_gps));
    }


    private SerialPortSpd mSerialPort;
    DeviceControl deviceControl;
    private static final byte senCmd[] = {(byte) 0xB5, 0x62, 0x06, 0x17, 0x0C, 0x00, 0x00
            , 0x41, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x6C, 0x3E};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_n80_outgps_layout);
        initView();
        initTitlebar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mSerialPort = new SerialPortSpd();
            deviceControl = new DeviceControl("/sys/class/misc/mtgpio/pin");
            deviceControl.PowerOnDevice("63");
            deviceControl.PowerOnDevice("99");
            SystemClock.sleep(200);
            mSerialPort.OpenSerial(SERIAL_TTYMT1, 9600);
            SystemClock.sleep(50);

            threads = new Threads();
            threads.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String datas = DataConversionUtils.byteArrayToString((byte[]) msg.obj);
            tvGps.setText(datas);
//            if (datas.indexOf("b562050102000617254e") >= 0) {
            showToast(getResources().getString(R.string.btn_success));
            setXml(App.KEY_GPS_OUT, App.KEY_FINISH);
            finish();
//            } else {
//                showToast("失败");
//                setXml(App.KEY_GPS_OUT, App.KEY_UNFINISH);
//                finish();
//            }


        }
    };

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        textView2 = (TextView) findViewById(R.id.textView2);
        edvInfors = (EditText) findViewById(R.id.edv_infors);
        tvGps = (TextView) findViewById(R.id.tv_gps);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);
        btnPass = (Button) findViewById(R.id.btn_pass);
        btnPass.setOnClickListener(this);
        btnNotPass = (Button) findViewById(R.id.btn_not_pass);
        btnNotPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_start:
                mSerialPort.WriteSerialByte(mSerialPort.getFd(), senCmd);
                SystemClock.sleep(200);
                try {
                    byte data[] = mSerialPort.ReadSerial(mSerialPort.getFd(), 1024);
                    if (data != null) {
                        String datas = DataConversionUtils.byteArrayToString(data);


                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_pass:
                setXml(App.KEY_GPS_OUT, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_GPS_OUT, App.KEY_UNFINISH);
                finish();
                break;
        }
    }

    class Threads extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                mSerialPort.WriteSerialByte(mSerialPort.getFd(), senCmd);
//                SystemClock.sleep(400);
                byte data[] = new byte[0];
                try {
                    data = mSerialPort.ReadSerial(mSerialPort.getFd(), 1024);
                    if (data != null) {
                        handler.sendMessage(handler.obtainMessage(0, data));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                SystemClock.sleep(40);
            }
        }
    }

    ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deviceControl.PowerOffDevice("63");
        deviceControl.PowerOffDevice("99");
        mSerialPort.CloseSerial(mSerialPort.getFd());
        if (threads != null) {
            threads.interrupt();
            threads = null;
        }
    }
}
