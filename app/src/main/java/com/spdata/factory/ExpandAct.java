package com.spdata.factory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Message;
import android.os.SystemClock;
import android.serialport.DeviceControl;
import android.serialport.SerialPort;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.FingerUtil.FingerTypes;
import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.DataConversionUtils;

/**
 * Created by suntianwei on 2017/2/10.
 */
@EActivity(R.layout.act_expand)
public class ExpandAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvVersionInfor;
    @ViewById
    TextView tv_infor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_ser1;
    @ViewById
    Button btn_ser2;
    @ViewById
    Button btn_usb;
    @ViewById
    Button btn_state;
    @ViewById
    Button btn_tcs1g;
    private static String SERIAPORT1 = "/dev/ttyMT1";
    private static String SERIAPORT2 = "/dev/ttyMT2";
    private static final String TAG = "ExpandAct";   //记录标识
    private SerialPort mSerialPort;
    private DeviceControl mdeviceControl2;
    private DeviceControl mdeviceControl;
    private boolean isstate;
    private Timer timer = null;

    @Click
    void btnNotPass() {
        setXml(App.KEY_EXPAND, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_EXPAND, App.KEY_FINISH);
        finish();
    }

    @Click
    void btn_tcs1g() {
        btn_ser1.performClick();
        btn_ser2.performClick();
        btn_usb.performClick();

    }

    @Click
    void btn_ser1() {
        Log.i(TAG, "btn_ser1: " + 111111111);
        initDevice(73, SERIAPORT1, 9600);
        byte[] pss = "This is SerialPort 1".getBytes();
        mSerialPort.WriteSerialByte(mSerialPort.getFd(), pss);
        SystemClock.sleep(200);
        byte[] data = null;
        try {
            data = mSerialPort.ReadSerial(mSerialPort.getFd(), 1024);
            if (data != null) {
                handler.sendMessage(handler.obtainMessage(0, data));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Click
    void btn_ser2() {
        Log.i(TAG, "btn_ser2: " + 111111);
        byte[] pss = "This is SerialPort 2".getBytes();
        initDevice(88, SERIAPORT2, 9600);
        mSerialPort.WriteSerialByte(mSerialPort.getFd(), pss);
        SystemClock.sleep(200);
//        byte[] data = null;
        try {
            byte[] data = mSerialPort.ReadSerial(mSerialPort.getFd(), 1024);
            if (data != null) {
                handler.sendMessage(handler.obtainMessage(7, data));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Click
    void btn_usb() {
        showLoading("检测模块中……");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (FingerTypes.getrwusbdevices(ExpandAct.this)) {
                            case 0:
                                showToast("无指纹模块！");
                                hideLoading();
                                handler.sendMessage(handler.obtainMessage(5, "无指纹模块！"));
                                break;
                            case 1:
                                hideLoading();
                                handler.sendMessage(handler.obtainMessage(1, "公安指纹模块！"));
                                break;
                            case 2:
                                hideLoading();
                                handler.sendMessage(handler.obtainMessage(2, "民用指纹模块！"));
                                break;
                            case 3:
                                hideLoading();
                                handler.sendMessage(handler.obtainMessage(3, "金色指纹模块！"));
                                break;
                        }
                    }
                });
            }
        }).start();

    }


    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("触点检测");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }


    @AfterViews
    protected void main() {
        initTitlebar();
        //监听背夹介入成功广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.hall.success");
        registerReceiver(receiver, intentFilter);
        mSerialPort = new SerialPort();
        try {
            mdeviceControl = new DeviceControl(DeviceControl.PowerType.MAIN);
            mdeviceControl2 = new DeviceControl(DeviceControl.PowerType.EXPAND);
            mdeviceControl.MainPowerOn(63);
            mdeviceControl2.ExpandPowerOn(6);
            mdeviceControl2.ExpandPowerOn(5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    /**
     * 背夹接入的广播
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.hall.success")) {
                mSerialPort = new SerialPort();
                try {
                    mdeviceControl.MainPowerOn(63);
                    mdeviceControl2.ExpandPowerOn(6);
                    mdeviceControl2.ExpandPowerOn(5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private void initDevice(int gpio, String seriaprot, int brd) {

        try {
            mSerialPort.OpenSerial(seriaprot, brd);
            mdeviceControl.MainPowerOn(gpio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    android.os.Handler handler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    byte[] temp = (byte[]) msg.obj;
                    String string = DataConversionUtils.byteArrayToAscii(temp);
                    if (string.equals("This is SerialPort 1")) {
                        tvVersionInfor.append("串口1通过\n");
                        btn_ser1.setBackgroundColor(Color.parseColor("#0AF229"));
                    } else {
                        tvVersionInfor.append("串口1失败\n");
                        btn_ser1.setBackgroundColor(Color.parseColor("#ed0c2e"));
                    }
                    break;
                case 7:
                    byte[] temps = (byte[]) msg.obj;
                    String strings = DataConversionUtils.byteArrayToAscii(temps);
                    if (strings.equals("This is SerialPort 2")) {
                        tvVersionInfor.append("串口2通过\n");
                        btn_ser2.setBackgroundColor(Color.parseColor("#0AF229"));
                    } else {
                        tvVersionInfor.append("串口2失败\n");
                        btn_ser2.setBackgroundColor(Color.parseColor("#ed0c2e"));
                    }
                    break;
                case 1:
                    tvVersionInfor.append((String) msg.obj + "\n");
                    btn_usb.setBackgroundColor(Color.parseColor("#0AF229"));
                    break;
                case 2:
                    tvVersionInfor.append((String) msg.obj + "\n");
                    btn_usb.setBackgroundColor(Color.parseColor("#0AF229"));
                    break;
                case 3:
                    tvVersionInfor.append((String) msg.obj + "\n");
                    btn_usb.setBackgroundColor(Color.parseColor("#0AF229"));
                    break;
                case 4:
                    if (msg.obj.equals("234")) {
                        tv_infor.setTextColor(Color.RED);
                        tv_infor.setText((String) msg.obj + "请链接背夹！");
                        btn_tcs1g.setEnabled(false);
                        btn_ser2.setBackgroundColor(Color.parseColor("#ed0c2e"));
                        btn_ser1.setBackgroundColor(Color.parseColor("#ed0c2e"));
                        btn_usb.setBackgroundColor(Color.parseColor("#ed0c2e"));
                        btn_state.setBackgroundColor(Color.parseColor("#ed0c2e"));
                    } else {
                        btn_tcs1g.setEnabled(true);
                        tv_infor.setTextColor(Color.GREEN);
                        tv_infor.setText((String) msg.obj + "背夹已连接！");
                        btn_state.setBackgroundColor(Color.parseColor("#0AF229"));
                    }

                    break;
                case 5:
                    btn_usb.setBackgroundColor(Color.parseColor("#ed0c2e"));
                    break;
            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        unregisterReceiver(receiver);
        try {
            mdeviceControl.MainPowerOff(73);
            mdeviceControl.MainPowerOff(88);
            mdeviceControl.MainPowerOff(63);
            mdeviceControl2.ExpandPowerOff(6);
            mdeviceControl2.ExpandPowerOff(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测被接入的背夹具体型号
     *
     * @return
     */
    public String readEm55() {
        String state = null;
        ///sys/class/misc/aw9523/gpio
        File file = new File("sys/class/misc/aw9523/gpio");
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            state = bufferedReader.readLine();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "readEm55state: " + state);
        return state;
    }


    /**
     * 检测背夹状态 是哪个背夹
     */
    private void start() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String date = readEm55();//检测背夹
                handler.sendMessage(handler.obtainMessage(4, date));

            }
        }, 0, 1000);

    }
}
