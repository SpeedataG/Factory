package com.spdata.factory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
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
    Button btn_sim1;
    @ViewById
    Button btn_sim2;
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
    void btn_sim1() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + 10086));
        startActivity(intent);
    }

    @Click
    void btn_sim() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + 10086));
        startActivity(intent);
    }

    @Click
    void btn_sim2() {//        Intent intent =new Intent();
//        intent.setAction("android.intent.action.CALL_BUTTON");
//        startActivity(intent);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + 10086));
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
        String model = Build.MODEL;
        if (model.equals("X300Q_X1") || model.equals("X300Q_P1") || model.equals("S510")
                || model.equals("H500A")||model.equals("X300Q_OLED")||model.equals("X300Q_OLED_GPS")
                ||model.equals("spda6735")||model.equals("DCD3")) {
            btn_sim1.setVisibility(View.GONE);
            btn_sim2.setVisibility(View.GONE);
            btn_sim.setVisibility(View.VISIBLE);
        }
    }
}
