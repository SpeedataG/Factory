package com.spdata.factory;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * Created by lenovo_pc on 2016/8/24.
 */
public class BarometerAct extends FragActBase implements View.OnClickListener {
    CustomTitlebar titlebar;
    TextView tvVersionInfor;
    Button btnPass;
    Button btnNotPass;
    private SensorManager sm;
    private StringBuffer sbpre;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, "气压计", null);
    }

    private void initSensor() {
        // 获取SensorManager对象
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor spre = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sm.registerListener(new PreSensorListener(), spre, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_barometer);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        sbpre = new StringBuffer();
        initSensor();
    }

    public void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvVersionInfor = (TextView) findViewById(R.id.tv_version_infor);
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
                setXml(App.KEY_BAROMETER, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_BAROMETER, App.KEY_UNFINISH);
                finish();
                break;
        }
    }

    public class PreSensorListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float pre = event.values[0];
            sbpre.setLength(0);
            sbpre.append("pre-> " + pre);
            sbpre.append("\n");
            tvVersionInfor.setText(sbpre.toString());
        }
    }
}
