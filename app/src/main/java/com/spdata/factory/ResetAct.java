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
import common.utils.SharedXmlUtil;

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
//        setXml(App.KEY_RESET, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
//        setXml(App.KEY_RESET, App.KEY_FINISH);
        finish();
    }

    @Click
    void tvInfor() {
        SharedXmlUtil.getInstance(this).clearAll();
        showToast("清除记录成功！");
        finish();

//        startActivity(new Intent(Settings.ACTION_SETTINGS));
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
        }, "清除测试记录", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }


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
