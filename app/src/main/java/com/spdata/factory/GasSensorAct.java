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
 * Created by suntianwei on 2017/1/13.
 */
public class GasSensorAct extends FragActBase implements View.OnClickListener {


    private CustomTitlebar titlebar;
    /**
     * 测试
     */
    private Button btnTest;
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
        }, getResources().getString(R.string.menu_gas_sensor), null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gassensor);
        initView();
        initTitlebar();
        setSwipeEnable(false);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btnTest = (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(this);
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
            case R.id.btn_test:
                try {
                    PackageManager packageManager = getPackageManager();
                    Intent intent = new Intent();
                    intent = packageManager.getLaunchIntentForPackage("cn.ccrise.mobile.cd3");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(getResources().getString(R.string.GasSensor_toast));
                    finish();
                }
                break;
            case R.id.btn_pass:
                setXml(App.KEY_GAS_SENSOR, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_GAS_SENSOR, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
