package com.spdata.factory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.utils.SDUtils;

public class SDCardAct extends FragActBase implements View.OnClickListener {

    private CustomTitlebar titlebar;
    private TextView tvInfor;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, R.string.menu_sdcard, null);
    }


    private SDUtils sdUtils;
    private Timer timer;
    private remindTask task;
    private int count = 0;
    private int yes = 0;
    private String stada;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sdcard);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        sdUtils = new SDUtils(mContext);
        String[] volumePaths = sdUtils.getVolumePaths();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getResources().getString(R.string.SDCardAct_neizhi)  + sdUtils.getSDTotalSize(volumePaths[0])
                + getResources().getString(R.string.SDCardAct_keyong)  + sdUtils.getSDAvailableSize(volumePaths[0]));
        tvInfor.append(stringBuffer);
        if (volumePaths.length > 1) {
            stringBuffer.append(getResources().getString(R.string.SDCardAct_wai) + sdUtils.getSDTotalSize(volumePaths[1])
                    + getResources().getString(R.string.SDCardAct_keyong) + sdUtils.getSDAvailableSize(volumePaths[1]));
            tvInfor.append(stringBuffer);
            for (int i = 0; i < volumePaths.length; i++) {
                try {
                    sdUtils.copyBigDataToSD(volumePaths[i]);
                    tvInfor.append(getResources().getString(R.string.SDCardAct_copy_suc));
                    yes = 1;
                } catch (IOException e) {
                    e.printStackTrace();
                    yes = 2;
                    tvInfor.append(getResources().getString(R.string.SDCardAct_copy_fa));
                }
            }
        } else {
            try {
                sdUtils.copyBigDataToSD(volumePaths[0]);
                tvInfor.append(getResources().getString(R.string.SDCardAct_copy_neisuc));
                yes = 1;
            } catch (IOException e) {
                e.printStackTrace();
                yes = 2;
                tvInfor.append(getResources().getString(R.string.SDCardAct_copy_neifa));
            }
        }
        task = new remindTask();
        remind(task);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
        btnPass = (Button) findViewById(R.id.btn_pass);
        btnPass.setOnClickListener(this);
        btnNotPass = (Button) findViewById(R.id.btn_not_pass);
        btnNotPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_pass:
                setXml(App.KEY_SDCARD, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_SDCARD, App.KEY_UNFINISH);
                finish();
                break;
        }
    }

    private class remindTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (yes == 1) {
                        setXml(App.KEY_SDCARD, App.KEY_FINISH);
                        finish();
                    } else {
                        setXml(App.KEY_SDCARD, App.KEY_UNFINISH);
                        finish();
                    }
                }
            });
        }
    }

    private void remind(TimerTask task) {
        timer = new Timer();
        timer.schedule(task, 1000 * 2);
    }

    public void finishTimer() {
        if (timer != null && task != null) {
            task.cancel();
            timer.cancel();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finishTimer();
    }
}
