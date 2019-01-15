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
 * Created by lenovo_pc on 2016/10/11.
 */
public class ButtonH500Act extends FragActBase implements View.OnClickListener {
    private CustomTitlebar titlebar;
    /**
     * SOS
     */
    private Button btn_sos;
    /**
     * VOLUME_UP
     */
    private Button btn_up;
    /**
     * FN
     */
    private Button btn_fn;
    /**
     * VOLUME_DOWN
     */
    private Button btn_down;
    /**
     * MENU
     */
    private Button btn_menu;
    /**
     * HOME
     */
    private Button btn_home;
    /**
     * BACK
     */
    private Button btn_back;
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

        setContentView(R.layout.act_btnh500);
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

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
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
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            if (btn_home.isPressed()) {
                btn_home.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_home.setPressed(false);
                return true;
            } else {
                btn_home.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_home.setPressed(true);
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
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_sos = (Button) findViewById(R.id.btn_sos);
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_fn = (Button) findViewById(R.id.btn_fn);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn_menu = (Button) findViewById(R.id.btn_menu);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_back = (Button) findViewById(R.id.btn_back);
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
