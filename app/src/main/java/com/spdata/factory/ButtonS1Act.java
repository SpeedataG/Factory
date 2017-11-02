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

import static com.spdata.factory.R.id.btn_del;

/**
 * Created by lenovo-pc on 2017/9/13.
 */
@EActivity(R.layout.act_btn_s1)
public class ButtonS1Act extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_menu;
    @ViewById
    Button btn_back;
    @ViewById
    Button btn_1;
    @ViewById
    Button btn_2;
    @ViewById
    Button btn_3;
    @ViewById
    Button btn_4;
    @ViewById
    Button btn_5;
    @ViewById
    Button btn_6;
    @ViewById
    Button btn_7;
    @ViewById
    Button btn_8;
    @ViewById
    Button btn_9;
    @ViewById
    Button btn_0;
    @ViewById
    Button btn_sahng;
    @ViewById
    Button btn_xia;
    @ViewById
    Button btn_fn;
    @ViewById
    Button btn_app;
    @ViewById
    Button btn_enter;
    @ViewById
    Button btn_shift;
    @ViewById
    Button btn_backspace;
    @ViewById
    Button btn_space;
    @ViewById
    Button btn_mi;
    @ViewById
    Button btn_jing;
    @ViewById
    Button btn_you;
    @ViewById
    Button btn_zuo;
    @ViewById
    Button btn_weizhi;


    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
    }

    @Click
    void btnPass() {
        setXml(App.KEY_BUTTON, App.KEY_FINISH);
        finish();
    }

    @Click
    void btnNotPass() {
        setXml(App.KEY_BUTTON, App.KEY_UNFINISH);
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
                isall();
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (btn_menu.isPressed()) {
                btn_menu.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_menu.setPressed(false);
                return true;
            } else {
                btn_menu.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_menu.setPressed(true);
                isall();
                return true;
            }

        }
        else if (keyCode == KeyEvent.KEYCODE_F1) {
            if (btn_app.isPressed()) {
                btn_app.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_app.setPressed(false);
                return true;
            } else {
                btn_app.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_app.setPressed(true);
                isall();
                return true;
            }

        }
        else if (keyCode == KeyEvent.KEYCODE_F3) {
            if (btn_fn.isPressed()) {
                btn_fn.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_fn.setPressed(false);
                return true;
            } else {
                btn_fn.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_fn.setPressed(true);
                isall();
                return true;
            }

        }
 else if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (btn_backspace.isPressed()) {
                btn_backspace.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_backspace.setPressed(false);
                return true;
            } else {
                btn_backspace.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_backspace.setPressed(true);
                return true;
            }
        }
        else if (keyCode == KeyEvent.KEYCODE_0) {
            if (btn_0.isPressed()) {
                btn_0.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_0.setPressed(false);
                return true;
            } else {
                btn_0.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_0.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_1) {
            if (btn_1.isPressed()) {
                btn_1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_1.setPressed(false);
                return true;
            } else {
                btn_1.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_1.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_2) {
            if (btn_2.isPressed()) {
                btn_2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_2.setPressed(false);
                return true;
            } else {
                btn_2.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_2.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_3) {
            if (btn_3.isPressed()) {
                btn_3.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_3.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_3.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_3.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_4) {
            if (btn_4.isPressed()) {
                btn_4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_4.setPressed(false);
                return true;
            } else {
                btn_4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_4.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_5) {
            if (btn_5.isPressed()) {
                btn_5.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_5.setPressed(false);
                return true;
            } else {
                btn_5.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_5.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_6) {
            if (btn_6.isPressed()) {
                btn_6.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_6.setPressed(false);
                return true;
            } else {
                btn_6.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_6.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_7) {
            if (btn_7.isPressed()) {
                btn_7.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_7.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_7.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_7.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_8) {
            if (btn_8.isPressed()) {
                btn_8.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_8.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_8.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_8.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_9) {
            if (btn_9.isPressed()) {
                btn_9.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_9.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_9.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_9.setPressed(true);
                isall();
                return true;
            }
        }
        else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (btn_sahng.isPressed()) {
                btn_sahng.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_sahng.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_sahng.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_sahng.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (btn_xia.isPressed()) {
                btn_xia.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_xia.setPressed(false);
                return true;
            } else {
                btn_xia.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_xia.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (btn_zuo.isPressed()) {
                btn_zuo.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_zuo.setPressed(false);
                return true;
            } else {
                btn_zuo.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_zuo.setPressed(true);
                isall();
                return true;
            }
        }
        else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (btn_you.isPressed()) {
                btn_you.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_you.setPressed(false);
                return true;
            } else {
                btn_you.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_you.setPressed(true);
                isall();
                return true;
            }
        }
        else if (keyCode == KeyEvent.KEYCODE_STAR) {//*
            if (btn_mi.isPressed()) {
                btn_mi.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_mi.setPressed(false);
                return true;
            } else {
                btn_mi.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_mi.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_POUND) {//#
            if (btn_jing.isPressed()) {
                btn_jing.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_jing.setPressed(false);
                return true;
            } else {
                btn_jing.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_jing.setPressed(true);
                isall();
                return true;
            }
        }
         else if (keyCode == KeyEvent.KEYCODE_F2) {//#
            if (btn_weizhi.isPressed()) {
                btn_weizhi.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_weizhi.setPressed(false);
                return true;
            } else {
                btn_weizhi.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_weizhi.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {//#
            if (btn_enter.isPressed()) {
                btn_enter.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_enter.setPressed(false);
                return true;
            } else {
                btn_enter.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_enter.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F4) {//#
            if (btn_shift.isPressed()) {
                btn_shift.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_shift.setPressed(false);
                return true;
            } else {
                btn_shift.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_shift.setPressed(true);
                isall();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_SPACE) {//#
            if (btn_space.isPressed()) {
                btn_space.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_space.setPressed(false);
                return true;
            } else {
                btn_space.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_space.setPressed(true);
                isall();
                return true;
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    public void isall() {
        if (btn_0.isPressed() && btn_1.isPressed() && btn_2.isPressed() && btn_3.isPressed()
                && btn_4.isPressed() && btn_5.isPressed() && btn_6.isPressed() && btn_7.isPressed()
                && btn_8.isPressed() && btn_9.isPressed() && btn_mi.isPressed() && btn_jing.isPressed()
//                && btn_del.isPressed() && btn_f1.isPressed() && btn_f2.isPressed() && btn_f3.isPressed()
//                && btn_ok.isPressed() && btn_back.isPressed() && btn_menu.isPressed() && btn_scan.isPressed()
//                && btn_up.isPressed() && btn_down.isPressed() && btn_f5.isPressed() && btn_f6.isPressed() && btn_f10.isPressed() && btn_camera.isPressed()
                ) {
//            setXml(App.KEY_BUTTON, App.KEY_FINISH);
//            finish();
        }
    }
}
