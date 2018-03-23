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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.SDUtils;

@EActivity(R.layout.act_sdcard)
public class SDCardkt80Act extends FragActBase {
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
        readSD2();
        String[] volumePaths = sdUtils.getVolumePaths();
        StringBuffer stringBuffer = new StringBuffer();
        String size = sdUtils.getSDTotalSize(volumePaths[1]);
        stringBuffer.append("\n内置SD卡总大小" + sdUtils.getSDTotalSize(volumePaths[0])
                + "\n可用大小:" + sdUtils.getSDAvailableSize(volumePaths[0]));

//        stringBuffer.append("\n外置SD2卡总大小" + sdUtils.getSDTotalSize(volumePaths[2])
//                + "\n可用大小:" + sdUtils.getSDAvailableSize(volumePaths[2]));
        tvInfor.setText(stringBuffer);
        if (size.equals("0.00 B")) {
            tvInfor.append("\nSD卡1不存在");
        } else {
            stringBuffer.append("\n外置SD1卡总大小" + size
                    + "\n可用大小:" + sdUtils.getSDAvailableSize(volumePaths[1]));
            tvInfor.setText(stringBuffer);
            tvInfor.append("\nSD卡1存在");
        }
        switch (stada) {
            case "0":
                tvInfor.append("\nSD2正常挂载可使用");
                break;
            case "1":
                tvInfor.append("\nSD2损坏或者没正常格式化");
                break;
            case "2":
                tvInfor.append("\n无SD2卡");
            case "":
                tvInfor.append("\n不支持SD2卡");
                break;
        }

        for (int i = 0; i < 2; i++) {
            try {
                sdUtils.copyBigDataToSD(volumePaths[i]);
                yes = 1;
            } catch (IOException e) {
                e.printStackTrace();
                yes = 2;
//                tvInfor.setText("SD卡检测失败");
            }
        }

        task = new remindTask();
        remind(task);
    }

    private void readSD2() {
        File file = new File("/sys/class/misc/hwoper/sd2_status");
        try {
            if (file != null) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                stada = bufferedReader.readLine();
                bufferedReader.close();
            }
        } catch (IOException e) {
            stada="";
            e.printStackTrace();
        }
    }

    private class remindTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (yes == 1 && stada.equals("0")) {
                        setXml(App.KEY_SDCARD, App.KEY_FINISH);
                        finish();
                    } else if (yes == 2 || stada.equals("1") || stada.equals("2")) {
                        setXml(App.KEY_SDCARD, App.KEY_UNFINISH);
                        showToast("失败");
                        finish();
                    } else {
                        setXml(App.KEY_SDCARD, App.KEY_UNFINISH);
                        showToast("失败");
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
    protected void onDestroy() {
        super.onDestroy();
        finishTimer();
    }
}
