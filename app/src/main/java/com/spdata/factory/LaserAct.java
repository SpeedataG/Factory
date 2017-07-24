package com.spdata.factory;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.DeviceControl;

/**
 * Created by lenovo_pc on 2016/8/12.
 */
@EActivity(R.layout.act_laser)
public class LaserAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tv_infor;
    @ViewById
    Button btn_on;
    @ViewById
    Button btn_off;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_LASER, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_LASER, App.KEY_FINISH);
        finish();
    }

    @Click
    void btn_on() {
        gpio.PowerOnLaser();
    }

    @Click
    void btn_off() {
        gpio.PowerOffLaser();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("激光");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }

    DeviceControl gpio;

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        gpio = new DeviceControl("/proc/driver/scan");

    }

    public void Writer(String s) {
        File file = new File("proc/driver/scan");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(s);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
