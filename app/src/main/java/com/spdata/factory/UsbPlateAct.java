package com.spdata.factory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;
import common.utils.DeviceControl;

/**
 * Created by lenovo_pc on 2016/9/3.
 */

public class UsbPlateAct extends FragActBase implements View.OnClickListener {

    private DeviceControl gpio;
    private CustomTitlebar titlebar;
    /**
     * 请插入U盘
     */
    private TextView tvVersionInfor;
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
        setContentView(R.layout.act_usbmlate);
        initView();
        initTitlebar();
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("U盘");
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String ss = (String) msg.obj;
            switch (msg.what) {
                case 0:
                    tvVersionInfor.setText(ss);
                    break;
                case 1:
                    tvVersionInfor.setText(ss);
                    break;
                case 2:
                    tvVersionInfor.setText(ss);
                    setXml(App.KEY_USBPLATE, App.KEY_FINISH);
                    finish();
                    break;
            }
        }
    };
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {
                handler.sendMessage(handler.obtainMessage(0, "U盘已挂载,请拔出"));
            } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                handler.sendMessage(handler.obtainMessage(1, "扫描U盘"));
            } else if (action.equals(Intent.ACTION_MEDIA_REMOVED) || action.equals(Intent.ACTION_MEDIA_EJECT)) {
                handler.sendMessage(handler.obtainMessage(2, "U盘已移除"));
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
        if (Build.MODEL.equals("M08")) {
            gpio.PowerOnDevice72();
        } else {
            gpio.PowerOffDevice63();
            gpio.PowerOnDevice99();
            gpio.PowerOnDevice98();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_CHECKING);//检查正在检查
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);//以挂载
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);//:表明SDCard 被卸载前己被移除
        filter.addDataScheme("file");
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        if (Build.MODEL.equals("M08")) {
            gpio.PowerOffDevice72();
        } else {
            gpio.PowerOffDevice98();
            gpio.PowerOffDevice63();
            gpio.PowerOffDevice99();
        }
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvVersionInfor = (TextView) findViewById(R.id.tv_version_infor);
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
                setXml(App.KEY_USBPLATE, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_USBPLATE, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
