package com.spdata.factory;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.serialport.DeviceControl;
import android.serialport.SerialPort;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

    @Click
    void btnNotPass() {
        setXml(App.KEY_PORT232, App.KEY_UNFINISH);
        finish();
    }

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
                        if (string.equals("") || string.equals("请将RS232串口自环线插入背面接口，" + "点击发送按钮\n\n发送内容“This is Seriaport!”接收到发送内容成功")) {
                            tvVersionInfor.setText("请插入耳机串口自环线");
                            sendcount = 1;
//                                return;
                        } else if (string.equals(sendstring)) {
                            tvVersionInfor.setText("接收内容：\n\n" + string);
                            sendcount++;
                            btnPass.setText("成功");
                        }
                    }
                });
            }
        }).start();

    }

    @Click
    void btnPass() {
        if (sendcount == 1) {
            if (sendstring != "") {
//                send();
                byte[] pss = sendstring.getBytes();
                mSerialPort.WriteSerialByte(fd, pss);
                handler.postDelayed(runnable, 10);
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
    private DeviceControl deviceControl;
    private int sendcount = 1;
    private int fd;
    public String sendstring = "This is Seriaport!";
    private String string;

    @AfterViews
    protected void main() {
        initTitlebar();
        tvVersionInfor.setText("请将RS232串口自环线插入背面接口，" +
                "点击发送按钮\n\n发送内容“This is Seriaport!”接收到发送内容成功");
        try {
            deviceControl = new DeviceControl(DeviceControl.POWER_MAIN);
            deviceControl.MainPowerOn(90);
            writeFile("1");
            SystemClock.sleep(200);
            mSerialPort = new SerialPort();
            mSerialPort.OpenSerial("/dev/ttyUSB0", 9600);
            fd = mSerialPort.getFd();
            showToast("fdfd:" + fd);

        } catch (IOException e) {
            e.printStackTrace();
            btnPass.setVisibility(View.GONE);
            showToast("串口打开失败请重试！！！" + e.toString());
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
                if (string.equals("") || string.equals("请将RS232串口自环线插入背面接口，" + "点击发送按钮\n\n发送内容“This is Seriaport!”接收到发送内容成功")) {
                    tvVersionInfor.setText("请插入耳机串口自环线");
                    sendcount = 1;
//                                return;
                } else if (string.equals(sendstring)) {
                    tvVersionInfor.setText("接收内容：\n\n" + string);
                    sendcount++;
                    btnPass.setText("成功");
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
}
