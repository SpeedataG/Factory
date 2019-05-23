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
 * Created by lenovo-pc on 2018/3/21.
 */
public class ButtonCtAct extends FragActBase implements View.OnClickListener {


    private CustomTitlebar titlebar;
    /**
     * FN
     */
    private Button btn_f2;
    /**
     * 1
     */
    private Button btn_key1;
    /**
     * 2
     */
    private Button btn_key2;
    /**
     * 3
     */
    private Button btn_key3;
    /**
     * 4
     */
    private Button btn_key4;
    /**
     * 5
     */
    private Button btn_key5;
    /**
     * 6
     */
    private Button btn_key6;
    /**
     * 7
     */
    private Button btn_key7;
    /**
     * 8
     */
    private Button btn_key8;
    /**
     * 9
     */
    private Button btn_key9;
    /**
     * 0
     */
    private Button btn_key0;
    /**
     * period
     */
    private Button btn_period;
    /**
     * minus
     */
    private Button btn_minus;
    /**
     * BS  BACK
     */
    private Button btn_bs;
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

        setContentView(R.layout.act_btn_ct_layout);
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
        if (keyCode == KeyEvent.KEYCODE_1) {
            if (btn_key1.isPressed()) {
                btn_key1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key1.setPressed(false);
                return true;
            } else {
                btn_key1.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key1.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_2) {
            if (btn_key2.isPressed()) {
                btn_key2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key2.setPressed(false);
                return true;
            } else {
                btn_key2.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key2.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_3) {
            if (btn_key3.isPressed()) {
                btn_key3.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key3.setPressed(false);
                return true;
            } else {
                btn_key3.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key3.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_4) {
            if (btn_key4.isPressed()) {
                btn_key4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key4.setPressed(false);
                return true;
            } else {
                btn_key4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key4.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_5) {
            if (btn_key5.isPressed()) {
                btn_key5.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key5.setPressed(false);
                return true;
            } else {
                btn_key5.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key5.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_6) {
            if (btn_key6.isPressed()) {
                btn_key6.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key6.setPressed(false);
                return true;
            } else {
                btn_key6.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key6.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_7) {
            if (btn_key7.isPressed()) {
                btn_key7.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key7.setPressed(false);
                return true;
            } else {
                btn_key7.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key7.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_8) {
            if (btn_key8.isPressed()) {
                btn_key8.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key8.setPressed(false);
                return true;
            } else {
                btn_key8.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key8.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_9) {
            if (btn_key9.isPressed()) {
                btn_key9.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key9.setPressed(false);
                return true;
            } else {
                btn_key9.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key9.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_0) {
            if (btn_key0.isPressed()) {
                btn_key0.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_key0.setPressed(false);
                return true;
            } else {
                btn_key0.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_key0.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_PERIOD) {
            if (btn_period.isPressed()) {
                btn_period.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_period.setPressed(false);
                return true;
            } else {
                btn_period.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_period.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_MINUS) {
            if (btn_minus.isPressed()) {
                btn_minus.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_minus.setPressed(false);
                return true;
            } else {
                btn_minus.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_minus.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (btn_bs.isPressed()) {
                btn_bs.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_bs.setPressed(false);
                return true;
            } else {
                btn_bs.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_bs.setPressed(true);
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
        btn_f2 = (Button) findViewById(R.id.btn_f2);
        btn_key1 = (Button) findViewById(R.id.btn_key1);
        btn_key2 = (Button) findViewById(R.id.btn_key2);
        btn_key3 = (Button) findViewById(R.id.btn_key3);
        btn_key4 = (Button) findViewById(R.id.btn_key4);
        btn_key5 = (Button) findViewById(R.id.btn_key5);
        btn_key6 = (Button) findViewById(R.id.btn_key6);
        btn_key7 = (Button) findViewById(R.id.btn_key7);
        btn_key8 = (Button) findViewById(R.id.btn_key8);
        btn_key9 = (Button) findViewById(R.id.btn_key9);
        btn_key0 = (Button) findViewById(R.id.btn_key0);
        btn_period = (Button) findViewById(R.id.btn_period);
        btn_minus = (Button) findViewById(R.id.btn_minus);
        btn_bs = (Button) findViewById(R.id.btn_bs);
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
