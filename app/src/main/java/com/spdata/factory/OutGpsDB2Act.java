package com.spdata.factory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.serialport.SerialPort;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.IOException;

import common.base.act.FragActBase;
import common.utils.DataConversionUtils;
import common.utils.DeviceControl;

/**
 * Created by lenovo_pc on 2016/9/28.
 */
public class OutGpsDB2Act extends FragActBase implements View.OnClickListener {

    private DeviceControl gpio;
    private SerialPort mSerialPort;
    private ReadTask readTask;
    private CustomTitlebar titlebar;
    /**
     * 读GPS数据
     */
    private TextView textView2;
    private TextView tvGps;
    private EditText edvInfors;
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
        titlebar.setAttrs("外置GPS");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_outgpsdb2);
        initView();
        initTitlebar();
        try {
            mSerialPort = new SerialPort();
            mSerialPort.OpenSerial("/dev/ttyMT3", 9600);
            gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
        } catch (IOException e) {
            e.printStackTrace();
        }
        gpio.PowerOnDevice71();
        gpio.PowerOnDevice70();
        gpio.PowerOnDevice64();
        gpio.PowerOnDevice99();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] temp = (byte[]) msg.obj;
            String s = DataConversionUtils.byteArrayToAscii(temp);
            tvGps.setText(s);
        }
    };

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        textView2 = (TextView) findViewById(R.id.textView2);
        tvGps = (TextView) findViewById(R.id.tv_gps);
        edvInfors = (EditText) findViewById(R.id.edv_infors);
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
                readTask = new ReadTask();
                readTask.start();
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

    private class ReadTask extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    byte[] temp1 = mSerialPort.ReadSerial(mSerialPort.getFd(), 1024);
                    if (temp1 != null) {
                        Message msg = new Message();
                        msg.obj = temp1;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSerialPort.CloseSerial(mSerialPort.getFd());
        gpio.PowerOffDevice71();
        gpio.PowerOffDevice64();
        gpio.PowerOffDevice70();
        gpio.PowerOffDevice99();
        if (readTask != null) {
            readTask.interrupt();
        }
        finish();

    }
}
