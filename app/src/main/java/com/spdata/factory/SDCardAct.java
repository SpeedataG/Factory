package com.spdata.factory;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.SDUtils;

@EActivity(R.layout.act_sdcard)
public class SDCardAct extends FragActBase {
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
        setXml(App.KEY_SDCARD, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_SDCARD, App.KEY_FINISH);
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
        }, "SD卡测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    private SDUtils sdUtils;
    private Timer timer;
    private remindTask task;
    private int count = 0;
    private int yes = 0;
    private String stada;

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        sdUtils = new SDUtils(mContext);
        String[] volumePaths = sdUtils.getVolumePaths();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n内置SD卡总大小" + sdUtils.getSDTotalSize(volumePaths[0])
                + "\n可用大小:" + sdUtils.getSDAvailableSize(volumePaths[0]));
       String s= sdUtils.getSDAvailableSize(volumePaths[1]);
        stringBuffer.append("\n外置SD卡总大小" + sdUtils.getSDTotalSize(volumePaths[1])
                + "\n可用大小:" + sdUtils.getSDAvailableSize(volumePaths[1]));
        tvInfor.setText(stringBuffer);
        tvInfor.setText("SD卡检测成功");
        for (int i = 0; i < 2; i++) {
            try {
                sdUtils.copyBigDataToSD(volumePaths[i]);
                yes = 1;
            } catch (IOException e) {
                e.printStackTrace();
                yes = 2;
                tvInfor.setText("SD卡检测失败");
            }
        }
        task = new remindTask();
        remind(task);
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
        timer.cancel();
        task.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finishTimer();
    }
}
