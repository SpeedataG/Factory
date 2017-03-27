package com.spdata.factory;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import common.base.act.FragActBase;
import common.event.ViewMessage;

//电子罗盘传感器测试
@EActivity(R.layout.act_compass_sensor)
public class CompassSeneorAct extends FragActBase implements SensorEventListener {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    ImageView image;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_COMPASS_SENSOR, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_COMPASS_SENSOR, App.KEY_FINISH);
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
        }, "电子罗盘测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    private int count = 1;
    private SensorManager sm;
    private Sensor compassSensor;
    private StringBuffer sb;
    private float currentDegree = 0f;

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        initSensor();
//        Timer timer=new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                count++;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showToast("rotate");
//                        rotate(-1*count);
//                    }
//                });
//            }
//        },0,1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initSensor() {
        // 获取SensorManager对象
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 获取Sensor对象
        compassSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);

    }

    //传感器报告新的值(方向改变)
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
              /*
            RotateAnimation类：旋转变化动画类
            参数说明:
            fromDegrees：旋转的开始角度。
            toDegrees：旋转的结束角度。
            pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
            pivotXValue：X坐标的伸缩值。
            pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
            pivotYValue：Y坐标的伸缩值
            */
            float degree = event.values[0];
            rotate(degree);
        }
    }

    private void rotate(float degree) {
//        showToast("onSensorChanged11"+-degree);
        RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        //旋转过程持续时间
        ra.setDuration(200);
        //罗盘图片使用旋转动画
        image.startAnimation(ra);
        currentDegree = -degree;
    }

    //传感器精度的改变
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
