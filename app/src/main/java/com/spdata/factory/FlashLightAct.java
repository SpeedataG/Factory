package com.spdata.factory;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.spdata.factory.application.App;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import common.base.act.FragActBase;
import common.event.ViewMessage;

/**
 * Created by xu on 2016/7/27.
 */
@EActivity(R.layout.activity_flashlight)
public class FlashLightAct extends FragActBase {

    Camera camera=null;// = Camera.open();
    Camera.Parameters parameter;// = camera.getParameters();
    @ViewById
    ToggleButton toggleButton;
    @ViewById
    TextView tv_flashlight;
    @ViewById
    Button btn_pass;
    @ViewById
    Button btn_not_pass;

    @Click
    void btn_not_pass() {
        setXml(App.KEY_FLASH_LIGHT, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btn_pass() {
        setXml(App.KEY_FLASH_LIGHT, App.KEY_FINISH);
        finish();
    }
    int allCount = 0;
    @AfterViews
    protected void main() {
        setSwipeEnable(false);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO 自动生成的方法存根
                if (arg1) {
                    tv_flashlight.setText("请关闭手电筒");
                    openLight();
                    allCount++;
                } else {
                    tv_flashlight.setText("请打开手电筒");
                    closeLight();
                    allCount++;
                    if (allCount >= 2) {
                        btn_not_pass.setVisibility(View.VISIBLE);
                        btn_pass.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        if (judgeSe4500()) {
            Intent intent = new Intent();
            intent.setAction("com.se4500.opencamera");
            this.sendBroadcast(intent);
            SystemClock.sleep(300);
        }
       if (camera==null){
           try {
               camera = Camera.open();
               parameter = camera.getParameters();
           } catch (Exception e) {
               e.printStackTrace();
           }
           System.out.println("ceshi----");
           openLight();
       }else {
           return;
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {
            camera = Camera.open();
            parameter = camera.getParameters();
            System.out.println("ceshi----");
            openLight();
        } catch (Exception e) {
            // TODO: handle exception
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
    protected void onDestroy() {
        if (judgeSe4500()) {
            Intent intent = new Intent();
            intent.setAction("com.se4500.closecamera");
            this.sendBroadcast(intent);
        }
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        super.onDestroy();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }
}
