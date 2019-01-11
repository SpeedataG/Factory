package com.spdata.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.serialport.DeviceControl;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.IOException;

import common.base.act.FragActBase;

/**
 * Created by 42040 on 2018/11/16.
 */
public class IndicatorSk80LightAct extends FragActBase implements View.OnClickListener {

    private DeviceControl deviceControl;
    private CustomTitlebar titlebar;
    /**
     * 左绿
     */
    private Button btn_l_green;
    /**
     * 右绿
     */
    private Button btn_r_green;
    /**
     * 右红
     */
    private Button btn_r_red;
    /**
     * 右下黄
     */
    private Button btn_1;
    /**
     * 右下蓝
     */
    private Button btn_2;
    /**
     * 右下红
     */
    private Button btn_3;
    /**
     * 右下绿
     */
    private Button btn_4;
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
        setContentView(R.layout.act_sk80_light);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        try {
            deviceControl = new DeviceControl(DeviceControl.POWER_EXTERNAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isPressed = true;
    private boolean isPressed2 = true;
    private boolean isPressed3 = true;
    private boolean isPressed4 = true;
    private boolean isPressed7 = true;
    private boolean isPressed5 = true;
    private boolean isPressed6 = true;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_indicator_light));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            deviceControl.Expand2PowerOff(0);
            deviceControl.Expand2PowerOff(1);
            deviceControl.Expand2PowerOff(2);
            deviceControl.Expand2PowerOff(3);
            deviceControl.ExpandPowerOff(8);
            deviceControl.ExpandPowerOff(9);
            deviceControl.ExpandPowerOff(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_l_green = (Button) findViewById(R.id.btn_l_green);
        btn_l_green.setOnClickListener(this);
        btn_r_green = (Button) findViewById(R.id.btn_r_green);
        btn_r_green.setOnClickListener(this);
        btn_r_red = (Button) findViewById(R.id.btn_r_red);
        btn_r_red.setOnClickListener(this);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
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
            case R.id.btn_l_green:
                if (isPressed) {
                    btn_l_green.setBackgroundColor(Color.parseColor("#0AF229"));
                    isPressed = false;
                    try {
                        deviceControl.ExpandPowerOn(8);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        deviceControl.ExpandPowerOff(8);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    btn_l_green.setBackgroundColor(Color.parseColor("#CEC7C7"));
                    isPressed = true;
                }
                break;
            case R.id.btn_r_green:
                if (isPressed2) {
                    btn_r_green.setBackgroundColor(Color.parseColor("#0AF229"));
                    isPressed2 = false;
                    try {
                        deviceControl.ExpandPowerOn(10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    btn_r_green.setBackgroundColor(Color.parseColor("#CEC7C7"));
                    isPressed2 = true;
                    try {
                        deviceControl.ExpandPowerOff(10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_r_red:
                if (isPressed3) {
                    btn_r_red.setBackgroundColor(Color.parseColor("#0AF229"));
                    isPressed3 = false;
                    try {
                        deviceControl.ExpandPowerOn(9);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    btn_r_red.setBackgroundColor(Color.parseColor("#CEC7C7"));
                    isPressed3 = true;
                    try {
                        deviceControl.ExpandPowerOff(9);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.btn_1:
                if (isPressed4) {
                    btn_1.setBackgroundColor(Color.parseColor("#0AF229"));
                    isPressed4 = false;
                    try {
                        deviceControl.Expand2PowerOn(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    btn_1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                    isPressed4 = true;
                    try {
                        deviceControl.Expand2PowerOff(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.btn_2:
                if (isPressed7) {
                    btn_2.setBackgroundColor(Color.parseColor("#0AF229"));
                    isPressed7 = false;
                    try {
                        deviceControl.Expand2PowerOn(1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    btn_2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                    isPressed7 = true;
                    try {
                        deviceControl.Expand2PowerOff(1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_3:
                if (isPressed5) {
                    btn_3.setBackgroundColor(Color.parseColor("#0AF229"));
                    isPressed5 = false;
                    try {
                        deviceControl.Expand2PowerOn(2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    btn_3.setBackgroundColor(Color.parseColor("#CEC7C7"));
                    isPressed5 = true;
                    try {
                        deviceControl.Expand2PowerOff(2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_4:
                if (isPressed6) {
                    btn_4.setBackgroundColor(Color.parseColor("#0AF229"));
                    isPressed6 = false;
                    try {
                        deviceControl.Expand2PowerOn(3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    btn_4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                    isPressed6 = true;
                    try {
                        deviceControl.Expand2PowerOff(3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_pass:
                setXml(App.KEY_INDICATOR_LIGHT, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
