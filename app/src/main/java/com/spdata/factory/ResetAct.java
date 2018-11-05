package com.spdata.factory;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scandecode.ScanDecode;
import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Timer;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.ScanUtil;

/**
 * Created by 42040 on 2018/11/2.
 */
@EActivity(R.layout.act_reset_layout)
public class ResetAct extends FragActBase {


    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;


    @Click
    void btnNotPass() {
        setXml(App.KEY_RESET, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_RESET, App.KEY_FINISH);
        finish();
    }

    @Click
    void tvInfor() {
//        Intent intent = new Intent(Intent.ACTION_FACTORY_RESET);
//
//        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//        intent.putExtra(Intent.EXTRA_REASON, "MasterClearConfirm");
//        intent.putExtra(Intent.EXTRA_WIPE_EXTERNAL_STORAGE, true);//清除数据
//        sendBroadcast(intent);
        startActivity(new Intent(Settings.ACTION_SETTINGS));
//        //   第一个参为包名，第二个各个设置的类名(可以参考下面，包名不用改变)
//        ComponentName cm = new ComponentName("com.spdata.factory",
//                "com.android.settings.MasterClear");
//        ComponentName cm = new ComponentName("com.android.settings",
//                "com.android.settings.SubSettings");
//        ComponentName cm = new ComponentName("com.android.settings",
//                "com.android.settings.SystemDashboardActivity");
//        Intent intent = new Intent();
//        intent.setComponent(cm);
//        intent.setAction("android.intent.action.VIEW");
//        startActivity(intent);


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
        }, "恢复出厂", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }


    Timer timer;
    boolean is = false;

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
