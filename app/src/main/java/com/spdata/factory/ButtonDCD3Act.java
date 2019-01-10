package com.spdata.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

public class ButtonDCD3Act extends FragActBase implements View.OnClickListener {


    boolean isOneClick = true;
    private CustomTitlebar titlebar;
    /**
     * TOP
     */
    private Button btn_top;
    /**
     * BOOTM
     */
    private Button btn_bootm;
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
     * 确认
     */
    private Button btn_sure;
    /**
     * 救援
     */
    private Button btn_help;
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
        setContentView(R.layout.act_dcd3_button);
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

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (btn_top.isPressed()) {
                btn_top.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_top.setPressed(false);
                return true;
            } else {
                btn_top.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_top.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (btn_bootm.isPressed()) {
                btn_bootm.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_bootm.setPressed(false);
                return true;
            } else {
                btn_bootm.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_bootm.setPressed(true);
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

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_top = (Button) findViewById(R.id.btn_top);
        btn_top.setOnClickListener(this);
        btn_bootm = (Button) findViewById(R.id.btn_bootm);
        btn_bootm.setOnClickListener(this);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_left.setOnClickListener(this);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setOnClickListener(this);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(this);
        btn_help = (Button) findViewById(R.id.btn_help);
        btn_help.setOnClickListener(this);
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
            case R.id.btn_top:
                break;
            case R.id.btn_bootm:
                break;
            case R.id.btn_left:
                break;
            case R.id.btn_right:
                break;
            case R.id.btn_ok:
                break;
            case R.id.btn_sure:
                break;
            case R.id.btn_help:
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
