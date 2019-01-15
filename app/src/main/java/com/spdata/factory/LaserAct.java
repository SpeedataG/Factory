package com.spdata.factory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import common.base.act.FragActBase;
import common.utils.DeviceControl;

/**
 * Created by lenovo_pc on 2016/8/12.
 */
public class LaserAct extends FragActBase implements View.OnClickListener {


    private CustomTitlebar titlebar;
    /**
     * 打开
     */
    private Button btnOn;
    /**
     * 关闭
     */
    private Button btnOff;
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
        titlebar.setAttrs(getResources().getString(R.string.menu_laser));
    }


    DeviceControl gpio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_laser);
        initView();
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

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btnOn = (Button) findViewById(R.id.btn_on);
        btnOn.setOnClickListener(this);
        btnOff = (Button) findViewById(R.id.btn_off);
        btnOff.setOnClickListener(this);
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
            case R.id.btn_on:
                gpio.PowerOnLaser();
                break;
            case R.id.btn_off:
                gpio.PowerOffLaser();
                break;
            case R.id.btn_pass:
                setXml(App.KEY_LASER, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_LASER, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
