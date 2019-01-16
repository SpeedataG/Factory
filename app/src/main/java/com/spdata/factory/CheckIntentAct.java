package com.spdata.factory;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.serialport.DeviceControl;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import common.base.act.FragActBase;

public class CheckIntentAct extends FragActBase implements View.OnClickListener {
    private static NetworkInfo mNetworkInfo;
    private CustomTitlebar titlebar;
    /**
     * xxx
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
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_rj45), null);
    }


    DeviceControl deviceControl = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_version);
        initView();
        onWindowFocusChanged(true);
        initTitlebar();
        setSwipeEnable(false);
        btnPass.setVisibility(View.GONE);
        try {
            deviceControl = new DeviceControl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.MODEL.equals("TC01")) {
            try {
                deviceControl.MainPowerOn(63);
                deviceControl.MainPowerOn(86);
                deviceControl.MainPowerOn(8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            writeFile("2");
            try {
                deviceControl.Expand2PowerOn(4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tvVersionInfor.setText(getResources().getString(R.string.CheckIntentAct_infor1));
        handler.postDelayed(runnable, 200);

    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isNetworkConnected()) {
                int netType = mNetworkInfo.getType();
                int netSubtype = mNetworkInfo.getSubtype();
                if (netType == ConnectivityManager.TYPE_ETHERNET) {
                    //以太网
//                    if (mNetworkInfo.isConnected()) {
                    tvVersionInfor.setText(getResources().getString(R.string.CheckIntentAct_infor2));
                    setXml(App.KEY_INTENET, App.KEY_FINISH);
                    finish();
//                    } else {
//                        tvVersionInfor.setText("RJ45网络未连接，但已接入网线");
//
//                    }
                } else {
                    tvVersionInfor.setText(getResources().getString(R.string.CheckIntentAct_infor1));
                }
            } else {
                tvVersionInfor.setText(getResources().getString(R.string.CheckIntentAct_infor3)+"请重新拔插网线");
            }
            handler.postDelayed(runnable, 200);
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.MODEL.equals("TC01")) {
            try {
                deviceControl.MainPowerOff(63);
                deviceControl.MainPowerOff(86);
                deviceControl.MainPowerOff(8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            writeFile("0");
            try {
                deviceControl.Expand2PowerOff(4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        handler.removeCallbacks(runnable);

    }

    /**
     * 判断是否有网络连接
     *
     * @return
     */
    public boolean isNetworkConnected() {//true是链接，false是没链接
        ConnectivityManager mConnectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        } else {
            return false;
        }
    }

    private void writeFile(String type) {
        try {
            File file = new File("/sys/class/misc/hwoper/usb_route/");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(type);
            bw.flush();
            bw.close();
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
            showToast(getResources().getString(R.string.CheckIntentAct_toast));
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
                setXml(App.KEY_INTENET, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_INTENET, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
