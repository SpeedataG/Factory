package com.spdata.factory;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

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
    Button btn_scan;
    @ViewById
    ListView listView1;
    private Handler handler = new Handler();
    /* 取得默认的蓝牙适配器 */
    private BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    /* 用来存储搜索到的蓝牙设备 */
    private List<BluetoothDevice> myBluetoothDevice = new ArrayList<BluetoothDevice>();
    /* 是否完成搜索 */
    private volatile boolean discoveryFinished;

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
//        /* 注册接收器 */
//        IntentFilter discoveryFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        registerReceiver(discoveryReceiver, discoveryFilter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter discoveryFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryReceiver, discoveryFilter);
        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(foundReceiver, foundFilter);

        if (myBluetoothAdapter.isEnabled()) {
            /* 开始搜索 */
            myBluetoothAdapter.startDiscovery();
        } else {
            myBluetoothAdapter.enable();
            SystemClock.sleep(500);
            myBluetoothAdapter.startDiscovery();
        }

        if (!myBluetoothAdapter.isEnabled()) {
            finish();
            return;
        }

    }

    /**
     * 接收器
     * 当搜索蓝牙设备完成时调用
     */
    private BroadcastReceiver foundReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            /* 从intent中取得搜索结果数据 */
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            /* 将结果添加到列表中 */
            myBluetoothDevice.add(device);
            /* 显示列表 */
            showDevices();
        }
    };
    private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            /* 卸载注册的接收器 */
            unregisterReceiver(foundReceiver);
            unregisterReceiver(this);
            discoveryFinished = true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        myBluetoothAdapter.disable();
    }

    /* 显示列表 */
    protected void showDevices() {
        List<String> list = new ArrayList<String>();
        for (int i = 0, size = myBluetoothDevice.size(); i < size; ++i) {
            StringBuilder b = new StringBuilder();
            BluetoothDevice d = myBluetoothDevice.get(i);
            b.append(d.getAddress());
            b.append('\n');
            b.append(d.getName());
            String s = b.toString();
            list.add(s);

        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
//        handler.post(new Runnable() {
//                    public void run() {
        listView1.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (listView1.getCount() > 0) {
            setXml(App.KEY_BLUETOOTH, App.KEY_FINISH);
            finish();
        } else {
            setXml(App.KEY_BLUETOOTH, App.KEY_UNFINISH);
            finish();
        }
//                    }
//                });
    }
}
