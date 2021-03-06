/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein
 * is confidential and proprietary to MediaTek Inc. and/or its licensors.
 * Without the prior written permission of MediaTek inc. and/or its licensors,
 * any reproduction, modification, use or disclosure of MediaTek Software,
 * and information contained herein, in whole or in part, shall be strictly prohibited.
 */
/* MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER ON
 * AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
 * NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
 * SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
 * SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES TO LOOK ONLY TO SUCH
 * THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. RECEIVER EXPRESSLY ACKNOWLEDGES
 * THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES
 * CONTAINED IN MEDIATEK SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK
 * SOFTWARE RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND
 * CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
 * AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
 * OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY RECEIVER TO
 * MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek Software")
 * have been modified by MediaTek Inc. All revisions are subject to any receiver's
 * applicable license agreements with MediaTek Inc.
 */

package com.spdata.factory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.spdata.factory.application.App;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import common.utils.SharedXmlUtil;

/**
 * Demonstrates wrapping a layout in a ScrollView.
 * 触摸屏幕
 */

public class TsHandWritingAct extends Activity {

    public static final int CLEAR_CANVAS_ID = Menu.FIRST;
    private static final String TAG = "EM/TouchScreen/HW";
    MyView mView = null;
    private Timer timer;
    private remindTask task;
    private int mZoom = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (App.getModel().equals("CT")) {
            //设置横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mView = new MyView(this);
        mView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(mView);
    }

