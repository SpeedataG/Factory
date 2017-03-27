package com.spdata.factory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.south.SDKMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import common.base.act.FragActBase;
import common.event.ViewMessage;

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
    @ViewById
    TextView tv_stop_scan;
    @ViewById
    TextView tvconnect_result;

    @Click
    void btn_scan() {
        String str = btn_scan.getText().toString();
        if (str.contains("OFF")) {
            if (mBluetoothAdapter.isEnabled()) {
                // 开始扫描
                mBluetoothAdapter.startDiscovery();
                btn_scan.setText(getResources().getString(
                        R.string.bluetooth_scan)
                        + ":ON");
                mBluetoothDeviceList.clear();
                mArrayAdapter.clear();
                mArrayAdapter.notifyDataSetChanged();
                listView1.setAdapter(mArrayAdapter);
                ShowConnectResult("");
                scan_timer.start();

            }
        } else {
            // 关闭扫描
            mBluetoothAdapter.cancelDiscovery();
            btn_scan.setText(getResources().getString(
                    R.string.bluetooth_scan)
                    + ":OFF");
            scan_timer.cancel();
            tv_stop_scan.setText(getResources().getString(
                    R.string.bluetooth_scan_stop));
        }
    }

    //    @Click
//        void btnPass() {
//        setXml(App.KEY_BLUETOOTH, App.KEY_FINISH);
//        finish();
//         }
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

    BluetoothAdapter mBluetoothAdapter;
    ArrayAdapter<String> mArrayAdapter;
    List<String> lstDevices = new ArrayList<String>();
    CountDownTimer scan_timer;


    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        btn_scan.setEnabled(false);
        // 获取蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lstDevices);
        if (mBluetoothAdapter == null) {
            // 退出程序
            setXml(App.KEY_BLUETOOTH, App.KEY_UNFINISH);
            showToast("无蓝牙设备！");
            finish();
        }
        // 判断蓝牙是否已经被打开
        if (mBluetoothAdapter.isEnabled()) {
            // 打开
            btn_scan.setEnabled(true);
        } else {
//            Intent enableBtIntent = new Intent(
//            BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothAdapter.enable();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        titlebar.setAttrs("蓝牙开启中……");
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mBluetoothAdapter.isEnabled()) {
                                    btn_scan.setEnabled(true);
                                    titlebar.setAttrs("蓝牙已打开，请点击扫描");
                                } else {
                                    tvconnect_result.setText("蓝牙未打开");
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    hideLoading();
                }
            }).start();
        }

        scan_timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 剩余时间
                tv_stop_scan.setText(getResources().getString(
                        R.string.bluetooth_left_scan_time)
                        + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                // 判断蓝牙是否已经被打开
                if (mBluetoothAdapter.isEnabled()) {
                    btn_scan.setEnabled(true);
                    // 关闭扫描
                    mBluetoothAdapter.cancelDiscovery();
                    btn_scan.setText(getResources().getString(
                            R.string.bluetooth_scan)
                            + ":OFF");
                    btn_scan.setText(getResources().getString(
                            R.string.bluetooth_scan_stop));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (lstDevices == null) {
                                        setXml(App.KEY_BLUETOOTH, App.KEY_UNFINISH);
                                        finish();
                                    }else {
                                        setXml(App.KEY_BLUETOOTH, App.KEY_FINISH);
                                        finish();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        };
        listView1.setOnItemClickListener(new ItemClickEvent());
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String str = "";
                    if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                        str = getResources().getString(
                                R.string.bluetooth_state_no_pair)
                                + "|"
                                + device.getName()
                                + "|"
                                + device.getAddress();
                    } else if (device.getBondState() == BluetoothDevice.BOND_BONDING) {
                        str = getResources().getString(
                                R.string.bluetooth_state_pairing)
                                + "|"
                                + device.getName()
                                + "|"
                                + device.getAddress();
                    } else if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                        str = getResources().getString(
                                R.string.bluetooth_state_pair)
                                + "|"
                                + device.getName()
                                + "|"
                                + device.getAddress();
                    }
                    int position = lstDevices.indexOf(str);
                    if (position == -1)// 防止重复添加
                    {
                        lstDevices.add(str); // 获取设备名称和mac地址
                    } else {
                        lstDevices.remove(position);
                        lstDevices.add(str);
                    }
                    mArrayAdapter.notifyDataSetChanged();

                } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED
                        .equals(action)) {
                    BluetoothDevice device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_BONDING:
                            Log.d("BlueToothTestActivity", "正在配对......");
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            Log.d("BlueToothTestActivity", "完成配对");
                            System.out.println("BlueToothTestActivity-start");
                            connect(device);
                            System.out.println("BlueToothTestActivity--end");
                            break;
                        case BluetoothDevice.BOND_NONE:
                            Log.d("BlueToothTestActivity", "取消配对");
                        default:
                            break;
                    }
                }

            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter); // Don't forget to
    }

    BroadcastReceiver mReceiver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // android.os.Process.killProcess(android.os.Process.myPid());
        mBluetoothAdapter.disable();
        unregisterReceiver(mReceiver);
    }

    private List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<BluetoothDevice>();

    private static final int REQUEST_ENABLE_BT = 2;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    // 获取蓝牙列表
                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                            .getBondedDevices();
                    mBluetoothDeviceList.clear();
                    mArrayAdapter.clear();
                    mArrayAdapter.notifyDataSetChanged();
                    if (pairedDevices.size() > 0) {
                        ShowConnectResult(getResources().getString(
                                R.string.bluetooth_state_pair)
                                + "：" + pairedDevices.size());
                        for (BluetoothDevice device : pairedDevices) {
                            mArrayAdapter.add(getResources().getString(
                                    R.string.bluetooth_state_pair)
                                    + "|"
                                    + device.getName()
                                    + "|"
                                    + device.getAddress());
                        }
                        listView1.setAdapter(mArrayAdapter);
                    }
                } else {
                    finish();
                }
        }
    }

    ProgressDialog mProgressDialog;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == 1) {
                ShowConnectResult((String) msg.obj
                        + getResources().getString(
                        R.string.bluetooth_state_pair));
            } else if (msg.what == 0) {

                ShowConnectResult((String) msg.obj
                        + getResources().getString(
                        R.string.bluetooth_state_pair));
            }
            if (mProgressDialog != null) {
                mProgressDialog.cancel();
            }
            super.handleMessage(msg);
        }

    };

    private void connect(final BluetoothDevice btDev) {
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        // showRemind(getResources()
        // .getString(R.string.bluetooth_state_connecting));
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message msg = new Message();
                try {
                    UUID uuid = UUID.fromString(SPP_UUID);
                    BluetoothSocket btSocket = btDev
                            .createRfcommSocketToServiceRecord(uuid);
                    Log.d("BlueToothTestActivity", "开始连接...");
                    // mBluetoothAdapter.cancelDiscovery();
                    btSocket.connect();
                    // SystemClock.sleep(1000);
                    String message = "link success ff";
                    OutputStream mmOutStream = btSocket.getOutputStream();
                    mmOutStream.write(message.getBytes());
                    msg.what = 1;

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    msg.what = 0;
                }
                msg.obj = btDev.getName();
                handler.sendMessage(msg);
            }
        }).start();

    }

    private void showRemind(String showdata) {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();// = new ProgressDialog(this);
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(showdata);
        mProgressDialog.show();
        System.out.println("mProgressDialog.show");
    }

    private void ShowConnectResult(String data) {
        tvconnect_result.setText(data);
    }

    class ItemClickEvent implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            if (mBluetoothAdapter.isDiscovering())
                mBluetoothAdapter.cancelDiscovery();
            String str = mArrayAdapter.getItem(arg2);
            String[] values = str.split("\\|");
            String address = values[2];
            Log.e("address", values[2]);
            BluetoothDevice btDev = mBluetoothAdapter.getRemoteDevice(address);
            try {
                Boolean returnValue = false;
                if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
                    // 利用反射方法调用BluetoothDevice.createBond(BluetoothDevice
                    // remoteDevice);
                    Method createBondMethod = BluetoothDevice.class
                            .getMethod("createBond");
                    Log.d("BlueToothTestActivity", "开始配对");
                    showRemind(getResources().getString(
                            R.string.bluetooth_state_pairing));
                    returnValue = (Boolean) createBondMethod.invoke(btDev);

                } else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
                    connect(btDev);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
