package com.spdata.factory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.serialport.SerialPortSpd;
import android.south.SDKMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.utils.DataConversionUtils;
import common.utils.DeviceControl;

/**
 * Created by lenovo_pc on 2016/8/25.
 */
public class OutGpsAct extends FragActBase implements View.OnClickListener {


    // 切换波特率到57600
    public String sendstring = "0200640d000000030001000204000600008103";
    public String sendstring2 = "0200646600000003000100000D706F7765725F75700F0B1D0A1C07080600030000000000070826000300000000000708120003000000000007080D0003000000000007080800030" +
            "0000000000708280003000000000007080C0003000000000007082900000000000000E603";
    private String Change = "02006F006F03";
    private DeviceControl gpio;
    private ReadThread readThread;
    private String[] poewro = {"63", "98"};
    private static final String TAG = "x5";
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
    ReadTimerTask readTimerTask;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_expan_gps));
    }


    private SerialPortSpd mSerialPort;
    private SDKMethod gpsnet_device;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_gps);
        initView();
        initTitlebar();
        gpsnet_device = new SDKMethod();
        gpsnet_device.setExtGps(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getgpio();
        gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
        gpio.PowerOnDevice63();
        gpio.PowerOnDevice98();
        mSerialPort = new SerialPortSpd();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    byte[] temp = (byte[]) msg.obj;
                    String s = DataConversionUtils.byteArrayToAscii(temp);
                    tv_gps.setText(s);
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
                try {
                    if (mSerialPort != null) {
                        mSerialPort.CloseSerial(fd);
                    }
                    mSerialPort.OpenSerial("/dev/ttyMT0", 38400);
                    fd = mSerialPort.getFd();
                    mSerialPort.WriteSerialByte(fd, DataConversionUtils.HexString2Bytes(Change));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                readThread = new ReadThread();
                readThread.start();
                break;
            case R.id.btn_x2:
                try {
                    if (mSerialPort != null) {
                        mSerialPort.CloseSerial(fd);
                    }
                    mSerialPort.OpenSerial("/dev/ttyMT1", 9600);
                    fd = mSerialPort.getFd();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                readThread = new ReadThread();
                readThread.start();
                break;
            case R.id.btn_x5:
                try {
                    if (mSerialPort != null) {
                        mSerialPort.CloseSerial(fd);
                    }
                    mSerialPort.OpenSerial("/dev/ttyMT1", 38400);
                    fd = mSerialPort.getFd();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                timer = new Timer();
                readTimerTask = new ReadTimerTask();
                timer.schedule(readTimerTask, 31000, 1000);
                x5ReadThread mReadThread = new x5ReadThread();
                mReadThread.start();
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
            if (!isInterrupted()) {
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
    }

    private class ReadTimerTask extends TimerTask {
        @Override
        public void run() {
            //以默认38400波特率打开串口
            try {
                mSerialPort.OpenSerial("/dev/ttyMT1", 38400);
                Thread.sleep(5);
                mSerialPort.WriteSerialByte(fd, DataConversionUtils.HexString2Bytes(sendstring));
                Thread.sleep(10);
                Log.e(TAG, "step1-2-----------------------------------");
                mSerialPort.CloseSerial(mSerialPort.getFd());
                Thread.sleep(5);
                mSerialPort.OpenSerial("/dev/ttyMT1", 57600);
                Thread.sleep(5);
                mSerialPort.WriteSerialByte(fd, DataConversionUtils.HexString2Bytes(sendstring2));
                Thread.sleep(10);

//                    spinProvince.setOnItemSelectedListener(new GpsOpen.ProvOnItemSelectedListener());
//                    sendButton.setOnClickListener(new GpsOpen.ClickEvent());
                final Timer timer1 = new Timer();
                TimerTask task = new TimerTask() {
                    public void run() {
                        try {
                            mSerialPort.WriteSerialByte(fd, DataConversionUtils.HexString2Bytes(sendstring2));
                            Thread.sleep(5);
                            byte[] temp1 = mSerialPort.ReadSerial(fd, 1024);
                            int i = 0;
                            i++;
                            Log.e(TAG, "read1------------qq---------------------");
                            if (handler == null) {
                                handler = new Handler();
                            }
                            Message msg = new Message();
                            if (temp1 == null || i > 10) {
                                timer1.cancel();
                                Log.e(TAG, "read------------qqqqq---------------------");
                                return;
                            }
                            msg.obj = temp1;
                            handler.sendMessage(msg);
                            Log.e(TAG, "read--------------qq------------------------");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                timer1.schedule(task, 0, 2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class x5ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                Log.e(TAG, "readxx1--------------------------------------");
                mSerialPort.WriteSerialByte(fd, DataConversionUtils.HexString2Bytes(sendstring2));
                Log.e(TAG, "readxx2--------------------------------------");
                Thread.sleep(5);
                byte[] temp1 = mSerialPort.ReadSerial(fd, 1024);
                Thread.sleep(10);
                tv_gps.append(DataConversionUtils.byteArrayToAscii(temp1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    boolean ispower;

    public void getgpio() {
        List lists = new ArrayList();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("sys/class/misc/mtgpio/pin"));
            for (int i = 1; i < 170; i++) {
                if ((line = reader.readLine()) != null) {
                    lists.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < lists.size(); i++) {
            String list = lists.get(i).toString();
            String gpio = list.substring(0, list.indexOf(":"));
            String upordown = list.substring(7, 8);
            for (int j = 0; j < poewro.length; j++) {
                if (poewro[j].equals(gpio.trim())) {//gpio去空格
                    if (upordown.equals("1")) {//上电
                        ispower = false;
                    } else if (upordown.equals("0")) {//下电
                        ispower = true;
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null && readTimerTask != null) {
            timer.cancel();
            readTimerTask.cancel();
        }
        if (mSerialPort != null) {
            mSerialPort.CloseSerial(fd);
        }
        if (!ispower) {
            gpio.PowerOffDevice63();
            gpio.PowerOffDevice98();
        }
        if (readThread != null) {
            readThread.interrupt();
        }
        gpsnet_device.setExtGps(0);
        super.onDestroy();
    }
}
