package com.spdata.factory;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.serialport.DeviceControlSpd;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;
import com.speedata.libutils.ConfigUtils;
import com.speedata.libutils.ReadBean;

import java.io.IOException;
import java.util.List;

import common.base.act.FragActBase;
import rego.printlib.export.regoPrinter;

public class PrintTestAct extends FragActBase implements View.OnClickListener {
    private CustomTitlebar mTitlebar;
    private TextView mTextShow;
    private Button mBtnStartPrint;
    private Button mBtnPass;
    private Button mBtnNotPass;

    @Override
    protected void initTitlebar() {
        mTitlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        mTitlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_print), null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_layout);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        try {
            printer = new regoPrinter(this);
            deviceControl = new DeviceControlSpd(DeviceControlSpd.PowerType.MAIN_AND_EXPAND, 85, 0);
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mTitlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        mTextShow = (TextView) findViewById(R.id.text_show);
        mBtnStartPrint = (Button) findViewById(R.id.btn_start_print);
        mBtnStartPrint.setOnClickListener(this);
        mBtnPass = (Button) findViewById(R.id.btn_pass);
        mBtnPass.setOnClickListener(this);
        mBtnNotPass = (Button) findViewById(R.id.btn_not_pass);
        mBtnNotPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_start_print:
                byte[] printdata = {0x1d, 0x28, 0x41, 0x00, 0x00, 0x00, 0x02};
                printer.ASCII_PrintBuffer(connectState, printdata, printdata.length);
                break;
            case R.id.btn_pass:
                setXml(App.KEY_PRINT, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_PRINT, App.KEY_UNFINISH);
                finish();
                break;
        }
    }

    public TextView version;
    public boolean mBconnect = false;
    private regoPrinter printer;
    private int connectState = 0;
    private ReadBean mRead;
    private DeviceControlSpd deviceControl;

    public void connect() {
        if (App.getModel().contains("SK80")) {
            try {
                connectState = printer.CON_ConnectDevices("RG-E487",
                        "/dev/ttyMT3" + ":" + "115200" + ":1:1", 200);
                deviceControl.PowerOnDevice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 读取配置文件
            modelJudgmen();
        }

        if (mBconnect) {
            printer.CON_CloseDevices(connectState);
//            con.setText(R.string.button_btcon);// "连接"
            mBconnect = false;
        } else {

            System.out.println("----RG---CON_ConnectDevices");

            if (connectState > 0) {
                Toast.makeText(PrintTestAct.this, R.string.mes_consuccess,
                        Toast.LENGTH_SHORT).show();
                mTextShow.setText(R.string.mes_consuccess);
                mBconnect = true;
                // TODO: 2019/3/28  daying
            } else {
                Toast.makeText(PrintTestAct.this, R.string.mes_confail,
                        Toast.LENGTH_SHORT).show();
                mTextShow.setText(R.string.mes_confail);
                mBconnect = false;
            }
        }
    }


    private void modelJudgmen() {
        boolean configFileExists = ConfigUtils.isConfigFileExists();
        mRead = ConfigUtils.readConfig(this);
        ReadBean.PrintBean print = mRead.getPrint();
        String powerType = print.getPowerType();
        int braut = print.getBraut();
        List<Integer> gpio = print.getGpio();
        String serialPort = print.getSerialPort();
        connectState = printer.CON_ConnectDevices("RG-E487",
                serialPort + ":" + braut + ":1:1", 200);
        int[] intArray = new int[gpio.size()];
        String intArrayStr = "";
        for (int i = 0; i < gpio.size(); i++) {
            intArray[i] = gpio.get(i);
            intArrayStr = intArrayStr + intArray[i] + " ";
        }
        try {
            deviceControl.PowerOnDevice();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        SharedXmlUtil.getInstance(this).write("PrintConfig",
//                "配置文件：" + configFileExists + ";" + serialPort + ";" + braut + ";" + intArrayStr);
    }

    // 程序退出时需要关闭电源 省电
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            deviceControl.PowerOffDevice();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
