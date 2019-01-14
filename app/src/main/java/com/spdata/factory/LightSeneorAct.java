package com.spdata.factory;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

//光感传感器测试
public class LightSeneorAct extends FragActBase implements View.OnClickListener {

    private MySensorListener mySensorListener;
    private ProSensorListener proSensorListener;
    private CustomTitlebar titlebar;
    /**
     * 请用手遮挡光感区，光感值变化为正常
     */
    private TextView tvInfor;
    /**
     * 距离传感器数值：
     */
    private TextView tvInfors;
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
        }, getResources().getString(R.string.menu_light_sensor), null);
    }


    private StringBuffer sbpro;
    private SensorManager sm;
    private StringBuffer sb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_light_sensor);
        initView();
        initTitlebar();
        setSwipeEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sbpro = new StringBuffer();
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

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
        tvInfors = (TextView) findViewById(R.id.tv_infors);
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
                setXml(App.KEY_LIGHT_SENSOR, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_LIGHT_SENSOR, App.KEY_UNFINISH);
                finish();
                break;
        }
    }


    public class MySensorListener implements SensorEventListener {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 获取精度
            float acc = event.accuracy;
            // 获取光线强度
            float lux = event.values[0];
            tvInfor.setText(getResources().getString(R.string.LightSeneorAct_msg) + lux + "\n");
        }
    }

    public class ProSensorListener implements SensorEventListener {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            //获取距离传感器数值
            float pro = event.values[0];
            sbpro.setLength(0);
            if (pro == 0.0) {
                sbpro.append(getResources().getString(R.string.LightSeneorAct_msg2) + pro);
            } else {
                sbpro.append(getResources().getString(R.string.LightSeneorAct_msg3) + pro);
            }
            sbpro.append("\n");
            tvInfors.setText(sbpro.toString());

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sm.unregisterListener(mySensorListener);
        sm.unregisterListener(proSensorListener);
    }
}
