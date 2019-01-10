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
 * Created by lenovo_pc on 2016/9/29.
 */
public class ButtonKT50Act extends FragActBase implements View.OnClickListener {

    boolean isOneClick = true;
    private CustomTitlebar titlebar;
    /**
     * VOLU_UP
     */
    private Button btn_vol_up;
    /**
     * VOLU_DOWN
     */
    private Button btn_vol_down;
    /**
     * DELETE
     */
    private Button btn_del;
    /**
     * ENTER
     */
    private Button btn_enter;
    /**
     * SCAN
     */
    private Button btn_scan;
    /**
     * 1
     */
    private Button btn_1;
    /**
     * 2
     */
    private Button btn_2;
    /**
     * 3
     */
    private Button btn_3;
    /**
     * 4
     */
    private Button btn_4;
    /**
     * 5
     */
    private Button btn_5;
    /**
     * UP
     */
    private Button btn_up;
    /**
     * 6
     */
    private Button btn_6;
    /**
     * 7
     */
    private Button btn_7;
    /**
     * 8
     */
    private Button btn_8;
    /**
     * 9
     */
    private Button btn_9;
    /**
     * 0
     */
    private Button btn_0;
    /**
     * DOWN
     */
    private Button btn_down;
    /**
     * MENU
     */
    private Button btn_menu;
    /**
     * BACK
     */
    private Button btn_back;
    /**
     * SPACE
     */
    private Button btn_space;
    /**
     * DISPLAY
     */
    private Button btn_display;
    /**
     * SCAN
     */
    private Button btn_scan2;
    /**
     * CAMERA
     */
    private Button btn_camera;
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
        setContentView(R.layout.act_btnkt50);
        initView();
        initTitlebar();
        setSwipeEnable(false);
    }


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("按键测试");
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

        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (btn_enter.isPressed()) {
                btn_enter.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_enter.setPressed(false);
                return true;
            } else {
                btn_enter.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_enter.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_F5 || keyCode == KeyEvent.KEYCODE_F4) {
            if (btn_scan.isPressed() && btn_scan2.isPressed()) {
                btn_scan.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_scan.setPressed(false);
                btn_scan2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_scan2.setPressed(false);
                return true;
            } else {
                btn_scan.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_scan.setPressed(true);
                btn_scan2.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_scan2.setPressed(true);
                return true;
            }


        } else if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (btn_del.isPressed()) {
                btn_del.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_del.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_del.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_del.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_0) {
            if (btn_0.isPressed()) {
                btn_0.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_0.setPressed(false);

                return true;
            } else {
                btn_0.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_0.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_1) {
            if (btn_1.isPressed()) {
                btn_1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_1.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_1.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_1.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_2) {
            if (btn_2.isPressed()) {
                btn_2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_2.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_2.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_2.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_3) {
            if (btn_3.isPressed()) {
                btn_3.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_3.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_3.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_3.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_4) {
            if (btn_4.isPressed()) {
                btn_4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_4.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_4.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_5) {
            if (btn_5.isPressed()) {
                btn_5.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_5.setPressed(false);
                return true;
            } else {
                btn_5.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_5.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_6) {
            if (btn_6.isPressed()) {
                btn_6.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_6.setPressed(false);
                return true;
            } else {
                btn_6.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_6.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_7) {
            if (btn_7.isPressed()) {
                btn_7.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_7.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_7.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_7.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_8) {
            if (btn_8.isPressed()) {
                btn_8.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_8.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_8.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_8.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_9) {
            if (btn_9.isPressed()) {
                btn_9.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_9.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_9.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_9.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (btn_up.isPressed()) {
                btn_up.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_up.setPressed(false);
                showToast(keyCode + "");
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
        } else if (keyCode == KeyEvent.KEYCODE_CAMERA) {
            if (btn_camera.isPressed()) {
                btn_camera.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_camera.setPressed(false);
                return true;
            } else {
                btn_camera.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_camera.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (btn_vol_up.isPressed()) {
                btn_vol_up.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_vol_up.setPressed(false);
                return true;
            } else {
                btn_vol_up.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_vol_up.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (btn_vol_down.isPressed()) {
                btn_vol_down.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_vol_down.setPressed(false);
                return true;
            } else {
                btn_vol_down.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_vol_down.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_PERIOD) {
            if (btn_space.isPressed()) {
                btn_space.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_space.setPressed(false);
                return true;
            } else {
                btn_space.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_space.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F1) {
            if (btn_display.isPressed()) {
                btn_display.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_display.setPressed(false);
                return true;
            } else {
                btn_display.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_display.setPressed(true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void isall() {
        if (btn_0.isPressed() && btn_1.isPressed()) {
            ff();


        }
    }

    private void ff() {
        setXml(App.KEY_BUTTON, App.KEY_FINISH);
        finish();
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_vol_up = (Button) findViewById(R.id.btn_vol_up);
        btn_vol_down = (Button) findViewById(R.id.btn_vol_down);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_enter = (Button) findViewById(R.id.btn_enter);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn_menu = (Button) findViewById(R.id.btn_menu);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_space = (Button) findViewById(R.id.btn_space);
        btn_display = (Button) findViewById(R.id.btn_display);
        btn_scan2 = (Button) findViewById(R.id.btn_scan2);
        btn_camera = (Button) findViewById(R.id.btn_camera);
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
//
}
