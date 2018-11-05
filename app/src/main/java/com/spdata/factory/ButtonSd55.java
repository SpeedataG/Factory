package com.spdata.factory;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
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

/**
 * Created by 42040 on 2018/11/5.
 */

@EActivity(R.layout.act_btnsd55_layout)
public class ButtonSd55 extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_up;
    @ViewById
    Button btn_down;
    @ViewById
    Button btn_left_F4;
    @ViewById
    Button btn_close;
    @ViewById
    Button btn_right_F4;
    @ViewById
    Button btn_home;
    boolean isOneClick = true;

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        btnPass.setVisibility(View.GONE);
    }

    @Click
    void btnNotPass() {
        setXml(App.KEY_BUTTON, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_BUTTON, App.KEY_FINISH);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showToast(keyCode + "");
        if (keyCode == 135) {
            if (btn_left_F4.isPressed()) {
                btn_left_F4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_left_F4.setPressed(false);
            } else {
                btn_left_F4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_left_F4.setPressed(true);
                ispress();
            }

        } else if (keyCode == 134) {
            if (btn_right_F4.isPressed()) {
                btn_right_F4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_right_F4.setPressed(false);

            } else {
                btn_left_F4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_right_F4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_left_F4.setPressed(true);
                ispress();
                btn_right_F4.setPressed(true);
            }

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (btn_up.isPressed()) {
                btn_up.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_up.setPressed(false);
                return true;
            } else {
                btn_up.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_up.setPressed(true);
                ispress();
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (btn_down.isPressed()) {
                btn_down.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_down.setPressed(false);
                return true;
            } else {
                btn_down.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_down.setPressed(true);
                ispress();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void ispress() {
        if (btn_left_F4.isPressed() && btn_down.isPressed() && btn_up.isPressed()) {
            btnPass.setVisibility(View.VISIBLE);
        }
    }

    /*产生numSize位16进制的数*/
    public static String getRandomValue(int numSize) {
        String str = "";
        for (int i = 0; i < numSize; i++) {
            char temp = 0;
            int key = (int) (Math.random() * 2);
            switch (key) {
                case 0:
                    temp = (char) (Math.random() * 10 + 48);//产生随机数字
                    break;
                case 1:
                    temp = (char) (Math.random() * 6 + 'a');//产生a-f
                    break;
                default:
                    break;
            }
            str = str + temp;
        }
        return str;
    }


}
