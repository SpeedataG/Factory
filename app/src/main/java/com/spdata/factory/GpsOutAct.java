package com.spdata.factory;

import android.content.Context;
import android.hardware.SerialManager;
import android.hardware.SerialPort;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
 * Created by lenovo_pc on 2016/8/12.
 */
@EActivity(R.layout.act_out_gps)
public class GpsOutAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
//    @ViewById
//    TextView tv_infor;
//    @ViewById
//    Button button1;
//    @ViewById
//    Button button2;
//    @ViewById
//    EditText edv_infor;

    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

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

    public Button sendButton, sendButton1;
    // 切换波特率到57600
    public String sendstring = "0200640d000000030001000204000600008103";
    //申请NMEA数据
    public String sendstring2 = "0200646600000003000100000D706F7765725F75700F0B1D0A1C07080600030000000000070826000300000000000708120003000000000007080D00030000000000070808000300000000000708280003000000000007080C0003000000000007082900000000000000E603";
    private SerialPort mSerialPort;
    static final String TAG = "SerialPort";
    private EditText edvreadInfor = null;
    private ReadThread mReadThread;
    private Spinner spinProvince = null;
    private SerialManager mSerialManager = null;
    private static final String SERIAL_SERVICE = "serial";
    private byte[] tmpbuf = new byte[1024];

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        DeviceControl gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
        gpio.PowerOnDevice63();
        gpio.PowerOnDevice98();
        mSerialManager = (SerialManager) getSystemService(SERIAL_SERVICE);
        spinProvince = (Spinner) super.findViewById(R.id.province);
        edvreadInfor = (EditText) findViewById(R.id.edv_infor);
        sendButton = (Button) this.findViewById(R.id.button1);//x5
        sendButton1 = (Button) this.findViewById(R.id.button2);
        sendButton1.setOnClickListener(new ClickEvent());

        TimerTask task = new SynchroTimerTask();
        Timer timer = new Timer();
        timer.schedule(task, 30000);




    }

   Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] temp2 = (byte[]) msg.obj;
            if (temp2 == null) {
                Log.e("read", "null");
                return;
            }
            try {
                edvreadInfor.append(DataConversionUtils.byteArrayToAscii(temp2));
                edvreadInfor.append("\n");
                Log.e("read", "成功");
            } catch (Exception e) {
            }
        }
    };
    private class ProvOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
            //获取选择的项的值
            String sInfo = adapter.getItemAtPosition(position).toString();
            if (sInfo.equals("38400")) {
                Log.e(TAG, "1-----------------------------------");
                try {
                    mSerialPort.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
                try {
                    mSerialPort = mSerialManager.openSerialPort("/dev/ttyMT1", 38400, 8, 1, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
            }
            if (sInfo.equals("57600")) {
                Log.e(TAG, "2-----------------------------------");
                try {
                    mSerialPort.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
                try {
                    mSerialPort = mSerialManager.openSerialPort("/dev/ttyMT1", 57600, 8, 1, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
            }
            if (sInfo.equals("9600")) {
                Log.e(TAG, "3-----------------------------------");
                try {
                    mSerialPort.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
                try {
                    mSerialPort = mSerialManager.openSerialPort("/dev/ttyMT1", 9600, 8, 1, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }

            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
            String sInfo = "什么也没选！";


        }
    }

    public class SynchroTimerTask extends TimerTask {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                mSerialPort = mSerialManager.openSerialPort("/dev/ttyMT1", 38400, 8, 1, 0);
                Thread.sleep(5);
                mSerialPort.write_byte(HexString2Bytes(sendstring), 1024);
                Thread.sleep(10);
                Log.e(TAG, "step1-2-----------------------------------");
                mSerialPort.close();
                Thread.sleep(5);
                mSerialPort = mSerialManager.openSerialPort("/dev/ttyMT1", 57600, 8, 1, 0);
                Thread.sleep(5);
                mSerialPort.write_byte(HexString2Bytes(sendstring2), 1024);
                Thread.sleep(10);

                spinProvince.setOnItemSelectedListener(new ProvOnItemSelectedListener());
                sendButton.setOnClickListener(new ClickEvent());

                final Timer timer1 = new Timer();
                TimerTask task = new TimerTask() {
                    public void run() {
                        try {
                            mSerialPort.write_byte(HexString2Bytes(sendstring2), 1024);

                            Thread.sleep(5);
                            int temp1 = mSerialPort.read_byte(tmpbuf, 1024);
                            int i = 0;
                            i++;
                            Log.e(TAG, "read1------------qq---------------------");

                            if (handler == null) {
                                handler = new Handler();
                            }
                            Message msg = new Message();
                            if (temp1 == 0 || i > 10) {
                                timer1.cancel();
                                Log.e(TAG, "read------------qqqqq---------------------");
                                return;
                            }
                            msg.obj = tmpbuf;
                            handler.sendMessage(msg);
                            Log.e(TAG, "read--------------qq------------------------");

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                };
                timer1.schedule(task, 0, 2000);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v == sendButton) {
                mReadThread = new ReadThread();
                mReadThread.start();
            }
            if (v == sendButton1) {
                try {
                    mSerialPort = mSerialManager.openSerialPort("/dev/ttyMT1", 57600, 8, 1, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "read==x2");
                x2ReadThread mthread = new x2ReadThread();
                mthread.start();

            }
        }
    }

    private class x2ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
//                Thread.sleep(400);
                int temp1 = mSerialPort.read_byte(tmpbuf, 1024);
                Thread.sleep(10);
                Log.e(TAG, "read==x2x2");
                if (temp1 > 0) {
                    byte[] byt = new byte[temp1];
                    System.arraycopy(tmpbuf, 0, byt, 0, temp1);
                    if (byt != null) {
                        Message msg = new Message();
                        msg.obj = byt;
                        handler.sendMessage(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();

            try {
                Log.e(TAG, "readxx1--------------------------------------");
                mSerialPort.write_byte(HexString2Bytes(sendstring2), 1024);
                Log.e(TAG, "readxx2--------------------------------------");
                Thread.sleep(5);
                int temp1 = mSerialPort.read_byte(tmpbuf, 1024);
                Thread.sleep(10);
                if (temp1 > 0) {
                    byte[] byt = new byte[temp1];
                    System.arraycopy(tmpbuf, 0, byt, 0, temp1);
                    edvreadInfor.append(DataConversionUtils.byteArrayToAscii(byt));
                }
                edvreadInfor.append("\n");
                Log.e(TAG, "readxx3--------------------------------------");
                edvreadInfor.append("\n");
                //	Thread.sleep(5);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    }

    public byte uniteBytes(byte src0, byte src1) {
        try {
            byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}))
                    .byteValue();
            _b0 = (byte) (_b0 << 4);
            byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
                    .byteValue();
            byte ret = (byte) (_b0 ^ _b1);
            return ret;
        } catch (Exception e) {
            // TODO: handle exception

            return 0;
        }

    }

    public byte[] HexString2Bytes(String src) {
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < src.length() / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static String bytesToHexString(byte[] src, int length) {
        StringBuilder stringBuilder = new StringBuilder(" ");
        if (src == null || length <= 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            int v = src[i] >= 0 ? src[i] : src[i] + 256;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv + " ");
        }
        return stringBuilder.toString();
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder(" ");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] >= 0 ? src[i] : src[i] + 256;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv + " ");
        }
        return stringBuilder.toString();
    }

    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch) + " ";
            str = str + s4;
        }
        return str;
    }

    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];

        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    public void onDestroy() {

//        try {
//            mSerialPort.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        finish();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //			mReadThread = new ReadThread();
//			mReadThread.start();

        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉虚拟按键全屏显示
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //点击屏幕不再显示
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        // bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

}
