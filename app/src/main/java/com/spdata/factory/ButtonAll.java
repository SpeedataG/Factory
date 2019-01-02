package com.spdata.factory;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import common.base.act.FragActBase;
import common.event.ViewMessage;

/**
 * Created by lenovo-pc on 2018/3/22.
 */
@EActivity(R.layout.activity_button)
public class ButtonAll extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_up;
    @ViewById
    Button btn_down;
    @ViewById
    Button btn_left_F4;
    @ViewById
    Button btn_close;
    @ViewById
    Button btn_right_F4;
    @ViewById
    TextView tv_infor;
    @ViewById
    TextView tv_infor2;
    boolean isOneClick = true;

    @AfterViews
    protected void main() {
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
        tv_infor2.setText("请根据显示键值判断按键是否正常！");



    }

    @Click
    void btnNotPass() {
        setXml(App.KEY_BUTTON, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_BUTTON, App.KEY_FINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("按键测试");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showToast(keyCode + "");
        tv_infor.setText("按键键值:"+keyCode+"");
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
