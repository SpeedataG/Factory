package com.spdata.factory;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

//电子罗盘传感器测试
public class CompassSeneorAct extends FragActBase implements SensorEventListener, View.OnClickListener {


    private CustomTitlebar titlebar;
    private TextView tvInfor;
    private ImageView image;
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
        }, getResources().getString(R.string.menu_compass_sensor), null);
    }


    private int count = 1;
    private SensorManager sm;
    private Sensor compassSensor;
    private StringBuffer sb;
    private float currentDegree = 0f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_compass_sensor);
        initView();
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
    @Override
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
        ra.setDuration(60);
        //罗盘图片使用旋转动画
        image.startAnimation(ra);
        currentDegree = -degree;
    }

    //传感器精度的改变
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
        image = (ImageView) findViewById(R.id.image);
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
                setXml(App.KEY_COMPASS_SENSOR, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_COMPASS_SENSOR, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
