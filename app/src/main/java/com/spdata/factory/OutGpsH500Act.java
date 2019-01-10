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
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.utils.DataConversionUtils;
import common.utils.DeviceControl;

/**
 * Created by lenovo_pc on 2016/10/11.
 */
public class OutGpsH500Act extends FragActBase implements View.OnClickListener {

    private DeviceControl gpio;
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


    int fd = 0;
    private Timer timer;
    private static final int TIME_TO_READDATA = 500;
    ReadTimerTask readTimerTask;
    private SerialPort mSerialPort;


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("外置GPS");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_gps_s510);
        initView();
        initTitlebar();
        try {
            gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
            gpio.PowerOnDevice131();
            mSerialPort = new SerialPort();
            mSerialPort.OpenSerial("/dev/ttyMT3", 9600);
            fd = mSerialPort.getFd();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        timer.cancel();
                        readTimerTask.cancel();
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
//        timer = new Timer();
//        readTimerTask = new ReadTimerTask();
//        timer.schedule(readTimerTask, 10, TIME_TO_READDATA);
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

    private class ReadTimerTask extends TimerTask {
        @Override
        public void run() {
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
        gpio.PowerOffDevice131();
        super.onDestroy();
    }
}
