package com.spdata.factory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
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

//指示灯测试
public class IndicatorLightAct extends FragActBase implements View.OnClickListener {

    private String model;
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
                    if (model.equals("M08")) {
                        PowerOffRed2();
                    }
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

    private void init() {
        File DeviceName = new File(DEVFILE_PATH);
        try {
            CtrlFile = new BufferedWriter(new FileWriter(DeviceName, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_indicator_light);
        initView();
        model = Build.MODEL;
        initTitlebar();
        setSwipeEnable(false);
        init();
        try {
            PowerOnRed();
            tvInfor.setText(R.string.IndicatorLightAct_red_open);
            showAlert(LED_RED);
        } catch (IOException e) {
            showToast(getResources().getString(R.string.IndicatorLightAct_red_faild));
            tvInfor.setText(R.string.IndicatorLightAct_red_faild);
            setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
            finish();
            e.printStackTrace();
        }
    }

    private final int LED_RED = 0;
    private final int LED_GREEN = 1;
    private final int LED_BLUE = 2;
    private final int LED_RED2 = 3;

    private void showAlert(final int currentLed) {
        String message = "";
        switch (currentLed) {
            case LED_BLUE:
                message = getResources().getString(R.string.IndicatorLightAct_bule_open);
                break;
            case LED_GREEN:
                message = getResources().getString(R.string.IndicatorLightAct_green_open);
                break;
            case LED_RED:
                message = getResources().getString(R.string.IndicatorLightAct_red_open);
                break;
            case LED_RED2:
                message = getResources().getString(R.string.IndicatorLightAct_red_open);
                break;
            default:
                break;
        }

        new AlertDialog.Builder(mContext).setMessage(message).setCancelable(false).setTitle("").setPositiveButton
                (R.string.btn_success, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (currentLed) {
                            case LED_RED:
                                //点亮绿灯
                                if (Build.MODEL.equals("CT")) {
                                    try {
                                        PowerOffRed();
                                        PowerOnBlue();
                                        tvInfor.setText(R.string.IndicatorLightAct_bule_open);
                                        showAlert(LED_BLUE);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        PowerOffRed();
                                        PowerOnGreen();
                                        tvInfor.setText(R.string.IndicatorLightAct_green_open);
                                        showAlert(LED_GREEN);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        tvInfor.setText(R.string.IndicatorLightAct_green_faild);
                                        setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
                                        finish();
                                        showToast(getResources().getString(R.string.IndicatorLightAct_green_faild));
                                    }
                                }
                                break;
                            case LED_GREEN:
                                try {
                                    PowerOffGreen();
                                    PowerOnBlue();
                                    tvInfor.setText(R.string.IndicatorLightAct_bule_open);
                                    showAlert(LED_BLUE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    tvInfor.setText(R.string.IndicatorLightAct_bule_faild);
                                    setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
                                    finish();
                                    showToast(getResources().getString(R.string.IndicatorLightAct_green_faild));
                                }
                                break;
                            case LED_BLUE:
                                try {
                                    PowerOffBlue();
                                    if (model.equals("M08")) {
                                        PowerOnRed2();
                                        tvInfor.setText(R.string.IndicatorLightAct_red_open);
                                        showAlert(LED_RED2);
                                    } else {
                                        setXml(App.KEY_INDICATOR_LIGHT, App.KEY_FINISH);
                                        finish();
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    if (model.equals("M08")) {
                                        tvInfor.setText(R.string.IndicatorLightAct_red_faild);
                                        setXml(App.KEY_INDICATOR_LIGHT, App.KEY_UNFINISH);
                                        finish();
                                        showToast(getResources().getString(R.string.IndicatorLightAct_red_faild));
                                    } else {
                                        showToast(getResources().getString(R.string.IndicatorLightAct_bule_faild));
                                    }
                                }
                                break;
                            case LED_RED2:
                                try {
                                    PowerOffRed2();
                                    setXml(App.KEY_INDICATOR_LIGHT, App.KEY_FINISH);
                                    finish();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    showToast(getResources().getString(R.string.IndicatorLightAct_red_faild));
                                }
                                break;
                            default:
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
            if (model.equals("M08")) {
                PowerOffRed2();
            }
            PowerOffBlue();
            PowerOffGreen();
            PowerOffRed();
        } catch (IOException e) {
            e.printStackTrace();
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

    private BufferedWriter CtrlFile;
    private static final String DEVFILE_PATH = "/sys/class/misc/mtgpio/pin";

    //红绿蓝：80 78 79 M08加一个红71
    public void PowerOnRed() throws IOException {
        CtrlFile.write("-wdout80 1");
        CtrlFile.flush();
    }

    public void PowerOffRed() throws IOException {
        CtrlFile.write("-wdout80 0");
        CtrlFile.flush();
    }

    public void PowerOnRed2() throws IOException {
        CtrlFile.write("-wdout71 1");
        CtrlFile.flush();
    }

    public void PowerOffRed2() throws IOException {
        CtrlFile.write("-wdout71 0");
        CtrlFile.flush();
    }

    public void PowerOnGreen() throws IOException {
        CtrlFile.write("-wdout79 1");
        CtrlFile.flush();
    }

    public void PowerOffGreen() throws IOException {
        CtrlFile.write("-wdout79 0");
        CtrlFile.flush();
    }

    public void PowerOnGreen2() throws IOException {
        CtrlFile.write("-wdout16 1");
        CtrlFile.flush();
    }

    public void PowerOffGreen2() throws IOException {
        CtrlFile.write("-wdout16 0");
        CtrlFile.flush();
    }

    public void PowerOnBlue() throws IOException {
        CtrlFile.write("-wdout78 1");
        CtrlFile.flush();
    }

    public void PowerOffBlue() throws IOException {
        CtrlFile.write("-wdout78 0");
        CtrlFile.flush();
    }

    public void PowerOnBlue2() throws IOException {
        CtrlFile.write("-wdout18 1");
        CtrlFile.flush();
    }

    public void PowerOffBlue2() throws IOException {
        CtrlFile.write("-wdout18 0");
        CtrlFile.flush();
    }

    public void DeviceClose() throws IOException {
        CtrlFile.close();
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
        }
    }
}
