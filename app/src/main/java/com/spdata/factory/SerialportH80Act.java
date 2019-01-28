package com.spdata.factory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.serialport.SerialPortSpd;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.utils.DataConversionUtils;
import common.utils.DeviceControl;

/**
 * Created by suntianwei on 2016/11/24.
 */
public class SerialportH80Act extends FragActBase implements View.OnClickListener {

    private ReadThread readThread;
    private CustomTitlebar titlebar;
    /**
     * xxx
     */
    private TextView tvVersionInfor;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    int send(String passwd) {
        byte[] pss = passwd.getBytes();
        mSerialPort.WriteSerialByte(fd, pss);
        return 0;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_serialport));
    }

    private SerialPortSpd mSerialPort;
    DeviceControl deviceControl;
    private int sendcount = 1;
    private int fd;
    private Timer timer;
    private ReadTimerTask readTimerTask;
    private static final int TIME_TO_READDATA = 400;
    public String sendstring = "This is Serialport!";
    private String string;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_serialport);
        initView();
        initTitlebar();
        tvVersionInfor.setText(getResources().getString(R.string.serialport_info1) +
                getResources().getString(R.string.serialport_info2));
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mSerialPort = new SerialPortSpd();
            deviceControl = new DeviceControl("/sys/class/misc/mtgpio/pin");
            mSerialPort.OpenSerial("/dev/ttyMT3", 9600);
            deviceControl.PowerOnDevice121();
            fd = mSerialPort.getFd();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] temp = (byte[]) msg.obj;
            if (temp == null) {
            } else {
                string = DataConversionUtils.byteArrayToAscii(temp);
                tvVersionInfor.setText(getResources().getString(R.string.serialport_info3) + string);
                if (sendstring.equals(string)) {
//                    readThread.stop();
                    sendcount++;
                    btnPass.setText(getResources().getString(R.string.btn_success));
                    timer.cancel();
                    readTimerTask.cancel();
                }


            }
        }
    };

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvVersionInfor = (TextView) findViewById(R.id.tv_version_infor);
        btnPass = (Button) findViewById(R.id.btn_pass);
        btnPass.setOnClickListener(this);
        btnNotPass = (Button) findViewById(R.id.btn_not_pass);
        btnNotPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_pass:
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
                    if (tvVersionInfor.getText().equals(getResources().getString(R.string.serialport_info1) +
                            getResources().getString(R.string.serialport_info2))) {
//                btnPass.setEnabled(false);
                        tvVersionInfor.setText(getResources().getString(R.string.serialport_info4));
                    }
                } else if (sendcount == 2) {
                    btnPass.setText(getResources().getString(R.string.btn_success));
                    setXml(App.KEY_SERIALPORT, App.KEY_FINISH);
                    finish();
                }
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_SERIALPORT, App.KEY_UNFINISH);
                finish();
                break;
        }
    }

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
        mSerialPort.CloseSerial(fd);
        deviceControl.PowerOffDevice121();
        timer.cancel();
        readTimerTask.cancel();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
