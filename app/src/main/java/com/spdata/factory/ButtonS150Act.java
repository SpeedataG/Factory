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
 * Created by lenovo_pc on 2016/9/12.
 */
public class ButtonS150Act extends FragActBase implements View.OnClickListener {
    private CustomTitlebar titlebar;
    /**
     * VOL_UP
     */
    private Button btn_up;
    /**
     * RESET
     */
    private Button btn_reset;
    /**
     * BEIDOU
     */
    private Button btn_beidou;
    /**
     * MENU
     */
    private Button btn_DIANYUAN;
    /**
     * VOL_DOWN
     */
    private Button btn_down;
    /**
     * CAMERA
     */
    private Button btn_camera;
    /**
     * SOS
     */
    private Button btn_sos;
    /**
     * MENU
     */
    private Button btn_f5;
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
     * PHONE
     */
    private Button btn_phone;
    /**
     * MORE
     */
    private Button btn_more;
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
        setContentView(R.layout.act_s150);
        initView();initTitlebar();
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_button));
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

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_beidou = (Button) findViewById(R.id.btn_beidou);
        btn_DIANYUAN = (Button) findViewById(R.id.btn_DIANYUAN);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_sos = (Button) findViewById(R.id.btn_sos);
        btn_f5 = (Button) findViewById(R.id.btn_f5);
        btn_menu = (Button) findViewById(R.id.btn_menu);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_phone = (Button) findViewById(R.id.btn_phone);
        btn_more = (Button) findViewById(R.id.btn_more);
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
//    public void onAttachedToWindow() {
//        this.getWindow().setType(WindowManager.LayoutParams.MEMORY_TYPE_CHANGED);
//        super.onAttachedToWindow();
//    }
}
