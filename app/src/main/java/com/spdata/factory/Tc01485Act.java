package com.spdata.factory;

import android.content.Context;
import android.os.Handler;
import android.serialport.DeviceControl;
import android.serialport.SerialPort;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.DataConversionUtils;

@EActivity(R.layout.tc01_485_layout)
public class Tc01485Act extends FragActBase {

    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tv_send;
    @ViewById
    TextView tv_accept1;
    @ViewById
    TextView tv_accept2;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private SerialPort serialPort;
    private SerialPort serialPort2;
    private int fd;
    private int fd2;
    private Handler handler = new Handler();

    @Click
    void btnNotPass() {
        setXml(App.KEY_485, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_485, App.KEY_FINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
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

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }


    @AfterViews
    protected void main() {
        onWindowFocusChanged(true);
        initTitlebar();
        setSwipeEnable(false);
        tv_send.setText("发送内容：This is 485 test");
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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                byte[] resultBytes = serialPort.ReadSerial(fd, 512);
                byte[] resultBytes2 = serialPort2.ReadSerial(fd2, 512);
                if (resultBytes!=null){

                    tv_accept1.setText("串口2接收：" + DataConversionUtils.byteArrayToAscii(resultBytes));
                }
                if (resultBytes2!=null){
                    tv_accept2.setText("串口3接收：" + DataConversionUtils.byteArrayToAscii(resultBytes2));
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
        if (serialPort!=null){
            serialPort.CloseSerial(fd);
        }
        if (serialPort2!=null){
            serialPort2.CloseSerial(fd2);
        }
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(runnable2);
    }
}
