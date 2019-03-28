package com.spdata.factory;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.serialport.DeviceControlSpd;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.FingerUtil.FingerTypes;
import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import common.base.act.FragActBase;

public class FingerPrint extends FragActBase implements View.OnClickListener {
    private Intent intent;
    private PackageManager packageManager;
    private DeviceControlSpd mdeviceControl;
    private CustomTitlebar titlebar;
    private TextView textShow;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

//    @Click
//    void btn_id() {
//        try {
//            packageManager = getPackageManager();
////        intent = new Intent();
//            intent = packageManager.getLaunchIntentForPackage("com.routon.iDR410SDKDemo");
//            startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            showToast("未找到指定应用");
//            finish();
//        }
//    }

    //    @Click
//    void btn_zh() {
//        try {
//            packageManager = getPackageManager();
////        intent = new Intent();
//            intent = packageManager.getLaunchIntentForPackage("com.hongda.S580");
//            startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            showToast("未找到指定应用");
//            finish();
//        }

//    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_finger), null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (App.getModel().equals("SK80") || App.getModel().equals("SK80H")) {
                writeFile("0");
                mdeviceControl.ExpandPowerOff(1);
                mdeviceControl.ExpandPowerOff(3);
            } else {
                mdeviceControl.MainPowerOff(93);
                mdeviceControl.MainPowerOff(126);
                mdeviceControl.MainPowerOff(94);
                mdeviceControl.MainPowerOff(99);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        try {
            mdeviceControl = new DeviceControlSpd(DeviceControlSpd.PowerType.MAIN);
            if (App.getModel().equals("SK80") || App.getModel().equals("SK80H")) {
                writeFile("1");
                mdeviceControl.ExpandPowerOn(1);
                mdeviceControl.ExpandPowerOn(3);
            } else {
                //sd80 金色指纹上电
                mdeviceControl.MainPowerOn(126);
                mdeviceControl.MainPowerOn(93);
                mdeviceControl.MainPowerOn(99);
                mdeviceControl.MainPowerOn(94);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        showLoading(getResources().getString(R.string.Expand_loading));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (FingerTypes.getrwusbdevices(FingerPrint.this)) {
                            case 0:
                                showToast(getResources().getString(R.string.Expand_msg_obj1));
                                hideLoading();
                                textShow.setText(getResources().getString(R.string.Expand_msg_obj1));
                                break;
                            case 1:
                                hideLoading();
                                showToast(getResources().getString(R.string.FingerPrint_toast1));
                                textShow.setText(getResources().getString(R.string.FingerPrint_toast1));
                                break;
                            case 2:
                                hideLoading();
                                showToast(getResources().getString(R.string.FingerPrint_toast2));
                                textShow.setText(getResources().getString(R.string.FingerPrint_toast2));
                                break;
                            case 3:
                                hideLoading();
                                showToast(getResources().getString(R.string.FingerPrint_toast3));
                                textShow.setText(getResources().getString(R.string.FingerPrint_toast3));
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        }).start();

    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        textShow = (TextView) findViewById(R.id.text_show);
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
                setXml(App.KEY_FINGER_PRINT, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_FINGER_PRINT, App.KEY_UNFINISH);
                finish();
                break;
        }
    }

    private void writeFile(String type) {
        try {
            File file = new File("/sys/class/misc/hwoper/usb_route/");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(type);
            bw.flush();
            bw.close();
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
            showToast("xiewenjianerr");
        }
    }
}
