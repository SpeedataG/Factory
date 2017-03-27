package com.spdata.factory;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
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
 * Created by lenovo_pc on 2016/10/11.
 */
@EActivity(R.layout.act_btnh500)
public class ButtonH500Act extends FragActBase {
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
    Button btn_fn;
    @ViewById
    Button btn_sos;
    @ViewById
    Button btn_back;
    @ViewById
    Button btn_menu;
    @ViewById
    Button btn_home;

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
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
        if (keyCode == KeyEvent.KEYCODE_F12) {
            if (btn_fn.isPressed()) {
                btn_fn.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_fn.setPressed(false);
            } else {
                btn_fn.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_fn.setPressed(true);
            }

        } else if (keyCode == KeyEvent.KEYCODE_F11) {
            if (btn_sos.isPressed()) {
                btn_sos.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_sos.setPressed(false);
                return true;
            } else {
                btn_sos.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_sos.setPressed(true);
                return true;
            }

        }else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
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
        }else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (btn_back.isPressed()) {
                btn_back.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_back.setPressed(false);
                return true;
            } else {
                btn_back.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_back.setPressed(true);
                return true;
            }
        }else if (keyCode == KeyEvent.KEYCODE_HOME) {
            if (btn_home.isPressed()) {
                btn_home.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_home.setPressed(false);
                return true;
            } else {
                btn_home.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_home.setPressed(true);
                return true;
            }
        }else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (btn_menu.isPressed()) {
                btn_menu.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_menu.setPressed(false);
                return true;
            } else {
                btn_menu.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_menu.setPressed(true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
