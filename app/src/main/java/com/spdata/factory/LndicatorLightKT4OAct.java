package com.spdata.factory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * Created by lenovo_pc on 2016/10/19.
 */
@EActivity(R.layout.act_indicator_light)
public class LndicatorLightKT4OAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

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

    private void init() {
        File DeviceName = new File(DEVFILE_PATH);
        try {
            CtrlFile = new BufferedWriter(new FileWriter(DeviceName, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        init();
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
                                    showToast("蓝灯关闭失败");
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

    //红绿蓝：80 78 79
    public void PowerOnRed() throws IOException {
        CtrlFile.write("-wdout15 1");
        CtrlFile.flush();
    }

    public void PowerOffRed() throws IOException {
        CtrlFile.write("-wdout15 0");
        CtrlFile.flush();
    }

    public void PowerOnGreen() throws IOException {
        CtrlFile.write("-wdout16 1");
        CtrlFile.flush();
    }

    public void PowerOffGreen() throws IOException {
        CtrlFile.write("-wdout16 0");
        CtrlFile.flush();
    }

    public void PowerOnBlue() throws IOException {
        CtrlFile.write("-wdout18 1");
        CtrlFile.flush();
    }

    public void PowerOffBlue() throws IOException {
        CtrlFile.write("-wdout18 0");
        CtrlFile.flush();
    }

    public void DeviceClose() throws IOException {
        CtrlFile.close();
    }
}
