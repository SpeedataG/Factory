package com.spdata.factory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.serialport.DeviceControl;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

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
 * Created by 42040 on 2018/7/23.
 */
@EActivity(R.layout.act_indicator_light)
public class IndicatorLightAct_sd55 extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private DeviceControl deviceControl;

    @Click
    void btnNotPass() {
        setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_INDICATOR_LIGHT, App.KEY_FINISH);
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
                try {
                    PowerOffBlue();
                    PowerOffGreen();
                    PowerOffRed();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }, "指示灯测试", null);
    }


    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    @AfterViews
    protected void main() {
        try {
            deviceControl = new DeviceControl(DeviceControl.POWER_NEWMAIN);
        } catch (IOException e) {
            e.printStackTrace();
        }
//mt6370_pmu_led2 $ echo 255 > brightness
//        File DeviceName = new File("/sys/class/leds/mt6370_pmu_led3/brightness/");
//        try {
//            BufferedWriter CtrlFile = new BufferedWriter(new FileWriter(DeviceName, false));
//            CtrlFile.write("255");
//            CtrlFile.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        initTitlebar();
        setSwipeEnable(false);
        try {
            PowerOnRed();
            tvInfor.setText("红灯点亮");
            showAlert(LED_RED);
        } catch (IOException e) {
            showToast("点亮红灯失败，返回");
            tvInfor.setText("红灯点亮失败");
            setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
            finish();
            e.printStackTrace();
        }
    }

    private final int LED_RED = 0;
    private final int LED_GREEN = 1;
    private final int LED_BLUE = 2;


    private void showAlert(final int currentLed) {
        String message = "";
        switch (currentLed) {
            case LED_BLUE:
                message = "已点亮蓝灯，请确认";
                break;
            case LED_GREEN:
                message = "已点亮绿灯，请确认";
                break;
            case LED_RED:
                message = "已点亮红灯，请确认";
                break;
        }

        new AlertDialog.Builder(mContext).setMessage(message).setCancelable(false).setTitle("提示").setPositiveButton
                ("成功", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (currentLed) {
                            case LED_RED:
                                //点亮绿灯
                                try {
                                    PowerOffRed();
                                    PowerOnGreen();
                                    tvInfor.setText("绿灯点亮");
                                    showAlert(LED_GREEN);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    tvInfor.setText("绿灯点亮失败");
                                    setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
                                    finish();
                                    showToast("绿灯点亮失败");
                                }

                                break;
                            case LED_GREEN:
                                try {
                                    PowerOffGreen();
                                    PowerOnBlue();
                                    tvInfor.setText("蓝灯点亮");
                                    showAlert(LED_BLUE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    tvInfor.setText("蓝灯点亮失败");
                                    setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
                                    finish();
                                    showToast("绿灯点亮失败");
                                }
                                break;
                            case LED_BLUE:
                                try {
                                    PowerOffBlue();

                                    setXml(App.KEY_INDICATOR_LIGHT, App.KEY_FINISH);
                                    finish();


                                } catch (IOException e) {
                                    e.printStackTrace();
//                                    if (model.equals("M08")) {
//                                        tvInfor.setText("红灯点亮失败");
//                                        setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
//                                        finish();
//                                        showToast("红灯点亮失败");
//                                    } else {
//                                        showToast("蓝灯关闭失败");
//                                    }
                                }
                                break;

                        }
                    }
                }).setNegativeButton("失败", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
                finish();
            }
        }).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            PowerOffBlue();
            PowerOffGreen();
            PowerOffRed();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        try {
            PowerOnRed();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private final String Red = "/sys/class/leds/mt6370_pmu_led1/brightness/";
    private final String Green = "/sys/class/leds/mt6370_pmu_led2/brightness/";
    private final String Blue = "/sys/class/leds/mt6370_pmu_led3/brightness/";

    public void PowerOnRed() throws IOException {
        try {
            BufferedWriter CtrlFile = new BufferedWriter(new FileWriter(new File(Red), false));
            CtrlFile.write("255");
            CtrlFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PowerOffRed() throws IOException {
        try {
            BufferedWriter CtrlFile = new BufferedWriter(new FileWriter(new File(Red), false));
            CtrlFile.write("0");
            CtrlFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void PowerOnGreen() throws IOException {
        try {
            BufferedWriter CtrlFile = new BufferedWriter(new FileWriter(new File(Green), false));
            CtrlFile.write("255");
            CtrlFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PowerOffGreen() throws IOException {
        try {
            BufferedWriter CtrlFile = new BufferedWriter(new FileWriter(new File(Green), false));
            CtrlFile.write("0");
            CtrlFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void PowerOnBlue() throws IOException {
        try {
            BufferedWriter CtrlFile = new BufferedWriter(new FileWriter(new File(Blue), false));
            CtrlFile.write("255");
            CtrlFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PowerOffBlue() throws IOException {
        try {
            BufferedWriter CtrlFile = new BufferedWriter(new FileWriter(new File(Blue), false));
            CtrlFile.write("0");
            CtrlFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}