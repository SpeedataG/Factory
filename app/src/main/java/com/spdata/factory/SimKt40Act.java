package com.spdata.factory;

import android.content.Context;
import android.os.SystemProperties;
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

/**
 * Created by lenovo_pc on 2016/10/20.
 */
@EActivity(R.layout.act_sim)
public class SimKt40Act extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private String sim1;
    private String sim2;
    private String imei1;
    private String imei2;

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

    @AfterViews
    protected void main() {
        initTitlebar();
        sim1 = SystemProperties.get("persist.sys.fsim1state");
        sim2 = SystemProperties.get("persist.sys.fsim2state");
        imei1 = SystemProperties.get("persist.sys.fimei1state");
        imei2 = SystemProperties.get("persist.sys.fimei2state");
        tvInfor.setText("IMEI1:" + imei1);
        tvInfor.append("\nIMEI2:"+imei2);
        if (!sim1.equals("")){
            tvInfor.append("SIM1存在:");
        }else {
            tvInfor.append("\nSIM1不存在:");
        }
        if (!sim2.equals("")){
            tvInfor.append("SIM2存在:");
        }else {
            tvInfor.append("\nSIM2不存在:");
        }

//        tvInfor.append("\nSIM1:"+imei1);
//        tvInfor.append("\nSIM2:"+imei2);
    }
}
