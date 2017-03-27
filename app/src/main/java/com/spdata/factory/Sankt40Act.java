package com.spdata.factory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Timer;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.ScanUtil;

/**
 * Created by lenovo_pc on 2016/10/24.
 */
@EActivity(R.layout.act_scan)
public class Sankt40Act extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private String result;

    @Click
    void btnNotPass() {
        scanUtil.cancelRepeat();
        scanUtil.stopScan();
        setXml(App.KEY_SCAN, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_SCAN, App.KEY_FINISH);
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
        }, "扫描测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }


    Timer timer;
    boolean is = false;

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        judgePropert();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scanUtil.cancelRepeat();
        if(SystemProperties.get("persist.sys.iscamera").equals("close")){
            Intent opencamera = new Intent();
            //SystemProperties.set("persist.sys.iscamera","open");
            opencamera.setAction("com.se4500.opencamera");
            this.sendOrderedBroadcast(opencamera,null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    ScanUtil scanUtil;

    private void init() {
        if(SystemProperties.get("persist.sys.iscamera").equals("open")){
            Intent closecamera = new Intent();
            //SystemProperties.set("persist.sys.iscamera","open");
            closecamera.setAction("com.se4500.closecamera");
            this.sendOrderedBroadcast(closecamera,null);
        }
        SystemClock.sleep(500);
        scanUtil = new ScanUtil(mContext);
        result = SystemProperties.get("persist.sys.keyreport", "false");
        if (result.equals("false")) {
            if (result.equals("true")) {
                scanUtil.startScan();
                scanUtil.repeatScan();

            }
        } else {
            scanUtil.startScan();
            scanUtil.repeatScan();
        }

        scanUtil.setOnScanListener(new ScanUtil.OnScanListener() {
            @Override
            public void getBarcode(String data) {

                    new AlertDialog.Builder(mContext).setMessage("扫描到的数据:" + data).setTitle("成功")
                            .setPositiveButton("成功", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    scanUtil.stopScan();
                                    setXml(App.KEY_SCAN, App.KEY_FINISH);
                                    finish();
                                }
                            }).show();

            }
        });
    }

    /**
     * 判断快捷扫描是否勾选 不勾选跳转到系统设置中进行设置
     */
    private void judgePropert() {
        result = SystemProperties.get("persist.sys.keyreport", "true");
//        SystemProperties.set("persist.sys.keyreport", "true");
        if (result.equals("false")) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.key_test_back_title)
                    .setMessage(R.string.action_dialog_setting_config)
                    .setPositiveButton(
                            R.string.action_dialog_setting_config_sure_go,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = new Intent(
                                            Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                    startActivityForResult(intent, 1);
                                }
                            })
                    .setNegativeButton(R.string.action_exit_cancel,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                }
                            }
                    ).show();
        }
    }
}
