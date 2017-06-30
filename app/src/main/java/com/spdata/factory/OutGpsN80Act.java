package com.spdata.factory;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import java.io.UnsupportedEncodingException;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.DataConversionUtils;

/**
 * Created by lenovo-pc on 2017/6/14.
 */
@EActivity(R.layout.act_n80_outgps_layout)
public class OutGpsN80Act extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tv_gps;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_start;
    private Threads threads;

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

        mSerialPort.WriteSerialByte(mSerialPort.getFd(), senCmd);
        SystemClock.sleep(200);
        try {
            byte data[] = mSerialPort.ReadSerial(mSerialPort.getFd(), 1024);
            if (data != null) {
                String datas = DataConversionUtils.byteArrayToString(data);


            }
        } catch (UnsupportedEncodingException e) {
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

    private SerialPort mSerialPort;
    DeviceControl deviceControl;
    private static final byte senCmd[] = {(byte) 0xB5, 0x62, 0x06, 0x17, 0x0C, 0x00, 0x00
            , 0x41, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x6C, 0x3E};

    @AfterViews
    protected void main() {
        initTitlebar();


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mSerialPort = new SerialPort();
            deviceControl = new DeviceControl("/sys/class/misc/mtgpio/pin");
            mSerialPort.OpenSerial("/dev/ttyMT1", 9600);
            deviceControl.PowerOnDevice("63");
            deviceControl.PowerOnDevice("99");
            threads = new Threads();
            threads.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String datas = DataConversionUtils.byteArrayToString((byte[]) msg.obj);
            if (datas.indexOf("b562050102000617254e") >= 0) {
                showToast("成功");
                setXml(App.KEY_GPS_OUT, App.KEY_FINISH);
                finish();
            } else {
                showToast("失败");
                setXml(App.KEY_GPS_OUT, App.KEY_UNFINISH);
                finish();
            }
            tv_gps.setText(datas);

        }
    };

    class Threads extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                mSerialPort.WriteSerialByte(mSerialPort.getFd(), senCmd);
                SystemClock.sleep(200);
                byte data[] = new byte[0];
                try {
                    data = mSerialPort.ReadSerial(mSerialPort.getFd(), 1024);
                    if (data != null) {
                        handler.sendMessage(handler.obtainMessage(0, data));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deviceControl.PowerOffDevice("63");
        deviceControl.PowerOffDevice("99");
        mSerialPort.CloseSerial(mSerialPort.getFd());
        if (threads!=null){
            threads.interrupt();
            threads=null;
        }
    }
}
