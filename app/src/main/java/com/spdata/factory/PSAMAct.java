package com.spdata.factory;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.serialport.DeviceControl;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;
import com.speedata.libutils.ConfigUtils;
import com.speedata.libutils.ReadBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import speedatacom.a3310libs.PsamManager;
import speedatacom.a3310libs.inf.IPsam;

import static jxl.Workbook.getVersion;

@EActivity(R.layout.activity_psam)
public class PSAMAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    TextView tv;
    @ViewById
    TextView btn_psam1;
    @ViewById
    TextView btn_psam2;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_PSAM, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_PSAM, App.KEY_FINISH);
        finish();
    }

    @Click
    void btn_psam1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final byte[] data = psamIntance.PsamPower(IPsam.PowerType.Psam1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (data == null) {
                            tvInfor.append("No Psam1\n");
                        } else {
                            tvInfor.append("Psam1 Succeed \n");
                        }
                    }
                });
            }
        }).start();


    }

    @Click
    void btn_psam2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final byte[] data = psamIntance.PsamPower(IPsam.PowerType.Psam2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (data == null) {
                            tvInfor.append("No Psam2\n");
                        } else {
                            tvInfor.append("Psam2 Succeed \n");
                        }
                    }
                });
            }
        }).start();
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
        }, "PSAM测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    //获取psam实例
    IPsam psamIntance = PsamManager.getPsamIntance();
    DeviceControl deviceControl1;

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        showConfig();
        try {
//            psamIntance.initDev(this);//初始化设备
//            psamIntance.resetDev();//复位
            switch (Build.MODEL) {
                case "SD55":
                    psamIntance.initDev("ttyMT1", 115200, this);
                    deviceControl1 = new DeviceControl(DeviceControl.PowerType.NEW_MAIN, 16, 46);
                    deviceControl1.PowerOnDevice();
                    psamIntance.resetDev(DeviceControl.PowerType.NEW_MAIN, 23);
                    break;

                default:
//                    psamIntance.resetDev();//复位
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
            tv.setText("定制配置：\n");
        } else {
            tv.setText("标准配置：\n");
        }
        ReadBean.PasmBean pasm = ConfigUtils.readConfig(this).getPasm();
        String gpio = "";
        List<Integer> gpio1 = pasm.getGpio();
        for (Integer s : gpio1) {
            gpio += s + ",";
        }
        tv.append("串口:ttyMT1" + "  波特率：" + pasm.getBraut() + " 上电类型:NEW_MAIN" +
                " GPIO:16,46" + " resetGpio:23");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            psamIntance.releaseDev();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
