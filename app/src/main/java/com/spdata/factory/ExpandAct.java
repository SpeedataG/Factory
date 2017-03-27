package com.spdata.factory;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Message;
import android.os.SystemClock;
import android.serialport.DeviceControl;
import android.serialport.SerialPort;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.IDWORLD.LAPI;
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
import java.util.HashMap;
import java.util.Iterator;
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

    @Click
    void btnNotPass() {
        setXml(App.KEY_EXPAND, App.KEY_UNFINISH);
        finish();
    }

    private readTherd readTherd = null;
    private PackageManager manager;

    @Click
    void btn_tcs1g() {
//        try {
//            Intent intent = new Intent();
//            intent = manager.getLaunchIntentForPackage("com.example.lenovo.kt50fingerprintdemo");
//            startActivity(intent);
//        } catch (Exception e) {
//            showToast("此应用不在！");
//        }
        btn_ser1.performClick();
        btn_ser2.performClick();
        btn_usb.performClick();
        btn_state.performClick();
    }

    @Click
    void btn_ser1() {
        initDevice("73", SERIAPORT1, 9600);
        byte[] pss = "This is SerialPort 1".getBytes();
        mSerialPort.WriteSerialByte(mSerialPort.getFd(), pss);
        SystemClock.sleep(200);
        startReadeSer();
    }

    @Click
    void btn_ser2() {
        byte[] pss = "This is SerialPort 2".getBytes();
        initDevice("88", SERIAPORT2, 9600);
        mSerialPort.WriteSerialByte(mSerialPort.getFd(), pss);
        SystemClock.sleep(200);
        startReadeSer();
    }

    private LAPI m_cLAPI;
    private int m_hDevice = 0;
    public static final String POWER_EXTERNAL = "/sys/class/misc/aw9523/gpio";
    DeviceControl mdeviceControl2;

    @Click
    void btn_usb() {
        Runnable r = new Runnable() {
            public void run() {
                OPEN_DEVICE();
            }
        };
        Thread s = new Thread(r);
        s.start();
//        initDevice("73", SERIAPORT1, 9600);
//        mSerialPort.WriteSerialByte(mSerialPort.getFd(), pss);
//        SystemClock.sleep(100);
//        startReadeSer();
////        SystemClock.sleep(1000);
////        mSerialPort.CloseSerial();
//        mdeviceControl.PowerOffDevice("73");
//        initDevice("88", SERIAPORT2, 9600);
//        mSerialPort.WriteSerialByte(mSerialPort.getFd(), pss2);
//        startReadeSer();
////        SystemClock.sleep(1000);
//        Runnable r = new Runnable() {
//            public void run() {
//                OPEN_DEVICE();
//            }
//        };
//        Thread s = new Thread(r);
//        s.start();
////        SystemClock.sleep(1000);
//        start();
    }

    @Click
    void btn_state() {
        start();

    }

    @Click
    void btnPass() {
        setXml(App.KEY_EXPAND, App.KEY_FINISH);
        finish();
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

    byte[] pss = "This is SerialPort 1".getBytes();
    byte[] pss2 = "This is SerialPort 2".getBytes();

    @AfterViews
    protected void main() {
        initTitlebar();
        manager = getPackageManager();
        mSerialPort = new SerialPort();
        mdeviceControl = new DeviceControl("/sys/class/misc/mtgpio/pin");
        mdeviceControl2 = new DeviceControl(POWER_EXTERNAL);
        mdeviceControl.PowerOnDevice63();
        mdeviceControl.PowerOnw("6");
        m_cLAPI = new LAPI(this);
    }

    // 寻找接口和分配结点

    public void startReadeSer() {
        if (readTherd == null) {
            readTherd = new readTherd();
            readTherd.start();
        }
    }

    private SerialPort mSerialPort;
    private DeviceControl mdeviceControl;


    private void initDevice(String gpio, String seriaprot, int brd) {

        try {
            mSerialPort.OpenSerial(seriaprot, brd);
            mdeviceControl.PowerOnDevice(gpio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    android.os.Handler handler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            tvVersionInfor.setText("接收内容：\n");
            switch (msg.what) {
                case 0:
                    byte[] temp = (byte[]) msg.obj;
                    String string = DataConversionUtils.byteArrayToAscii(temp);
                    tvVersionInfor.append(string + "\n");
                    break;
                case 1:
//                    tvVersionInfor.setTextColor(Color.RED);
                    tvVersionInfor.append((String) msg.obj + "\n");
                    break;
                case 2:
//                    tvVersionInfor.setTextColor(Color.RED);
                    tvVersionInfor.append((String) msg.obj + "\n");
                    break;
                case 3:
//                    tvVersionInfor.setTextColor(Color.WHITE);
                    tvVersionInfor.append((String) msg.obj + "\n");
                    break;
                case 4:
                    if (isColor && msg.obj.equals("234")) {
                        isColor = false;
                        tv_infor.setTextColor(Color.RED);
                    } else {
                        isColor = true;
                        tv_infor.setTextColor(Color.WHITE);
                    }
                    tv_infor.setText((String) msg.obj);
                    break;
            }

        }
    };
    boolean isColor = true;

    class readTherd extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    byte[] data = mSerialPort.ReadSerial(mSerialPort.getFd(), 1024);
                    if (data != null) {
                        handler.sendMessage(handler.obtainMessage(0, data));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void OPEN_DEVICE() {
        String msg = "";
        if (getrwusbdevices() != 1) {
            msg = "Find device Is Fail!";
            handler.sendMessage(handler.obtainMessage(1, msg));
            return;
        }
        m_hDevice = m_cLAPI.OpenDeviceEx();
        if (m_hDevice == 0) {
            msg = "Can't open device !";
            handler.sendMessage(handler.obtainMessage(2, msg));
        } else {
            msg = "OpenDevice() = OK";
            handler.sendMessage(handler.obtainMessage(3, msg));

        }
    }

    public int getrwusbdevices() {
        //public static final int VID = 0x0483;
        //public static final int PID = 0x5710;
        // get FileDescriptor by Android USB Host API
        UsbManager mUsbManager = (UsbManager) this
                .getSystemService(Context.USB_SERVICE);
        final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        BroadcastReceiver mUsbReceiver = null;
        this.registerReceiver(mUsbReceiver, filter);
        int fd = -1;
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            Log.e("1111111",
                    device.getDeviceName() + " "
                            + Integer.toHexString(device.getVendorId()) + " "
                            + Integer.toHexString(device.getProductId()));
            if ((device.getVendorId() == 0x0483)
                    && (0x5710 == device.getProductId())) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (readTherd != null) {
            readTherd.interrupt();
        }
        if (timer != null) {
            timer.cancel();
        }
        mdeviceControl.PowerOffDevice("73");
        mdeviceControl.PowerOffDevice("88");
        mdeviceControl.PowerOffDevice63();
        mdeviceControl2.PowerOnw("6");
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

    Timer timer = null;

    private void start() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String date = readEm55();
                handler.sendMessage(handler.obtainMessage(4, date));

            }
        }, 0, 1000);

    }
}
