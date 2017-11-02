package com.spdata.factory;

import android.content.Context;
import android.os.Message;
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
import common.utils.DeviceControl;

/**
 * Created by lenovo_pc on 2016/9/28.
 */
@EActivity(R.layout.act_outgpsdb2)
public class OutGpsDB2Act extends FragActBase {
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
    private SerialPort mSerialPort;
    private ReadTask readTask;

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
        readTask = new ReadTask();
        readTask.start();
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

    @AfterViews
    protected void main() {
        initTitlebar();
        try {
            mSerialPort = new SerialPort();
            mSerialPort.OpenSerial("/dev/ttyMT3", 9600);
            gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
        } catch (IOException e) {
            e.printStackTrace();
        }
        gpio.PowerOnDevice71();
        gpio.PowerOnDevice70();
        gpio.PowerOnDevice64();
        gpio.PowerOnDevice99();

    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] temp = (byte[]) msg.obj;
            String s = DataConversionUtils.byteArrayToAscii(temp);
            tv_gps.setText(s);
        }
    };

    private class ReadTask extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    byte[] temp1 = mSerialPort.ReadSerial(mSerialPort.getFd(), 1024);
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
    protected void onDestroy() {
        super.onDestroy();
        mSerialPort.CloseSerial(mSerialPort.getFd());
        gpio.PowerOffDevice71();
        gpio.PowerOffDevice64();
        gpio.PowerOffDevice70();
        gpio.PowerOffDevice99();
        if (readTask != null) {
            readTask.interrupt();
        }
        finish();

    }
}
