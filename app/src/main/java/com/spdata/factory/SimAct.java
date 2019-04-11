package com.spdata.factory;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;
import common.utils.getimei;
import common.utils.security.CTelephoneInfo;

public class SimAct extends FragActBase implements View.OnClickListener {

    private boolean isSIM1Ready;
    private boolean isSIM2Ready;
    private String model;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sim);
        initView();
        initTitlebar();
        String ss = getimei.getImsiAll(mContext);
//        initQualcommDoubleSim();
//
//        titlebar.setAttrs(imei_1);
//        titlebar.setAttrs(imei_2);
        mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phonetype = mgr.getPhoneType();
        CTelephoneInfo telephonyInfo = CTelephoneInfo.getInstance(this);
        telephonyInfo.setCTelephoneInfo();
        isSIM1Ready = telephonyInfo.isSIM1Ready();
        isSIM2Ready = telephonyInfo.isSIM2Ready();
        String imeiSIM1 = telephonyInfo.getImeiSIM1();
        String imeiSIM2 = telephonyInfo.getImeiSIM2();
        boolean isDualSIM = telephonyInfo.isDualSim();//判断是否为双卡
        tvInfor.setText("IMEI1:" + imeiSIM1 + "\n" + "IMEI2:" + imeiSIM2 + "\n");
        model = Build.MODEL;
//        models = model.substring(0, 4);
        if (model.equals("X300Q_X1") || model.equals("X300Q_P1")
                || model.equals("S510") || model.equals("H500A")
                || model.equals("X300Q_OLED") || model.equals("X300Q_OLED_GPS")
                || model.equals("spda6735") || model.equals("DCD3") || model.equals("mt6753")
                || model.equals("M08") || Build.MODEL.equals("S1_35") || Build.MODEL.equals("H5_53")
                || Build.MODEL.equals("H5") || Build.MODEL.equals("S1") || Build.MODEL.equals("H5_35")
                || Build.MODEL.equals("CT") || model.equals("TC01") || model.equals("X300Q")) {
            tvInfor.setText("IMEI:" + imeiSIM1 + "\n");
            if (isCanUseSim()) {
                tvInfor.append(getResources().getString(R.string.SimAct_on) );
            } else {
                tvInfor.append(getResources().getString(R.string.SimAct_off));
            }
        } else {
            if (isSIM1Ready) {
                tvInfor.append(getResources().getString(R.string.SimAct_on1));
            } else {
                tvInfor.append(getResources().getString(R.string.SimAct_off1));
            }
            if (isSIM2Ready) {
                tvInfor.append(getResources().getString(R.string.SimAct_on2));
            } else {
                tvInfor.append(getResources().getString(R.string.SimAct_off2));
            }

            if (!isSIM1Ready || !isSIM2Ready) {
                titlebar.setAttrs(getResources().getString(R.string.SimAct_msg));
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (model.equals("X300Q_X1") || model.equals("X300Q_P1") || model.equals("S510")
                                || model.equals("H500A") || model.equals("X300Q_OLED") || model.equals("CT")
                                || model.equals("X300Q_OLED_GPS") || model.equals("spda6735")
                                || model.equals("DCD3") || model.equals("mt6753") || model.equals("M08")
                                || Build.MODEL.equals("S1_35") || Build.MODEL.equals("H5_53")
                                || Build.MODEL.equals("H5") || Build.MODEL.equals("S1")
                                || Build.MODEL.equals("H5_35") || model.equals("TC01")
                                || model.equals("X300Q")) {
                            if (isCanUseSim()) {
                                setXml(App.KEY_SIM, App.KEY_FINISH);
                                finish();
                            } else {
                                setXml(App.KEY_SIM, App.KEY_UNFINISH);
                                finish();
                            }

                        } else {
                            if (isSIM1Ready && isSIM2Ready) {
                                setXml(App.KEY_SIM, App.KEY_FINISH);
                                finish();
                            } else {
                                setXml(App.KEY_SIM, App.KEY_UNFINISH);
                                finish();
                            }
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, R.string.menu_simcard, null);
    }

    private TelephonyManager mgr;
    int phonetype;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //sim卡是否可读
    public boolean isCanUseSim() {
        try {
            TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
                setXml(App.KEY_SIM, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_SIM, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
