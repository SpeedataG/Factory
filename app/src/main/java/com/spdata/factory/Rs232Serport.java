package com.spdata.factory;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.serialport.DeviceControlSpd;
import android.serialport.SerialPortSpd;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import common.base.act.FragActBase;
import common.utils.DataConversionUtils;

/**
 * Created by 42040 on 2018/11/16.
 */
public class Rs232Serport extends FragActBase implements View.OnClickListener {

    private CustomTitlebar titlebar;
    /**
     * xxx
     */
    private TextView tvVersionInfor;
    /**
     * 发送
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    private void send() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] pss = sendstring.getBytes();
                mSerialPort.clearPortBuf(fd);
                mSerialPort.WriteSerialByte(fd, pss);
//                    SystemClock.sleep(20);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] temp1 = new byte[0];
                        try {
                            temp1 = mSerialPort.ReadSerial(fd, 256);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (temp1 == null) {
                            sendcount = 1;
                            string = tvVersionInfor.getText().toString();
                        } else {
                            string = DataConversionUtils.byteArrayToAscii(temp1);
                        }
                        if (string.equals("") || string.equals(getResources().getString(R.string.Rs232Serport_equals1) + getResources().getString(R.string.Rs232Serport_equals2))) {
                            tvVersionInfor.setText(getResources().getString(R.string.Rs232Serport_infor1));
                            sendcount = 1;
//                                return;
                        } else if (string.equals(sendstring)) {
                            tvVersionInfor.setText(getResources().getString(R.string.Rs232Serport_infor2) + string);
                            sendcount++;
                            btnPass.setText(getResources().getString(R.string.btn_success));
                        }
                    }
                });
            }
        }).start();

    }


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_rs232));
    }


    private SerialPortSpd mSerialPort;
    private DeviceControlSpd deviceControl;
    private int sendcount = 1;
    private int fd;
    public String sendstring = "This is Seriaport!";
    private String string;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_port232_layout);
        initView();
        initTitlebar();
        tvVersionInfor.setText(getResources().getString(R.string.Rs232Serport_equals1) + getResources().getString(R.string.Rs232Serport_equals2));
        try {
            deviceControl = new DeviceControlSpd(DeviceControlSpd.POWER_MAIN);
            deviceControl.MainPowerOn(90);
            writeFile("1");
            SystemClock.sleep(200);
            mSerialPort = new SerialPortSpd();
            mSerialPort.OpenSerial("/dev/ttyUSB0", 9600);
            fd = mSerialPort.getFd();
            showToast("fdfd:" + fd);

        } catch (IOException e) {
            e.printStackTrace();
            btnPass.setVisibility(View.GONE);
            showToast(getResources().getString(R.string.Rs232Serport_open_fail) + e.toString());
        }
    }

    private void writeFile(String type) {
        try {
            File file = new File("/sys/class/misc/hwoper/usb_route/");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(type);
            bw.flush();
            bw.close();
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
            showToast("xiewenjianerr");
        }
    }

    private Handler handler = new Handler();

    //    @SuppressLint("HandlerLeak")
//    android.os.Handler handler = new android.os.Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            byte[] temp = (byte[]) msg.obj;
//            if (temp == null) {
//                sendcount = 1;
//            } else {
//                string = DataConversionUtils.byteArrayToAscii(temp);
//                tvVersionInfor.setText("接收内容：\n\n" + string);
//                if (sendstring.equals(string)) {
//                    sendcount++;
//                    btnPass.setText("成功");
//                }
//            }
//        }
//    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            showToast("dddddd");
            try {
                byte[] temp1 = mSerialPort.ReadSerial(fd, 256);
                if (temp1 == null) {
                    sendcount = 1;
                    string = tvVersionInfor.getText().toString();
                } else {
                    string = DataConversionUtils.byteArrayToAscii(temp1);
                }
                if (string.equals("") || string.equals(getResources().getString(R.string.Rs232Serport_equals1) + getResources().getString(R.string.Rs232Serport_equals2))) {
                    tvVersionInfor.setText(getResources().getString(R.string.Rs232Serport_infor1));
                    sendcount = 1;
//                                return;
                } else if (string.equals(sendstring)) {
                    tvVersionInfor.setText(getResources().getString(R.string.Rs232Serport_infor2) + string);
                    sendcount++;
                    btnPass.setText(getResources().getString(R.string.btn_success));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            handler.postDelayed(runnable, 50);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            writeFile("0");
            handler.removeCallbacks(runnable);
            mSerialPort.CloseSerial(fd);
            deviceControl.MainPowerOff(90);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
//                send();
                        byte[] pss = sendstring.getBytes();
                        mSerialPort.WriteSerialByte(fd, pss);
                        handler.postDelayed(runnable, 10);
                    }
                } else if (sendcount == 2) {
                    btnPass.setText(getResources().getString(R.string.btn_success));
                    setXml(App.KEY_PORT232, App.KEY_FINISH);
                    finish();
                }
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_PORT232, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
