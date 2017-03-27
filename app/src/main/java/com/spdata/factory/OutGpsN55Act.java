package com.spdata.factory;

import android.content.Context;
import android.os.Message;
import android.serialport.DeviceControl;
import android.serialport.SerialPort;
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
import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.DataConversionUtils;
/**
 * Created by suntianwei on 2016/11/25.
 */
@EActivity(R.layout.act_outgpsn55)
public class OutGpsN55Act extends FragActBase {
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
    int fd = 0;
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

    @Click
    void btn_start() {
        ReadThread readThread = new ReadThread();
        readThread.start();

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

    private SerialPort mSerialPort;

    @AfterViews
    protected void main() {
        initTitlebar();
        mSerialPort = new SerialPort();
        try {
            gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
            gpio.PowerOnDevice("120");
            gpio.PowerOnDevice("73");
            gpio.PowerOnDevice("94");
            gpio.PowerOnDevice("119");
            mSerialPort.OpenSerial("/dev/ttyMT1", 57600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fd = mSerialPort.getFd();
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
                    }
                    break;
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

    @Override
    protected void onDestroy() {
        mSerialPort.CloseSerial(fd);
        finish();
        gpio.PowerOffDevice("120");
        gpio.PowerOffDevice("73");
        gpio.PowerOffDevice("94");
        gpio.PowerOffDevice("119");
        super.onDestroy();
    }
}
