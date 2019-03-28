package com.spdata.factory;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.serialport.DeviceControlSpd;
import android.serialport.SerialPortSpd;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;
import com.speedata.libid2.IDInfor;
import com.speedata.libid2.IDManager;
import com.speedata.libid2.IDReadCallBack;
import com.speedata.libid2.IID2Service;

import java.io.IOException;

import javax.mail.UIDFolder;

import common.base.act.FragActBase;

/**
 * Created by lenovo-pc on 2018/3/8.
 */
public class Id2TestAct extends FragActBase implements View.OnClickListener {

    private CustomTitlebar titlebar;
    private TextView tvVersionInfor;
    private ToggleButton btnStart;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;
    private DeviceControlSpd deviceControlSpd;


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_id2), null);
    }


    private IID2Service iid2Service;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_id2);
        initView();
        onWindowFocusChanged(true);
        initTitlebar();
        setSwipeEnable(false);
        btnStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                iid2Service.getIDInfor(false, isChecked);
                if (!isChecked) {
                    tvVersionInfor.setText(getResources().getString(R.string.ID2_init_ok));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initIDService();
    }

    @Override
    protected void onPause() {
        iid2Service.getIDInfor(false, false);
        btnStart.setChecked(false);
        super.onPause();
    }

    private IDInfor idInfor;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            iid2Service.getIDInfor(false, btnStart.isChecked());
            idInfor = (IDInfor) msg.obj;
            if (idInfor.isSuccess()) {
                tvVersionInfor.setText(getResources().getString(R.string.ID2_idInfor1) + idInfor.getName() + getResources().getString(R.string.ID2_idInfor2) + idInfor.getSex() + getResources().getString(R.string.ID2_idInfor3) + idInfor.getYear() + "-" + idInfor.getMonth() + "-" + idInfor.getDay()
                        + getResources().getString(R.string.ID2_idInfor4) + idInfor.getAddress() + getResources().getString(R.string.ID2_idInfor5) + idInfor.getNum());
//                iid2Service.getIDInfor(false, false);
            } else {
//                tvVersionInfor.setText("读取信息失败");
            }

        }
    };

    /*
     * 初始化二代证模块   失败退出
     */
    public void initIDService() {
        iid2Service = IDManager.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.ID2_init_ing));
        progressDialog.setCancelable(false);
        progressDialog.show();
//        showDialog(dia);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean result = false;
                    if (App.getModel().equals("DM-P80")) {
                        result = iid2Service.initDev(Id2TestAct.this, new IDReadCallBack() {
                            @Override
                            public void callBack(IDInfor infor) {
                                Message message = new Message();
                                message.obj = infor;
                                handler.sendMessage(message);
                            }
                        }, SerialPortSpd.SERIAL_TTYMT2, 115200, DeviceControlSpd.PowerType.MAIN, 94);
                    } else if (App.getModel().equals("SK80") || App.getModel().equals("SK80H")) {
                        result = iid2Service.initDev(Id2TestAct.this, new IDReadCallBack() {
                            @Override
                            public void callBack(IDInfor infor) {
                                Message message = new Message();
                                message.obj = infor;
                                handler.sendMessage(message);
                            }
                        }, SerialPortSpd.SERIAL_TTYMT0, 115200, DeviceControlSpd.PowerType.MAIN_AND_EXPAND, 85, 3);


                    } else {
                        result = iid2Service.initDev(Id2TestAct.this, new
                                IDReadCallBack() {
                                    @Override
                                    public void callBack(IDInfor infor) {
                                        Message message = new Message();
                                        message.obj = infor;
                                        handler.sendMessage(message);
                                    }
                                });
                    }

                    final boolean finalResult = result;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            dismissDialog(dia);
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                progressDialog.cancel();
                            }
                            if (!finalResult) {
                                showToast(getResources().getString(R.string.ID2_init_fail));
                                setXml(App.KEY_ID2, App.KEY_UNFINISH);
                                finish();
                            } else {
                                tvVersionInfor.setText(getResources().getString(R.string.ID2_init_ok));
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getResources().getString(R.string.ID2_init_ing));
            progressDialog.setCancelable(false);
        }
        return progressDialog;
    }

    @Override
    protected void onStop() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog.cancel();
        }
        try {
            if (iid2Service != null) {
                iid2Service.releaseDev();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvVersionInfor = (TextView) findViewById(R.id.tv_version_infor);
        btnStart = (ToggleButton) findViewById(R.id.btn_start);
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
                setXml(App.KEY_ID2, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_ID2, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
