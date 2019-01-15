package com.spdata.factory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * Created by lenovo_pc on 2016/8/24.
 */
public class Kt80Zhongli extends FragActBase implements View.OnClickListener {

    private CustomTitlebar titlebar;
    private LinearLayout zhongli;
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
        }, getResources().getString(R.string.menu_zhongli), null);
    }

    private SensorManager sm;
    private StringBuffer sbacc;
    private StringBuffer sbori;
    float[] accelerometerValues = new float[3];
    MyView mAnimView = null;
    boolean i = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_zhongli_layout);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        sbacc = new StringBuffer();
        sbori = new StringBuffer();
        initSensor();
        mAnimView = new MyView(mContext);
        LinearLayout ll = (LinearLayout) findViewById(R.id.zhongli);
        ll.addView(mAnimView);
    }

    private void initSensor() {
        // 获取SensorManager对象
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sacc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(new AccSensorListener(), sacc, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        zhongli = (LinearLayout) findViewById(R.id.zhongli);
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
                setXml(App.KEY_ZHONGLI, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_ZHONGLI, App.KEY_UNFINISH);
                finish();
                break;
        }
    }

    public class AccSensorListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            //坐标系x轴上的加速度分量
            float accx = event.values[0];
            //坐标系y轴上的加速度分量
            float accy = event.values[1];
            //坐标系z轴上的加速度分量
            float accz = event.values[2];
            sbacc.setLength(0);
            sbacc.append("x-> " + accx);
            sbacc.append("\n");
            sbacc.append("y-> " + accy);
            sbacc.append("\n");
            sbacc.append("z-> " + accz);
            sbacc.append("\n");
            accelerometerValues = event.values;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        i = false;
        Log.d("-----", "pwpwpwpwp");
        super.onDestroy();
    }

    public class MyView extends SurfaceView implements SurfaceHolder.Callback, Runnable,
            SensorEventListener {
        /**
         * 每50帧刷新一次屏幕
         **/
        public static final int TIME_IN_FRAME = 50;
        /**
         * 游戏画笔
         **/
        Paint mPaint = null;
        Paint mTextPaint = null;
        SurfaceHolder mSurfaceHolder = null;
        /**
         * 控制游戏更新循环
         **/
        boolean mRunning = false;
        /**
         * 游戏画布
         **/
        Canvas mCanvas = null;
        boolean mIsRunning = false;
        /**
         * SensorManager管理器
         **/
        private SensorManager mSensorMgr = null;
        Sensor mSensor = null;
        /**
         * 手机屏幕宽高
         **/
        int mScreenWidth = 0;
        int mScreenHeight = 0;
        /**
         * 小球资源文件越界区域
         **/
        private int mScreenBallWidth = 0;
        private int mScreenBallHeight = 0;
        /**
         * 游戏背景文件
         **/
        private Bitmap mbitmapBg;
        /**
         * 小球资源文件
         **/
        private Bitmap mbitmapBall;

        /**
         * 小球的坐标位置
         **/
        private float mPosX = 200;
        private float mPosY = 0;

        /**
         * 重力感应X轴 Y轴 Z轴的重力值
         **/
        private float mGX = 0;
        private float mGY = 0;
        private float mGZ = 0;

        public MyView(Context context) {
            super(context);
            /** 设置当前View拥有控制焦点 **/
            this.setFocusable(true);
            /** 设置当前View拥有触摸事件 **/
            this.setFocusableInTouchMode(true);
            /** 拿到SurfaceHolder对象 **/
            mSurfaceHolder = this.getHolder();
            /** 将mSurfaceHolder添加到Callback回调函数中 **/
            mSurfaceHolder.addCallback(this);
            /** 创建画布 **/
            mCanvas = new Canvas();
            /** 创建曲线画笔 **/
            mPaint = new Paint();
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(23);
            /** 加载小球资源 **/
            mbitmapBall = BitmapFactory.decodeResource(this.getResources(),
                    R.mipmap.bal);
            /** 加载游戏背景 **/
            mbitmapBg = BitmapFactory.decodeResource(this.getResources(),
                    R.mipmap.bg2);
            mbitmapBg.getPixel(mScreenWidth, mScreenHeight);

            /** 得到SensorManager对象 **/
            mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
            mSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorMgr.registerListener(this, mSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }

        private void Draw() {
            if (i) {
                /** 绘制游戏背景 **/
                if (mCanvas == null) {
                    return;
                }
                mCanvas.drawBitmap(mbitmapBg, 0, 0, mPaint);
                /** 绘制小球 **/
                mCanvas.drawBitmap(mbitmapBall, mPosX, mPosY, mPaint);
                /** X轴 Y轴 Z轴的重力值 **/

                mCanvas.drawText(getResources().getString(R.string.ZhongLiGanYing_x) + mGX, 0, 20, mPaint);
                mCanvas.drawText(getResources().getString(R.string.ZhongLiGanYing_y) + mGY, 0, 40, mPaint);
                mCanvas.drawText(getResources().getString(R.string.ZhongLiGanYing_z) + mGZ, 0, 60, mPaint);
                Log.d("1111", "2222");
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mIsRunning = true;
            new Thread(this).start();
            mScreenWidth = this.getWidth();
            mScreenHeight = this.getHeight();
            mScreenBallWidth = mScreenWidth - mbitmapBall.getWidth();
            mScreenBallHeight = mScreenHeight - mbitmapBall.getHeight();
            Log.d("3333", "4444");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mIsRunning = false;
            Log.d("5555", "6666");
        }

        @Override
        public void run() {
            while (mIsRunning) {
                if (i) {
                    long startTime = System.currentTimeMillis();
                    mCanvas = mSurfaceHolder.lockCanvas();
                    Draw();
                    if (mCanvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                    }
                    long endTime = System.currentTimeMillis();
                    int diffTime = (int) (endTime - startTime);
                    while (diffTime <= TIME_IN_FRAME) {
                        diffTime = (int) (System.currentTimeMillis() - startTime);
                        /** 线程等待 **/
                        Thread.yield();
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (i) {
                mGX = event.values[SensorManager.DATA_X];
                mGY = event.values[SensorManager.DATA_Y];
                mGZ = event.values[SensorManager.DATA_Z];
                mPosX -= mGX * 2;
                mPosY += mGY * 2;
                if (mPosX < 0) {
                    mPosX = 0;
                } else if (mPosX > mScreenBallWidth) {
                    mPosX = mScreenBallWidth;
                }
                if (mPosY < 0) {
                    mPosY = 0;
                } else if (mPosY > mScreenBallHeight) {
                    mPosY = mScreenBallHeight;
                }
            }
        }
    }
}