    private class remindTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(TsHandWritingAct.this).setTitle(R.string.TsHandWritingAct_dialog_title)
                            .setPositiveButton
                                    (R.string.btn_success, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedXmlUtil.getInstance(TsHandWritingAct.this).write(App
                                                    .KEY_TOUCH_SCREEN, App.KEY_FINISH);
                                            finish();
                                        }
                                    })
                            .setNeutralButton(R.string.TsHandWritingAct_dialog_btn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    remindTask task = new remindTask();
                                    remind(task);
                                }
                            }).setNegativeButton(R.string.btn_fail, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedXmlUtil.getInstance(TsHandWritingAct.this).write(App
                                    .KEY_TOUCH_SCREEN, App.KEY_UNFINISH);
                            finish();
                        }
                    }).setMessage(R.string.TsHandWritingAct_dialog_mes).setCancelable(false).show();
                }
            });
        }
    }

    private void remind(TimerTask task) {
        timer = new Timer();
        timer.schedule(task, 1000 * 15);
    }

    public void finishTimer() {
        timer.cancel();
        task.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        final SharedPreferences preferences = this.getSharedPreferences(
                "touch_screen_settings", Context.MODE_PRIVATE);
        String file = preferences.getString("filename", "N");
        if (!"N".equals(file)) {
            String[] cmd = {"/system/bin/sh", "-c",
                    "echo [ENTER_HAND_WRITING] >> " + file}; // file
        }
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        task = new remindTask();
        remind(task);
    }

    @Override
    public void onPause() {
        final SharedPreferences preferences = this.getSharedPreferences(
                "touch_screen_settings", Context.MODE_PRIVATE);
        String file = preferences.getString("filename", "N");
        if (!"N".equals(file)) {
//        if (!file.equals("N")) {
            String[] cmd = {"/system/bin/sh", "-c",
                    "echo [LEAVE_HAND_WRITING] >> " + file}; // file
        }
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, CLEAR_CANVAS_ID, 0, "Clean Table.");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        if (CLEAR_CANVAS_ID == mi.getItemId()) {
            mView.clear();
        }
        return super.onOptionsItemSelected(mi);
    }

    public class PT {
        public Float mX;
        public Float mY;

        public PT(Float x, Float y) {
            this.mX = x;
            this.mY = y;
        }
    }


    public class MyView extends View {
        private final Paint mTextPaint;
        private final Paint mTextBackgroundPaint;
        private final Paint mTextLevelPaint;
        private final Paint mPaint;
        private final Paint mTargetPaint;
        private final FontMetricsInt mTextMetrics = new FontMetricsInt();
        private ArrayList<ArrayList<PT>> mLines = new ArrayList<ArrayList<PT>>();
        private ArrayList<PT> mCurLine;
        private ArrayList<VelocityTracker> mVelocityList = new ArrayList<VelocityTracker>();
        private int mHeaderBottom;
        private boolean mCurDown;
        private int mCurX;
        private int mCurY;
        private float mCurPressure;
        private int mCurWidth;
        private VelocityTracker mVelocity;

        public MyView(Context c) {
            super(c);

            DisplayMetrics dm = new DisplayMetrics();
            dm = TsHandWritingAct.this.getApplicationContext().getResources()
                    .getDisplayMetrics();
            int screenWidth = dm.widthPixels;
            int screenHeight = dm.heightPixels;
            if ((480 == screenWidth && 800 == screenHeight)
                    || (800 == screenWidth && 480 == screenHeight)) {
                mZoom = 2;
            }

            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextSize(10 * mZoom);
            mTextPaint.setARGB(255, 0, 0, 0);
            mTextBackgroundPaint = new Paint();
            mTextBackgroundPaint.setAntiAlias(false);
            mTextBackgroundPaint.setARGB(255, 0, 255, 0);
            mTextLevelPaint = new Paint();
            mTextLevelPaint.setAntiAlias(false);
            mTextLevelPaint.setARGB(192, 255, 0, 0);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setARGB(255, 0, 0, 0);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(3);
            mTargetPaint = new Paint();
            mTargetPaint.setAntiAlias(false);
            mTargetPaint.setARGB(192, 0, 255, 0);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(5);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            mTextPaint.getFontMetricsInt(mTextMetrics);
            mHeaderBottom = -mTextMetrics.ascent + mTextMetrics.descent + 2;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int w = getWidth() / 5;
            int base = -mTextMetrics.ascent + 1;
            int bottom = mHeaderBottom;
            canvas.drawColor(Color.BLACK);
            canvas.drawRect(0, 0, w - 1, bottom, mTextBackgroundPaint);
            canvas.drawText("X: " + mCurX, 1, base, mTextPaint);

            canvas.drawRect(w, 0, (w * 2) - 1, bottom, mTextBackgroundPaint);
            canvas.drawText("Y: " + mCurY, 1 + w, base, mTextPaint);

            canvas
                    .drawRect(w * 2, 0, (w * 3) - 1, bottom,
                            mTextBackgroundPaint);
            canvas.drawRect(w * 2, 0, (w * 2) + (mCurPressure * w) - 1, bottom,
                    mTextLevelPaint);
            canvas.drawText("Pres: " + mCurPressure, 1 + w * 2, base,
                    mTextPaint);
            canvas
                    .drawRect(w * 3, 0, (w * 4) - 1, bottom,
                            mTextBackgroundPaint);
            int xVelocity = mVelocity == null ? 0 : (int) (Math.abs(mVelocity
                    .getXVelocity()) * 1000);
            canvas.drawText("XVel: " + xVelocity, 1 + w * 3, base, mTextPaint);

            canvas.drawRect(w * 4, 0, getWidth(), bottom, mTextBackgroundPaint);
            int yVelocity = mVelocity == null ? 0 : (int) (Math.abs(mVelocity
                    .getYVelocity()) * 1000);
            canvas.drawText("YVel: " + yVelocity, 1 + w * 4, base, mTextPaint);

            int lineSz = mLines.size();
            int k = 0;
            for (k = 0; k < lineSz; k++) {
                ArrayList<PT> m = mLines.get(k);

                float lastX = 0;
                float lastY = 0;
                mPaint.setARGB(255, 0, 255, 255);
                int sz = m.size();
                int i = 0;
                for (i = 0; i < sz; i++) {
                    PT n = m.get(i);
                    if (i > 0) {
                        canvas.drawLine(lastX, lastY, n.mX, n.mY, mTargetPaint);
                        canvas.drawPoint(lastX, lastY, mPaint);
                    }

                    lastX = n.mX;
                    lastY = n.mY;
                }

                VelocityTracker velocity = mVelocityList.get(k);
                if (velocity == null) {
                    canvas.drawPoint(lastX, lastY, mPaint);
                } else {
                    mPaint.setARGB(255, 255, 0, 0);
                    float xVel = velocity.getXVelocity() * (1000 / 60);
                    float yVel = velocity.getYVelocity() * (1000 / 60);
                    canvas.drawLine(lastX, lastY, lastX + xVel, lastY + yVel,
                            mPaint);
                }

                if (mCurDown) {
                    canvas.drawLine(0, (int) mCurY, getWidth(), (int) mCurY,
                            mTargetPaint);
                    canvas.drawLine((int) mCurX, 0, (int) mCurX, getHeight(),
                            mTargetPaint);
                    int pressureLevel = (int) (mCurPressure * 255);
                    mPaint
                            .setARGB(255, pressureLevel, 128,
                                    255 - pressureLevel);
                    canvas.drawPoint(mCurX, mCurY, mPaint);
                    canvas.drawCircle(mCurX, mCurY, mCurWidth, mPaint);
                }

            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {

                mVelocity = VelocityTracker.obtain();
                mVelocityList.add(mVelocity);

                mCurLine = new ArrayList<PT>();
                mLines.add(mCurLine);
            }
            mVelocity.addMovement(event);
            mVelocity.computeCurrentVelocity(1);
            final int num = event.getHistorySize();
            for (int i = 0; i < num; i++) {
                mCurLine.add(new PT(event.getHistoricalX(i), event
                        .getHistoricalY(i)));
            }
            mCurLine.add(new PT(event.getX(), event.getY()));
            mCurDown = action == MotionEvent.ACTION_DOWN
                    || action == MotionEvent.ACTION_MOVE;
            mCurX = (int) event.getX();
            mCurY = (int) event.getY();
            mCurPressure = event.getPressure();
//            Xlog.w(TAG, "event.getPressure()= " + mCurPressure);
            mCurWidth = (int) (event.getSize() * (getWidth() / 3));

            invalidate();
            return true;
        }

        public void clear() {
            for (ArrayList<PT> m : mLines) {
                m.clear();
            }
            mLines.clear();
            mVelocityList.clear();
            invalidate();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishTimer();
    }
}
