package com.spdata.factory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.spdata.factory.application.App;
import com.spdata.factory.view.MTView;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

import common.utils.SharedXmlUtil;

/*
屏幕多点触摸
 */
public class MultitouchVisible extends Activity {
    private Timer timer;
    private remindTask task;
    private int count;
    private MTView mtView;
    private AlertDialog.Builder builder;
    private boolean is = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mtView = new MTView(this);
        builder = new AlertDialog.Builder(MultitouchVisible.this);
        setContentView(mtView);
        task = new remindTask();
        remind(task);
    }

    private class remindTask extends TimerTask {
        @Override
        public void run() {
            count = mtView.pointerCount();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final remindTask task = new remindTask();
                    remind(task);
                    if (count != 0 && is == true) {
                        is = false;
                        builder.setTitle("提示")
                                .setPositiveButton
                                        ("成功", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                SharedXmlUtil.getInstance(MultitouchVisible.this).write(App
                                                        .KEY_ACTION_TOUCH_SCREEN_MOR, App.KEY_FINISH);
                                                is = true;
                                                finish();
                                            }
                                        })
                                .setNeutralButton("重新测试", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        remindTask task = new remindTask();
//                                        remind(task)
                                        is = true;
                                    }
                                }).setNegativeButton("失败", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedXmlUtil.getInstance(MultitouchVisible.this).write(App
                                        .KEY_ACTION_TOUCH_SCREEN_MOR, App.KEY_UNFINISH);
                                is = true;
                                finish();

                            }
                        }).setMessage("屏幕支持" + count + "个点，请选择！").setCancelable(false).show();
                    }
                }
            });
        }
    }

    private void remind(TimerTask task) {
        timer = new Timer();
        timer.schedule(task, 1000);
    }

    public void finishTimer() {
        timer.cancel();
        task.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishTimer();
    }
}