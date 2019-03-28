package com.spdata.factory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.serialport.DeviceControlSpd;
import android.serialport.SerialPortSpd;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;
import com.spdata.factory.wifibrobe.MainActAdapter;
import com.spdata.factory.wifibrobe.WifiBean;
import com.speedata.libutils.DataConversionUtils;
import com.speedata.utils.DataCleanUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import common.base.act.FragActBase;

public class WifiProbeTestAct extends FragActBase implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    private CustomTitlebar mTitlebar;
    private TextView mTextShow;
    private Button mBtnStartPrint;
    private Button mBtnPass;
    private Button mBtnNotPass;
    private RecyclerView mRvContent;
    private MainActAdapter mAdapter;
    private List<WifiBean> datas;
    private SerialPortSpd serialPort;
    private ReadThread readThread;
    private int fd;
    private DeviceControlSpd deviceControlSpd;

    @Override
    protected void initTitlebar() {
        mTitlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        mTitlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_wifi_tanzhen), null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wifi_brobe_layout);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        initRV();
        try {
            deviceControlSpd = new DeviceControlSpd(DeviceControlSpd.PowerType.MAIN_AND_EXPAND2, 85, 7);
            deviceControlSpd.PowerOnDevice();
            serialPort = new SerialPortSpd();
            serialPort.OpenSerial("/dev/ttyMT2", 115200);
            fd = serialPort.getFd();
//            Toast.makeText(this, "初始化成功", Toast.LENGTH_SHORT).show();
            readThread = new ReadThread();
            readThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void initRV() {
        datas = new ArrayList<>();
        mAdapter = new MainActAdapter(R.layout.wifi_brobe_list_title, datas);
        mRvContent.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layoutManager.setReverseLayout(true);//列表翻转
        mRvContent.setLayoutManager(layoutManager);
        mRvContent.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void initView() {
        mTitlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        mTextShow = (TextView) findViewById(R.id.text_show);
        mBtnPass = (Button) findViewById(R.id.btn_pass);
        mBtnPass.setOnClickListener(this);
        mBtnNotPass = (Button) findViewById(R.id.btn_not_pass);
        mBtnNotPass.setOnClickListener(this);
        mRvContent = findViewById(R.id.rv_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_pass:
                setXml(App.KEY_WIFI_PROBE, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_WIFI_PROBE, App.KEY_UNFINISH);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!interrupted()) {
                try {
                    if (fd == -1) {
                        readThread.interrupt();
                        return;
                    }
                    byte[] bytes = serialPort.ReadSerial(fd, 512);
                    if (bytes != null) {
                        String factory = DataConversionUtils.byteArrayToAscii(bytes);
                        Log.i("stw", "run: " + factory);
                        String[] split = factory.split("\\|");
                        if (stringIsMac(split[0]) && stringIsMac(split[1]) && stringIsMac(split[2])) {
                            WifiBean wifiBean = new WifiBean(split[0], split[1], split[2], split[3], split[4], split[5], split[6], split[7], split[8]);
                            datas.add(wifiBean);
                            handler.sendMessage(handler.obtainMessage());
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean stringIsMac(String val) {
        String trueMacAddress = "([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";
        if (val.matches(trueMacAddress)) {
            return true;
        } else {
            return false;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        close();
    }

    private void close() {
        try {
            if (deviceControlSpd != null) {
                deviceControlSpd.PowerOffDevice();
            }
            if (serialPort != null) {
                serialPort.CloseSerial(fd);
            }
            if (readThread != null) {
                readThread.interrupt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
