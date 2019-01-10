package com.spdata.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

public class ButtonSd35Act extends FragActBase implements View.OnClickListener {


    boolean isOneClick = true;
    private CustomTitlebar titlebar;
    /**
     * VOL_UP
     */
    private Button btn_vol_up;
    /**
     * VOL_DOWN
     */
    private Button btn_vol_down;
    /**
     * SCAN
     */
    private Button btn_left_F4;
    /**
     * SCAN
     */
    private Button btn_right_F4;
    /**
     * UP
     */
    private Button btn_up;
    /**
     * SCAN
     */
    private Button btn_zhong;
    /**
     * BACK
     */
    private Button btn_back;
    /**
     * MENU
     */
    private Button btn_menu;
    /**
     * LEFT
     */
    private Button btn_left;
    /**
     * RIGHT
     */
    private Button btn_right;
    /**
     * OK
     */
    private Button btn_ok;
    /**
     * screen
     */
    private Button btn_12;
    /**
     * DWON
     */
    private Button btn_dwon;
    /**
     * ←
     */
    private Button btn_13;
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
    /** * */
    private Button btn_mi;
    /**
     * 0
     */
    private Button btn_0;
    /**
     * #
     */
    private Button btn_jing;
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
     * F4
     */
    private Button btn_f4;
    /**
     * aA
     */
    private Button btn_aA;
    /**
     * -
     */
    private Button btn_14;
    /**
     * -
     */
    private Button btn_15;
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
        setContentView(R.layout.act_btn_sd35_layout);
        initView(); initTitlebar();
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
        if (keyCode == KeyEvent.KEYCODE_F7) {
            if (btn_left_F4.isPressed()) {
                btn_left_F4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_right_F4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_left_F4.setPressed(false);
                btn_right_F4.setPressed(false);

            } else {
                btn_left_F4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_right_F4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_left_F4.setPressed(true);
                btn_right_F4.setPressed(true);
            }

        } else if (keyCode == KeyEvent.KEYCODE_F6) {
            if (btn_zhong.isPressed()) {
                btn_zhong.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_zhong.setPressed(false);
                return true;
            } else {
                btn_zhong.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_zhong.setPressed(true);
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

        } else if (keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
            if (btn_menu.isPressed()) {
                btn_menu.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_menu.setPressed(false);
                return true;
            } else {
                btn_menu.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_menu.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
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
            if (btn_dwon.isPressed()) {
                btn_dwon.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_dwon.setPressed(false);
                return true;
            } else {
                btn_dwon.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_dwon.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (btn_right.isPressed()) {
                btn_right.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_right.setPressed(false);
                return true;
            } else {
                btn_right.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_right.setPressed(true);
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
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (btn_right.isPressed()) {
                btn_right.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_right.setPressed(false);
                return true;
            } else {
                btn_right.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_right.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (btn_left.isPressed()) {
                btn_left.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_left.setPressed(false);
                return true;
            } else {
                btn_left.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_left.setPressed(true);
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

        } else if (keyCode == KeyEvent.KEYCODE_F8) {
            if (btn_12.isPressed()) {
                btn_12.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_12.setPressed(false);
                return true;
            } else {
                btn_12.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_12.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (btn_13.isPressed()) {
                btn_13.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_13.setPressed(false);
                return true;
            } else {
                btn_13.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_13.setPressed(true);
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
        } else if (keyCode == KeyEvent.KEYCODE_STAR) {//*
            if (btn_mi.isPressed()) {
                btn_mi.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_mi.setPressed(false);
                return true;
            } else {
                btn_mi.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_mi.setPressed(true);
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
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F4) {//#
            if (btn_f4.isPressed()) {
                btn_f4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f4.setPressed(false);
                return true;
            } else {
                btn_f4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f4.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {//#
            if (btn_aA.isPressed()) {
                btn_aA.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_aA.setPressed(false);
                return true;
            } else {
                btn_aA.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_aA.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F9) {//#
            if (btn_14.isPressed()) {
                btn_14.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_14.setPressed(false);
                return true;
            } else {
                btn_14.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_14.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F10) {//#
            if (btn_15.isPressed()) {
                btn_15.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_15.setPressed(false);
                return true;
            } else {
                btn_15.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_15.setPressed(true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_vol_up = (Button) findViewById(R.id.btn_vol_up);
        btn_vol_down = (Button) findViewById(R.id.btn_vol_down);
        btn_left_F4 = (Button) findViewById(R.id.btn_left_F4);
        btn_right_F4 = (Button) findViewById(R.id.btn_right_F4);
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_zhong = (Button) findViewById(R.id.btn_zhong);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_menu = (Button) findViewById(R.id.btn_menu);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_12 = (Button) findViewById(R.id.btn_12);
        btn_dwon = (Button) findViewById(R.id.btn_dwon);
        btn_13 = (Button) findViewById(R.id.btn_13);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_mi = (Button) findViewById(R.id.btn_mi);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_jing = (Button) findViewById(R.id.btn_jing);
        btn_f1 = (Button) findViewById(R.id.btn_f1);
        btn_f2 = (Button) findViewById(R.id.btn_f2);
        btn_f3 = (Button) findViewById(R.id.btn_f3);
        btn_f4 = (Button) findViewById(R.id.btn_f4);
        btn_aA = (Button) findViewById(R.id.btn_aA);
        btn_14 = (Button) findViewById(R.id.btn_14);
        btn_15 = (Button) findViewById(R.id.btn_15);
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
