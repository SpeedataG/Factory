package com.spdata.factory;

import android.content.Context;
import android.os.Message;
import android.os.SystemProperties;
import android.serialport.DeviceControl;
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
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.DataConversionUtils;

/**
 * Created by 42040 on 2018/11/16.
 */
@EActivity(R.layout.act_port232_layout)
public class Rs232Serport extends FragActBase {

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
        setXml(App.KEY_PORT232, App.KEY_UNFINISH);
        finish();
    }

    int send(String passwd) {
        byte[] pss = passwd.getBytes();
        mSerialPort.WriteSerialByte(fd, pss);
        return 0;
    }

    @Click
    void btnPass() {
        if (sendcount == 1) {
            if (sendstring != "") {
                send(sendstring);
            }
//            readThread.start();
            timer = new Timer();
            readTimerTask = new ReadTimerTask();
            timer.schedule(readTimerTask, 10, TIME_TO_READDATA);
//            sendcount++;
//            btnPass.setText("成功");
            if (tvVersionInfor.getText().equals("请将RS232串口自环线插入背面接口，" +
                    "点击发送按钮\n\n发送内容“This is Seriaport!”接收到发送内容成功")) {
//                btnPass.setEnabled(false);
                tvVersionInfor.setText("请插入耳机串口自环线");
            }
        } else if (sendcount == 2) {
            btnPass.setText("成功");
            setXml(App.KEY_PORT232, App.KEY_FINISH);
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
        titlebar.setAttrs("RS232串口");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    private SerialPort mSerialPort;
    DeviceControl deviceControl;
    private int sendcount = 1;
    private int fd;
    private Timer timer;
    private ReadTimerTask readTimerTask;
    private static final int TIME_TO_READDATA = 400;
    public String sendstring = "This is Seriaport!";
    private String string;

    @AfterViews
    protected void main() {
        initTitlebar();
        tvVersionInfor.setText("请将RS232串口自环线插入背面接口，" +
                "点击发送按钮\n\n发送内容“This is Seriaport!”接收到发送内容成功");

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            SystemProperties.set("sys/class/misc/hwoper/usb_route", "1");
            mSerialPort = new SerialPort();
            deviceControl = new DeviceControl(DeviceControl.POWER_MAIN);
            mSerialPort.OpenSerial("/dev/ttyUSB0", 9600);
            deviceControl.MainPowerOn(90);
            fd = mSerialPort.getFd();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    android.os.Handler handler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] temp = (byte[]) msg.obj;
            if (temp == null) {
            } else {
                string = DataConversionUtils.byteArrayToAscii(temp);
                tvVersionInfor.setText("接收内容：\n\n" + string);
                if (sendstring.equals(string)) {
//                    readThread.stop();
                    sendcount++;
                    btnPass.setText("成功");
                    timer.cancel();
                    readTimerTask.cancel();
                }


            }
        }
    };

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
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

    private class ReadTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                fd = mSerialPort.getFd();
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
    protected void onPause() {
        SystemProperties.set("sys/class/misc/hwoper/usb_route", "0");
        mSerialPort.CloseSerial(fd);
        try {
            deviceControl.MainPowerOff(90);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (timer != null) {
            timer.cancel();
            readTimerTask.cancel();
        }
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
