package com.spdata.factory;

import android.os.Bundle;
import android.os.Handler;
import android.serialport.DeviceControlSpd;
import android.serialport.SerialPortSpd;
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


    private SerialPortSpd serialPort;
    private SerialPortSpd serialPort2;
    private SerialPortSpd serialPort3;
    private int fd;
    private int fd2;
    private int fd3;
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
    private TextView tvAccept;
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
        initView();
        onWindowFocusChanged(true);
        initTitlebar();
        setSwipeEnable(false);
        tvSend.setText(getResources().getString(R.string.Tc01485Act_send));
        try {
            DeviceControlSpd deviceControl = new DeviceControlSpd(DeviceControlSpd.PowerType.MAIN, 86,65);
            deviceControl.PowerOnDevice();
            serialPort = new SerialPortSpd();
            serialPort2 = new SerialPortSpd();
            serialPort3 = new SerialPortSpd();
            serialPort.OpenSerial(SerialPortSpd.SERIAL_TTYMT2, 9600);
            serialPort2.OpenSerial(SerialPortSpd.SERIAL_TTYMT3, 9600);
            serialPort3.OpenSerial(SerialPortSpd.SERIAL_TTYMT1, 115200);
            fd = serialPort.getFd();
            fd2 = serialPort2.getFd();
            fd3 = serialPort3.getFd();
            handler.postDelayed(runnable2, 100);
            handler.postDelayed(runnable, 100);
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
        }, getResources().getString(R.string.menu_test485), null);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                byte[] resultBytes = serialPort.ReadSerial(fd, 512);
                byte[] resultBytes2 = serialPort2.ReadSerial(fd2, 512);
                byte[] resultBytes3 = serialPort3.ReadSerial(fd3, 512);
                if (resultBytes != null) {
                    tvAccept1.setText(getResources().getString(R.string.Tc01485Act_accept1) + DataConversionUtils.byteArrayToAscii(resultBytes));
                }
                if (resultBytes2 != null) {
                    tvAccept2.setText(getResources().getString(R.string.Tc01485Act_accept2) + DataConversionUtils.byteArrayToAscii(resultBytes2));
                }
                if (resultBytes3 != null) {
                    tvAccept2.setText(getResources().getString(R.string.Tc01485Act_2_4G) + DataConversionUtils.byteArrayToAscii(resultBytes3));
                }
                handler.postDelayed(runnable, 10);
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
            serialPort3.WriteSerialByte(fd3, "This is 2.4G test".getBytes());
            handler.postDelayed(runnable2, 100);
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
        if (serialPort3 != null) {
            serialPort3.CloseSerial(fd3);
        }
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(runnable2);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvSend = (TextView) findViewById(R.id.tv_send);
        tvAccept1 = (TextView) findViewById(R.id.tv_accept1);
        tvAccept2 = (TextView) findViewById(R.id.tv_accept2);
        tvAccept = (TextView) findViewById(R.id.tv_accept);
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
