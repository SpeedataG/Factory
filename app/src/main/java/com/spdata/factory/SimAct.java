package com.spdata.factory;

import android.content.Context;
import android.os.Build;
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

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.getimei;
import common.utils.security.CTelephoneInfo;

@EActivity(R.layout.act_sim)
public class SimAct extends FragActBase {
    private static Context mContext;
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private boolean isSIM1Ready;
    private boolean isSIM2Ready;
    private String model;
    private String models;
    private static String imei_2;
    private static String imei_1;

    @Click
    void btnNotPass() {
        setXml(App.KEY_SIM, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_SIM, App.KEY_FINISH);
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
        }, "SIM测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    private TelephonyManager mgr;
    int phonetype;

    @AfterViews
    protected void main() {
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
        if (model.equals("X300Q_X1") || model.equals("X300Q_P1") ||
                model.equals("S510") || model.equals("H500A") ||
                model.equals("X300Q_OLED") || model.equals("X300Q_OLED_GPS")
                || model.equals("spda6735") || model.equals("DCD3") || model.equals("mt6753")
                || model.equals("M08") || Build.MODEL.equals("S1_35") || Build.MODEL.equals("H5_53")
                || Build.MODEL.equals("H5") || Build.MODEL.equals("S1") || Build.MODEL.equals("H5_35")
                || Build.MODEL.equals("CT")) {
            tvInfor.setText("IMEI:" + imeiSIM1 + "\n");
            if (isCanUseSim()) {
                tvInfor.append("SIM卡存在");
            } else {
                tvInfor.setText("SIM不存在");
            }
        } else {
            if (isSIM1Ready) {
                tvInfor.append("sim1:存在\n");
            } else {
                tvInfor.append("sim1:不存在\n");
            }
            if (isSIM2Ready) {
                tvInfor.append("sim2:存在\n");
            } else {
                tvInfor.append("sim2:不存在\n");
            }

            if (!isSIM1Ready || !isSIM2Ready) {
                titlebar.setAttrs("请插入两张SIM卡进行测试！");
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
                                || model.equals("H500A") || model.equals("X300Q_OLED")|| model.equals("CT")
                                || model.equals("X300Q_OLED_GPS") || model.equals("spda6735")
                                || model.equals("DCD3") || model.equals("mt6753") || model.equals("M08") || Build.MODEL.equals("S1_35") || Build.MODEL.equals("H5_53")
                                || Build.MODEL.equals("H5") || Build.MODEL.equals("S1") || Build.MODEL.equals("H5_35")) {
                            if (isCanUseSim()) {
                                setXml(App.KEY_SIM, App.KEY_FINISH);
                                finish();
                            } else {
                                setXml(App.KEY_SIM, App.KEY_UNFINISH);
                                finish();
                            }

                        } else if (model.equals("T450") || model.equals("KT80") || model.equals("KT55") ||
                                model.equals("W6") || model.equals("S510") || model.equals("DB2_LVDS")
                                || model.equals("KT50") || model.equals("KT50_B2") || model.equals("R40")
                                || model.equals("T50") || model.equals("KT50_YQ")
                                || model.equals("T55") || model.equals("DM-P80")
                                || model.equals("N80") || model.equals("N55") || model.equals("X55")
                                || model.equals("N55/X55") || model.equals("T550") || model.equals("M55")
                                || model.equals("KT55L") || model.equals("RT801")
                                || model.equals("T80") || model.equals("T800") || model.equals("FC-K80") ||
                                model.equals("Biowolf LE") || model.equals("KT45") || model.equals("UHF45")
                                || model.equals("3000U") || model.equals("N800") || model.equals("FC-PK80")
                                || model.equals("KT45Q_B2") || model.equals("JM45Q") || model.equals("FT43")
                                || model.equals("PT145") || model.equals("TT43")) {
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
    protected void onDestroy() {
        super.onDestroy();
    }

    //sim卡是否可读
    public boolean isCanUseSim() {
        try {
            TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            return TelephonyManager.SIM_STATE_READY == mgr
                    .getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
