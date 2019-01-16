package com.spdata.factory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.utils.SDUtils;

public class SDCardkt80Act extends FragActBase implements View.OnClickListener {


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
        if (volumePaths.length > 1) {
            StringBuffer stringBuffer = new StringBuffer();
            String size = sdUtils.getSDTotalSize(volumePaths[1]);
            stringBuffer.append("\n" + getResources().getString(R.string.SDCardAct_neizhi) + sdUtils.getSDTotalSize(volumePaths[0])
                    + getResources().getString(R.string.SDCardAct_keyong) + sdUtils.getSDAvailableSize(volumePaths[0]));
            tvInfor.setText(stringBuffer);
            if (size.equals("0.00 B")) {
                tvInfor.append(getResources().getString(R.string.SDCardkt80Act_sd1_off));
            } else {
                stringBuffer.append(getResources().getString(R.string.SDCardkt80Act_sd1_wai) + size
                        + getResources().getString(R.string.SDCardAct_keyong) + sdUtils.getSDAvailableSize(volumePaths[1]));
                tvInfor.setText(stringBuffer);
                tvInfor.append(getResources().getString(R.string.SDCardkt80Act_sd1_on));
            }
            for (int i = 0; i < 1; i++) {
                try {
                    sdUtils.copyBigDataToSD(volumePaths[i]);
                    yes = 1;
                } catch (IOException e) {
                    e.printStackTrace();
                    yes = 2;
                }
            }
        } else {
            yes = 2;
        }
        //kt80-6.0注释
        if (getApiVersion() < 23) {
            readSD2();
            switch (stada) {
                case "0":
                    tvInfor.append(getResources().getString(R.string.SDCardkt80Act_sd2_normal));
                    break;
                case "1":
                    tvInfor.append(getResources().getString(R.string.SDCardkt80Act_sd2_damage));
                    break;
                case "2":
                    tvInfor.append(getResources().getString(R.string.SDCardkt80Act_sd2_none));
                case "":
                    tvInfor.append(getResources().getString(R.string.SDCardkt80Act_sd2_notsupport));
                    break;
            }
            //kt80-6.0注释
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
            stada = "";
            e.printStackTrace();
        }
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
                    if (getApiVersion() < 23) {
                        if (yes == 1) {
                            setXml(App.KEY_SDCARD, App.KEY_FINISH);
                            finish();
                        } else if (yes == 2 || stada.equals("1") || stada.equals("2")) {
                            setXml(App.KEY_SDCARD, App.KEY_UNFINISH);
                            showToast(getResources().getString(R.string.btn_fail));
                            finish();
                        } else {
                            setXml(App.KEY_SDCARD, App.KEY_UNFINISH);
                            showToast(getResources().getString(R.string.btn_fail));
                            finish();
                        }
                    } else {
                        if (yes == 1) {
                            setXml(App.KEY_SDCARD, App.KEY_FINISH);
                            finish();
                        } else if (yes == 2) {
                            setXml(App.KEY_SDCARD, App.KEY_UNFINISH);
                            showToast(getResources().getString(R.string.btn_fail));
                            finish();
                        } else {
                            setXml(App.KEY_SDCARD, App.KEY_UNFINISH);
                            showToast(getResources().getString(R.string.btn_fail));
                            finish();
                        }
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
