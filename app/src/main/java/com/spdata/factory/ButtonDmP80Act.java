package com.spdata.factory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * Created by lenovo-pc on 2018/3/9.
 */
public class ButtonDmP80Act extends FragActBase implements View.OnClickListener {
    private CustomTitlebar titlebar;
    /**
     * light_up
     */
    private Button btn_back;
    /**
     * light_dow
     */
    private Button btn_menu;
    /**
     * F5
     */
    private Button btn_ok;
    /**
     * F1
     */
    private Button btn_up;
    /**
     * F2
     */
    private Button btn_down;
    /**
     * Vol_up
     */
    private Button btn_f1;
    /**
     * Vol_down
     */
    private Button btn_f2;
    /**
     * F5
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
        setContentView(R.layout.act_dmp80_layout);
        initView();
        initTitlebar();
        setSwipeEnable(false);
    }



    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("按键测试");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.yysgm.key_f1_sd");
        intentFilter.addAction("com.yysgm.key_f2_sd");
        intentFilter.addAction("com.yysgm.key_f4_sd");
        intentFilter.addAction("com.middlekey.longpressed");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "com.yysgm.key_f1_sd":
                    if (btn_up.isPressed()) {
                        btn_up.setBackgroundColor(Color.parseColor("#CEC7C7"));
                        btn_up.setPressed(false);
                    } else {
                        btn_up.setBackgroundColor(Color.parseColor("#0AF229"));
                        btn_up.setPressed(true);
                    }
                    break;
                case "com.yysgm.key_f2_sd":
                    if (btn_down.isPressed()) {
                        btn_down.setBackgroundColor(Color.parseColor("#CEC7C7"));
                        btn_down.setPressed(false);
                    } else {
                        btn_down.setBackgroundColor(Color.parseColor("#0AF229"));
                        btn_down.setPressed(true);
                    }
                    break;
                case "com.yysgm.key_f4_sd":
                    if (btn_f4.isPressed()) {
                        btn_f4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                        btn_f4.setPressed(false);
                    } else {
                        btn_f4.setBackgroundColor(Color.parseColor("#0AF229"));
                        btn_f4.setPressed(true);
                    }
                    break;
                case "com.middlekey.longpressed":
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
                    break;
            }
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showToast(keyCode + "");
        if (keyCode == KeyEvent.KEYCODE_F1) {
            if (btn_up.isPressed()) {
                btn_up.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_up.setPressed(false);
                return true;
            } else {
                btn_up.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_up.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F2) {
            if (btn_down.isPressed()) {
                btn_down.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_down.setPressed(false);
                return true;
            } else {
                btn_down.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_down.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BRIGHTNESS_UP) {
            if (btn_back.isPressed()) {
                btn_back.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_back.setPressed(false);
                return true;
            } else {
                btn_back.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_back.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BRIGHTNESS_DOWN) {
            if (btn_menu.isPressed()) {
                btn_menu.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_menu.setPressed(false);
            } else {
                btn_menu.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_menu.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_F5) {
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
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (btn_f1.isPressed()) {
                btn_f1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f1.setPressed(false);
            } else {
                btn_f1.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f1.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
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
