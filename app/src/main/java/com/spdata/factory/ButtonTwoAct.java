package com.spdata.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * Created by lenovo_pc on 2016/8/20.
 */
public class ButtonTwoAct extends FragActBase implements View.OnClickListener {

    CustomTitlebar titlebar;

    Button btnPass;

    Button btnNotPass;

    Button btn_vol_up;

    Button btn_dwon;

    Button btn_camera;

    Button btn_ok;

    Button btn_fn;

    boolean isOneClick = true;
    /**
     * X5只需要前两项，X2需要全部测试
     */
    private TextView tvInfor;
    /**
     * FN
     */
    private Button btn_vol_fn;


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_button));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_button_two);
        initView();
        initTitlebar();
        setSwipeEnable(false);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        showToast(keyCode + "");
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (btn_ok.isPressed()) {
                btn_ok.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_ok.setPressed(false);
            } else {
                btn_ok.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_ok.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (btn_dwon.isPressed()) {
                btn_dwon.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_dwon.setPressed(false);
                return true;
            } else {
                btn_dwon.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_dwon.setPressed(true);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showToast(keyCode + "");
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            showToast(keyCode + "");
            if (btn_vol_up.isPressed()) {
                btn_vol_up.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_vol_up.setPressed(false);
                return true;
            } else {
                btn_vol_up.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_vol_up.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_CAMERA) {
            if (btn_camera.isPressed()) {
                btn_camera.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_camera.setPressed(false);
            } else {
                btn_camera.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_camera.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (btn_ok.isPressed()) {
                btn_ok.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_ok.setPressed(false);
            } else {
                btn_ok.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_ok.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_F3 || keyCode == KeyEvent.KEYCODE_F5) {
            if (btn_fn.isPressed()) {
                btn_fn.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_fn.setPressed(false);
            } else {
                btn_fn.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_fn.setPressed(true);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
        btnPass = (Button) findViewById(R.id.btn_pass);
        btnPass.setOnClickListener(this);
        btnNotPass = (Button) findViewById(R.id.btn_not_pass);
        btnNotPass.setOnClickListener(this);
        btn_dwon = (Button) findViewById(R.id.btn_dwon);
        btn_vol_fn = (Button) findViewById(R.id.btn_vol_fn);
        btn_vol_up = (Button) findViewById(R.id.btn_vol_up);
        btn_fn = (Button) findViewById(R.id.btn_fn);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_camera = (Button) findViewById(R.id.btn_camera);
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
