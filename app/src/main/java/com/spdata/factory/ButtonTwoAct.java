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
 * Created by lenovo_pc on 2016/8/20.
 */
@EActivity(R.layout.act_button_two)
public class ButtonTwoAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_vol_up;
    @ViewById
    Button btn_dwon;
    @ViewById
    Button btn_camera;
    @ViewById
    Button btn_ok;
    @ViewById
    Button btn_fn;

    boolean isOneClick = true;

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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        showToast(keyCode+"");
        if (keyCode==KeyEvent.KEYCODE_ENTER){
            if (btn_ok.isPressed()) {
                btn_ok.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_ok.setPressed(false);
            } else {
                btn_ok.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_ok.setPressed(true);
            }
        }else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
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
}
