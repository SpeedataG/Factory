package com.spdata.factory;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.serialport.DeviceControlSpd;
import android.serialport.SerialPortSpd;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import common.utils.SharedXmlUtil;


/**
 * @author zzc
 */
public class GPSTestCT extends Activity {

    private TextView mTvGpsCt;
    String TAG = "CIT/GPSTest";
    private DeviceControlSpd deviceControlSpd;
    private SerialPortSpd serialPortSpd;
    private int fd;

    @Override
    public void onCreate(Bundle bundle) {
        Log.v(TAG, "onCreate ");
        super.onCreate(bundle);
        //横屏
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.test_gps_ct_layout);
        mTvGpsCt = findViewById(R.id.tv_gps_ct);
        CustomTitlebar customTitlebar = findViewById(R.id.titlebar);
        customTitlebar.setAttrs(getResources().getString(R.string.menu_gps));

        Button btnSuccess = findViewById(R.id.btn_pass);
        Button btnFailed = findViewById(R.id.btn_not_pass);
        btnFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedXmlUtil.getInstance(GPSTestCT.this).write(App
                        .KEY_GPS, App.KEY_UNFINISH);
                finish();
            }
        });
        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedXmlUtil.getInstance(GPSTestCT.this).write(App
                        .KEY_GPS, App.KEY_FINISH);
                finish();
            }
        });
        openSerial();
    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart ");
        super.onStart();
    }

    private void openSerial() {
        try {
            deviceControlSpd = new DeviceControlSpd(DeviceControlSpd.PowerType.MAIN, 88, 73, 131);
            deviceControlSpd.PowerOnDevice();
            serialPortSpd = new SerialPortSpd();
            serialPortSpd.OpenSerial(SerialPortSpd.SERIAL_TTYMT2, 57600);
            mTvGpsCt.setText("打开串口成功");
            mTvGpsCt.append("\n" + getResources().getString(R.string.gps_ct_append1) + "dev/ttyMT2 " + getResources().getString(R.string.gps_ct_append2) + 57600 + getResources().getString(R.string.gps_ct_append3) +
                    "MAIN " + getResources().getString(R.string.gps_ct_append4) + "[88,73,131] ");
        } catch (IOException e) {
            mTvGpsCt.setText("打开串口失败");
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉虚拟按键全屏显示
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (serialPortSpd != null) {
            fd = serialPortSpd.getFd();
            serialPortSpd.CloseSerial(fd);
        }
        if (deviceControlSpd != null) {
            try {
                deviceControlSpd.PowerOffDevice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
