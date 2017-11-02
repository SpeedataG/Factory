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
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.DataConversionUtils;
import common.utils.DeviceControl;

/**
 * Created by suntianwei on 2016/11/24.
 */
@EActivity(R.layout.act_serialport)
public class SerialportH80Act extends FragActBase  {

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
            if (tvVersionInfor.getText().equals("请将耳机串口自环线插入耳机接口左侧无标示接口，" +
                    "点击发送按钮\n\n发送内容“This is Seriaport!”接收到发送内容成功")){
//                btnPass.setEnabled(false);
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
        tvVersionInfor.setText("请将耳机串口自环线插入耳机接口左侧无标示接口，" +
                "点击发送按钮\n\n发送内容“This is Seriaport!”接收到发送内容成功");
        mSerialPort = new SerialPort();
        try {
            deviceControl=new DeviceControl("/sys/class/misc/mtgpio/pin");
            mSerialPort.OpenSerial("/dev/ttyMT3", 9600);
            deviceControl.PowerOnDevice121();
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
                tvVersionInfor.setText("接收内容：\n\n"+ string);
                if (sendstring.equals(string)){
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
    protected void onDestroy() {
        finish();
        super.onDestroy();
        mSerialPort.CloseSerial(fd);
        deviceControl.PowerOffDevice121();
    }
}
