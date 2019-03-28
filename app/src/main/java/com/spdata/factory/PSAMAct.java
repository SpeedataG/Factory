package com.spdata.factory;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.serialport.DeviceControlSpd;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;
import com.speedata.libutils.ConfigUtils;
import com.speedata.libutils.DataConversionUtils;
import com.speedata.libutils.ReadBean;
import com.speedata.utils.DataCleanUtils;

import java.io.IOException;
import java.util.List;

import common.base.act.FragActBase;
import speedatacom.a3310libs.PsamManager;
import speedatacom.a3310libs.inf.IPsam;

import static jxl.Workbook.getVersion;

public class PSAMAct extends FragActBase implements View.OnClickListener {


    private CustomTitlebar titlebar;
    private TextView tv;
    private TextView tvInfor;
    /**
     * psam1
     */
    private Button btnPsam1;
    /**
     * psam2
     */
    private Button btnPsam2;
    /**
     * sk80串口切换开关
     */
    private Button btnSK80Switch;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    private boolean isSwitch = true;


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_ris_psam), null);
    }

    //获取psam实例
    IPsam psamIntance = PsamManager.getPsamIntance();
    DeviceControlSpd deviceControl1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psam);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        showConfig();
        try {
            switch (App.getModel()) {
                case "SD55":
//                case "SD55UHF":
                    psamIntance.initDev("ttyMT1", 115200, this);
                    deviceControl1 = new DeviceControlSpd(DeviceControlSpd.PowerType.NEW_MAIN, 16, 46);
                    deviceControl1.PowerOnDevice();
                    psamIntance.resetDev(DeviceControlSpd.PowerType.NEW_MAIN, 23);
                    break;

                case "SK80":
                    psamIntance.initDev("ttyMT1", 115200, this);
                    deviceControl1 = new DeviceControlSpd();
                    deviceControl1.Expand2PowerOff(7);
                    btnSK80Switch.setText("大卡");
                    btnSK80Switch.setVisibility(View.VISIBLE);
                    break;

                case "SK80H":
                    deviceControl1 = new DeviceControlSpd();
                    deviceControl1.MainPowerOn(85);
                    psamIntance.initDev("ttyMT1", 115200, this);
                    //小卡
                    deviceControl1.Expand2PowerOn(7);
                    deviceControl1.Expand2PowerOn(5);
                    deviceControl1.Expand2PowerOn(6);
                    deviceControl1.Expand2PowerOff(6);
                    deviceControl1.Expand2PowerOn(6);
                    btnSK80Switch.setText("切换到大卡");
                    btnSK80Switch.setVisibility(View.VISIBLE);
                    btnPsam2.setVisibility(View.GONE);
                    break;

                default:
                    psamIntance.initDev(this);//初始化设备
                    psamIntance.resetDev();//复位
                    break;

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Reset PSAM");
        progressDialog.setMessage("Reset……");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                progressDialog.cancel();
            }
        }).start();
    }

    private void showConfig() {

        String verson = getVersion();
        tv.setText("V" + verson);

        boolean isExit = ConfigUtils.isConfigFileExists();
        if (isExit) {
            tv.setText(getResources().getString(R.string.psam_custom_config));
        } else {
            tv.setText(getResources().getString(R.string.psam_standard_config));
        }
        ReadBean.PasmBean pasm = ConfigUtils.readConfig(this).getPasm();
        String gpio = "";
        List<Integer> gpio1 = pasm.getGpio();
        for (Integer s : gpio1) {
            gpio += s + ",";
        }
        tv.append(getResources().getString(R.string.psam_append1) + pasm.getSerialPort() + getResources().getString(R.string.psam_append2) + pasm.getBraut() + getResources().getString(R.string.psam_append3) +
                pasm.getPowerType() + getResources().getString(R.string.psam_append4) + pasm.getGpio() + getResources().getString(R.string.psam_append5) + pasm.getResetGpio());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (App.getModel().equals("SK80H") && App.getModel().equals("SK80")) {

                deviceControl1.ExpandPowerOff(4);
                deviceControl1.MainPowerOff(85);
                deviceControl1.Expand2PowerOff(5);
                deviceControl1.ExpandPowerOff(12);
                deviceControl1.Expand2PowerOff(6);
                deviceControl1.MainPowerOff(12);
            }
            psamIntance.releaseDev();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tv = (TextView) findViewById(R.id.tv);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
        btnPsam1 = (Button) findViewById(R.id.btn_psam1);
        btnPsam1.setOnClickListener(this);
        btnPsam2 = (Button) findViewById(R.id.btn_psam2);
        btnPsam2.setOnClickListener(this);
        btnPass = (Button) findViewById(R.id.btn_pass);
        btnPass.setOnClickListener(this);
        btnNotPass = (Button) findViewById(R.id.btn_not_pass);
        btnNotPass.setOnClickListener(this);
        btnSK80Switch = (Button) findViewById(R.id.btn_sk80_switch);
        btnSK80Switch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_psam1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final byte[] data = psamIntance.PsamPower(IPsam.PowerType.Psam1);
                        Log.i("psam1", "run: " + DataConversionUtils.byteArrayToString(data));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data != null && DataConversionUtils.byteArrayToString(data).contains("3b")) {
                                    tvInfor.append(getResources().getString(R.string.psam1_succeed));
                                } else {
                                    tvInfor.append(getResources().getString(R.string.psam1_no));
                                }
                            }
                        });
                    }
                }).start();

                break;
            case R.id.btn_psam2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final byte[] data = psamIntance.PsamPower(IPsam.PowerType.Psam2);
                        Log.i("psam2", "run: " + DataConversionUtils.byteArrayToString(data));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data != null && DataConversionUtils.byteArrayToString(data).contains("3b")) {
                                    tvInfor.append(getResources().getString(R.string.psam2_succeed));
                                } else {
                                    tvInfor.append(getResources().getString(R.string.psam2_no));
                                }
                            }
                        });
                    }
                }).start();
                break;
            case R.id.btn_sk80_switch:
                if (isSwitch) {
                    isSwitch = false;
                    btnSK80Switch.setText("切换到小卡");
                    try {
                        deviceControl1.Expand2PowerOff(5);
                        deviceControl1.Expand2PowerOff(6);
                        deviceControl1.MainPowerOff(85);
                        psamIntance.releaseDev();
                        deviceControl1.Expand2PowerOff(7);
                        deviceControl1.MainPowerOn(85);
                        psamIntance.initDev("ttyMT1", 115200, this);

                        deviceControl1.ExpandPowerOn(4);
                        deviceControl1.ExpandPowerOn(12);
                        deviceControl1.ExpandPowerOff(12);
                        deviceControl1.ExpandPowerOn(12);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    isSwitch = true;
                    btnSK80Switch.setText("切换到大卡");
                    try {
                        deviceControl1.MainPowerOff(85);
                        deviceControl1.ExpandPowerOff(4);
                        deviceControl1.ExpandPowerOff(12);
                        psamIntance.releaseDev();
                        deviceControl1.Expand2PowerOn(7);
                        deviceControl1.MainPowerOn(85);
                        psamIntance.initDev("ttyMT1", 115200, this);

                        deviceControl1.Expand2PowerOn(5);
                        deviceControl1.Expand2PowerOn(6);
                        deviceControl1.Expand2PowerOff(6);
                        deviceControl1.Expand2PowerOn(6);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.btn_pass:
                setXml(App.KEY_PSAM, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_PSAM, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
