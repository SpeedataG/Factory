package com.spdata.factory;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * Created by lenovo-pc on 2018/3/22.
 */
public class ButtonAll extends FragActBase implements View.OnClickListener {
    boolean isOneClick = true;
    private CustomTitlebar mTitlebar;
    /**
     * 成功
     */
    private Button mBtnPass;
    /**
     * 失败
     */
    private Button mBtnNotPass;
    /**
     * VOLUME_UP
     */
    private Button btn_up;
    /**
     * VOLUME_DOWN
     */
    private Button btn_down;
    private Button btn_close;
    /**
     * SCAN
     */
    private Button btn_left_F4;
    /**
     * SCAN
     */
    private Button btn_right_F4;
    private TextView tv_infor2;
    private TextView tv_infor;

    @Override
    protected void initTitlebar() {
        mTitlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        mTitlebar.setAttrs(getResources().getString(R.string.menu_button));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        btn_close.setVisibility(View.GONE);
        btn_down.setVisibility(View.GONE);
        btn_left_F4.setVisibility(View.GONE);
        btn_right_F4.setVisibility(View.GONE);
        btn_up.setVisibility(View.GONE);
        btn_down.setVisibility(View.GONE);
        tv_infor.setVisibility(View.VISIBLE);
        tv_infor2.setVisibility(View.VISIBLE);
        tv_infor2.setText(getResources().getString(R.string.btnAll_infor1));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showToast(keyCode + "");
        tv_infor.setText(getResources().getString(R.string.btnAll_infor2) + keyCode + "");
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        mTitlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        mBtnPass = (Button) findViewById(R.id.btn_pass);
        mBtnPass.setOnClickListener(this);
        mBtnNotPass = (Button) findViewById(R.id.btn_not_pass);
        mBtnNotPass.setOnClickListener(this);
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_left_F4 = (Button) findViewById(R.id.btn_left_F4);
        btn_right_F4 = (Button) findViewById(R.id.btn_right_F4);
        tv_infor2 = (TextView) findViewById(R.id.tv_infor2);
        tv_infor = (TextView) findViewById(R.id.tv_infor);
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
