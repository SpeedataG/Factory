package com.spdata.factory;

import android.hardware.SerialManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.serialport.SerialPort;
import android.util.Log;
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
 * Created by lenovo_pc on 2016/8/25.
 */
public class OutGps_x300Act extends FragActBase implements View.OnClickListener {


    // 切换波特率到57600
    public String sendstring = "0200640d000000030001000204000600008103";
    private DeviceControl gpio;
    private CustomTitlebar titlebar;
    /**
     * 读GPS数据
     */
    private TextView textView2;
    private TextView tv_gps;
    private EditText edv_infors;
    /**
     * 切换测试
     */
    private Button btn_zh;
    /**
     * X2测试
     */
    private Button btn_x2;
    /**
     * X5测试
     */
    private Button btn_x5;
    /**
     * 成功
     */
    private Button btn_pass;
    /**
     * 失败
     */
    private Button btn_not_pass;



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
    private SerialPort mSerialPort;
    private byte[] tmpbuf = new byte[1024];
    private static final int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_gps);
        initView();
        initTitlebar();
        gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
        gpio.PowerOnDevice63();
        gpio.PowerOnDevice98();
        mSerialPort = new SerialPort();
//        mSerialManager = (SerialManager) getSystemService(SERIAL_SERVICE);
        btn_x5.setEnabled(false);
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        btn_x2.setEnabled(true);
//                        btn_x2.callOnClick();
//                    }
//                });
//            }
//        }, 1000 * 30);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    byte[] temp = (byte[]) msg.obj;
                    if (temp == null) {
                        titlebar.setAttrs(getResources().getString(R.string.out_gps_x300));
                    } else {
                        String s = DataConversionUtils.byteArrayToAscii(temp);

                        tv_gps.append(s);
                        timer.cancel();
                        readTimerTask.cancel();
//                        edv_infors.setText(DataConversionUtils.byteArrayToAscii(temp));
                    }
//                    edv_infors.append("\n");
                    break;

            }
        }
    };

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        textView2 = (TextView) findViewById(R.id.textView2);
        tv_gps = (TextView) findViewById(R.id.tv_gps);
        edv_infors = (EditText) findViewById(R.id.edv_infors);
        btn_zh = (Button) findViewById(R.id.btn_zh);
        btn_zh.setOnClickListener(this);
        btn_x2 = (Button) findViewById(R.id.btn_x2);
        btn_x2.setOnClickListener(this);
        btn_x5 = (Button) findViewById(R.id.btn_x5);
        btn_x5.setOnClickListener(this);
        btn_pass = (Button) findViewById(R.id.btn_pass);
        btn_pass.setOnClickListener(this);
        btn_not_pass = (Button) findViewById(R.id.btn_not_pass);
        btn_not_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_zh:
                break;
            case R.id.btn_x2:
                try {
                    mSerialPort.OpenSerial("/dev/ttyMT1", 57600);
                    fd = mSerialPort.getFd();
                    timer = new Timer();
                    readTimerTask = new ReadTimerTask();
                    timer.schedule(readTimerTask, 10, TIME_TO_READDATA);
//            ReadThread readThread = new ReadThread();
//            readThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
                //以默认38400波特率打开串口
//                    mSerialPort = mSerialManager.openSerialPort("/dev/ttyMT1", 57600, 8, 1, 0);
//                    SystemClock.sleep(10);
//                    //指令切换到57600
//                    mSerialPort.write_byte(DataConversionUtils.HexString2Bytes(sendstring), 1024);
//                    SystemClock.sleep(10);
//                    //重新以57600波特率打开串口
//                    mSerialPort = mSerialManager.openSerialPort("/dev/ttyMT1", 57600, 8, 1, 0);
//                    while (temp1 < 10 || temp1 <= 0) {
//                        temp1 = mSerialPort.read_byte(tmpbuf, 1024);
//                        if (temp1 > 0) {
//                            byte[] byt = new byte[temp1];
//                            System.arraycopy(tmpbuf, 0, byt, 0, temp1);
//                            Message msg = new Message();
//                            msg.obj = byt;
//                            handler.sendMessage(msg);
//                            return;
//                        }
//                        byte[] temp1 = mSerialPort.ReadSerial(fd, 1024);
//                        if (temp1 != null) {
//                            Message msg = new Message();
//                            msg.obj = temp1;
//                            handler.sendMessage(msg);
//                            return;
//                        }
//                    }
//                    Message msg = new Message();
//                    msg.obj = null;
//                    handler.sendMessage(msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
                break;
            case R.id.btn_x5:
                break;
            case R.id.btn_pass:  setXml(App.KEY_GPS_OUT, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:  setXml(App.KEY_GPS_OUT, App.KEY_UNFINISH);
                finish();
                break;
        }
    }

    private class ReadTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                Log.i("ss", "sss");
                byte[] temp1 = mSerialPort.ReadSerial(fd, 1024);
                if (temp1 != null) {
                    Message msg = new Message();
                    msg.obj = temp1;
                    handler.sendMessage(msg);
//                    edv_infors.append("\n");
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
        gpio.PowerOffDevice63();
        gpio.PowerOffDevice98();
        super.onDestroy();
    }
}
