package com.spdata.factory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemProperties;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;
import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.Timer;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.DeviceControl;
import common.utils.ScanUtil;

import static android.R.attr.data;
import static com.umeng.analytics.pro.x.S;

@EActivity(R.layout.act_scan)
public class ScanAct extends FragActBase {

    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private String result;
    private ScanDecode scanDecode;

    @Click
    void btnNotPass() {
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
//        judgePropert();
        init();
//        scanDecode = new ScanDecode(this);
//        scanDecode.initService("true");
//        scanDecode.getBarCode(new ScanInterface.OnScanListener() {
//            @Override
//            public void getBarcode(String s) {
//                if (s == null) {
//                    scanDecode.stopScan();
//                } else {
//                    new AlertDialog.Builder(mContext).setMessage("扫描到的数据:" + s).setTitle("成功")
//                            .setPositiveButton("成功", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    setXml(App.KEY_SCAN, App.KEY_FINISH);
//                                    finish();
//                                }
//                            }).show();
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        scanDecode.starScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        scanDecode.stopScan();
    }

    ScanUtil scanUtil;

    private void init() {
        scanUtil = new ScanUtil(mContext);
//        result = SystemProperties.get("persist.sys.keyreport", "true");
//        if (result.equals("false")) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (SystemProperties.get("persist.sys.scanheadtype").equals("6603")) {
                    startScanService();
                    scanUtil.repeatScans();
                } else {
                    scanUtil.repeatScans();
                }
            }
        }).start();
//        } else {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    if(SystemProperties.get("persist.sys.scanheadtype").equals("6603")) {
//                        scanUtil.repeatScan();
//                    }else {
//                        scanUtil.repeatScan();
//                    }
//                }
//            }).start();
//        }

        scanUtil.setOnScanListener(new ScanUtil.OnScanListener() {
            @Override
            public void getBarcode(String data) {
                new AlertDialog.Builder(mContext).setMessage("扫描到的数据:" + data).setTitle("成功")
                        .setPositiveButton("成功", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setXml(App.KEY_SCAN, App.KEY_FINISH);
                                finish();
                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        scanDecode.stopScan();
//        scanDecode.onDestroy();
        scanUtil.unScan();
        new Thread(new Runnable() {
            @Override
            public void run() {
                stopScanService();
            }
        }).start();
    }

    private void startScanService() {//启动扫描服务
//        SystemProperties.set("persist.sys.keyreport", "true");
        Intent Barcodeintent = new Intent();
        Barcodeintent.setPackage("com.geomobile.oemscanservice");
        mContext.startService(Barcodeintent);
    }

    private void stopScanService() {//停止扫描服务
//        SystemProperties.set("persist.sys.keyreport", "false");
        SystemProperties.set("persist.sys.scanstopimme", "true");
        Intent Barcodeintent = new Intent();
        Barcodeintent.setPackage("com.geomobile.oemscanservice");
        mContext.stopService(Barcodeintent);
    }

    /**
     * 判断快捷扫描是否勾选 不勾选跳转到系统设置中进行设置
     */
    private void judgePropert() {
        result = SystemProperties.get("persisy.sys.scankeydisable", "true");
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
                                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
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
