package com.spdata.factory;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;
import common.crash.utils.SysInfoUtil;


public class VersionAct extends FragActBase implements View.OnClickListener {


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_version);
        initView();
        onWindowFocusChanged(true);
        initTitlebar();
        setSwipeEnable(false);
        TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String Versionum = Build.DISPLAY;
        if (Build.MODEL.equals("N80") || Build.MODEL.equals("S1_35") || Build.MODEL.equals("H5_53") || Build.MODEL.equals("H5")
                || Build.MODEL.equals("S1") || Build.MODEL.equals("H5_35") || Build.MODEL.equals("S550")) {
            tvVersionInfor.setText("型号：" + Build.MODEL
                    + "\n"
                    + "系统版本号:" + Versionum
                    + "\n"
                    + "APK版本号:"
                    + SysInfoUtil.getVersionName(this)
                    + "\n");
        } else {
            tvVersionInfor.setText("型号：" + Build.MODEL
                    + "\n"
                    + "系统版本号:" + Versionum
                    + "\n"
                    + "SN号:" + Build.SERIAL
                    + "\n"
                    + "APK版本号:"
                    + SysInfoUtil.getVersionName(this)
                    + "\n");
        }
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, "版本信息", null);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
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
                setXml(App.KEY_VERSION, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_VERSION, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}



