package com.spdata.factory;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * Created by suntianwei on 2017/4/5.
 */
public class BluetoothAct extends FragActBase implements View.OnClickListener {
    CustomTitlebar titlebar;
    Button btn_not_pass;
    Button btn_pass;
    Button btn_scan;
    /* 取得默认的蓝牙适配器 */
    private BluetoothAdapter myBluetoothAdapter = null;
    private StringBuffer sb = new StringBuffer();
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
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_bluetooth));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
        //intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, intentFilter);

        if (!myBluetoothAdapter.isEnabled()) {
            myBluetoothAdapter.enable();
        }

        if (myBluetoothAdapter.getState() != BluetoothAdapter.STATE_ON) {
            myBluetoothAdapter.enable();
        }
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 1000);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
            if (!myBluetoothAdapter.isDiscovering()) {
                myBluetoothAdapter.startDiscovery();
            }
        }
    };
    int num = 0;
    // 搜索周围的蓝牙设备
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //找到设备
            if (action.equals(BluetoothDevice.ACTION_FOUND) ||
                    action.equals(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                num++;
                Log.i("Bluetoothtest000", "num=" + num);

                //执行更新列表的代码
                if (device != null) {
                    sb = sb.append("【" + device.getName() + "】 " + "\n").append(device.getAddress() + "\n");
                }
                tvInfor.setText(getResources().getString(R.string.bluetooth_state) + "\n" + sb.toString());
            }
            //搜索完成
            else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                if (num == 0) {
                    Toast.makeText(context, getResources().getString(R.string.bluetooth_toast), Toast.LENGTH_SHORT).show();
                    setXml(App.KEY_BLUETOOTH, App.KEY_UNFINISH);
                    finish();
                } else {
                    Toast.makeText(context, getResources().getString(R.string.bluetooth_toast2), Toast.LENGTH_SHORT).show();
                    setXml(App.KEY_BLUETOOTH, App.KEY_FINISH);
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        myBluetoothAdapter.disable();
        unregisterReceiver(mReceiver);
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
                setXml(App.KEY_BLUETOOTH, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_BLUETOOTH, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
