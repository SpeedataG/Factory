package com.spdata.factory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.serialport.SerialPortSpd;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.IOException;

import common.base.act.FragActBase;
import common.utils.DataConversionUtils;

/**
 * Created by lenovo_pc on 2016/9/3.
 */

public class SerialportAct extends FragActBase implements View.OnClickListener {

    private ReadThread readThread;
    private CustomTitlebar titlebar;
    /**
     * xxx
     */
    private TextView tvVersionInfor;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    int send(String passwd) {
        byte[] pss = passwd.getBytes();
        if (mSerialPort != null) {
            mSerialPort.WriteSerialByte(fd, pss);
        }
        return 0;
    }


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_serialport));
    }

    private SerialPortSpd mSerialPort;
    private int sendcount = 1;
    private int fd;
    public String sendstring = "This is Serialport!";
    private String string;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_serialport);
        initView();
        initTitlebar();
        tvVersionInfor.setText(getResources().getString(R.string.serialport_info1) +
                getResources().getString(R.string.serialport_info2));
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mSerialPort = new SerialPortSpd();
            mSerialPort.OpenSerial("/dev/ttyMT3", 9600);
            fd = mSerialPort.getFd();
            readThread = new ReadThread();
            readThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] temp = (byte[]) msg.obj;
            string = DataConversionUtils.byteArrayToAscii(temp);
            tvVersionInfor.setText(getResources().getString(R.string.serialport_info3) + string);
            if (sendstring.equals(string)) {
                if (readThread != null) {
                    readThread.interrupt();
                    readThread = null;
                }
                sendcount++;
                btnPass.setText(getResources().getString(R.string.btn_success));
            }
        }
    };

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvVersionInfor = (TextView) findViewById(R.id.tv_version_infor);
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
            case R.id.btn_pass:
                if (sendcount == 1) {
                    if (sendstring != "") {
                        send(sendstring);
                    }
                    if (tvVersionInfor.getText().equals(getResources().getString(R.string.serialport_info1) +
                            getResources().getString(R.string.serialport_info2))) {
                        tvVersionInfor.setText(getResources().getString(R.string.serialport_info4));
                    }
                } else if (sendcount == 2) {
                    btnPass.setText(getResources().getString(R.string.btn_success));
                    setXml(App.KEY_SERIALPORT, App.KEY_FINISH);
                    finish();
                }
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
            while (!isInterrupted()) {
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

    @Override
    protected void onPause() {
        if (readThread != null) {
            readThread.interrupt();
            readThread = null;
        }
        if (mSerialPort != null) {
            mSerialPort.CloseSerial(fd);
            mSerialPort = null;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (readThread != null) {
            readThread.interrupt();
            readThread = null;
        }
        if (mSerialPort != null) {
            mSerialPort.CloseSerial(fd);
            mSerialPort = null;
        }
        super.onDestroy();
    }
}
