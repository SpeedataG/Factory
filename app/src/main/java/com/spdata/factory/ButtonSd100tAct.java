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
 * @author zzc
 * @date 2019/4/3
 */
public class ButtonSd100tAct extends FragActBase implements View.OnClickListener {
    private CustomTitlebar titlebar;
    /**
     * F1
     */
    private Button btnF1;
    /**
     * F2
     */
    private Button btnF2;
    /**
     * F3
     */
    private Button btnF3;
    /**
     * OK
     */
    private Button btnOk;
    /**
     * left
     */
    private Button btnLeft;
    /**
     * up
     */
    private Button btnUp;
    /**
     * down
     */
    private Button btnDown;
    /**
     * right
     */
    private Button btnRight;
    /**
     * BACK
     */
    private Button btnBack;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_btn_sd100t);
        initView();
        initTitlebar();
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_button));
    }


    boolean isOneClick = true;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showToast(keyCode + "");
        if (keyCode == KeyEvent.KEYCODE_F1) {
            if (btnF1.isPressed()) {
                btnF1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnF1.setPressed(false);
            } else {
                btnF1.setBackgroundColor(Color.parseColor("#0AF229"));
                btnF1.setPressed(true);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_F2) {
            if (btnF2.isPressed()) {
                btnF2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnF2.setPressed(false);
            } else {
                btnF2.setBackgroundColor(Color.parseColor("#0AF229"));
                btnF2.setPressed(true);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_F3) {
            if (btnF3.isPressed()) {
                btnF3.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnF3.setPressed(false);
            } else {
                btnF3.setBackgroundColor(Color.parseColor("#0AF229"));
                btnF3.setPressed(true);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (btnOk.isPressed()) {
                btnOk.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnOk.setPressed(false);
            } else {
                btnOk.setBackgroundColor(Color.parseColor("#0AF229"));
                btnOk.setPressed(true);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (btnLeft.isPressed()) {
                btnLeft.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnLeft.setPressed(false);
            } else {
                btnLeft.setBackgroundColor(Color.parseColor("#0AF229"));
                btnLeft.setPressed(true);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (btnUp.isPressed()) {
                btnUp.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnUp.setPressed(false);
            } else {
                btnUp.setBackgroundColor(Color.parseColor("#0AF229"));
                btnUp.setPressed(true);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (btnDown.isPressed()) {
                btnDown.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnDown.setPressed(false);
            } else {
                btnDown.setBackgroundColor(Color.parseColor("#0AF229"));
                btnDown.setPressed(true);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (btnRight.isPressed()) {
                btnRight.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnRight.setPressed(false);
            } else {
                btnRight.setBackgroundColor(Color.parseColor("#0AF229"));
                btnRight.setPressed(true);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (btnBack.isPressed()) {
                btnBack.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnBack.setPressed(false);
            } else {
                btnBack.setBackgroundColor(Color.parseColor("#0AF229"));
                btnBack.setPressed(true);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        titlebar = findViewById(R.id.titlebar);
        btnF1 = findViewById(R.id.btn_f1);
        btnF2 = findViewById(R.id.btn_f2);
        btnF3 = findViewById(R.id.btn_f3);
        btnOk = findViewById(R.id.btn_ok);
        btnLeft = findViewById(R.id.btn_left);
        btnUp = findViewById(R.id.btn_up);
        btnDown = findViewById(R.id.btn_down);
        btnRight = findViewById(R.id.btn_right);
        btnBack = findViewById(R.id.btn_back);
        btnPass = findViewById(R.id.btn_pass);
        btnPass.setOnClickListener(this);
        btnNotPass = findViewById(R.id.btn_not_pass);
        btnNotPass.setOnClickListener(this);
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
