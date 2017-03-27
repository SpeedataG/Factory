package com.spdata.factory;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

/**
 * Created by lenovo_pc on 2016/8/24.
 */
@EActivity(R.layout.act_barometer)
public class BarometerAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvVersionInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_BAROMETER, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_BAROMETER, App.KEY_FINISH);
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
        }, "气压计", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    private SensorManager sm;
    private StringBuffer sbpre;

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        sbpre = new StringBuffer();
        initSensor();
    }

    private void initSensor() {
        // 获取SensorManager对象
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor spre = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sm.registerListener(new PreSensorListener(), spre, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public class PreSensorListener implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            float pre = event.values[0];
            sbpre.setLength(0);
            sbpre.append("pre-> " + pre);
            sbpre.append("\n");
            tvVersionInfor.setText(sbpre.toString());
        }
    }
}
