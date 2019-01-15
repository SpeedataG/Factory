package com.spdata.factory;

import android.os.Bundle;
import android.os.SystemProperties;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * Created by lenovo_pc on 2016/10/20.
 */
public class SimKt40Act extends FragActBase implements View.OnClickListener {
    Button btnNotPass;
    private String sim1;
    private String sim2;
    private String imei1;
    private String imei2;
    private CustomTitlebar titlebar;
    private TextView tvInfor;
    /**
     * 成功
     */
    private Button btnPass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sim);
        initView();
        initTitlebar();
        sim1 = SystemProperties.get("persist.sys.fsim1state");
        sim2 = SystemProperties.get("persist.sys.fsim2state");
        imei1 = SystemProperties.get("persist.sys.fimei1state");
        imei2 = SystemProperties.get("persist.sys.fimei2state");
        tvInfor.setText("IMEI1:" + imei1);
        tvInfor.append("\nIMEI2:" + imei2);
        if (!sim1.equals("")) {
            tvInfor.append(getResources().getString(R.string.SimKt40_on1));
        } else {
            tvInfor.append(getResources().getString(R.string.SimKt40_off1));
        }
        if (!sim2.equals("")) {
            tvInfor.append(getResources().getString(R.string.SimKt40_on2));
        } else {
            tvInfor.append(getResources().getString(R.string.SimKt40_off2));
        }

//        tvInfor.append("\nSIM1:"+imei1);
//        tvInfor.append("\nSIM2:"+imei2);
    }


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_simcard), null);
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
