package com.spdata.factory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * Created by lenovo_pc on 2016/8/10.
 */
public class CallPhoneActi extends FragActBase implements View.OnClickListener {
    private CustomTitlebar titlebar;
    /**
     * 拨打电话
     */
    private Button btn_sim;
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
        setContentView(R.layout.act_call_phone);
        initView();
        initTitlebar();
        setSwipeEnable(false);
    }


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setTitlebarNameText(getResources().getString(R.string.menu_call_phone));
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_sim = (Button) findViewById(R.id.btn_sim);
        btn_sim.setOnClickListener(this);
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
                setXml(App.KEY_ACTION_CALL_PHONE, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_ACTION_CALL_PHONE, App.KEY_UNFINISH);
                finish();
                break;
            case R.id.btn_sim:
                Intent intent = new Intent();
                //调用系统的拨号键盘用: ACTION_DIAL
                intent.setAction(Intent.ACTION_DIAL);
                startActivity(intent);
                break;
        }
    }
}
