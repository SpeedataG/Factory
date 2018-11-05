package com.spdata.factory;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import common.base.act.FragActBase;
import common.event.ViewMessage;

//光感传感器测试
@EActivity(R.layout.act_light_sensor)
public class LightSeneorAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    TextView tv_infors;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private MySensorListener mySensorListener;
    private ProSensorListener proSensorListener;

    @Click
    void btnNotPass() {
        setXml(App.KEY_LIGHT_SENSOR, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_LIGHT_SENSOR, App.KEY_FINISH);
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
        }, "光感测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    private String model;
    private StringBuffer sbpro;
    private SensorManager sm;
    private StringBuffer sb;

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        sbpro = new StringBuffer();
        model = Build.MODEL;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获取SensorManager对象
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        mySensorListener = new MySensorListener();
        proSensorListener = new ProSensorListener();
        // 获取Sensor对象 光感传感器
        Sensor ligthSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        sm.registerListener(mySensorListener, ligthSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        //通过调用getDefaultSensor  获取距离传感器
        Sensor spro = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(proSensorListener, spro, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public class MySensorListener implements SensorEventListener {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        public void onSensorChanged(SensorEvent event) {
            // 获取精度
            float acc = event.accuracy;
            // 获取光线强度
            float lux = event.values[0];
            tvInfor.setText("请用手遮挡光感区，光感值变化为正常 " + "\n\n" + "光线强度：" + lux + "\n");
        }
    }

    public class ProSensorListener implements SensorEventListener {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        public void onSensorChanged(SensorEvent event) {
            //获取距离传感器数值
            float pro = event.values[0];
            sbpro.setLength(0);
            if (pro == 0.0) {
                sbpro.append("距离传感器数值：接近" + pro);
            } else {
                sbpro.append("距离传感器数值：离开" + pro);
            }
            sbpro.append("\n");
            tv_infors.setText(sbpro.toString());

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sm.unregisterListener(mySensorListener);
        sm.unregisterListener(proSensorListener);
    }
}
