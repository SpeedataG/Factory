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

public class FlashCustomActivity extends FragActBase implements View.OnClickListener {

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
        openLight();
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    tvFlashlight.setText(getResources().getString(R.string.flash_light_close));
                    openLight();
                    allCount++;
                } else {
                    tvFlashlight.setText(getResources().getString(R.string.flash_light_open));
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

    private void openLight() {
        try {
            java.io.File file = new File("/sys/class/misc/hwoper/led2t/");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
            bufferedWriter.write("1");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            java.io.File file = new File("/sys/class/misc/mgpio/torch/");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
            bufferedWriter.write("on");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeLight() {
        try {
            File file = new File("/sys/class/misc/hwoper/ledda/");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
            bufferedWriter.write("1");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            java.io.File file = new File("/sys/class/misc/mgpio/torch/");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
            bufferedWriter.write("off");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();
        closeLight();
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
                setXml(App.KEY_FLASH_CUSTOM, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_FLASH_CUSTOM, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
