package com.spdata.factory;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
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

import static com.spdata.factory.R.id.btn_left_F4;
import static com.spdata.factory.R.id.btn_right_F4;

/**
 * Created by 42040 on 2018/6/20.
 */
@EActivity(R.layout.act_btn_sd80)
public class ButtonSd80Act extends FragActBase {
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
    Button btn_back;
    @ViewById
    Button btn_x;
    @ViewById
    Button btn_y;


    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (btn_back.isPressed()) {
                btn_back.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_back.setPressed(false);
                return true;
            } else {
                btn_back.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_back.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (btn_up.isPressed()) {
                btn_up.setBackgroundColor(Color.parseColor("#CEC7C7"));
                return true;
            } else {
                btn_up.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_up.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (btn_down.isPressed()) {
                btn_down.setBackgroundColor(Color.parseColor("#CEC7C7"));
                return true;
            } else {
                btn_down.setBackgroundColor(Color.parseColor("#0AF229"));
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_X) {
            if (btn_x.isPressed()) {
                btn_x.setBackgroundColor(Color.parseColor("#CEC7C7"));
                return true;
            } else {
                btn_x.setBackgroundColor(Color.parseColor("#0AF229"));
                return true;
            }
        }else if (keyCode == KeyEvent.KEYCODE_CAMERA) {
            if (btn_y.isPressed()) {
                btn_y.setBackgroundColor(Color.parseColor("#CEC7C7"));
                return true;
            } else {
                btn_y.setBackgroundColor(Color.parseColor("#0AF229"));
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
