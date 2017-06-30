package com.spdata.factory;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import common.base.act.FragActBase;
import common.event.ViewMessage;

/**
 * Created by suntianwei on 2017/4/5.
 */
@EActivity(R.layout.activity_bluetooth)
public class BluetoothAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btn_not_pass;
    @ViewById
    Button btn_pass;
    @ViewById
    Button btn_scan;
    @ViewById
    TextView tv_infor;
    /* 取得默认的蓝牙适配器 */
    private BluetoothAdapter myBluetoothAdapter = null;
    private StringBuffer sb = new StringBuffer();

    @Click
    void btnPass() {
        setXml(App.KEY_BLUETOOTH, App.KEY_FINISH);
        finish();
    }

    @Click
    void btnNotPass() {
        setXml(App.KEY_BLUETOOTH, App.KEY_UNFINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("蓝牙测试");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    @AfterViews
    protected void main() {

        initTitlebar();
        setSwipeEnable(false);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
// 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
//		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, intentFilter);

        if (!myBluetoothAdapter.isEnabled())
            myBluetoothAdapter.enable();

        if (myBluetoothAdapter.getState() != myBluetoothAdapter.STATE_ON)
            myBluetoothAdapter.enable();
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
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //找到设备
            if (action.equals(BluetoothDevice.ACTION_FOUND) ||
                    action.equals(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                num++;
                Log.i("Bluetoothtest000", "num=" + num);

                //执行更新列表的代码
                if (device != null)
                    sb = sb.append("【" + device.getName() + "】 " + "\n").append(device.getAddress() + "\n");
                tv_infor.setText("可用设备:" + "\n" + sb.toString());
            }
            //搜索完成
            else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                if (num == 0) {
                    Toast.makeText(context, "搜索完成,未搜索到任何设备", Toast.LENGTH_SHORT).show();
                    setXml(App.KEY_BLUETOOTH, App.KEY_UNFINISH);
                    finish();
                } else {
                    Toast.makeText(context, "搜索完成", Toast.LENGTH_SHORT).show();
                    setXml(App.KEY_BLUETOOTH, App.KEY_FINISH);
                    finish();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myBluetoothAdapter.disable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        myBluetoothAdapter.disable();
    }

}
