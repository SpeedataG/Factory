package com.spdata.factory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

public class USBAct extends FragActBase implements View.OnClickListener {


    private CustomTitlebar titlebar;
    /**
     * xxx
     */
    private TextView tvInfor;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_usb);
        initView(); initTitlebar();
        setSwipeEnable(false);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        registerReceiver(usBroadcastReceiver, filter);
        tvInfor.setText("请插入usb");
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

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
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
            case R.id.btn_pass:setXml(App.KEY_USB, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:setXml(App.KEY_USB, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
