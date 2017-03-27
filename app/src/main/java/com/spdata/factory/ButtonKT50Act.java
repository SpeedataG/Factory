package com.spdata.factory;

import android.app.AlertDialog;
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
 * Created by lenovo_pc on 2016/9/29.
 */
@EActivity(R.layout.act_btnkt50)
public class ButtonKT50Act extends FragActBase {
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
    Button btn_del;
    @ViewById
    Button btn_enter;
    @ViewById
    Button btn_scan;
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
    Button btn_up;
    @ViewById
    Button btn_down;
    @ViewById
    Button btn_space;
    @ViewById
    Button btn_display;
    @ViewById
    Button btn_vol_up;
    @ViewById
    Button btn_vol_down;
    @ViewById
    Button btn_camera;

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

        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (btn_menu.isPressed()) {
                btn_menu.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_menu.setPressed(false);
                return true;
            } else {
                btn_menu.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_menu.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (btn_enter.isPressed()) {
                btn_enter.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_enter.setPressed(false);
                return true;
            } else {
                btn_enter.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_enter.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_F5) {
            if (btn_scan.isPressed()) {
                btn_scan.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_scan.setPressed(false);
                return true;
            } else {
                btn_scan.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_scan.setPressed(true);
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (btn_del.isPressed()) {
                btn_del.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_del.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_del.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_del.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_0) {
            if (btn_0.isPressed()) {
                btn_0.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_0.setPressed(false);

                return true;
            } else {
                btn_0.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_0.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_1) {
            if (btn_1.isPressed()) {
                btn_1.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_1.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_1.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_1.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_2) {
            if (btn_2.isPressed()) {
                btn_2.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_2.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_2.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_2.setPressed(true);
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
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_4) {
            if (btn_4.isPressed()) {
                btn_4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_4.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_4.setPressed(true);
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
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (btn_up.isPressed()) {
                btn_up.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_up.setPressed(false);
                showToast(keyCode + "");
                return true;
            } else {
                btn_up.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_up.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (btn_down.isPressed()) {
                btn_down.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_down.setPressed(false);
                return true;
            } else {
                btn_down.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_down.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_CAMERA) {
            if (btn_camera.isPressed()) {
                btn_camera.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_camera.setPressed(false);
                return true;
            } else {
                btn_camera.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_camera.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (btn_vol_up.isPressed()) {
                btn_vol_up.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_vol_up.setPressed(false);
                return true;
            } else {
                btn_vol_up.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_vol_up.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (btn_vol_down.isPressed()) {
                btn_vol_down.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_vol_down.setPressed(false);
                return true;
            } else {
                btn_vol_down.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_vol_down.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_PERIOD) {
            if (btn_space.isPressed()) {
                btn_space.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_space.setPressed(false);
                return true;
            } else {
                btn_space.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_space.setPressed(true);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_F4) {
            if (btn_display.isPressed()) {
                btn_display.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_display.setPressed(false);
                return true;
            } else {
                btn_display.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_display.setPressed(true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void isall() {
        if (btn_0.isPressed() && btn_1.isPressed()) {
            ff();


        }
    }

    private void ff() {
        setXml(App.KEY_BUTTON, App.KEY_FINISH);
        finish();
    }
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_ENTER:
//                if (btn_enter.isPressed()) {
//                    btn_enter.setBackgroundColor(Color.parseColor("#CEC7C7"));
//                    btn_enter.setPressed(false);
//                    showToast(keyCode + "");
//                    break;
//                } else {
//                    btn_enter.setBackgroundColor(Color.parseColor("#0AF229"));
//                    btn_enter.setPressed(true);
//                    break;
//                }
//
//            case KeyEvent.KEYCODE_MENU:
//                if (btn_menu.isPressed()) {
//                    btn_menu.setBackgroundColor(Color.parseColor("#CEC7C7"));
//                    btn_menu.setPressed(false);
//                    showToast(keyCode + "");
//                    break;
//                } else {
//                    btn_menu.setBackgroundColor(Color.parseColor("#0AF229"));
//                    btn_menu.setPressed(true);
//                    break;
//                }
//            case KeyEvent.KEYCODE_0:
//                if (btn_0.isPressed()) {
//                    btn_0.setBackgroundColor(Color.parseColor("#CEC7C7"));
//                    btn_0.setPressed(false);
//                    showToast(keyCode + "");
//                    break;
//                } else {
//                    btn_0.setBackgroundColor(Color.parseColor("#0AF229"));
//                    btn_0.setPressed(true);
//                    break;
//                }
//        }
//        return super.onKeyUp(keyCode, event);
//    }
}
