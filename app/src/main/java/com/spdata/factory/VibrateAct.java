package com.spdata.factory;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * Created by lenovo_pc on 2016/8/17.
 */
public class VibrateAct extends FragActBase implements View.OnClickListener {
    private Vibrator myVibrator;
    private CustomTitlebar titlebar;
    /**
     * sss
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
        setContentView(R.layout.act_vibrate);
        initView();
        initTitlebar();
        //获得系统的Vibrator实例:
        myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        if (myVibrator.hasVibrator()) {
            titlebar.setAttrs("当前设备无振动器");
        }
        titlebar.setAttrs("当前设备有振动器");
        tv_infor.setText("振动中……");
        myVibrator.vibrate(new long[]{100, 100, 100, 1000}, 0);
        btn_pass.setVisibility(View.GONE);

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
                        btn_pass.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
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
    protected void onDestroy() {
        super.onDestroy();
        myVibrator.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myVibrator.cancel();
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
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
                setXml(App.KEY_VIBRATE, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_VIBRATE, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
