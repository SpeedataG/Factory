package com.spdata.factory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.south.SDKMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;

/**
 * Created by lenovo_pc on 2016/8/12.
 */
public class EepromAct extends FragActBase implements View.OnClickListener {

    private CustomTitlebar titlebar;
    /**
     * E2PROM Test
     */
    private TextView textViewE;
    private TextView textViewS;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("EEROM");
    }

    private SDKMethod nv_device;
    private int offsetbuffer;
    private byte[] wrbuffer = new byte[8];
    private byte[] rebuffer = new byte[8];
    private String Wrdata = " ";
    private int lengthbuffer, i;
    private static final char SPACE = 0x20;
    byte[] buftmp = new byte[8];
    private String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_eeprom);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        textViewS.setText("Read Magic Number:\n");
        nv_device = new SDKMethod();
        offsetbuffer = 80;
        lengthbuffer = 8;
        nv_device.readE2(offsetbuffer, rebuffer, lengthbuffer);
        String accept_show = bytesToHexString(rebuffer, lengthbuffer);
        textViewS.append("  " + accept_show + "\n");
        byte[] a = new byte[1];
        a[0] = 'A';
        byte[] b = new byte[1];
        b[0] = 'B';
        byte[] d = new byte[1];
        d[0] = 'D';
        byte[] e = new byte[1];
        e[0] = 'E';
        byte[] f = new byte[1];
        f[0] = 'F';
        buftmp[0] = uniteBytes(d[0], e[0]);
        buftmp[1] = uniteBytes(a[0], d[0]);
        buftmp[2] = uniteBytes(b[0], e[0]);
        buftmp[3] = uniteBytes(a[0], f[0]);
        buftmp[4] = uniteBytes(d[0], e[0]);
        buftmp[5] = uniteBytes(a[0], d[0]);
        buftmp[6] = uniteBytes(b[0], e[0]);
        buftmp[7] = uniteBytes(a[0], f[0]);
        if (Arrays.equals(buftmp, rebuffer)) {
            textViewS.append("Magic Number OK!\n");
        } else {
            textViewS.append("Magic Number Error\n");
        }
        TimerTask task = new SynchroTimerTask();
        Timer timer = new Timer();
        timer.schedule(task, 1000);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            textViewS.append(msg.obj.toString());
            return;
        }
    };

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        textViewE = (TextView) findViewById(R.id.textViewE);
        textViewS = (TextView) findViewById(R.id.textViewS);
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
                setXml(App.KEY_EEPROM, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_EEPROM, App.KEY_UNFINISH);
                finish();
                break;
        }
    }

    public class SynchroTimerTask extends TimerTask {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                Wrdata = removeAllSpace("00 00 00 00 00 00 00 00");
                wrbuffer = HexString2Bytes(Wrdata);
                for (int j = 0; j < 81; ) {
                    nv_device.writeE2(j, wrbuffer, 8);
                    j = j + 8;
                }
                offsetbuffer = 80;
                Wrdata = removeAllSpace("DE AD BE AF DE AD BE AF");
                wrbuffer = HexString2Bytes(Wrdata);
                nv_device.writeE2(offsetbuffer, wrbuffer, lengthbuffer);

                nv_device.readE2(offsetbuffer, rebuffer, lengthbuffer);
                String accept_show2 = bytesToHexString(rebuffer, lengthbuffer);
                if (Arrays.equals(buftmp, rebuffer)) {
                    name = "\nRetry:\n " + accept_show2 + "\nMagic Number OK!\n";
                } else {
                    name = "\nRetry:\n " + accept_show2 + "\nMagic Number Error!\n";
                }
                if (handler == null) {
                    handler = new Handler();
                }
                Message message = Message.obtain();
                message.obj = name;
                handler.sendMessage(message);
            } catch (Exception e) {
            }
        }
    }

    public String removeAllSpace(String s) {
        String endString = "";
        StringBuilder builder = new StringBuilder(endString);
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c != SPACE) {
                builder.append(String.valueOf(c));
            }
        }
        return builder.toString();
    }

    public byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
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
}
