package com.spdata.factory;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * Created by suntianwei on 2017/1/23.
 */
public class CameraUSBAct extends FragActBase implements View.OnClickListener {
    private CustomTitlebar titlebar;
    /**
     * 相机测试
     */
    private Button btn_test;
    /**
     * 成功
     */
    private Button btn_pass;
    /**
     * 失败
     */
    private Button btn_not_pass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_camera_usb);
        initView();
        initTitlebar();
        setSwipeEnable(false);
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_camera_usb), null);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_test = (Button) findViewById(R.id.btn_test);
        btn_test.setOnClickListener(this);
        btn_pass = (Button) findViewById(R.id.btn_pass);
        btn_pass.setOnClickListener(this);
        btn_not_pass = (Button) findViewById(R.id.btn_not_pass);
        btn_not_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_test:
                try {
                    PackageManager packageManager = getPackageManager();
                    Intent intent = packageManager.getLaunchIntentForPackage("com.camera.app");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(getResources().getString(R.string.camera_toast));
                    finish();
                }
                break;
            case R.id.btn_pass:
                setXml(App.KEY_CAMERA_USB, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_CAMERA_USB, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
