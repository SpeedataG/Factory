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
 * Created by suntianwei on 2016/11/25.
 */
public class OutGpsN55Act extends FragActBase implements View.OnClickListener {
    private DeviceControl gpio;
    int fd = 0;
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


    private SerialPort mSerialPort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_outgpsn55);
        initView();
        initTitlebar();
        mSerialPort = new SerialPort();
        try {
            gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
            gpio.PowerOnDevice("120");
            gpio.PowerOnDevice("73");
            gpio.PowerOnDevice("94");
            gpio.PowerOnDevice("119");
            mSerialPort.OpenSerial("/dev/ttyMT1", 57600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fd = mSerialPort.getFd();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    byte[] temp = (byte[]) msg.obj;
                    if (temp == null) {
                        titlebar.setAttrs("测试失败！");
                    } else {
                        String s = DataConversionUtils.byteArrayToAscii(temp);
                        tvGps.setText(s);
                    }
                    break;
            }
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
                ReadThread readThread = new ReadThread();
                readThread.start();
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


    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                byte[] temp1 = mSerialPort.ReadSerial(fd, 1024);
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

    @Override
    protected void onDestroy() {
        mSerialPort.CloseSerial(fd);
        finish();
        gpio.PowerOffDevice("120");
        gpio.PowerOffDevice("73");
        gpio.PowerOffDevice("94");
        gpio.PowerOffDevice("119");
        super.onDestroy();
    }
}
