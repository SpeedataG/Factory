package com.spdata.factory;

import android.content.Context;
import android.os.Message;
import android.serialport.SerialPort;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.DataConversionUtils;
import common.utils.DeviceControl;

/**
 * Created by suntianwei on 2017/1/12.
 */
@EActivity(R.layout.act_x300serport)
public class X300Serport extends FragActBase {

    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvVersionInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_mt1;
    @ViewById
    Button btn_mt0;
    ReadThread readThread;

    @Click
    void btnNotPass() {
        setXml(App.KEY_SERIALPORT, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btn_mt0() {
        try {
            mSerialPort.OpenSerial("/dev/ttyMT0", 38400);
            fd = mSerialPort.getFd();
        } catch (IOException e) {
            e.printStackTrace();
        }
        send();
        readThread = new ReadThread();
        readThread.start();
    }


    @Click
    void btn_mt1() {
        try {
            mSerialPort.OpenSerial("/dev/ttyMT1", 38400);
            fd = mSerialPort.getFd();
        } catch (IOException e) {
            e.printStackTrace();
        }
        send();
        readThread = new ReadThread();
        readThread.start();
    }

    int send() {
        String sendMasg = "SPEEDATA";
        byte[] pss = sendMasg.getBytes();
        mSerialPort.WriteSerialByte(fd, pss);
        return 0;
    }

    @Click
    void btnPass() {
        setXml(App.KEY_SERIALPORT, App.KEY_FINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("串口孔");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }

    private SerialPort mSerialPort;
    DeviceControl deviceControl;
    int fd;

    @AfterViews
    protected void main() {
        initTitlebar();
        mSerialPort = new SerialPort();
        deviceControl = new DeviceControl("/sys/class/misc/mtgpio/pin");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (readThread != null) {
            readThread.interrupt();
        }
    }

    android.os.Handler handler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] temp = (byte[]) msg.obj;
            String string = DataConversionUtils.byteArrayToAscii(temp);
            tvVersionInfor.setText("接收内容：\n" + string);
        }
    };

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
