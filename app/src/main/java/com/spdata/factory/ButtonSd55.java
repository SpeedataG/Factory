package com.spdata.factory;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * Created by 42040 on 2018/11/5.
 */

public class ButtonSd55 extends FragActBase implements View.OnClickListener {

    private boolean isOneClick = true;
    private CustomTitlebar titlebar;
    /**
     * VOLUME_UP
     */
    private Button btn_up;
    /**
     * VOLUME_DOWN
     */
    private Button btn_down;
    private Button btn_close;
    /**
     * HOME
     */
    private Button btn_home;
    /**
     * SCAN
     */
    private Button btn_left_F4;
    /**
     * SCAN
     */
    private Button btn_right_F4;
    /**
     * dsafasdf
     */
    private TextView tv_infor;
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
        setContentView(R.layout.act_btnsd55_layout);
        initView();initTitlebar();
        setSwipeEnable(false);
        btn_pass.setVisibility(View.GONE);
    }



    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("按键测试");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showToast(keyCode + "");
        if (keyCode == 135) {
            if (btn_left_F4.isPressed()) {
                btn_left_F4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_left_F4.setPressed(false);
            } else {
                btn_left_F4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_left_F4.setPressed(true);
                ispress();
            }

        } else if (keyCode == 134) {
            if (btn_right_F4.isPressed()) {
                btn_right_F4.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_right_F4.setPressed(false);
                return true;
            } else {
                btn_right_F4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_right_F4.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_right_F4.setPressed(true);
                ispress();
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (btn_up.isPressed()) {
                btn_up.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_up.setPressed(false);
                return true;
            } else {
                btn_up.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_up.setPressed(true);
                ispress();
                return true;
            }

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (btn_down.isPressed()) {
                btn_down.setBackgroundColor(Color.parseColor("#CEC7C7"));
                btn_down.setPressed(false);
                return true;
            } else {
                btn_down.setBackgroundColor(Color.parseColor("#0AF229"));
                btn_down.setPressed(true);
                ispress();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void ispress() {
        if (btn_left_F4.isPressed() && btn_down.isPressed() && btn_up.isPressed() && btn_right_F4.isPressed()) {
            btn_pass.setVisibility(View.VISIBLE);
        }
    }

    /*产生numSize位16进制的数*/
    public static String getRandomValue(int numSize) {
        String str = "";
        for (int i = 0; i < numSize; i++) {
            char temp = 0;
            int key = (int) (Math.random() * 2);
            switch (key) {
                case 0:
                    temp = (char) (Math.random() * 10 + 48);//产生随机数字
                    break;
                case 1:
                    temp = (char) (Math.random() * 6 + 'a');//产生a-f
                    break;
                default:
                    break;
            }
            str = str + temp;
        }
        return str;
    }


    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_left_F4 = (Button) findViewById(R.id.btn_left_F4);
        btn_right_F4 = (Button) findViewById(R.id.btn_right_F4);
        tv_infor = (TextView) findViewById(R.id.tv_infor);
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
                setXml(App.KEY_BUTTON, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_BUTTON, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
