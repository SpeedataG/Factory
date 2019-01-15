package com.spdata.factory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.serialport.DeviceControl;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import common.base.act.FragActBase;

/**
 * Created by 42040 on 2018/7/23.
 */
public class IndicatorLightAct_sd55 extends FragActBase implements View.OnClickListener {
    private DeviceControl deviceControl;
    private CustomTitlebar titlebar;
    /**
     * xxx
     */
    private TextView tvInfor;
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
                try {
                    PowerOffBlue();
                    PowerOffGreen();
                    PowerOffRed();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }, R.string.menu_indicator_light, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_indicator_light);
        initView();
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
            tvInfor.setText(getResources().getString(R.string.LndicatorLight_redtrue));
            showAlert(LED_RED);
        } catch (IOException e) {
            showToast(getResources().getString(R.string.LndicatorLight_red_return));
            tvInfor.setText(getResources().getString(R.string.LndicatorLight_red_false));
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
                message = getResources().getString(R.string.LndicatorLight_blue_sure);
                break;
            case LED_GREEN:
                message = getResources().getString(R.string.LndicatorLight_green_sure);
                break;
            case LED_RED:
                message = getResources().getString(R.string.LndicatorLight_red_sure);
                break;
        }

        new AlertDialog.Builder(mContext).setMessage(message).setCancelable(false).setTitle(getResources().getString(R.string.LndicatorLight_tips)).setPositiveButton
                (R.string.btn_success, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (currentLed) {
                            case LED_RED:
                                //点亮绿灯
                                try {
                                    PowerOffRed();
                                    PowerOnGreen();
                                    tvInfor.setText(getResources().getString(R.string.LndicatorLight_green_true));
                                    showAlert(LED_GREEN);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    tvInfor.setText(getResources().getString(R.string.LndicatorLight_green_false));
                                    setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
                                    finish();
                                    showToast(getResources().getString(R.string.LndicatorLight_green_false));
                                }

                                break;
                            case LED_GREEN:
                                try {
                                    PowerOffGreen();
                                    PowerOnBlue();
                                    tvInfor.setText(getResources().getString(R.string.LndicatorLight_blue_true));
                                    showAlert(LED_BLUE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    tvInfor.setText(getResources().getString(R.string.LndicatorLight_blue_false));
                                    setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
                                    finish();
                                    showToast(getResources().getString(R.string.LndicatorLight_green_false));
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
                }).setNegativeButton(R.string.btn_fail, new DialogInterface.OnClickListener() {
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

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
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
                setXml(App.KEY_INDICATOR_LIGHT, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
