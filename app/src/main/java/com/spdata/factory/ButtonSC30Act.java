package com.spdata.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

public class ButtonSC30Act extends FragActBase implements View.OnClickListener {


    boolean isOneClick = true;
    private CustomTitlebar titlebar;
    /**
     * 录音
     */
    private Button btnRecord;
    /**
     * 摄像机
     */
    private Button btnVideo;
    /**
     * 相机
     */
    private Button btnCamera;
    /**
     * PTT
     */
    private Button btnPtt;
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
        setContentView(R.layout.act_sc30_button);
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
        if (keyCode == KeyEvent.KEYCODE_F1) {
            if (btnRecord.isPressed()) {
                btnRecord.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnRecord.setPressed(false);
                return true;
            } else {
                btnRecord.setBackgroundColor(Color.parseColor("#0AF229"));
                btnRecord.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_F2) {
            if (btnVideo.isPressed()) {
                btnVideo.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnVideo.setPressed(false);
                return true;
            } else {
                btnVideo.setBackgroundColor(Color.parseColor("#0AF229"));
                btnVideo.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_F5) {
            if (btnCamera.isPressed()) {
                btnCamera.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnCamera.setPressed(false);
                return true;
            } else {
                btnCamera.setBackgroundColor(Color.parseColor("#0AF229"));
                btnCamera.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_F4) {
            if (btnPtt.isPressed()) {
                btnPtt.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btnPtt.setPressed(false);
                return true;
            } else {
                btnPtt.setBackgroundColor(Color.parseColor("#0AF229"));
                btnPtt.setPressed(true);
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        titlebar = findViewById(R.id.titlebar);
        btnRecord = findViewById(R.id.btn_record);
        btnVideo = findViewById(R.id.btn_video);
        btnCamera = findViewById(R.id.btn_camera);
        btnPtt = findViewById(R.id.btn_ptt);
        btn_pass = findViewById(R.id.btn_pass);
        btn_pass.setOnClickListener(this);
        btn_not_pass = findViewById(R.id.btn_not_pass);
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
