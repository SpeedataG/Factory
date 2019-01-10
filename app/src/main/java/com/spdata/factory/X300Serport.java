package com.spdata.factory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.serialport.SerialPort;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.IOException;

import common.base.act.FragActBase;
import common.utils.DataConversionUtils;
import common.utils.DeviceControl;

/**
 * Created by suntianwei on 2017/1/12.
 */
public class X300Serport extends FragActBase implements View.OnClickListener {

    ReadThread readThread;
    private CustomTitlebar titlebar;
    /**
     * xxx
     */
    private TextView tv_version_infor;
    /**
     * ttyMT0
     */
    private Button btn_mt0;
    /**
     * ttyMT1
     */
    private Button btn_mt1;
    /**
     * 成功
     */
    private Button btn_pass;
    /**
     * 失败
     */
    private Button btn_not_pass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_x300serport);
        initView(); initTitlebar();
        mSerialPort = new SerialPort();
        deviceControl = new DeviceControl("/sys/class/misc/mtgpio/pin");

    }


    int send() {
        String sendMasg = "SPEEDATA";
        byte[] pss = sendMasg.getBytes();
        mSerialPort.WriteSerialByte(fd, pss);
        return 0;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("串口孔");
    }


    private SerialPort mSerialPort;
    DeviceControl deviceControl;
    int fd;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (readThread != null) {
            readThread.interrupt();
        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] temp = (byte[]) msg.obj;
            String string = DataConversionUtils.byteArrayToAscii(temp);
            tv_version_infor.setText("接收内容：\n" + string);
        }
    };

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tv_version_infor = (TextView) findViewById(R.id.tv_version_infor);
        btn_mt0 = (Button) findViewById(R.id.btn_mt0);
        btn_mt0.setOnClickListener(this);
        btn_mt1 = (Button) findViewById(R.id.btn_mt1);
        btn_mt1.setOnClickListener(this);
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
            case R.id.btn_mt0:
                try {
                    mSerialPort.OpenSerial("/dev/ttyMT0", 38400);
                    fd = mSerialPort.getFd();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                send();
                readThread = new ReadThread();
                readThread.start();
                break;
            case R.id.btn_mt1:
                try {
                    mSerialPort.OpenSerial("/dev/ttyMT1", 38400);
                    fd = mSerialPort.getFd();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                send();
                readThread = new ReadThread();
                readThread.start();
                break;
            case R.id.btn_pass:
                setXml(App.KEY_SERIALPORT, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_SERIALPORT, App.KEY_UNFINISH);
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
}
