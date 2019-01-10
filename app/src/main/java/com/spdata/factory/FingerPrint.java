package com.spdata.factory;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.serialport.DeviceControl;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.FingerUtil.FingerTypes;
import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.IOException;

import common.base.act.FragActBase;

public class FingerPrint extends FragActBase implements View.OnClickListener {
    TextView text_show;
    private Intent intent;
    private PackageManager packageManager;
    private DeviceControl mdeviceControl;
    private CustomTitlebar titlebar;
    private TextView textShow;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

//    @Click
//    void btn_id() {
//        try {
//            packageManager = getPackageManager();
////        intent = new Intent();
//            intent = packageManager.getLaunchIntentForPackage("com.routon.iDR410SDKDemo");
//            startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            showToast("未找到指定应用");
//            finish();
//        }
//    }

    //    @Click
//    void btn_zh() {
//        try {
//            packageManager = getPackageManager();
////        intent = new Intent();
//            intent = packageManager.getLaunchIntentForPackage("com.hongda.S580");
//            startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            showToast("未找到指定应用");
//            finish();
//        }

//    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, "指纹", null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            mdeviceControl.MainPowerOff(93);
            mdeviceControl.MainPowerOff(126);
            mdeviceControl.MainPowerOff(94);
            mdeviceControl.MainPowerOff(99);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        try {
            //sd80 金色指纹上电
            mdeviceControl = new DeviceControl(DeviceControl.PowerType.MAIN);
            mdeviceControl.MainPowerOn(126);
            mdeviceControl.MainPowerOn(93);
            mdeviceControl.MainPowerOn(99);
            mdeviceControl.MainPowerOn(94);
        } catch (IOException e) {
            e.printStackTrace();
        }
        showLoading("检测模块中……");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (FingerTypes.getrwusbdevices(FingerPrint.this)) {
                            case 0:
                                showToast("无指纹模块！");
                                hideLoading();
                                text_show.setText("无指纹模块！");
                                break;
                            case 1:
                                hideLoading();
                                showToast("查找到公安指纹模块！");
                                text_show.setText("查找到公安指纹模块！");
                                break;
                            case 2:
                                hideLoading();
                                showToast("查找到民用指纹模块！");
                                text_show.setText("查找到民用指纹模块！");
                                break;
                            case 3:
                                hideLoading();
                                showToast("查找到金色指纹模块！");
                                text_show.setText("查找到金色指纹模块！");
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        }).start();

    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        textShow = (TextView) findViewById(R.id.text_show);
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
                setXml(App.KEY_FINGER_PRINT, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_FINGER_PRINT, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
