package com.spdata.factory;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.serialport.DeviceControl;
import android.serialport.SerialPort;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import common.base.act.FragActBase;
import common.event.ViewMessage;

/**
 * Created by lenovo-pc on 2018/3/8.
 */
@EActivity(R.layout.act_id2)
public class Id2TestAct extends FragActBase {

    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvVersionInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    ToggleButton btnStart;

    @Click
    void btnNotPass() {
        setXml(App.KEY_ID2, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_ID2, App.KEY_FINISH);
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
        }, "ID2", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    private IID2Service iid2Service;
    private ProgressDialog progressDialog;

    @AfterViews
    protected void main() {
        onWindowFocusChanged(true);
        initTitlebar();
        setSwipeEnable(false);
        btnStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                iid2Service.getIDInfor(false, isChecked);
                if (!isChecked) {
                    tvVersionInfor.setText("ID2模块初始化成功\n请将身份证靠近读卡器");
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
                tvVersionInfor.setText("姓名：" + idInfor.getName() + "性别：" + idInfor.getSex() + "\n出生年月：" + idInfor.getYear() + "-" + idInfor.getMonth() + "-" + idInfor.getDay()
                        + "\n住址：" + idInfor.getAddress() + "\n身份证号：" + idInfor.getNum());
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
        progressDialog.setMessage("正在初始化");
        progressDialog.setCancelable(false);
        progressDialog.show();
//        showDialog(dia);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean result = false;
                    if (Build.MODEL.equals("DM-P80")) {
                        result = iid2Service.initDev(Id2TestAct.this, new IDReadCallBack() {
                            @Override
                            public void callBack(IDInfor infor) {
                                Message message = new Message();
                                message.obj = infor;
                                handler.sendMessage(message);
                            }
                        }, SerialPort.SERIAL_TTYMT2, 115200, DeviceControl.PowerType.MAIN, 94);
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
                                showToast("二代证模块初始化失败");
                                setXml(App.KEY_ID2, App.KEY_UNFINISH);
                                finish();
                            } else {
                                tvVersionInfor.setText("ID2模块初始化成功\n请将身份证靠近读卡器");
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
            progressDialog.setMessage("正在初始化");
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
            if (iid2Service != null)
                iid2Service.releaseDev();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }
}
