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
 * Created by lenovo_pc on 2016/10/20.
 */
public class ButtonKt40Act extends FragActBase implements View.OnClickListener {
    private CustomTitlebar titlebar;
    /**
     * 左上
     */
    private Button btn_f10;
    /**
     * BACK
     */
    private Button btn_back;
    /**
     * SCAN
     */
    private Button btn_scan;
    /**
     * UP
     */
    private Button btn_up;
    /**
     * 右上
     */
    private Button btn_camera;
    /**
     * 左下
     */
    private Button btn_f6;
    /**
     * MENU
     */
    private Button btn_menu;
    /**
     * DOWN
     */
    private Button btn_down;
    /**
     * 右下
     */
    private Button btn_f5;
    /**
     * DEL
     */
    private Button btn_del;
    /**
     * F1
     */
    private Button btn_f1;
    /**
     * F2
     */
    private Button btn_f2;
    /**
     * F3
     */
    private Button btn_f3;
    /**
     * OK
     */
    private Button btn_ok;
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
    /** * */
    private Button btn_mi;
    /**
     * 4
     */
    private Button btn_4;
    /**
     * 5
     */
    private Button btn_5;
    /**
     * 6
     */
    private Button btn_6;
    /**
     * #
     */
    private Button btn_jing;
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
        setContentView(R.layout.act_btnkt40q);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        btn_pass.setVisibility(View.GONE);
    }


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_button));
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
                isall();
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
                isall();
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (btn_ok.isPressed()) {
                btn_ok.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_ok.setPressed(false);
                return true;
            } else {
                btn_ok.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_ok.setPressed(true);
                isall();
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_F4) {
            if (btn_scan.isPressed()) {
                btn_scan.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_scan.setPressed(false);
                return true;
            } else {
                btn_scan.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_scan.setPressed(true);
                isall();
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
        } else if (keyCode == KeyEvent.KEYCODE_0) {
            if (btn_0.isPressed()) {
                btn_0.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_0.setPressed(false);
                return true;
            } else {
                btn_0.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_0.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_1) {
            if (btn_1.isPressed()) {
                btn_1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_1.setPressed(false);
                return true;
            } else {
                btn_1.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_1.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_2) {
            if (btn_2.isPressed()) {
                btn_2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_2.setPressed(false);
                return true;
            } else {
                btn_2.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_2.setPressed(true);
                isall();
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
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_4) {
            if (btn_4.isPressed()) {
                btn_4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_4.setPressed(false);
                return true;
            } else {
                btn_4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_4.setPressed(true);
                isall();
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
                isall();
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
                isall();
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
                isall();
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
                isall();
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
                isall();
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
                isall();
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
                isall();
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
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_STAR) {//*
            if (btn_mi.isPressed()) {
                btn_mi.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_mi.setPressed(false);
                return true;
            } else {
                btn_mi.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_mi.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_POUND) {//#
            if (btn_jing.isPressed()) {
                btn_jing.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_jing.setPressed(false);
                return true;
            } else {
                btn_jing.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_jing.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F1) {//#
            if (btn_f1.isPressed()) {
                btn_f1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f1.setPressed(false);
                return true;
            } else {
                btn_f1.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f1.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F2) {//#
            if (btn_f2.isPressed()) {
                btn_f2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f2.setPressed(false);
                return true;
            } else {
                btn_f2.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f2.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F3) {//#
            if (btn_f3.isPressed()) {
                btn_f3.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f3.setPressed(false);
                return true;
            } else {
                btn_f3.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f3.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F10) {//#
            if (btn_f10.isPressed()) {
                btn_f10.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f10.setPressed(false);
                return true;
            } else {
                btn_f10.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f10.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F6) {//#
            if (btn_f6.isPressed()) {
                btn_f6.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f6.setPressed(false);
                return true;
            } else {
                btn_f6.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f6.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F5) {//#
            if (btn_f5.isPressed()) {
                btn_f5.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f5.setPressed(false);
                return true;
            } else {
                btn_f5.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f5.setPressed(true);
                isall();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void isall() {
        if (btn_0.isPressed() && btn_1.isPressed() && btn_2.isPressed() && btn_3.isPressed()
                && btn_4.isPressed() && btn_5.isPressed() && btn_6.isPressed() && btn_7.isPressed()
                && btn_8.isPressed() && btn_9.isPressed() && btn_mi.isPressed() && btn_jing.isPressed()
                && btn_del.isPressed() && btn_f1.isPressed() && btn_f2.isPressed() && btn_f3.isPressed()
                && btn_ok.isPressed() && btn_back.isPressed() && btn_menu.isPressed() && btn_scan.isPressed()
                && btn_up.isPressed() && btn_f6.isPressed() && btn_f10.isPressed() && btn_down.isPressed() && btn_camera.isPressed()
                && btn_f5.isPressed()) {
            btn_pass.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_f10 = (Button) findViewById(R.id.btn_f10);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_f6 = (Button) findViewById(R.id.btn_f6);
        btn_menu = (Button) findViewById(R.id.btn_menu);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn_f5 = (Button) findViewById(R.id.btn_f5);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_f1 = (Button) findViewById(R.id.btn_f1);
        btn_f2 = (Button) findViewById(R.id.btn_f2);
        btn_f3 = (Button) findViewById(R.id.btn_f3);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_mi = (Button) findViewById(R.id.btn_mi);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_jing = (Button) findViewById(R.id.btn_jing);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_0 = (Button) findViewById(R.id.btn_0);
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
