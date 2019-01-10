package com.spdata.factory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.utils.ScanUtil;

/**
 * Created by suntianwei on 2017/1/3.
 */
public class ScanX300qAct extends FragActBase implements View.OnClickListener {
    private String result;
    private String START_SCAN_ACTION = "com.geomobile.se4500barcode";

    private String STOP_SCAN_ACTION = "com.geomobile.se4500barcodestop";
    private String SOUTH_SCAN = "com.south.scanctrl";

    private String STOP_SCAN = "com.geomobile.se4500barcode.poweroff";
    private String state;
    private CustomTitlebar titlebar;
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
                finish();
            }
        }, "扫描测试", null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_scan);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        init();
    }

    //扫描：开始扫描
    public void startScan() {
        SystemProperties.set("persist.sys.scanstopimme", "false");
        Intent intent = new Intent();
        intent.setAction(START_SCAN_ACTION);
        sendBroadcast(intent, null);
    }

    // 关闭扫描：
    public void stopScan() {
        SystemProperties.set("persist.sys.scanstopimme", "true");
        Intent intent = new Intent();
        intent.setAction(STOP_SCAN_ACTION);
        sendBroadcast(intent, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        stopScan();
        SystemProperties.set("persist.sys.keyreport", result);
    }

    ScanUtil scanUtil;

    private void init() {
        scanUtil = new ScanUtil(mContext);
        result = SystemProperties.get("persist.sys.keyreport", "true");
        if (result.equals("false")) {
            Intent intent = new Intent(SOUTH_SCAN);
            intent.putExtra("enablescan", true);
            sendBroadcast(intent);
            runscan();
        } else {
            runscan();
        }
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

    private void runscan() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new MyTask(), 100, 3 * 1000);
    }

    private Timer timer = new Timer();

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

    public class MyTask extends TimerTask {
        @Override
        public void run() {
            startScan();
        }
    }
}
