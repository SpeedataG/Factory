package com.spdata.factory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.spdata.factory.application.App;
import com.spdata.factory.view.LcdDrawView;
import com.umeng.analytics.MobclickAgent;

import common.utils.SharedXmlUtil;

public class DisplayTest extends Activity {
    boolean finished;
    private Message msg;
    Thread mThread = new Thread();
    LcdDrawView mView;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                LcdDrawView.flag += 1;
                if (LcdDrawView.flag > 6) {
                    LcdDrawView.flag = 0;
                    finished = false;
                    checkDialog();
                }
            }
            mView.invalidate();
            return;
        }
    };

    private void checkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.display)
                .setMessage(R.string.lcdtesttip)
                .setNegativeButton(R.string.successed, new OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        DisplayTest.this.setResult(1);
                        SharedXmlUtil.getInstance(DisplayTest.this).write(App.KEY_DISPLAY, App.KEY_FINISH);
                        DisplayTest.this.finish();
                    }
                })
                .setPositiveButton(R.string.failed, new OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        DisplayTest.this.setResult(-1);
                        SharedXmlUtil.getInstance(DisplayTest.this).write(App.KEY_DISPLAY, App.KEY_UNFINISH);
                        DisplayTest.this.finish();
                    }
                });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.MODEL.equals("CT")) {
            //设置横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        mView = new LcdDrawView(this);
        setContentView(mView);
        msg = mHandler.obtainMessage(1);
        mHandler.sendMessage(msg);
        finished = true;
    }

    @Override
    protected void onPause() {
        LcdDrawView.flag = 0;
        finished = false;
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                msg = mHandler.obtainMessage(1);
                mHandler.sendMessage(msg);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //去掉虚拟按键全屏显示
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //点击屏幕不再显示
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        // bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}