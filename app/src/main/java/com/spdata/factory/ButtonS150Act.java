package com.spdata.factory;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
 * Created by lenovo_pc on 2016/9/12.
 */
@EActivity(R.layout.act_s150)
public class ButtonS150Act extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_up;
    @ViewById
    Button btn_beidou;
    @ViewById
    Button btn_down;
    @ViewById
    Button btn_sos;
    @ViewById
    Button btn_camera;
    @ViewById
    Button btn_menu;
    @ViewById
    Button btn_home;
    @ViewById
    Button btn_back;
    @ViewById
    Button btn_phone;
    @ViewById
    Button btn_more;

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

    @AfterViews
    protected void main() {
        initTitlebar();
    }

    boolean isOneClick = true;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showToast(keyCode + "");
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (btn_up.isPressed()) {
                btn_up.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_up.setPressed(false);
                return true;
            } else {
                btn_up.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_up.setPressed(true);
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
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (btn_back.isPressed()) {
                btn_back.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_back.setPressed(false);
                return true;
            } else {
                btn_back.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_back.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (btn_menu.isPressed()) {
                btn_menu.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_menu.setPressed(false);
                return true;
            } else {
                btn_menu.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_menu.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F5) {
            if (btn_beidou.isPressed()) {
                btn_beidou.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_beidou.setPressed(false);
            } else {
                btn_beidou.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_beidou.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_CAMERA) {
            if (btn_camera.isPressed()) {
                btn_camera.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_camera.setPressed(false);
            } else {
                btn_camera.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_camera.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_F2) {
            if (btn_phone.isPressed()) {
                btn_phone.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_phone.setPressed(false);
                return true;
            } else {
                btn_phone.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_phone.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            if (isOneClick) {
                btn_home.setBackgroundColor(Color.parseColor("#CEC7C7"));
                isOneClick = false;
                return true;
            } else {
                btn_home.setBackgroundColor(Color.parseColor("#0AF229"));
                isOneClick = true;
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F4) {
            if (btn_sos.isPressed()) {
                btn_sos.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_sos.setPressed(false);
            } else {
                btn_sos.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_sos.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_F3) {
            if (btn_more.isPressed()) {
                btn_more.setBackgroundColor(Color.parseColor("#CEC7C7"));

                btn_more.setPressed(false);
                return true;
            } else {
                btn_more.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_more.setPressed(true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
//    public void onAttachedToWindow() {
//        this.getWindow().setType(WindowManager.LayoutParams.MEMORY_TYPE_CHANGED);
//        super.onAttachedToWindow();
//    }
}
