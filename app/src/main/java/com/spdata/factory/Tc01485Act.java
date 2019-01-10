package com.spdata.factory;

import android.os.Bundle;
import android.os.Handler;
import android.serialport.DeviceControl;
import android.serialport.SerialPort;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import common.base.act.FragActBase;
import common.utils.DataConversionUtils;

public class Tc01485Act extends FragActBase implements View.OnClickListener {


    private SerialPort serialPort;
    private SerialPort serialPort2;
    private int fd;
    private int fd2;
    private Handler handler = new Handler();
    private CustomTitlebar titlebar;
    /**  */
    private TextView tvSend;
    /**
     * 串口2接收：
     */
    private TextView tvAccept1;
    /**
     * 串口3接收：
     */
    private TextView tvAccept2;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tc01_485_layout);
        initView(); onWindowFocusChanged(true);
        initTitlebar();
        setSwipeEnable(false);
        tvSend.setText("发送内容：This is 485 test");
        try {
            DeviceControl deviceControl = new DeviceControl(DeviceControl.PowerType.MAIN, 21);
            deviceControl.PowerOnDevice();
            serialPort = new SerialPort();
            serialPort2 = new SerialPort();
            serialPort.OpenSerial(SerialPort.SERIAL_TTYMT2, 9600);
            serialPort2.OpenSerial(SerialPort.SERIAL_TTYMT3, 9600);
            fd = serialPort.getFd();
            fd2 = serialPort2.getFd();
            handler.postDelayed(runnable, 100);
            handler.postDelayed(runnable2, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, "485测试", null);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                byte[] resultBytes = serialPort.ReadSerial(fd, 512);
                byte[] resultBytes2 = serialPort2.ReadSerial(fd2, 512);
                if (resultBytes != null) {

                    tvAccept1.setText("串口2接收：" + DataConversionUtils.byteArrayToAscii(resultBytes));
                }
                if (resultBytes2 != null) {
                    tvAccept2.setText("串口3接收：" + DataConversionUtils.byteArrayToAscii(resultBytes2));
                }
                handler.postDelayed(runnable, 50);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            serialPort.WriteSerialByte(fd, "This is 485 test".getBytes());
            serialPort2.WriteSerialByte(fd2, "This is 485 test".getBytes());
            handler.postDelayed(runnable2, 500);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serialPort != null) {
            serialPort.CloseSerial(fd);
        }
        if (serialPort2 != null) {
            serialPort2.CloseSerial(fd2);
        }
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(runnable2);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvSend = (TextView) findViewById(R.id.tv_send);
        tvAccept1 = (TextView) findViewById(R.id.tv_accept1);
        tvAccept2 = (TextView) findViewById(R.id.tv_accept2);
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
                setXml(App.KEY_485, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_485, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
