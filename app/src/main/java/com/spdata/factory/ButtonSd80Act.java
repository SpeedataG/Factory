package com.spdata.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;
import com.speedata.postest.PosC;

import common.base.act.FragActBase;

/**
 * Created by 42040 on 2018/6/20.
 */
public class ButtonSd80Act extends FragActBase implements View.OnClickListener {

    private CustomTitlebar titlebar;
    /**
     * VOLUME_DOWN
     */
    private Button btn_down;
    /**
     * VOLUME_UP
     */
    private Button btn_up;
    /**
     * BACK
     */
    private Button btn_back;
    /**
     * F2
     */
    private Button btn_y;
    /**
     * F1
     */
    private Button btn_x;
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
        setContentView(R.layout.act_btn_sd80);
        initView();  initTitlebar();
        setSwipeEnable(false);
        PosC.home(true, this);
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
        } else if (keyCode == KeyEvent.KEYCODE_F1) {
            if (btn_x.isPressed()) {
                btn_x.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_x.setPressed(false);
                return true;
            } else {
                btn_x.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_x.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F2) {
            if (btn_y.isPressed()) {
                btn_y.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_y.setPressed(false);
                return true;
            } else {
                btn_y.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_y.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            showToast("caidanjian");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_y = (Button) findViewById(R.id.btn_y);
        btn_x = (Button) findViewById(R.id.btn_x);
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
