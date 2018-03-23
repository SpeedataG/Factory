package com.spdata.factory;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import common.base.act.FragActBase;
import common.event.ViewMessage;

@EActivity(R.layout.activity_finger_print)
public class FingerPrint extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_zh;
    @ViewById
    Button btn_id;
    private Intent intent;
    private PackageManager packageManager;

    @Click
    void btn_id() {
        try {
            packageManager = getPackageManager();
//        intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.routon.iDR410SDKDemo");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            showToast("未找到指定应用");
            finish();
        }
    }

    @Click
    void btn_zh() {
        try {
            packageManager = getPackageManager();
//        intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.hongda.S580");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            showToast("未找到指定应用");
            finish();
        }
    }

    @Click
    void btnNotPass() {
        setXml(App.KEY_GAS_SENSOR, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_GAS_SENSOR, App.KEY_FINISH);
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
                finish();
            }
        }, "指纹&ID2", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);

    }

}
