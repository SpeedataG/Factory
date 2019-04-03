package com.spdata.factory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.util.ArrayList;
import java.util.List;

import common.adapter.WifiRelayListAdapter;
import common.base.act.FragActBase;
import common.utils.LinkWifi;

public class WifiAct extends FragActBase implements View.OnClickListener {

    ListView listItem;
    private final int LATE = 0;

    private Thread thread;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            openWifi();
            showWifiList();
            handler.postDelayed(this, 1000);
            Log.e("zzc:", "==runnable()==run()==");
        }
    };
    private CustomTitlebar titlebar;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wifi);
        initView();
        initTitlebar();
        init();
    }

    @Override
    protected void initTitlebar() {
        setSwipeEnable(false);
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_wifi), null);
    }


    /**
     * 强制帮用户打开GPS
     */
    public void openGPS() {
        //获取GPS现在的状态（打开或是关闭状态）
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER);

        if (gpsEnabled) {
            //关闭GPS
            Settings.Secure.setLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER, false);
        } else {
            //打开GPS  www.2cto.com
            Settings.Secure.setLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER, true);

        }
    }

    private void closeGPS() {
        Settings.Secure.setLocationProviderEnabled(
                getContentResolver(), "gps", false);
    }

    /**
     * 打开WIFI
     */
    private void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            titlebar.setAttrs(getResources().getString(R.string.wifi_opening));
            regWifiReceiver();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            titlebar.setAttrs(getResources().getString(R.string.wifi_open));
                            if (wifiManager.isWifiEnabled()) {
                                scanAndGetResult();
                            }
                            wifiListAdapter.notifyDataSetChanged();
                            Log.e("zzc:", "==wifiListAdapter.notifyDataSetChanged();==");

                        }
                    });
                }
            });
            thread.start();
        }
        scanAndGetResult();
    }


    private WifiManager wifiManager = null;
    private Context context = null;
    public SetWifiHandler setWifiHandler;
    private WifiRelayListAdapter wifiListAdapter;
    private ListView wifi_list;
    private ImageButton refresh_list_btn;
    private ImageButton wifi_on_off_btn;
    private LinkWifi linkWifi;
    ConnectivityManager connManager;
    NetworkInfo mWifi;

    private void init() {
        context = this;
        linkWifi = new LinkWifi(context);
        wifiManager = (WifiManager) context
                .getSystemService(Service.WIFI_SERVICE);
        setWifiHandler = new SetWifiHandler(Looper.getMainLooper());
        wifi_list = (ListView) findViewById(R.id.list_item);
        connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        if (mWifi.isConnected()) {
//            setXml(App.KEY_WIFI, App.KEY_FINISH);
//            showToast("wifi已连接！");
//            finish();
//        }
        if (wifiManager.isWifiEnabled()) {
            scanAndGetResult();
        } else {
            openWifi();
            Log.e("zzc:", "==openWifi();==");
            //showToast("正在打开WiFi");
            if (wifi_list == null) {
                scanAndGetResult();
            }
        }
    }

    private void regWifiReceiver() {
        System.out.println("注册一个当wifi热点列表发生变化时要求获得通知的消息");
        IntentFilter labelIntentFilter = new IntentFilter();
        labelIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        labelIntentFilter.addAction("android.net.wifi.STATE_CHANGE"); // ConnectivityManager.CONNECTIVITY_ACTION);
        labelIntentFilter.setPriority(1000); // 设置优先级，最高为1000

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(new NetworkConnectChangedReceiver(), filter);
    }


    private void scanAndGetResult() {
        // 开始扫描
        wifiManager.startScan();
        showWifiList();
    }

    List<ScanResult> wifiList;
    List<ScanResult> newWifList;

    private void showWifiList() {
        // 剔除ssid中的重复项，只保留相同ssid中信号最强的哪一个
        wifiList = wifiManager.getScanResults();
        newWifList = new ArrayList<ScanResult>();
        boolean isAdd = true;

        if (wifiList != null) {
            for (int i = 0; i < wifiList.size(); i++) {
                isAdd = true;
                for (int j = 0; j < newWifList.size(); j++) {
                    if (newWifList.get(j).SSID.equals(wifiList.get(i).SSID)) {
                        isAdd = false;
                        if (newWifList.get(j).level < wifiList.get(i).level) {
                            // ssid相同且新的信号更强
                            newWifList.remove(j);
                            newWifList.add(wifiList.get(i));
                            break;
                        }
                    }
                }
                if (isAdd) {
                    newWifList.add(wifiList.get(i));
                }
            }
        }

        wifiListAdapter = new WifiRelayListAdapter(context, newWifList,
                setWifiHandler);
        wifi_list.setAdapter(wifiListAdapter);
        wifiListAdapter.notifyDataSetChanged();

    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        listItem = (ListView) findViewById(R.id.list_item);
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
                setXml(App.KEY_WIFI, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_WIFI, App.KEY_UNFINISH);
                finish();
                break;
        }
    }

    public class SetWifiHandler extends Handler {
        public SetWifiHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// 请求操作某一无线网络
                    ScanResult wifiinfo = (ScanResult) msg.obj;
                    configWifiRelay(wifiinfo);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        openGPS();
        showWifiList();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        closeGPS();
    }

    private void configWifiRelay(final ScanResult wifiinfo) {

        System.out.println("SSID=" + wifiinfo.SSID);

        // 如果本机已经配置过的话
        if (linkWifi.IsExsits(wifiinfo.SSID) != null) {
            showToast(getResources().getString(R.string.wifi_toast));
            setXml(App.KEY_WIFI, App.KEY_FINISH);
            finish();
            final int netID = linkWifi.IsExsits(wifiinfo.SSID).networkId;
            String actionStr;
            // 如果目前连接了此网络
            if (wifiManager.getConnectionInfo().getNetworkId() == netID) {
                actionStr = getResources().getString(R.string.wifi_dialog_but2);
            } else {
                actionStr = getResources().getString(R.string.wifi_dialog_but);
            }
            System.out
                    .println("wifiManager.getConnectionInfo().getNetworkId()="
                            + wifiManager.getConnectionInfo().getNetworkId());

            new AlertDialog.Builder(context)
                    .setTitle(getResources().getString(R.string.wifi_dialog_title))
                    .setMessage(getResources().getString(R.string.wifi_dialog_msg))
                    .setPositiveButton(actionStr,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                    if (wifiManager.getConnectionInfo()
                                            .getNetworkId() == netID) {
                                        wifiManager.disconnect();
                                    } else {
                                        WifiConfiguration config = linkWifi
                                                .IsExsits(wifiinfo.SSID);
                                        if (config == null) {
                                            showToast(getResources().getString(R.string.wifi_toast2));
                                            setXml(App.KEY_WIFI, App.KEY_UNFINISH);
                                            finish();
                                        } else {
                                            linkWifi.setMaxPriority(config);
                                            linkWifi.ConnectToNetID(config.networkId);
                                            showToast(getResources().getString(R.string.wifi_toast));
                                            setXml(App.KEY_WIFI, App.KEY_FINISH);
                                            finish();
                                        }

                                    }
                                }
                            })
                    .setNeutralButton(getResources().getString(R.string.wifi_dialog_but3),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    wifiManager.removeNetwork(netID);
                                    return;
                                }
                            })
                    .setNegativeButton(getResources().getString(R.string.wifi_dialog_but4),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    return;
                                }
                            }).show();

            return;
        }

        String capabilities = "";

        if (wifiinfo.capabilities.contains("WPA2-PSK")) {
            // WPA-PSK加密
            capabilities = "psk2";
        } else if (wifiinfo.capabilities.contains("WPA-PSK")) {
            // WPA-PSK加密
            capabilities = "psk";
        } else if (wifiinfo.capabilities.contains("WPA-EAP")) {
            // WPA-EAP加密
            capabilities = "eap";
        } else if (wifiinfo.capabilities.contains("WEP")) {
            // WEP加密
            capabilities = "wep";
        } else {
            // 无密码
            capabilities = "";
        }

        if (!capabilities.equals("")) {
            // 有密码，提示输入密码进行连接
            // final String encryption = capabilities;
            LayoutInflater factory = LayoutInflater.from(context);
            final View inputPwdView = factory.inflate(R.layout.dialog_inputpwd,
                    null);
            new AlertDialog.Builder(context)
                    .setTitle(getResources().getString(R.string.wifi_dialog2_title))
                    .setMessage(getResources().getString(R.string.wifi_dialog2_msg) + wifiinfo.SSID)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(inputPwdView)
                    .setPositiveButton(getResources().getString(R.string.wifi_dialog_but5),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    EditText pwd = (EditText) inputPwdView
                                            .findViewById(R.id.etPassWord);
                                    String wifipwd = pwd.getText().toString();

                                    // 此处加入连接wifi代码
                                    int netID = linkWifi.CreateWifiInfo2(
                                            wifiinfo, wifipwd);
                                    if (netID == -1) {
                                        showToast(getResources().getString(R.string.wifi_toast2));
                                        setXml(App.KEY_WIFI, App.KEY_UNFINISH);
                                        finish();
                                    } else {
                                        linkWifi.ConnectToNetID(netID);
                                        showToast(getResources().getString(R.string.wifi_toast));
                                        setXml(App.KEY_WIFI, App.KEY_FINISH);
                                        finish();
                                    }
                                }
                            })
                    .setNegativeButton(getResources().getString(R.string.wifi_dialog_but4),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            }).setCancelable(false).show();
        } else {
            // 无密码
            new AlertDialog.Builder(context)
                    .setTitle(getResources().getString(R.string.wifi_dialog_title))
                    .setMessage(getResources().getString(R.string.wifi_dialog3_msg))
                    .setPositiveButton(getResources().getString(R.string.wifi_dialog_but5),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    // 此处加入连接wifi代码
                                    int netID = linkWifi.CreateWifiInfo2(
                                            wifiinfo, "");
                                    if (netID == -1) {
                                        showToast(getResources().getString(R.string.wifi_toast2));
                                        setXml(App.KEY_WIFI, App.KEY_UNFINISH);
                                        finish();
                                    } else {
                                        linkWifi.ConnectToNetID(netID);
                                        showToast(getResources().getString(R.string.wifi_toast));
                                        setXml(App.KEY_WIFI, App.KEY_FINISH);
                                        finish();
                                    }
                                }

                            })
                    .setNegativeButton(getResources().getString(R.string.wifi_dialog_but4),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    return;
                                }
                            }).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        if (thread != null) {
            thread.interrupt();
        }

//        wifiManager.setWifiEnabled(false);//关闭WiFi
    }

    public class NetworkConnectChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {//wifi打开与否
                int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                    System.out.println("系统关闭wifi");
                } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                    System.out.println("系统开启wifi");
//                    regWifiReceiver();
//                    wifiManager.startScan();
//                    Message message = new Message();
//                    message.what = LATE;
//                    handler.sendMessage(message);
                    handler.postDelayed(runnable, 1000);
                    Log.d("zzc:", "系统开启wifi");
                }
            } else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {//wifi连接上与否
                System.out.println("网络状态改变");
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                    System.out.println("wifi网络连接断开");
                } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    //获取当前wifi名称
                    System.out.println("连接到网络 " + wifiInfo.getSSID());
                    showToast(getResources().getString(R.string.wifi_toast3) + wifiInfo.getSSID() + "  WIFI");
                    setXml(App.KEY_WIFI, App.KEY_FINISH);
                    finish();
                }
            }
        }
    }
}
