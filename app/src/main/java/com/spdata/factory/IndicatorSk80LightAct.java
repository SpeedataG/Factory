package com.spdata.factory;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import common.base.act.FragActBase;
import common.event.ViewMessage;

/**
 * Created by 42040 on 2018/11/16.
 */
@EActivity(R.layout.act_sk80_light)
public class IndicatorSk80LightAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_l_green;
    @ViewById
    Button btn_r_green;
    @ViewById
    Button btn_r_red;
    @ViewById
    Button btn_1;
    @ViewById
    Button btn_2;
    @ViewById
    Button btn_3;
    @ViewById
    Button btn_4;
    private android.serialport.DeviceControl deviceControl;


    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        try {
            deviceControl = new android.serialport.DeviceControl(android.serialport.DeviceControl.POWER_EXTERNAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isPressed = true;

    @Click
    void btn_l_green() {
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


    }

    private boolean isPressed2 = true;

    @Click
    void btn_r_green() {
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


    }

    private boolean isPressed3 = true;

    @Click
    void btn_r_red() {
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


    }

    private boolean isPressed4 = true;

    @Click
    void btn_1() {
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


    }

    private boolean isPressed7 = true;

    @Click
    void btn_2() {
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


    }

    private boolean isPressed5 = true;

    @Click
    void btn_3() {
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


    }

    private boolean isPressed6 = true;

    @Click
    void btn_4() {
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


    }

    @Click
    void btnNotPass() {
        setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_INDICATOR_LIGHT, App.KEY_FINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("按键测试");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

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
}
