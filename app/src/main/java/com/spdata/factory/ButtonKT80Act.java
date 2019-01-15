package com.spdata.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * Created by lenovo_pc on 2016/9/5.
 */
public class ButtonKT80Act extends FragActBase implements View.OnClickListener {
    private CustomTitlebar titlebar;
    /**
     * BACK
     */
    private Button btn_back;
    /**
     * MENU
     */
    private Button btn_menu;
    /**
     * OK
     */
    private Button btn_ok;
    /**
     * DPAD_UP
     */
    private Button btn_up;
    /**
     * DPAD_DOWN
     */
    private Button btn_down;
    /**
     * F1
     */
    private Button btn_f1;
    /**
     * F2
     */
    private Button btn_f2;
    /**
     * OK
     */
    private Button btn_ok2;
    /**
     * F3
     */
    private Button btn_f3;
    /**
     * F4
     */
    private Button btn_f4;
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
        setContentView(R.layout.act_kt80);
        initView();
        initTitlebar();
        setSwipeEnable(false);
    }


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_button));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showToast(keyCode + "");
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (btn_up.isPressed()) {
                btn_up.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_up.setPressed(false);
                return true;
            } else {
                btn_up.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_up.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
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
            } else {
                btn_menu.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_menu.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (btn_ok.isPressed()) {
                btn_ok.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_ok2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_ok.setPressed(false);
                btn_ok2.setPressed(false);
            } else {
                btn_ok.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_ok2.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_ok.setPressed(true);
                btn_ok2.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_F1) {
            if (btn_f1.isPressed()) {
                btn_f1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f1.setPressed(false);
            } else {
                btn_f1.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f1.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_F2) {
            if (btn_f2.isPressed()) {
                btn_f2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f2.setPressed(false);
            } else {
                btn_f2.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f2.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_F3) {
            if (btn_f3.isPressed()) {
                btn_f3.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f3.setPressed(false);
            } else {
                btn_f3.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f3.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_F4) {
            if (btn_f4.isPressed()) {
                btn_f4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f4.setPressed(false);
            } else {
                btn_f4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f4.setPressed(true);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_menu = (Button) findViewById(R.id.btn_menu);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn_f1 = (Button) findViewById(R.id.btn_f1);
        btn_f2 = (Button) findViewById(R.id.btn_f2);
        btn_ok2 = (Button) findViewById(R.id.btn_ok2);
        btn_f3 = (Button) findViewById(R.id.btn_f3);
        btn_f4 = (Button) findViewById(R.id.btn_f4);
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
            case R.id.btn_pass:
                setXml(App.KEY_BUTTON, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_BUTTON, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
