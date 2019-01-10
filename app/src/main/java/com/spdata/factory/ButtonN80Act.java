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
 * Created by suntianwei on 2017/3/6.
 */
public class ButtonN80Act extends FragActBase implements View.OnClickListener {
    private CustomTitlebar titlebar;
    /**
     * VOLUME_UP
     */
    private Button btn_vup;
    /**
     * F1
     */
    private Button btn_f1;
    /**
     * VOLUME_DOWN
     */
    private Button btn_vdown;
    /**
     * F2
     */
    private Button btn_f2;
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
        setContentView(R.layout.act_btn_n80);
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
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (btn_vup.isPressed()) {
                btn_vup.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_vup.setPressed(false);
                return true;
            } else {
                btn_vup.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_vup.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (btn_vdown.isPressed()) {
                btn_vdown.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_vdown.setPressed(false);
                return true;
            } else {
                btn_vdown.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_vdown.setPressed(true);
                return true;
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
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_vup = (Button) findViewById(R.id.btn_vup);
        btn_f1 = (Button) findViewById(R.id.btn_f1);
        btn_vdown = (Button) findViewById(R.id.btn_vdown);
        btn_f2 = (Button) findViewById(R.id.btn_f2);
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
