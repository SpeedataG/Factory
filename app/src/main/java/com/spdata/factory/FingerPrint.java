package com.spdata.factory;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Message;
import android.serialport.DeviceControl;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.FingerUtil.FingerTypes;
import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.DataConversionUtils;

import static com.spdata.factory.R.id.btn_ser1;
import static com.spdata.factory.R.id.btn_ser2;
import static com.spdata.factory.R.id.btn_state;
import static com.spdata.factory.R.id.btn_tcs1g;
import static com.spdata.factory.R.id.btn_usb;
import static com.spdata.factory.R.id.btn_zh;
import static com.spdata.factory.R.id.tv_infor;

@EActivity(R.layout.activity_finger_print)
public class FingerPrint extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    TextView text_show;
    private Intent intent;
    private PackageManager packageManager;
    private DeviceControl mdeviceControl;

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

    @Click
    void btnNotPass() {
        setXml(App.KEY_FINGER_PRINT, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_FINGER_PRINT, App.KEY_FINISH);
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
        }, "指纹", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            mdeviceControl.MainPowerOff(93);
            mdeviceControl.MainPowerOff(126);
            mdeviceControl.MainPowerOff(94);
            mdeviceControl.MainPowerOff(99);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        try {
            //sd80 金色指纹上电
            mdeviceControl = new DeviceControl(DeviceControl.PowerType.MAIN);
            mdeviceControl.MainPowerOn(126);
            mdeviceControl.MainPowerOn(93);
            mdeviceControl.MainPowerOn(99);
            mdeviceControl.MainPowerOn(94);
        } catch (IOException e) {
            e.printStackTrace();
        }
        showLoading("检测模块中……");
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
                                showToast("无指纹模块！");
                                hideLoading();
                                text_show.setText("无指纹模块！");
                                break;
                            case 1:
                                hideLoading();
                                showToast("查找到公安指纹模块！");
                                text_show.setText("查找到公安指纹模块！");
                                break;
                            case 2:
                                hideLoading();
                                showToast("查找到民用指纹模块！");
                                text_show.setText("查找到民用指纹模块！");
                                break;
                            case 3:
                                hideLoading();
                                showToast("查找到金色指纹模块！");
                                text_show.setText("查找到金色指纹模块！");
                                break;
                        }
                    }
                });
            }
        }).start();

    }

}
