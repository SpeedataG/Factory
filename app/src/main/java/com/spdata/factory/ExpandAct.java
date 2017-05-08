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
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.dpfpddusbhost.DPFPDDUsbException;
import com.digitalpersona.uareu.dpfpddusbhost.DPFPDDUsbHost;
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
import common.utils.Globals;

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
    private ReaderCollection readers = null;
    private Reader m_reader;
    private boolean isstate = false;

    @Click
    void btnNotPass() {
        setXml(App.KEY_EXPAND, App.KEY_UNFINISH);
        finish();
    }

    private PackageManager manager;
    private String m_deviceName = "";

    @Click
    void btn_tcs1g() {
        btn_ser1.performClick();
        btn_ser2.performClick();
        btn_usb.performClick();

    }

    @Click
    void btn_ser1() {
        Log.i(TAG, "btn_ser1: "+111111111);
        initDevice("73", SERIAPORT1, 9600);
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
        Log.i(TAG, "btn_ser2: "+111111);
        byte[] pss = "This is SerialPort 2".getBytes();
        initDevice("88", SERIAPORT2, 9600);
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
        Log.i(TAG, "btn_ser2: "+222222);
    }

    private LAPI m_cLAPI;
    private int m_hDevice = 0;
    public static final String POWER_EXTERNAL = "/sys/class/misc/aw9523/gpio";
    DeviceControl mdeviceControl2;

    @Click
    void btn_usb() {
//        Runnable r = new Runnable() {
//            public void run() {
//                OPEN_DEVICE();
//            }
//        };
//        Thread s = new Thread(r);
//        s.start();
        Intent i = new Intent(ExpandAct.this, GetReaderActivity.class);
        i.putExtra("device_name", m_deviceName);
        startActivityForResult(i, 1);
    }

    @Click
    void btn_state() {

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
    private static final String ACTION_USB_PERMISSION = "com.digitalpersona.uareu.dpfpddusbhost.USB_PERMISSION";

    //背夹接入的广播
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.hall.success")) {
                mdeviceControl.PowerOnDevice63();
                mdeviceControl2.PowerOnw("6");
                mdeviceControl2.PowerOnw("5");
            }
        }
    };

    @AfterViews
    protected void main() {
        initTitlebar();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.hall.success");
        registerReceiver(receiver, intentFilter);
        manager = getPackageManager();
        mSerialPort = new SerialPort();
        mdeviceControl = new DeviceControl("/sys/class/misc/mtgpio/pin");
        mdeviceControl2 = new DeviceControl(POWER_EXTERNAL);
        mdeviceControl.PowerOnDevice63();
        mdeviceControl2.PowerOnw("6");
        mdeviceControl2.PowerOnw("5");
        m_cLAPI = new LAPI(this);
        start();
    }

    // 寻找接口和分配结点

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
            switch (msg.what) {
                case 0:
                    byte[] temp = (byte[]) msg.obj;
                    String string = DataConversionUtils.byteArrayToAscii(temp);
                    if (string.equals("This is SerialPort 1")) {
                        isstate = true;
                        tvVersionInfor.append( "串口1通过\n");
                        btn_ser1.setBackgroundColor(Color.parseColor("#0AF229"));
                    } else {
                        isstate = false;
                        tvVersionInfor.append( "串口1失败\n");
                        btn_ser1.setBackgroundColor(Color.parseColor("#ed0c2e"));
                    }
                    break;
                case 7:
                    byte[] temps = (byte[]) msg.obj;
                    String strings = DataConversionUtils.byteArrayToAscii(temps);
                    if (strings.equals("This is SerialPort 2")) {
                        isstate = true;
                        tvVersionInfor.append( "串口2通过\n");
                        btn_ser2.setBackgroundColor(Color.parseColor("#0AF229"));
                    } else {
                        isstate = false;
                        tvVersionInfor.append( "串口2失败\n");
                        btn_ser2.setBackgroundColor(Color.parseColor("#ed0c2e"));
                    }


                    break;
                case 1:
                    tvVersionInfor.append((String) msg.obj + "\n");
                    break;
                case 2:
                    tvVersionInfor.append((String) msg.obj + "\n");
                    break;
                case 3:
                    tvVersionInfor.append((String) msg.obj + "\n");
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
                    tvVersionInfor.append("USB失败\n");
                    btn_usb.setBackgroundColor(Color.parseColor("#ed0c2e"));
                    break;
                case 6:
                    tvVersionInfor.append("USB成功\n");
                    btn_usb.setBackgroundColor(Color.parseColor("#0AF229"));
                    break;
            }

        }
    };
    boolean isColor = true;


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
        if (timer != null) {
            timer.cancel();
        }
        unregisterReceiver(receiver);
        mdeviceControl.PowerOffDevice("73");
        mdeviceControl.PowerOffDevice("88");
        mdeviceControl.PowerOffDevice63();
        mdeviceControl2.PowerOnw("6");
        mdeviceControl2.PowerOnw("5");
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

    protected void CheckDevice() {
        try {
            m_reader.Open(Reader.Priority.EXCLUSIVE);
            Reader.Capabilities cap = m_reader.GetCapabilities();
            handler.sendMessage(handler.obtainMessage(6, "Reader ok"));
            m_reader.Close();
        } catch (UareUException e1) {
            displayReaderNotFound();
        }
    }

    private final int GENERAL_ACTIVITY_RESULT = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            displayReaderNotFound();
            return;
        }

        Globals.ClearLastBitmap();
        m_deviceName = (String) data.getExtras().get("device_name");

        switch (requestCode) {
            case GENERAL_ACTIVITY_RESULT:

                if ((m_deviceName != null) && !m_deviceName.isEmpty()) {

                    try {
                        Context applContext = getApplicationContext();
                        m_reader = Globals.getInstance().getReader(m_deviceName, applContext);

                        {
                            PendingIntent mPermissionIntent;
                            mPermissionIntent = PendingIntent.getBroadcast(applContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
                            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                            applContext.registerReceiver(mUsbReceiver, filter);

                            if (DPFPDDUsbHost.DPFPDDUsbCheckAndRequestPermissions(applContext, mPermissionIntent, m_deviceName)) {
                                CheckDevice();
                            }
                        }
                    } catch (UareUException e1) {
                        displayReaderNotFound();
                    } catch (DPFPDDUsbException e) {
                        displayReaderNotFound();
                    }

                } else {
                    displayReaderNotFound();
                }

                break;
        }
    }

    private void displayReaderNotFound() {
        handler.sendMessage(handler.obtainMessage(5, "No reader"));
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //call method to set up device communication
                            CheckDevice();
                        }
                    }
                }
            }
        }
    };
}
