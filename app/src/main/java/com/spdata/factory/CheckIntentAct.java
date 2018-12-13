package com.spdata.factory;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.serialport.DeviceControl;
import android.telephony.TelephonyManager;
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
import common.crash.utils.SysInfoUtil;
import common.event.ViewMessage;

@EActivity(R.layout.act_version)
public class CheckIntentAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvVersionInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private static NetworkInfo mNetworkInfo;

    @Click
    void btnNotPass() {
        setXml(App.KEY_INTENET, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_INTENET, App.KEY_FINISH);
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
        }, "RJ45网线接口测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    DeviceControl deviceControl = null;

    @AfterViews
    protected void main() {
        onWindowFocusChanged(true);
        initTitlebar();
        setSwipeEnable(false);
        btnPass.setVisibility(View.GONE);
        try {
            deviceControl = new DeviceControl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.MODEL.equals("TC01")) {
            try {
                deviceControl.MainPowerOn(63);
                deviceControl.MainPowerOn(86);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            writeFile("2");
            try {
                deviceControl.Expand2PowerOn(4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tvVersionInfor.setText("请插入RJ45网线");
        handler.postDelayed(runnable, 500);

    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isNetworkConnected()) {
                int netType = mNetworkInfo.getType();
                int netSubtype = mNetworkInfo.getSubtype();
                if (netType == ConnectivityManager.TYPE_ETHERNET) {
                    //以太网

//                    if (mNetworkInfo.isConnected()) {
                    tvVersionInfor.setText("RJ45网络已连接");
                    setXml(App.KEY_INTENET, App.KEY_FINISH);
                    finish();
//                    } else {
//                        tvVersionInfor.setText("RJ45网络未连接，但已接入网线");
//
//                    }
                } else {
                    tvVersionInfor.setText("请插入RJ45网线");
                }
            } else {
                tvVersionInfor.setText("未连接网络");
            }
            handler.postDelayed(runnable, 500);
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.MODEL.equals("TC01")) {
            try {
                deviceControl.MainPowerOff(63);
                deviceControl.MainPowerOff(86);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            writeFile("0");
            try {
                deviceControl.Expand2PowerOff(4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        handler.removeCallbacks(runnable);

    }

    /**
     * 判断是否有网络连接
     *
     * @return
     */
    public boolean isNetworkConnected() {//true是链接，false是没链接
        ConnectivityManager mConnectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        } else {
            return false;
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
