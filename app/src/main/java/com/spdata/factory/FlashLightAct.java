package com.spdata.factory;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.spdata.factory.application.App;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import common.base.act.FragActBase;

/**
 * Created by xu on 2016/7/27.
 */
public class FlashLightAct extends FragActBase implements View.OnClickListener {

    Camera camera = null;// = Camera.open();
    Camera.Parameters parameter;// = camera.getParameters();
    /**
     * 请关闭手电筒
     */
    private TextView tvFlashlight;
    private ToggleButton toggleButton;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    int allCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);
        initView();
        setSwipeEnable(false);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    tvFlashlight.setText("请关闭手电筒");
                    openLight();
                    allCount++;
                } else {
                    tvFlashlight.setText("请打开手电筒");
                    closeLight();
                    allCount++;
                    if (allCount >= 2) {
                        btnNotPass.setVisibility(View.VISIBLE);
                        btnPass.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SystemProperties.get("persist.sys.iscamera").equals("close")) {
            SystemProperties.set("persist.sys.scanstopimme", "true");
            Intent opencam = new Intent();
            opencam.setAction("com.se4500.opencamera");
            this.sendBroadcast(opencam, null);
        }
        if (camera == null) {
            try {
                camera = Camera.open();
                parameter = camera.getParameters();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("ceshi----");
            openLight();
        } else {
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            camera = Camera.open();
            parameter = camera.getParameters();
            System.out.println("ceshi----");
            openLight();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean judgeSe4500() {
        File DeviceName = new File("proc/se4500");
        try {
            BufferedWriter CtrlFile = new BufferedWriter(new FileWriter(
                    DeviceName, false));
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return false;
        } // open
    }


    public void openLight() {
        // 打开闪光灯关键代码
        camera.startPreview();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameter);
        camera.startPreview();
//        Toast.makeText(this, "open", Toast.LENGTH_SHORT).show();
    }

    public void closeLight() {// 关闭闪关灯关键代码
        parameter = camera.getParameters();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameter);// 关闭闪关灯关键代码
        parameter = camera.getParameters();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameter);
//        Toast.makeText(this, "close", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        if (SystemProperties.get("persist.sys.iscamera").equals("open")) {
            SystemProperties.set("persist.sys.scanstopimme", "false");
            Intent opencam = new Intent();
            opencam.setAction("com.se4500.closecamera");
            this.sendBroadcast(opencam, null);
        }
    }


    @Override
    protected void initTitlebar() {
    }


    private void initView() {
        tvFlashlight = (TextView) findViewById(R.id.tv_flashlight);
        toggleButton = (ToggleButton) findViewById(R.id.toggle_button);
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
                setXml(App.KEY_FLASH_LIGHT, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_FLASH_LIGHT, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
