package com.spdata.factory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
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

@EActivity(R.layout.act_usb)
public class USBAct extends FragActBase {

    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_USB, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_USB, App.KEY_FINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
            }
        }, "USB测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    private final int LATE = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LATE:
                    setXml(App.KEY_USB, App.KEY_FINISH);
                    finish();
                    break;
            }

        }
    };

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        registerReceiver(usBroadcastReceiver, filter);
        tvInfor.setText("请插入usb");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usBroadcastReceiver);//关闭接收器
    }

    private boolean is;
    private final static String ACTION = "android.hardware.usb.action.USB_STATE";
    BroadcastReceiver usBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION)) {
                boolean connected = intent.getExtras().getBoolean("connected");
                if (connected) {
                    tvInfor.setText("\n" + getResources().getString(
                            R.string.usb_state_connect) + "\n");
                    is = true;
                } else {
                    tvInfor.setText("\n" + getResources().getString(
                            R.string.usb_state_no_connect) + "\n");
                    is = false;
                }
                if (!is) {
                    tvInfor.append("请接入USB进行测试！");
//                    setXml(App.KEY_USB, App.KEY_UNFINISH);
//                    finish();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                Message message = new Message();
                                message.what = LATE;
                                handler.sendMessage(message);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        }
    };
}
