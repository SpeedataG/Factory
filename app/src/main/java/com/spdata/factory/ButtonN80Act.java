package com.spdata.factory;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
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
 * Created by suntianwei on 2017/3/6.
 */
@EActivity(R.layout.act_btn_n80)
public class ButtonN80Act  extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_vup;
    @ViewById
    Button btn_vdown;
    @ViewById
    Button btn_f1;
    @ViewById
    Button btn_f2;
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
    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showToast(keyCode + "");
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (btn_vup.isPressed()) {
                btn_vup.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_vup.setPressed(false);
                return true;
            } else {
                btn_vup.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_vup.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (btn_vdown.isPressed()) {
                btn_vdown.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_vdown.setPressed(false);
                return true;
            } else {
                btn_vdown.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_vdown.setPressed(true);
                return true;
            }
        }  else if (keyCode == KeyEvent.KEYCODE_F1) {
            if (btn_f1.isPressed()) {
                btn_f1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f1.setPressed(false);
            } else {
                btn_f1.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f1.setPressed(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_F2) {
            if (btn_f2.isPressed()) {
                btn_f2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_f2.setPressed(false);
            } else {
                btn_f2.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_f2.setPressed(true);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
