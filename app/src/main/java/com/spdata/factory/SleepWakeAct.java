package com.spdata.factory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

public class SleepWakeAct extends FragActBase implements View.OnClickListener {

    private CustomTitlebar titlebar;
    private TextView tvInfor;
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
        setContentView(R.layout.act_sleep_wake);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        tvInfor.setText(getResources().getString(R.string.SleepWakeAct_msg));
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, R.string.menu_sleep_wake, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        count++;
    }

    private int count = 0;

    @Override
    protected void onResume() {
        super.onResume();
        if (count == 0) {
            tvInfor.setText(getResources().getString(R.string.SleepWakeAct_msg));
        } else {
            setXml(App.KEY_SLEEP_WAKE, App.KEY_FINISH);
            finish();
        }
        count++;
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
        btnPass = (Button) findViewById(R.id.btn_pass);
        btnPass.setOnClickListener(this);
        btnNotPass = (Button) findViewById(R.id.btn_not_pass);
        btnNotPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_pass:
                setXml(App.KEY_SLEEP_WAKE, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_SLEEP_WAKE, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
