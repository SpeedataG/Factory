package com.spdata.factory;

import android.content.Context;
import android.hardware.SerialManager;
import android.os.Message;
import android.serialport.SerialPortBackup;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by lenovo_pc on 2016/9/12.
 */
@EActivity(R.layout.out_gps_s510)
public class OutGpsS510Act extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btn_start;
    @ViewById
    Button btnNotPass;
    @ViewById
    EditText edv_infors;
    @ViewById
    TextView tv_gps;
    private DeviceControl gpio;

    @Click
    void btnNotPass() {
        setXml(App.KEY_GPS_OUT, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_GPS_OUT, App.KEY_FINISH);
        finish();
    }

    int temp1 = 0;
    int fd = 0;
    private Timer timer;
    private static final int TIME_TO_READDATA = 500;
    ReadTimerTask readTimerTask;

    @Click
    void btn_start() {
        try {
            mSerialPort.OpenSerial("/dev/ttyMT2", 9600);
            fd = mSerialPort.getFd();
            timer = new Timer();
            readTimerTask = new ReadTimerTask();
            timer.schedule(readTimerTask, 10, TIME_TO_READDATA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("外置GPS");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }

    private SerialManager mSerialManager = null;
    private static final String SERIAL_SERVICE = "serial";
    private SerialPortBackup mSerialPort;
    private byte[] tmpbuf = new byte[1024];
    private static final int count = 0;

    @AfterViews
    protected void main() {
        initTitlebar();
        gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
        gpio.PowerOnDevice71();
        gpio.PowerOnDevice72();
        gpio.PowerOnDevice73();
        gpio.PowerOnDevice87();
        mSerialPort = new SerialPortBackup();
//        timer = new Timer();
//        readTimerTask = new ReadTimerTask();
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    byte[] temp = (byte[]) msg.obj;
                    if (temp == null) {
                        titlebar.setAttrs("测试失败！");
                    } else {
                        String s = DataConversionUtils.byteArrayToAscii(temp);
                        tv_gps.setText(s);
                        timer.cancel();
                        readTimerTask.cancel();
                    }
                    break;
            }
        }
    };

    private class ReadTimerTask extends TimerTask {
        @Override
        public void run() {
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
    @Override
    protected void onDestroy() {
        mSerialPort.CloseSerial(fd);
        finish();
        gpio.PowerOffDevice71();
        gpio.PowerOffDevice72();
        gpio.PowerOffDevice73();
        gpio.PowerOffDevice87();
        super.onDestroy();
    }
}

