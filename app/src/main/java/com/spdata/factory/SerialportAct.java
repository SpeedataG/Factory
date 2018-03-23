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

/**
 * Created by lenovo_pc on 2016/9/3.
 */
@EActivity(R.layout.act_serialport)
public class SerialportAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvVersionInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private ReadThread readThread;

    @Click
    void btnNotPass() {
        setXml(App.KEY_SERIALPORT, App.KEY_UNFINISH);
        finish();
    }

    int send(String passwd) {
        byte[] pss = passwd.getBytes();
        if (mSerialPort != null) {
            mSerialPort.WriteSerialByte(fd, pss);
        }
        return 0;
    }

    @Click
    void btnPass() {
        if (sendcount == 1) {
            if (sendstring != "") {
                send(sendstring);
            }
            if (tvVersionInfor.getText().equals("请将耳机串口自环线插入耳机接口左侧无标示接口，" +
                    "点击发送按钮\n\n发送内容“This is Seriaport!”接收到发送内容成功")) {
                tvVersionInfor.setText("请插入耳机串口自环线");
            }
        } else if (sendcount == 2) {
            btnPass.setText("成功");
            setXml(App.KEY_SERIALPORT, App.KEY_FINISH);
            finish();
        }
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
    private int sendcount = 1;
    private int fd;
    public String sendstring = "This is Seriaport!";
    private String string;

    @AfterViews
    protected void main() {
        initTitlebar();
        tvVersionInfor.setText("请将耳机串口自环线插入耳机接口左侧无标示接口，" +
                "点击发送按钮\n\n发送内容“This is Seriaport!”接收到发送内容成功");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mSerialPort = new SerialPort();
            mSerialPort.OpenSerial("/dev/ttyMT3", 9600);
            fd = mSerialPort.getFd();
            readThread = new ReadThread();
            readThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    android.os.Handler handler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] temp = (byte[]) msg.obj;
            string = DataConversionUtils.byteArrayToAscii(temp);
            tvVersionInfor.setText("接收内容：\n\n" + string);
            if (sendstring.equals(string)) {
                if (readThread != null) {
                    readThread.interrupt();
                    readThread = null;
                }
                sendcount++;
                btnPass.setText("成功");
            }
        }
    };

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
