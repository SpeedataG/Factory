package com.spdata.factory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.serialport.DeviceControlSpd;
import android.serialport.SerialPortSpd;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.FingerUtil.FingerTypes;
import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.utils.DataConversionUtils;

/**
 * Created by suntianwei on 2017/2/10.
 */
public class ExpandAct extends FragActBase implements View.OnClickListener {

    private static String SERIAPORT1 = "/dev/ttyMT1";
    private static String SERIAPORT2 = "/dev/ttyMT2";
    private static final String TAG = "ExpandAct";   //记录标识
    private SerialPortSpd mSerialPort;
    private DeviceControlSpd mdeviceControl2;
    private DeviceControlSpd mdeviceControl;
    private boolean isstate;
    private Timer timer = null;
    private CustomTitlebar titlebar;
    private TextView tv_version_infor;
    private TextView tv_infor;
    /**
     * 串口1
     */
    private Button btn_ser1;
    /**
     * 串口2
     */
    private Button btn_ser2;
    /**
     * USB指纹
     */
    private Button btn_usb;
    /**
     * 连接转态
     */
    private Button btn_state;
    /**
     * 自动
     */
    private Button btn_tcs1g;
    /**
     * 成功
     */
    private Button btn_pass;
    /**
     * 失败
     */
    private Button btn_not_pass;


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_expand));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_expand);
        initView();
        initTitlebar();
    }

    @Override
    protected void onResume() {
        //监听背夹介入成功广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.hall.success");
        registerReceiver(receiver, intentFilter);
        mSerialPort = new SerialPortSpd();
        try {
            mdeviceControl = new DeviceControlSpd(DeviceControlSpd.PowerType.MAIN);
            mdeviceControl2 = new DeviceControlSpd(DeviceControlSpd.PowerType.EXPAND);
            mdeviceControl.MainPowerOn(63);
            mdeviceControl2.ExpandPowerOn(6);
            mdeviceControl2.ExpandPowerOn(5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();

        super.onResume();
    }

    /**
     * 背夹接入的广播
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.hall.success")) {
                mSerialPort = new SerialPortSpd();
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

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    byte[] temp = (byte[]) msg.obj;
                    String string = DataConversionUtils.byteArrayToAscii(temp);
                    if (string.equals("This is SerialPort 1")) {
                        tv_version_infor.append(getResources().getString(R.string.Expand_port1_pass));
                        btn_ser1.setBackgroundColor(Color.parseColor("#0AF229"));
                    } else {
                        tv_version_infor.append(getResources().getString(R.string.Expand_port1_fail));
                        btn_ser1.setBackgroundColor(Color.parseColor("#ed0c2e"));
                    }
                    break;
                case 7:
                    byte[] temps = (byte[]) msg.obj;
                    String strings = DataConversionUtils.byteArrayToAscii(temps);
                    if (strings.equals("This is SerialPort 2")) {
                        tv_version_infor.append(getResources().getString(R.string.Expand_port2_pass));
                        btn_ser2.setBackgroundColor(Color.parseColor("#0AF229"));
                    } else {
                        tv_version_infor.append(getResources().getString(R.string.Expand_port2_fail));
                        btn_ser2.setBackgroundColor(Color.parseColor("#ed0c2e"));
                    }
                    break;
                case 1:
                    tv_version_infor.append((String) msg.obj + "\n");
                    btn_usb.setBackgroundColor(Color.parseColor("#0AF229"));
                    break;
                case 2:
                    tv_version_infor.append((String) msg.obj + "\n");
                    btn_usb.setBackgroundColor(Color.parseColor("#0AF229"));
                    break;
                case 3:
                    tv_version_infor.append((String) msg.obj + "\n");
                    btn_usb.setBackgroundColor(Color.parseColor("#0AF229"));
                    break;
                case 4:
                    if (msg.obj.equals("234")) {
                        tv_infor.setTextColor(Color.RED);
                        tv_infor.setText((String) msg.obj + getResources().getString(R.string.Expand_msg1));
                        btn_tcs1g.setEnabled(false);
                        btn_ser2.setBackgroundColor(Color.parseColor("#ed0c2e"));
                        btn_ser1.setBackgroundColor(Color.parseColor("#ed0c2e"));
                        btn_usb.setBackgroundColor(Color.parseColor("#ed0c2e"));
                        btn_state.setBackgroundColor(Color.parseColor("#ed0c2e"));
                    } else {
                        btn_tcs1g.setEnabled(true);
                        tv_infor.setTextColor(Color.GREEN);
                        tv_infor.setText((String) msg.obj + getResources().getString(R.string.Expand_msg2));
                        btn_state.setBackgroundColor(Color.parseColor("#0AF229"));
                    }

                    break;
                case 5:
                    btn_usb.setBackgroundColor(Color.parseColor("#ed0c2e"));
                    break;
                case 8:
                    showToast(getResources().getString(R.string.Expand_toast));
                    finish();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onStop() {
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
        super.onStop();
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
            handler.sendMessage(handler.obtainMessage(8));
        } catch (IOException e) {
            handler.sendMessage(handler.obtainMessage(8));
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
                if (date == null) {
                    handler.sendMessage(handler.obtainMessage(8, date));
                } else {
                    handler.sendMessage(handler.obtainMessage(4, date));
                }
            }
        }, 0, 1000);

    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tv_version_infor = (TextView) findViewById(R.id.tv_version_infor);
        tv_infor = (TextView) findViewById(R.id.tv_infor);
        btn_ser1 = (Button) findViewById(R.id.btn_ser1);
        btn_ser1.setOnClickListener(this);
        btn_ser2 = (Button) findViewById(R.id.btn_ser2);
        btn_ser2.setOnClickListener(this);
        btn_usb = (Button) findViewById(R.id.btn_usb);
        btn_usb.setOnClickListener(this);
        btn_state = (Button) findViewById(R.id.btn_state);
        btn_state.setOnClickListener(this);
        btn_tcs1g = (Button) findViewById(R.id.btn_tcs1g);
        btn_tcs1g.setOnClickListener(this);
        btn_pass = (Button) findViewById(R.id.btn_pass);
        btn_pass.setOnClickListener(this);
        btn_not_pass = (Button) findViewById(R.id.btn_not_pass);
        btn_not_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_ser1:
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
                break;
            case R.id.btn_ser2:
                Log.i(TAG, "btn_ser2: " + 111111);
                pss = "This is SerialPort 2".getBytes();
                initDevice(88, SERIAPORT2, 9600);
                mSerialPort.WriteSerialByte(mSerialPort.getFd(), pss);
                SystemClock.sleep(200);
                try {
                    byte[] data2 = mSerialPort.ReadSerial(mSerialPort.getFd(), 1024);
                    if (data2 != null) {
                        handler.sendMessage(handler.obtainMessage(7, data2));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_usb:
                showLoading(getResources().getString(R.string.Expand_loading));
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
                                        showToast(getResources().getString(R.string.Expand_msg_obj1));
                                        hideLoading();
                                        handler.sendMessage(handler.obtainMessage(5, getResources().getString(R.string.Expand_msg_obj1)));
                                        break;
                                    case 1:
                                        hideLoading();
                                        handler.sendMessage(handler.obtainMessage(1, getResources().getString(R.string.Expand_msg_obj2)));
                                        break;
                                    case 2:
                                        hideLoading();
                                        handler.sendMessage(handler.obtainMessage(2, getResources().getString(R.string.Expand_msg_obj3)));
                                        break;
                                    case 3:
                                        hideLoading();
                                        handler.sendMessage(handler.obtainMessage(3, getResources().getString(R.string.Expand_msg_obj4)));
                                        break;
                                    default:
                                        break;

                                }
                            }
                        });
                    }
                }).start();
                break;
            case R.id.btn_state:
                break;
            case R.id.btn_tcs1g:
                btn_ser1.performClick();
                btn_ser2.performClick();
                btn_usb.performClick();
                break;
            case R.id.btn_pass:
                setXml(App.KEY_EXPAND, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_EXPAND, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
