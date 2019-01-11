package com.spdata.factory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scandecode.ScanDecode;
import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.util.Timer;

import common.base.act.FragActBase;
import common.utils.ScanUtil;

public class ScanAct extends FragActBase implements View.OnClickListener {

    Button btnNotPass;
    private String result;
    private ScanDecode scanDecode;
    private CustomTitlebar titlebar;
    private TextView tvInfor;
    /**
     * 成功
     */
    private Button btnPass;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_scan), null);
    }

    Timer timer;
    boolean is = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_scan);
        initView();
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
                new AlertDialog.Builder(mContext).setMessage(getResources().getString(R.string.ScanAct_dialog_msg) + data).setTitle(getResources().getString(R.string.btn_fail))
                        .setPositiveButton(getResources().getString(R.string.btn_success), new DialogInterface.OnClickListener() {
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
                setXml(App.KEY_SCAN, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_SCAN, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
