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
 * Created by lenovo-pc on 2018/3/21.
 */
@EActivity(R.layout.act_btn_ct_layout)
public class ButtonCtAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_back;
    @ViewById
    Button btn_key1;
    @ViewById
    Button btn_key2;
    @ViewById
    Button btn_key3;
    @ViewById
    Button btn_key4;
    @ViewById
    Button btn_key5;
    @ViewById
    Button btn_key6;
    @ViewById
    Button btn_key7;
    @ViewById
    Button btn_key8;
    @ViewById
    Button btn_key9;
    @ViewById
    Button btn_key0;
    @ViewById
    Button btn_period;
    @ViewById
    Button btn_minus;
    @ViewById
    Button btn_del;
    @ViewById
    Button btn_menu;
    @ViewById
    Button btn_f2;


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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (btn_back.isPressed()) {
                btn_back.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_back.setPressed(false);
                return true;
            } else {
                btn_back.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_back.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_1) {
            if (btn_key1.isPressed()) {
                btn_key1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key1.setPressed(false);
                return true;
            } else {
                btn_key1.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key1.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_2) {
            if (btn_key2.isPressed()) {
                btn_key2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key2.setPressed(false);
                return true;
            } else {
                btn_key2.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key2.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_3) {
            if (btn_key3.isPressed()) {
                btn_key3.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key3.setPressed(false);
                return true;
            } else {
                btn_key3.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key3.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_4) {
            if (btn_key4.isPressed()) {
                btn_key4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key4.setPressed(false);
                return true;
            } else {
                btn_key4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key4.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_5) {
            if (btn_key5.isPressed()) {
                btn_key5.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key5.setPressed(false);
                return true;
            } else {
                btn_key5.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key5.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_6) {
            if (btn_key6.isPressed()) {
                btn_key6.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key6.setPressed(false);
                return true;
            } else {
                btn_key6.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key6.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_7) {
            if (btn_key7.isPressed()) {
                btn_key7.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key7.setPressed(false);
                return true;
            } else {
                btn_key7.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key7.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_8) {
            if (btn_key8.isPressed()) {
                btn_key8.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key8.setPressed(false);
                return true;
            } else {
                btn_key8.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key8.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_9) {
            if (btn_key9.isPressed()) {
                btn_key9.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key9.setPressed(false);
                return true;
            } else {
                btn_key9.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key9.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_0) {
            if (btn_key0.isPressed()) {
                btn_key0.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key0.setPressed(false);
                return true;
            } else {
                btn_key0.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key0.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_PERIOD) {
            if (btn_period.isPressed()) {
                btn_period.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_period.setPressed(false);
                return true;
            } else {
                btn_period.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_period.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_MINUS) {
            if (btn_minus.isPressed()) {
                btn_minus.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_minus.setPressed(false);
                return true;
            } else {
                btn_minus.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_minus.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (btn_del.isPressed()) {
                btn_del.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_del.setPressed(false);
                return true;
            } else {
                btn_del.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_del.setPressed(true);
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
        } else if (keyCode == KeyEvent.KEYCODE_F2) {
            if (btn_f2.isPressed()) {
                btn_f2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f2.setPressed(false);
                return true;
            } else {
                btn_f2.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f2.setPressed(true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
