package com.spdata.factory;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
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
import common.crash.utils.SysInfoUtil;
import common.event.ViewMessage;

@EActivity(R.layout.act_version)
public class VersionAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvVersionInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_VERSION, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_VERSION, App.KEY_FINISH);
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
        }, "版本信息", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    @AfterViews
    protected void main() {
        onWindowFocusChanged(true);
        initTitlebar();
        setSwipeEnable(false);
        TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String Versionum=Build.DISPLAY;
        if (Build.MODEL.equals("N80")||Build.MODEL.equals("S1_35")||Build.MODEL.equals("S1")) {
            tvVersionInfor.setText("型号：" + android.os.Build.MODEL
                    + "\n"
                    + "版本号:" + Versionum
                    + "\n"
                    + "APK版本:"
                    + SysInfoUtil.getVersionName(this)
                    + "\n");
        }else {
            tvVersionInfor.setText("型号：" + android.os.Build.MODEL
                    + "\n"
                    + "版本号:" + Versionum
                    + "\n"
                    +"SN号:"+Build.SERIAL
                    + "\n"
                    + "APK版本:"
                    + SysInfoUtil.getVersionName(this)
                    + "\n");
        }

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }
}



