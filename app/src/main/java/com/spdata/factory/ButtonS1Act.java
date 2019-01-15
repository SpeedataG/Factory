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
 * Created by lenovo-pc on 2017/9/13.
 */
public class ButtonS1Act extends FragActBase implements View.OnClickListener {
    private CustomTitlebar titlebar;
    /**
     * menu
     */
    private Button btn_menu;
    /**
     * back
     */
    private Button btn_back;
    /**
     * app
     */
    private Button btn_app;
    /**
     * up
     */
    private Button btn_sahng;
    /**
     * location
     */
    private Button btn_weizhi;
    /**
     * left
     */
    private Button btn_zuo;
    /**
     * right
     */
    private Button btn_you;
    /**
     * fn
     */
    private Button btn_fn;
    /**
     * down
     */
    private Button btn_xia;
    /**
     * enter
     */
    private Button btn_enter;
    /**
     * 1,.?
     */
    private Button btn_1;
    /**
     * 2ABC
     */
    private Button btn_2;
    /**
     * 3DEF
     */
    private Button btn_3;
    /**
     * ←
     */
    private Button btn_backspace;
    /**
     * 4GHI
     */
    private Button btn_4;
    /**
     * 5JKL
     */
    private Button btn_5;
    /**
     * 6MNO
     */
    private Button btn_6;
    /**
     * space
     */
    private Button btn_space;
    /**
     * 7PQRS
     */
    private Button btn_7;
    /**
     * 8TUV
     */
    private Button btn_8;
    /**
     * 9WXYZ
     */
    private Button btn_9;
    /**
     * shift
     */
    private Button btn_shift;
    /**
     * -*\t
     */
    private Button btn_mi;
    /**
     * 0_
     */
    private Button btn_0;
    /**
     * .#
     */
    private Button btn_jing;
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
        setContentView(R.layout.act_btn_s1);
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

        } else if (keyCode == KeyEvent.KEYCODE_F1) {
            if (btn_app.isPressed()) {
                btn_app.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_app.setPressed(false);
                return true;
            } else {
                btn_app.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_app.setPressed(true);
                isall();
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_F2) {
            if (btn_fn.isPressed()) {
                btn_fn.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_fn.setPressed(false);
                return true;
            } else {
                btn_fn.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_fn.setPressed(true);
                isall();
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (btn_backspace.isPressed()) {
                btn_backspace.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_backspace.setPressed(false);
                return true;
            } else {
                btn_backspace.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_backspace.setPressed(true);
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
            if (btn_sahng.isPressed()) {
                btn_sahng.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_sahng.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_sahng.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_sahng.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (btn_xia.isPressed()) {
                btn_xia.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_xia.setPressed(false);
                return true;
            } else {
                btn_xia.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_xia.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (btn_zuo.isPressed()) {
                btn_zuo.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_zuo.setPressed(false);
                return true;
            } else {
                btn_zuo.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_zuo.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (btn_you.isPressed()) {
                btn_you.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_you.setPressed(false);
                return true;
            } else {
                btn_you.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_you.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_STAR || keyCode == KeyEvent.KEYCODE_MINUS) {//*
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
        } else if (keyCode == KeyEvent.KEYCODE_POUND || keyCode == KeyEvent.KEYCODE_PERIOD) {//#
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
        } else if (keyCode == KeyEvent.KEYCODE_F3) {//#
            if (btn_weizhi.isPressed()) {
                btn_weizhi.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_weizhi.setPressed(false);
                return true;
            } else {
                btn_weizhi.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_weizhi.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {//#
            if (btn_enter.isPressed()) {
                btn_enter.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_enter.setPressed(false);
                return true;
            } else {
                btn_enter.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_enter.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {//#
            if (btn_shift.isPressed()) {
                btn_shift.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_shift.setPressed(false);
                return true;
            } else {
                btn_shift.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_shift.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_SPACE) {//#
            if (btn_space.isPressed()) {
                btn_space.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_space.setPressed(false);
                return true;
            } else {
                btn_space.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_space.setPressed(true);
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
//                && btn_del.isPressed() && btn_f1.isPressed() && btn_f2.isPressed() && btn_f3.isPressed()
//                && btn_ok.isPressed() && btn_back.isPressed() && btn_menu.isPressed() && btn_scan.isPressed()
//                && btn_up.isPressed() && btn_down.isPressed() && btn_f5.isPressed() && btn_f6.isPressed() && btn_f10.isPressed() && btn_camera.isPressed()
                ) {
//            setXml(App.KEY_BUTTON, App.KEY_FINISH);
//            finish();
        }
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_menu = (Button) findViewById(R.id.btn_menu);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_app = (Button) findViewById(R.id.btn_app);
        btn_sahng = (Button) findViewById(R.id.btn_sahng);
        btn_weizhi = (Button) findViewById(R.id.btn_weizhi);
        btn_zuo = (Button) findViewById(R.id.btn_zuo);
        btn_you = (Button) findViewById(R.id.btn_you);
        btn_fn = (Button) findViewById(R.id.btn_fn);
        btn_xia = (Button) findViewById(R.id.btn_xia);
        btn_enter = (Button) findViewById(R.id.btn_enter);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_backspace = (Button) findViewById(R.id.btn_backspace);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_space = (Button) findViewById(R.id.btn_space);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_shift = (Button) findViewById(R.id.btn_shift);
        btn_mi = (Button) findViewById(R.id.btn_mi);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_jing = (Button) findViewById(R.id.btn_jing);
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
