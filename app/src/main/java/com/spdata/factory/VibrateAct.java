package com.spdata.factory;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
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

/**
 * Created by lenovo_pc on 2016/8/17.
 */
@EActivity(R.layout.act_vibrate)
public class VibrateAct extends FragActBase {
    private Vibrator myVibrator;
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    TextView tv_infor;

    @Click
    void btnNotPass() {
        setXml(App.KEY_VIBRATE, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_VIBRATE, App.KEY_FINISH);
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
        }, "震动器", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }


    @AfterViews
    protected void main() {
        initTitlebar();
        //获得系统的Vibrator实例:
        myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        if (myVibrator.hasVibrator()) {
            titlebar.setAttrs("当前设备无振动器");
        }
        titlebar.setAttrs("当前设备有振动器");
        tv_infor.setText("振动中……");
        myVibrator.vibrate(new long[]{100, 100, 100, 1000}, 0);
        btnPass.setVisibility(View.GONE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnPass.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myVibrator.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myVibrator.cancel();
    }
}
