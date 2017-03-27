package com.spdata.factory;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

@EActivity(R.layout.act_sleep_wake)
public class SleepWakeAct extends FragActBase {

    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_SLEEP_WAKE, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_SLEEP_WAKE, App.KEY_FINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, "休眠唤醒测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        tvInfor.setText("请按下休眠按键\n");
    }

    @Override
    protected void onPause() {
        super.onPause();
        tvInfor.append("挂起\n");
    }

    private int count = 0;

    @Override
    protected void onResume() {
        super.onResume();
        if (count == 0) {
            tvInfor.setText("请按下休眠按键\n");
        } else {
            tvInfor.append("已经唤醒\n");
            setXml(App.KEY_SLEEP_WAKE, App.KEY_FINISH);
            finish();
        }
        count++;
    }
}
