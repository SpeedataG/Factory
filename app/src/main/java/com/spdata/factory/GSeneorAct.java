package com.spdata.factory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.PlaySoundPool;
import common.utils.ShakeUtils;

//加速传感器测试
@EActivity(R.layout.act_gsensor)
public class GSeneorAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    TextView tv_x;
    @ViewById
    TextView tv_y;
    @ViewById
    TextView tv_z;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_G_SENSOR, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_G_SENSOR, App.KEY_FINISH);
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
        }, "加速传感器测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }


    private ShakeUtils mShakeUtils;
    private int shakeCount = 0;
    private SensorManager sm;
    int sensorType;
    private Timer timer;
    private remindTask task;
    private int change = 0;

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        tvInfor.setText("请上下左右移动设备，观察x、y、z变化\n");
        //创建一个SensorManager来获取系统的传感器服务
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //选取加速度感应器
        sensorType = Sensor.TYPE_ACCELEROMETER;

        /*
         * 最常用的一个方法 注册事件
         * 参数1 ：SensorEventListener监听器
         * 参数2 ：Sensor 一个服务可能有多个Sensor实现，此处调用getDefaultSensor获取默认的Sensor
         * 参数3 ：模式 可选数据变化的刷新频率
         * SENSOR_DELAY_NORMAL:匹配屏幕方向的变化，默认传感器速度
SENSOR_DELAY_UI：匹配用户接口
如果更新UI建议使用SENSOR_DELAY_GAME：
匹配游戏，游戏开发建议使用SENSOR_DELAY_FASTEST.：匹配所能达到的最快
         * */
        sm.registerListener(myAccelerometerListener, sm.getDefaultSensor(sensorType), SensorManager.SENSOR_DELAY_NORMAL);

        task = new remindTask();
        remind(task);

    }

    /*
         * SensorEventListener接口的实现，需要实现两个方法
         * 方法1 onSensorChanged 当数据变化的时候被触发调用
         * 方法2 onAccuracyChanged 当获得数据的精度发生变化的时候被调用，比如突然无法获得数据时
         * */
    final SensorEventListener myAccelerometerListener = new SensorEventListener() {

        //复写onSensorChanged方法
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float X_lateral = sensorEvent.values[0];
                float Y_longitudinal = sensorEvent.values[1];
                float Z_vertical = sensorEvent.values[2];
                tv_x.setText("x=" + X_lateral);
                tv_y.setText("y=" + Y_longitudinal);
                tv_z.setText("z=" + Z_vertical);
            }
        }
        //复写onAccuracyChanged方法
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//            tvInfor.setText("onAccuracyChanged被触发");
        }
    };
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            change = 1;
            setXml(App.KEY_G_SENSOR, App.KEY_FINISH);
            showToast("加速传感器正常!");
            finish();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private class remindTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (change == 0) {
                        tv_x.addTextChangedListener(textWatcher);
                        tv_y.addTextChangedListener(textWatcher);
                        tv_z.addTextChangedListener(textWatcher);
                    } else {
                        setXml(App.KEY_G_SENSOR, App.KEY_UNFINISH);
                        showToast("加速传感器不正常！");
                        finish();
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
    protected void onDestroy() {
        super.onDestroy();
        finishTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(myAccelerometerListener, sm.getDefaultSensor(sensorType), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
//        mShakeUtils.onPause();
        /*
         * 很关键的部分：注意，说明文档中提到，即使activity不可见的时候，感应器依然会继续的工作，测试的时候可以发现，没有正常的刷新频率
    	 * 也会非常高，所以一定要在onPause方法中关闭触发器，否则讲耗费用户大量电量，很不负责。
    	 * */
        sm.unregisterListener(myAccelerometerListener);
        super.onPause();
    }

}
