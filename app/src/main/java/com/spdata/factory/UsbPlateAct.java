package com.spdata.factory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.serialport.DeviceControl;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import common.base.act.FragActBase;
import common.event.ViewMessage;

/**
 * Created by lenovo_pc on 2016/9/3.
 */
@EActivity(R.layout.act_usbmlate)
public class UsbPlateAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvVersionInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private DeviceControl gpio;

    private int count = 0;

    @Click
    void btnNotPass() {
        setXml(App.KEY_USBPLATE, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_USBPLATE, App.KEY_FINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("U盘");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    MyHandler mhandler;
    UsbStatesReceiver usbstates;

    @AfterViews
    protected void main() {
        initTitlebar();
        gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
        gpio.PowerOffDevice63();
        gpio.PowerOnDevice99();
        gpio.PowerOnDevice98();
        mhandler = new MyHandler();
        usbstates = new UsbStatesReceiver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpio.PowerOffDevice98();
        gpio.PowerOffDevice63();
        gpio.PowerOffDevice99();
    }

    @Override
    protected void onStart() {
        super.onStart();
        usbstates.registerReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usbstates.unregisterReceiver();
    }

    class MyHandler extends Handler {
        public MyHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.arg1 == 0x00021) {
                tvVersionInfor.setText("U盘已接入,请拔出");
            } else if (msg.arg1 == 0x00022) {
                tvVersionInfor.setText("U盘已拔出");
                count = 1;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (count == 1) {
                                setXml(App.KEY_USBPLATE, App.KEY_FINISH);
                                finish();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    public class UsbStatesReceiver extends BroadcastReceiver {
        UsbPlateAct plateAct;
        public static final int USB_STATE_MSG = 0x00020;
        public static final int USB_STATE_ON = 0x00021;
        public static final int USB_STATE_OFF = 0x00022;
        public IntentFilter filter = new IntentFilter();

        public UsbStatesReceiver(Context context) {
            plateAct = (UsbPlateAct) context;
            filter.addAction(Intent.ACTION_MEDIA_CHECKING);
            filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            filter.addAction(Intent.ACTION_MEDIA_EJECT);
            filter.addAction(Intent.ACTION_MEDIA_REMOVED);
            filter.addDataScheme("file");
        }

        public Intent registerReceiver() {
            return plateAct.registerReceiver(this, filter);
        }

        public void unregisterReceiver() {
            plateAct.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (plateAct.mhandler == null) {
                return;
            }
            Message msg = new Message();
            msg.what = USB_STATE_MSG;
            if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED) ||
                    intent.getAction().equals(Intent.ACTION_MEDIA_CHECKING)) {
                msg.arg1 = USB_STATE_ON;
            } else {
                msg.arg1 = USB_STATE_OFF;
            }
            plateAct.mhandler.sendMessage(msg);
        }
    }
}
