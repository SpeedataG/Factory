package com.spdata.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;


public class ButtonM08Act extends FragActBase implements View.OnClickListener {

    private CustomTitlebar titlebar;
    /**
     * F1
     */
    private Button btn_f1;
    /**
     * F2
     */
    private Button btn_f2;
    /**
     * V-
     */
    private Button btn_down;
    /**
     * V+
     */
    private Button btn_up;
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
        setContentView(R.layout.act_btn_m08);
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

        } else if (keyCode == KeyEvent.KEYCODE_F1) {
            if (btn_f1.isPressed()) {
                btn_f1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f1.setPressed(false);
                return true;
            } else {
                btn_f1.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f1.setPressed(true);
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

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_f1 = (Button) findViewById(R.id.btn_f1);
        btn_f2 = (Button) findViewById(R.id.btn_f2);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn_up = (Button) findViewById(R.id.btn_up);
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
