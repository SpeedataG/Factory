package com.spdata.factory;

import android.content.Context;
import android.hardware.SerialManager;
import android.os.Message;
import android.serialport.SerialPortBackup;
import android.util.Log;
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
 * Created by lenovo_pc on 2016/8/25.
 */
@EActivity(R.layout.out_gps)
public class OutGps_x300Act extends FragActBase {

    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btn_x2;
    @ViewById
    Button btn_x5;
    @ViewById
    Button btnNotPass;
    @ViewById
    EditText edv_infors;
    @ViewById
    TextView tv_gps;

    // 切换波特率到57600
    public String sendstring = "0200640d000000030001000204000600008103";
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
    void btn_x2() {
        try {
            mSerialPort.OpenSerial("/dev/ttyMT1", 57600);
            fd = mSerialPort.getFd();
            timer = new Timer();
            readTimerTask = new ReadTimerTask();
            timer.schedule(readTimerTask, 10, TIME_TO_READDATA);
//            ReadThread readThread = new ReadThread();
//            readThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
        //以默认38400波特率打开串口
//                    mSerialPort = mSerialManager.openSerialPort("/dev/ttyMT1", 57600, 8, 1, 0);
//                    SystemClock.sleep(10);
//                    //指令切换到57600
//                    mSerialPort.write_byte(DataConversionUtils.HexString2Bytes(sendstring), 1024);
//                    SystemClock.sleep(10);
//                    //重新以57600波特率打开串口
//                    mSerialPort = mSerialManager.openSerialPort("/dev/ttyMT1", 57600, 8, 1, 0);
//                    while (temp1 < 10 || temp1 <= 0) {
//                        temp1 = mSerialPort.read_byte(tmpbuf, 1024);
//                        if (temp1 > 0) {
//                            byte[] byt = new byte[temp1];
//                            System.arraycopy(tmpbuf, 0, byt, 0, temp1);
//                            Message msg = new Message();
//                            msg.obj = byt;
//                            handler.sendMessage(msg);
//                            return;
//                        }
//                        byte[] temp1 = mSerialPort.ReadSerial(fd, 1024);
//                        if (temp1 != null) {
//                            Message msg = new Message();
//                            msg.obj = temp1;
//                            handler.sendMessage(msg);
//                            return;
//                        }
//                    }
//                    Message msg = new Message();
//                    msg.obj = null;
//                    handler.sendMessage(msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    @Click
    void btn_x5() {

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
        gpio.PowerOnDevice63();
        gpio.PowerOnDevice98();
        mSerialPort = new SerialPortBackup();
//        mSerialManager = (SerialManager) getSystemService(SERIAL_SERVICE);
        btn_x5.setEnabled(false);
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        btn_x2.setEnabled(true);
//                        btn_x2.callOnClick();
//                    }
//                });
//            }
//        }, 1000 * 30);
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    byte[] temp = (byte[]) msg.obj;
                    if (temp == null) {
                        titlebar.setAttrs("X2测试失败！");
                    } else {
                        String s = DataConversionUtils.byteArrayToAscii(temp);

                        tv_gps.append(s);
                        timer.cancel();
                        readTimerTask.cancel();
//                        edv_infors.setText(DataConversionUtils.byteArrayToAscii(temp));
                    }
//                    edv_infors.append("\n");
                    break;

            }
        }
    };

    private class ReadTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                Log.i("ss", "sss");
                byte[] temp1 = mSerialPort.ReadSerial(fd, 1024);
                if (temp1 != null) {
                    Message msg = new Message();
                    msg.obj = temp1;
                    handler.sendMessage(msg);
//                    edv_infors.append("\n");
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
        gpio.PowerOffDevice63();
        gpio.PowerOffDevice98();
        super.onDestroy();
    }
}
