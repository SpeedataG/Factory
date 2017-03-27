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

@EActivity(R.layout.act_dcd3_button)
public class DCD3ButtonAct extends FragActBase {

    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_ok;
    @ViewById
    Button btn_sure;
    @ViewById
    Button btn_top;
    @ViewById
    Button btn_bootm;
    @ViewById
    Button btn_left;
    @ViewById
    Button btn_right;
    @ViewById
    Button btn_help;

    boolean isOneClick = true;

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
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (btn_ok.isPressed()) {
                btn_ok.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_ok.setPressed(false);
                return true;
            } else {
                btn_ok.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_ok.setPressed(true);
                return true;
            }

        }else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (btn_top.isPressed()) {
                btn_top.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_top.setPressed(false);
                return true;
            } else {
                btn_top.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_top.setPressed(true);
                return true;
            }

        }
        else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (btn_bootm.isPressed()) {
                btn_bootm.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_bootm.setPressed(false);
                return true;
            } else {
                btn_bootm.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_bootm.setPressed(true);
                return true;
            }

        }else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (btn_right.isPressed()) {
                btn_right.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_right.setPressed(false);
                return true;
            } else {
                btn_right.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_right.setPressed(true);
                return true;
            }

        }
        else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (btn_left.isPressed()) {
                btn_left.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_left.setPressed(false);
                return true;
            } else {
                btn_left.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_left.setPressed(true);
                return true;
            }

        }
//        else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//            if (btn_help.isPressed()) {
//                btn_help.setBackgroundColor(Color.parseColor("#CEC7C7"));
//                btn_help.setPressed(false);
//                return true;
//            } else {
//                btn_help.setBackgroundColor(Color.parseColor("#0AF229"));
//                btn_help.setPressed(true);
//                return true;
//            }
//
//        }
//        else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//            if (btn_sure.isPressed()) {
//                btn_sure.setBackgroundColor(Color.parseColor("#CEC7C7"));
//                btn_sure.setPressed(false);
//                return true;
//            } else {
//                btn_sure.setBackgroundColor(Color.parseColor("#0AF229"));
//                btn_sure.setPressed(true);
//                return true;
//            }
//
//        }

        return super.onKeyDown(keyCode, event);
    }
}
