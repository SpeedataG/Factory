package com.spdata.factory;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import common.base.act.FragActBase;
import common.event.ViewMessage;

/**
 * Created by lenovo_pc on 2016/8/10.
 */
@EActivity(R.layout.act_call_phone)
public class CallPhoneActi extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_sim;

    @Click
    void btnNotPass() {
        setXml(App.KEY_ACTION_CALL_PHONE, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_ACTION_CALL_PHONE, App.KEY_FINISH);
        finish();
    }

    @Click
    void btn_sim() {
        Intent intent = new Intent();
        //调用系统的拨号键盘用: ACTION_DIAL
        intent.setAction(intent.ACTION_DIAL);
        startActivity(intent);
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setTitlebarNameText("打电话测试");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
    }
}
