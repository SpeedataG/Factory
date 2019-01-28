package com.spdata.factory;

import android.hardware.SerialManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.serialport.SerialPortSpd;
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
 * Created by lenovo_pc on 2016/9/12.
 */
public class OutGpsS510Act extends FragActBase implements View.OnClickListener {

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


    int temp1 = 0;
    int fd = 0;
    private Timer timer;
    private static final int TIME_TO_READDATA = 500;
    ReadTimerTask readTimerTask;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_expan_gps));
    }


    private SerialManager mSerialManager = null;
    private static final String SERIAL_SERVICE = "serial";
    private SerialPortSpd mSerialPort;
    private byte[] tmpbuf = new byte[1024];
    private static final int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_gps_s510);
        initView();
        initTitlebar();
        gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
        gpio.PowerOnDevice71();
        gpio.PowerOnDevice72();
        gpio.PowerOnDevice73();
        gpio.PowerOnDevice87();
        mSerialPort = new SerialPortSpd();
//        timer = new Timer();
//        readTimerTask = new ReadTimerTask();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    byte[] temp = (byte[]) msg.obj;
                    if (temp == null) {
                        titlebar.setAttrs(getResources().getString(R.string.out_gps_msg));
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
                try {
                    mSerialPort.OpenSerial("/dev/ttyMT2", 9600);
                    fd = mSerialPort.getFd();
                    timer = new Timer();
                    readTimerTask = new ReadTimerTask();
                    timer.schedule(readTimerTask, 10, TIME_TO_READDATA);
                } catch (IOException e) {
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
        gpio.PowerOffDevice71();
        gpio.PowerOffDevice72();
        gpio.PowerOffDevice73();
        gpio.PowerOffDevice87();
        super.onDestroy();
    }
}

