package com.spdata.factory;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
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
 * Created by suntianwei on 2017/1/23.
 */
@EActivity(R.layout.act_camera_usb)
public class CameraUSBAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvVersionInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_test;
    @Click
    void btn_test() {
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent();
        intent = packageManager.getLaunchIntentForPackage("com.camera.app");
        startActivity(intent);
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
        }, "矿灯摄像头", null);
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
