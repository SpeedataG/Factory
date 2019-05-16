package com.spdata.factory;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.spdata.factory.application.App;
import com.spdata.factory.bean.ListItem;
import com.spdata.factory.bean.ResultState;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import common.adapter.CommonAdapter;
import common.adapter.ViewHolder;
import common.base.act.FragActBase;
import common.utils.ExcelUtil;
import common.utils.permissUtils;

public class MenuActivity extends FragActBase {
    ListView listMenu;
    private String[] strings;
    private String model;

    @Override
    protected void initTitlebar() {
    }

    private CommonAdapter<ListItem> adapter;
    //版本
    private final static int ACTION_VERSION = 0;
    //休眠唤醒
    private final static int ACTION_SLEEP_WAKE = 1;
    private final static int ACTION_BUTTON = 2; //按键
    private final static int ACTION_DISPLAY = 3;//屏幕显示
    private final static int ACTION_TOUCH_SCREEN = 4;//触屏
    private final static int ACTION_LIGHT = 5;//亮度调节
    private final static int ACTION_SMALL_SCREEN = 6;//小屏幕
    private final static int ACTION_INDICATOR_LIGHT = 7;//指示灯
    private final static int ACTION_SD_SCARD = 8;//sd卡
    private final static int ACTION_SIM = 9;//sim卡
    private final static int ACTION_CAMMAR_BACKGROUND = 10;//后置相机
    private final static int ACTION_FLASH_LIGHT = 11;//手电筒
    private final static int ACTION_CAMMAR_FRONT = 12;//前置相机
    private final static int ACTION_BELL = 13;//喇叭
    private final static int ACTION_SCAN = 14;//扫描//
    private final static int ACTION_EIJI_MIC = 15;//耳机MIC
    private final static int ACTION_PHONE_MIC = 16;//主机MIC

    private final static int ACTION_WIFI = 17;//wifi
    private final static int ACTION_BLUETOOTH = 18;//蓝牙
    private final static int ACTION_GPS = 19;//GPS
    private final static int ACTION_NFC = 20;//NFC
    private final static int ACTION_USB = 21;//USB
    private final static int ACTION_OTG = 22;//OTG
    private final static int ACTION_CHARGE = 23;//有线充电
    private final static int ACTION_GSENSOR = 24;//加速传感器
    private final static int ACTION_LIGHT_SENSOR = 25;//光感

    private final static int ACTION_CALL_PHONE = 26;//打电话
    private final static int ACTION_EEPROM = 27;
    private final static int ACTION_GPS_OUT = 28;//外置gps模块
    private final static int ACTION_LASER = 29;//激光
    private final static int ACTION_VIBRATE = 30;//震动
    private final static int ACTION_COMPASS_SENSOR = 31;//电子罗盘
    private final static int ACTION_SPK = 32;//听筒
    private final static int ACTION_IRIS_CAMERA = 33;//虹膜摄像头
    private final static int ACTION_PSAM = 34;//PSAM
    private final static int ACTION_FINGER_PRINT = 35;//指纹
    private final static int ACTION_CHARGE_NOLINE = 36;//无线充电
    private final static int ACTION_TOUCH_SCREEN_MOR = 37;//多点触摸
    private final static int ACTION_BAROMETER = 38;//气压计
    private final static int ACTION_ZHONGLI = 39;//重力感应
    private final static int ACTION_USBPLATE = 40;//U盘plate
    private final static int ACTION_MAGLEV = 41;//maglev 磁悬附充电
    private final static int ACTION_SERIALPORT = 42;//串口孔serial port
    private final static int ACTION_R6 = 43;//高频RFID
    private final static int ACTION_UHF = 44;//超高频UHF
    private final static int ACTION_GAS_SENSOR = 45;//气体传感器
    private final static int ACTION_CAMERA_USB = 46;//DCD3矿灯摄像头
    private final static int ACTION_EXPAND = 47;//KT55触点检测
    private final static int ACTION_EXPORT = 48;//导出结果
    private final static int ACTION_ID2 = 49;//二代证测试
    private final static int ACTION_RESET = 50;//清除记录
    private final static int ACTION_PORT232 = 51;//Rs232串口
    private final static int ACTION_INTENET = 52;//RJ45网线接口测试
    private final static int ACTION_GPIOS = 53;//tc01 主板gpio测试
    private final static int ACTION_485 = 54;//tc01 485测试
    private final static int ACTION_PRINT = 55;//打印机
    private final static int ACTION_WIFI_PROBE = 56;//WiFi探针
    private final static int ACTION_LAMP_MIC = 57;//矿灯MIC
    private final static int ACTION_LAMP_LIGHT = 58;//矿灯
    private final static int ACTION_POSITION_KEY = 59;//定位卡按键
    private final static int ACTION_POSITION_LIGHT = 60;//定位卡指示灯
    private final static int ACTION_POSITION_BUZZER = 61;//定位卡蜂鸣器
    private String[] meneList = null;
    private String nulls[] = new String[0];
    private String WRITE_SETTINGS[] = {"android.permission.WRITE_SETTINGS", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private String wifi[] = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_WIFI_STATE"};
    List<permissUtils> permissUtilses = new ArrayList<>();

    private List<ListItem> listItemList = new ArrayList<>();
    private WifiManager mWifiManager;

    /**
     * 导出测试结果到sd卡
     */
    @SuppressLint("MissingPermission")
    private void exportFile() {
        List<ResultState> list = new ArrayList<>();
        for (int i = 0; i < strings.length - 1; i++) {
            ResultState bean = new ResultState();
            int index = Integer.parseInt(strings[i]);
            String title = meneList[index];
            bean.setName(title);
            bean.setNum(1 + i);
            if (listItemList.get(i).getState().equals(App.KEY_FINISH)) {
                bean.setContent("Pass");
            } else if (listItemList.get(i).getState().equals(App.KEY_UNFINISH)) {
                bean.setContent("Fail");
            } else {
                bean.setContent("null");
            }
            list.add(bean);
        }
        //导出Excel表格
        Map<String, String> titleMap = new LinkedHashMap<String, String>();
        titleMap.put("num", getResources().getString(R.string.Excle_title));
        titleMap.put("name", getResources().getString(R.string.Excle_title2));
        titleMap.put("content", getResources().getString(R.string.Excle_title3));
        TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        ExcelUtil.excelExport(MenuActivity.this, list, titleMap, "Factory-" + mgr.getDeviceId());
    }

    // 打开WIFI
    public void openWifi(Context context) {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    private void initUI() {
        listItemList.clear();
        model = App.getModel();
        if (model.equals("T450") || model.equals("KT55") || model.equals("T550")
                || model.equals("M55") || model.equals("T55") || model.equals("KT55L")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
                    "23", "24", "25", "26", "30", "31", "32", "37", "39", "47", "35", "49", "50", "48"};
        } else if (model.equals("KT80") || model.equals("W6") || model.equals("RT801")
                || model.equals("T80") || model.equals("T800") || model.equals("FC-K80") || model.equals("Biowolf LE")
                || model.equals("N800") || model.equals("FC-PK80") || model.equals("SD-55")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
                    "24", "25", "26", "30", "31", "37", "39", "40", "41", "42", "49", "50", "48"};
        } else if (model.equals("S510")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "12", "13", "15", "16", "17", "18", "21", "22", "23", "24", "25", "26",
                    "28", "30", "31", "32", "38", "37", "39", "50", "48"};
        } else if (model.equals("DB2_LVDS")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "8", "9", "10", "11", "13",
                    "15", "16", "17", "18", "20", "21", "22", "23", "24", "25", "26", "27",
                    "28", "30", "31", "37", "38", "39", "50", "48"};
//
        } else if (model.equals("KT50") || model.equals("KT50_B2")
                || model.equals("R40") || model.equals("T50") || model.equals("KT50_YQ")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "12", "13", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
                    "26", "30", "31", "32", "34", "37", "39", "50", "48"};
        } else if (model.equals("X300Q") || model.equals("X300Q_X1") || model.equals("X300Q_P1") ||
                model.equals("X300Q_OLED") || model.equals("X300Q_OLED_GPS")) {
            strings = new String[]{"0", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "15", "16", "17", "18", "21", "22", "23", "24", "25", "26",
                    "28", "29", "30", "32", "31", "37", "38", "39", "50", "48"};
        } else if (model.equals("H500A")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "12", "13", "16", "17", "18", "20", "21", "23", "24", "25", "26",
                    "28", "30", "32", "31", "37", "39", "50", "48"};
        } else if (model.equals("N80") || model.equals("S550")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "12", "13", "15", "16", "17", "18", "20", "21", "22", "23",
                    "24", "25", "26", "28", "30", "31", "37", "39", "38", "50", "48"};
        } else if (model.equals("N55") || model.equals("X55") || model.equals("N55/X55")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "20", "21", "22",
                    "23", "24", "25", "26", "28", "30", "31", "32", "37", "39", "50", "48"};
        } else if (model.equals("SC30")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "12",
                    "13", "15", "16", "17", "18", "19", "21", "22", "24", "25",
                    "23", "26", "30", "32", "37", "39", "45", "46", "57", "58", "59", "60", "61", "50", "48"};
        } else if (model.equals("spda6735") || model.equals("DCD3")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9",
                    "13", "16", "17", "18", "21", "24",
                    "23", "26", "30", "37", "45", "46", "50", "48"};
        } else if (model.equals("mt6753")) {
            strings = new String[]{"0", "1", "3", "4", "5", "7", "8", "9", "10",
                    "11", "12", "15", "17", "18", "19", "21", "22",
                    "23", "25", "26", "31", "34", "35", "37", "39", "50", "48"};
        } else if (model.equals("M08")) {
            strings = new String[]{"0", "2", "3", "4", "5", "7", "8", "9", "10",
                    "11", "13", "16", "17", "18", "19", "20", "21", "40", "23", "26", "30", "34", "37", "50", "48"};
        } else if (model.equals("KT45Q") || model.equals("UHF45") || model.equals("3000U")
                || model.equals("KT45Q_B2") || model.equals("JM45Q") || model.equals("FT43")
                || model.equals("PT145") || model.equals("TT43")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10",
                    "11", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
                    "23", "24", "26", "30", "31", "32", "34", "37",
                    "39", "43", "44", "50", "48"};

        } else if (model.equals("S1_35") || model.equals("H5_53") || model.equals("H5") || model.equals("S1") || model.equals("H5_35")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "13", "16", "17", "18", "19", "20", "21", "22", "23",
                    "24", "25", "26", "30", "31", "37", "39", "50", "48"};
        } else if (model.equals("CT")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9",
                    "13", "16", "17", "18", "19", "21", "22", "24", "26", "39", "50", "48"};
        } else if (model.equals("DM-P80")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "9", "10", "11",
                    "12", "13", "15", "16", "19", "30", "37", "48", "50", "49"};
        } else if (model.equals("SD80") || model.equals("AQUARIUS Cmp NS208")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
                    "23", "24", "25", "26", "30", "31", "32", "35", "37", "39", "41", "50", "48"};

        } else if (model.equals("KT40") || model.equals("KT40Q") || model.equals("KT40Q_O")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "10", "11",
                    "13", "14", "15", "16", "17", "19", "20", "18", "21", "22", "23",
                    "24", "26", "30", "32", "37", "39", "41", "50", "48"};
        } else if (model.equals("k63v2_64_bsp") || model.equals("SD55") || model.equals("SD60") || model.equals("X2") || model.equals("X37")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
                    "23", "24", "25", "26", "30", "31", "32", "37", "39", "50", "48"};
        } else if (model.equals("SD55L") || model.equals("SD55UHF")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
                    "23", "24", "25", "26", "30", "31", "32", "37", "39", "44", "50", "48"};
        } else if (model.equals("SC80H") || model.equals("SC80") || model.equals("SC37") || model.equals("SC53")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10",
                    "13", "14", "16", "17", "18", "19", "20", "21", "22",
                    "23", "24", "31", "39", "51", "52", "50", "48"};

        } else if (model.equals("SK80H") || model.equals("SK80")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10",
                    "13", "14", "16", "17", "18", "19", "20", "21",
                    "23", "24", "31", "34", "35", "37", "39", "51", "52", "49", "55", "56", "50", "48"};

        } else if (model.equals("TC01")) {
            strings = new String[]{"0", "3", "4", "5", "7", "8", "9", "10",
                    "13", "16", "17", "18", "19", "21", "23", "37", "52", "53", "54", "50", "48"};
        } else if (model.equals("SD35")) {
            strings = new String[]{"0", "2", "3", "4", "5", "7", "8", "9", "10",
                    "11", "12", "13", "14", "16", "17", "18", "19", "20", "21", "22",
                    "23", "24", "25", "26", "30", "31", "32", "34", "37", "38", "39", "50", "48"};
        } else if (model.equals("SD100T") || model.equals("X80") || model.equals("X47")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
                    "23", "24", "25", "26", "30", "31", "34", "37", "38", "39","49", "50", "48"};
        } else {
            strings = new String[]{"0", "3", "4", "5", "6", "7", "8", "9", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
                    "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34",
                    "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46",
                    "47", "49", "51", "52", "53", "54", "55", "56", "50", "48"};
        }

        for (int i = 0; i < strings.length; i++) {
            int index = Integer.parseInt(strings[i]);
            String title = meneList[index];
            ListItem listItem = new ListItem();
            listItem.setTitle(i + 1 + "." + title);
            listItem.setState(getState(index));
            listItemList.add(listItem);
        }
        adapter.notifyDataSetChanged();
    }

    private void initList() {
        adapter = new CommonAdapter<ListItem>(this, listItemList, R.layout.item_menu_list) {
            @Override
            public void convert(ViewHolder helper, ListItem item, int position) {

                String state = item.getState();
                helper.setText(R.id.tv_title, item.getTitle());
                if (state.equals(App.KEY_FINISH)) {
                    helper.setImageResource(R.id.image, R
                            .mipmap.right);
                    helper.setBackground(R.id.relative, getResources()
                            .getDrawable(R.drawable.selector_item));
                } else if (state.equals(App.KEY_UNFINISH)) {
                    helper.setImageResource(R.id.image, R.mipmap.error);
                    helper.setBackground(R.id.relative, getResources()
                            .getDrawable(R.drawable.selector_item));
                } else {
                    helper.setImageResource(R.id.image, getResources().getColor(android.R.color
                            .transparent));
                    helper.setBackground(R.id.relative, getResources()
                            .getDrawable(R.drawable.selector_item));
                }
            }
        };
        listMenu.setAdapter(adapter);
        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                gotoNextPage(Integer.parseInt(strings[position]));
                permiss(Integer.parseInt(strings[position]), permissUtilses.get(Integer.parseInt(strings[position])).getPermiss());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUI();
    }

    @Override
    protected void onDestroy() {
//        if (mWifiManager.isWifiEnabled()) {
//            mWifiManager.setWifiEnabled(false);
//        }
        super.onDestroy();
    }

    private String getState(int position) {
        String result = "";
        switch (position) {
            case ACTION_DISPLAY:
                result = getXml(App.KEY_DISPLAY, "");
                break;
            case ACTION_VERSION:
                result = getXml(App.KEY_VERSION, "");
                break;
            case ACTION_USB:
                result = getXml(App.KEY_USB, "");
                break;
            case ACTION_SD_SCARD:
                result = getXml(App.KEY_SDCARD, "");
                break;
            case ACTION_WIFI:
                result = getXml(App.KEY_WIFI, "");
                break;
            case ACTION_SLEEP_WAKE:
                result = getXml(App.KEY_SLEEP_WAKE, "");
                break;
            case ACTION_TOUCH_SCREEN:
                result = getXml(App.KEY_TOUCH_SCREEN, "");
                break;
            case ACTION_LIGHT:
                result = getXml(App.KEY_LIGHT, "");
                break;
            case ACTION_INDICATOR_LIGHT:
                result = getXml(App.KEY_INDICATOR_LIGHT, "");
                break;
            case ACTION_OTG:
                result = getXml(App.KEY_OTG, "");
                break;
            case ACTION_LIGHT_SENSOR:
                result = getXml(App.KEY_LIGHT_SENSOR, "");
                break;
            case ACTION_CALL_PHONE:
                result = getXml(App.KEY_ACTION_CALL_PHONE, "");
                break;
            case ACTION_FLASH_LIGHT:
                result = getXml(App.KEY_FLASH_LIGHT, "");
                break;
            case ACTION_GSENSOR:
                result = getXml(App.KEY_G_SENSOR, "");
                break;
            case ACTION_NFC:
                result = getXml(App.KEY_NFC, "");
                break;
            case ACTION_GPS:
                result = getXml(App.KEY_GPS, "");
                break;
            case ACTION_CAMMAR_BACKGROUND:
                result = getXml(App.KEY_CAMMAR_BACKGROUND, "");
                break;
            case ACTION_CAMMAR_FRONT:
                result = getXml(App.KEY_CAMMAR_FRONT, "");
                break;
            case ACTION_BELL:
                result = getXml(App.KEY_BELL, "");
                break;
            case ACTION_SCAN:
                result = getXml(App.KEY_SCAN, "");
                break;
            case ACTION_EIJI_MIC:
                result = getXml(App.KEY_EIJI_MIC, "");
                break;
            case ACTION_COMPASS_SENSOR:
                result = getXml(App.KEY_COMPASS_SENSOR, "");
                break;
            case ACTION_PSAM:
                result = getXml(App.KEY_PSAM, "");
                break;
            case ACTION_SPK:
                result = getXml(App.KEY_SPK, "");
                break;
            case ACTION_SIM:
                result = getXml(App.KEY_SIM, "");
                break;
            case ACTION_SMALL_SCREEN:
                result = getXml(App.KEY_SMALL_SCREEN, "");
                break;
            case ACTION_BLUETOOTH:
                result = getXml(App.KEY_BLUETOOTH, "");
                break;
            //有线充电
            case ACTION_CHARGE:
                result = getXml(App.KEY_CHANGE, "");
                break;
            case ACTION_BUTTON:
                result = getXml(App.KEY_BUTTON, "");
                break;
            case ACTION_FINGER_PRINT:
                result = getXml(App.KEY_FINGER_PRINT, "");
                break;
            case ACTION_IRIS_CAMERA:
                result = getXml(App.KEY_IRIS_CAMERA, "");
                break;

            case ACTION_PHONE_MIC:
                result = getXml(App.KEY_PHONE_MIC, "");
                break;
            case ACTION_CHARGE_NOLINE:
                result = getXml(App.KEY_CHARGE_NOLINE, "");
                break;
            case ACTION_TOUCH_SCREEN_MOR:
                result = getXml(App.KEY_ACTION_TOUCH_SCREEN_MOR, "");
                break;
            case ACTION_LASER:
                result = getXml(App.KEY_LASER, "");
                break;
            case ACTION_GPS_OUT:
                result = getXml(App.KEY_GPS_OUT, "");
                break;
            case ACTION_EEPROM:
                result = getXml(App.KEY_EEPROM, "");
                break;
            case ACTION_VIBRATE:
                result = getXml(App.KEY_VIBRATE, "");
                break;
            case ACTION_BAROMETER:
                result = getXml(App.KEY_BAROMETER, "");
                break;
            case ACTION_ZHONGLI:
                result = getXml(App.KEY_ZHONGLI, "");
                break;
            case ACTION_USBPLATE:
                result = getXml(App.KEY_USBPLATE, "");
                break;
            case ACTION_MAGLEV:
                result = getXml(App.KEY_MAGLEV, "");
                break;
            case ACTION_SERIALPORT:
                result = getXml(App.KEY_SERIALPORT, "");
                break;
            case ACTION_R6:
                result = getXml(App.KEY_R6, "");
                break;
            case ACTION_UHF:
                result = getXml(App.KEY_UHF, "");
                break;
            case ACTION_GAS_SENSOR:
                result = getXml(App.KEY_GAS_SENSOR, "");
                break;
            case ACTION_CAMERA_USB:
                result = getXml(App.KEY_CAMERA_USB, "");
                break;
            case ACTION_EXPAND:
                result = getXml(App.KEY_EXPAND, "");
                break;
            case ACTION_ID2:
                result = getXml(App.KEY_ID2, "");
                break;
            case ACTION_RESET:
                result = getXml(App.KEY_RESET, "");
                break;
            case ACTION_PORT232:
                result = getXml(App.KEY_PORT232, "");
                break;
            case ACTION_INTENET:
                result = getXml(App.KEY_INTENET, "");
                break;
            case ACTION_GPIOS:
                result = getXml(App.KEY_GPIOS, "");
                break;
            case ACTION_485:
                result = getXml(App.KEY_485, "");
                break;
            case ACTION_PRINT:
                result = getXml(App.KEY_PRINT, "");
                break;
            case ACTION_WIFI_PROBE:
                result = getXml(App.KEY_WIFI_PROBE, "");
                break;
            case ACTION_LAMP_MIC:
                result = getXml(App.KEY_LAMP_MIC, "");
                break;
            case ACTION_LAMP_LIGHT:
                result = getXml(App.KEY_LAMP_LIGHT, "");
                break;
            case ACTION_POSITION_KEY:
                result = getXml(App.KEY_POSITION_KEY, "");
                break;
            case ACTION_POSITION_LIGHT:
                result = getXml(App.KEY_POSITION_LIGHT, "");
                break;
            case ACTION_POSITION_BUZZER:
                result = getXml(App.KEY_POSITION_BUZZER, "");
                break;
            default:
                break;

        }
        return result;
    }


    /**
     * 跳转到对应的测试界面
     *
     * @param position
     */
    private void gotoNextPage(int position) {
        switch (position) {
            case ACTION_VERSION:
                openAct(VersionAct.class);
                break;
            case ACTION_SLEEP_WAKE:
                openAct(SleepWakeAct.class);
                break;
            case ACTION_DISPLAY:
                openAct(DisplayTestAct.class);
                break;
            case ACTION_USB:
                openAct(USBAct.class);
                break;
            case ACTION_OTG:
                openAct(OTGAct.class);
                break;
            case ACTION_SD_SCARD:
                if (model.equals("KT80") || model.equals("W6") || model.equals("RT801")
                        || model.equals("T80") || model.equals("T800") || model.equals("FC-K80")
                        || model.equals("Biowolf LE") || model.equals("N800") || model.equals("FC-PK80")
                        || model.equals("DM-P80") || model.equals("SD-55")) {
                    openAct(SDCardkt80Act.class);
                } else {
                    openAct(SDCardAct.class);
                }
                break;
            case ACTION_WIFI:
                openAct(WifiAct.class);
                break;
            case ACTION_TOUCH_SCREEN:
//                if (model.equals("N80") || model.equals("M08") || model.equals("S1_35") || model.equals("S1")) {
                openAct(TsHandWritingAct.class);
//                } else {
//                    openAct(TouchTest.class);
//                }

                break;
            case ACTION_LIGHT:
                openAct(LightAct.class);
                break;
            case ACTION_INDICATOR_LIGHT:
                if (model.equals("k63v2_64_bsp") || model.equals("SD55") || model.equals("SD60") || model.equals("SC30") || model.equals("X2") || model.equals("X37")) {
                    openAct(IndicatorLightAct_sd55.class);
                } else if (model.equals("SD100T") || model.equals("X80") || model.equals("X47")) {
                    openAct(IndicatorLightAct_sd100t.class);
                } else if (model.equals("SK80H") || model.equals("SK80") || model.equals("SC80H") || model.equals("SC80") || model.equals("SC37") || model.equals("SC53")) {
                    openAct(IndicatorSk80LightAct.class);
                } else {
                    openAct(IndicatorLightAct.class);
                }
                break;
            case ACTION_LIGHT_SENSOR:
                openAct(LightSeneorAct.class);
                break;
            case ACTION_COMPASS_SENSOR:
                openAct(CompassSeneorAct.class);
                break;
            case ACTION_GSENSOR:
                openAct(GSeneorAct.class);
                break;
            case ACTION_NFC:
                openAct(NFCAct.class);
                break;
            case ACTION_GPS:
                openAct(GPSTest.class);
                break;
            case ACTION_SCAN:
                if (model.equals("X300Q_X1") || model.equals("X300Q_P1") ||
                        model.equals("X300Q_OLED") || model.equals("X300Q_OLED_GPS")
                        || model.equals("X300Q")) {
                    openAct(ScanX300qAct.class);
                } else {
                    openAct(ScanAct.class);
                }
                break;
            case ACTION_CAMMAR_BACKGROUND:
                if (model.equals("spda6735") || model.equals("DCD3")) {
                    openAct(DCD3CammerBackgroundAct.class);
                } else if (model.equals("M08")) {
                    openAct(M08CameraBacckAct.class);
                } else if (model.equals("SK80H") || model.equals("SK80")) {
                    openAct(CammerSystemSk80Act.class);
                } else {
                    openAct(CammerBackgroundAct.class);
                }
                break;
            case ACTION_SIM:
                openAct(SimAct.class);
                break;
            case ACTION_SMALL_SCREEN:
                openAct(SmallScreenAct.class);
                break;
            case ACTION_CAMMAR_FRONT:
                // TODO 跳转到前置相机测试界面
                if ("SC30".equals(model)) {
                    openAct(CammerFrontSC30Act.class);
                } else {
                    openAct(CammerFrontAct.class);
                }
                break;
            case ACTION_BELL:
                // TODO 跳转到喇叭测试界面
                openAct(BellAct.class);
                break;
            case ACTION_SPK:
                // TODO 跳转到听筒测试界面
                openAct(SpeakerAct.class);
                break;
            case ACTION_EIJI_MIC:
                // TODO 跳转到耳机MIC测试界面
                openAct(EarMICAct.class);
                break;
            case ACTION_PHONE_MIC:
                //手机mic
                openAct(PhoneMICAct.class);
                break;

            case ACTION_PSAM:
                // TODO 跳转到PSAM测试界面
                openAct(PSAMAct.class);
                break;
            case ACTION_BLUETOOTH:
                // TODO 跳转到蓝牙测试界面
                openAct(BluetoothAct.class);
                break;
            case ACTION_CHARGE:
                openAct(ChangeAct.class);
                break;

            case ACTION_BUTTON:
                if (model.equals("T450") || model.equals("KT55")
                        || model.equals("N55") || model.equals("X55") || model.equals("T55")
                        || model.equals("N55/X55") || model.equals("T550") || model.equals("M55")
                        || model.equals("KT55L")) {
                    openAct(ButtonAct.class);
                } else if (model.equals("KT80") || model.equals("W6") || model.equals("RT801")
                        || model.equals("T80") || model.equals("T800") || model.equals("FC-K80")
                        || model.equals("Biowolf LE") || model.equals("N800") || model.equals("FC-PK80")
                        || model.equals("SD-55")) {
                    openAct(ButtonKT80Act.class);
                } else if (model.equals("DM-P80")) {
                    openAct(ButtonDmP80Act.class);
                } else if (model.equals("S510")) {
                    openAct(ButtonS150Act.class);
                } else if (model.equals("DB2_LVDS")) {
                    openAct(ButtonDb2Act.class);
                } else if (model.equals("KT50") || model.equals("KT50_B2")
                        || model.equals("R40") || model.equals("T50") || model.equals("KT50_YQ")) {
                    openAct(ButtonKT50Act.class);
                } else if (model.equals("X300Q_X1") || model.equals("X300Q_P1") ||
                        model.equals("X300Q_OLED") || model.equals("X300Q_OLED_GPS")
                        || model.equals("X300Q")) {
                    openAct(ButtonTwoAct.class);
                } else if (model.equals("H500A")) {
                    openAct(ButtonH500Act.class);
                } else if (model.equals("spda6735") || model.equals("DCD3")) {
                    openAct(ButtonDCD3Act.class);
                } else if (model.equals("SC30")) {
                    openAct(ButtonSC30Act.class);
                } else if (model.equals("N80") || model.equals("S550")) {
                    openAct(ButtonN80Act.class);
                } else if (model.equals("M08")) {
                    openAct(ButtonM08Act.class);
                } else if (model.equals("KT45Q") || model.equals("UHF45") || model.equals("3000U")
                        || model.equals("KT45Q_B2") || model.equals("JM45Q") || model.equals("FT43")
                        || model.equals("PT145") || model.equals("TT43")) {
                    openAct(ButtonKT45qAct.class);
                } else if (model.equals("S1_35") || model.equals("H5_53") || model.equals("H5") || model.equals("S1") || model.equals("H5_35")) {
                    openAct(ButtonS1Act.class);
                } else if (model.equals("CT")) {
                    openAct(ButtonCtAct.class);
                } else if (model.equals("SD80") || model.equals("AQUARIUS Cmp NS208")) {
                    openAct(ButtonSd80Act.class);
                } else if (model.equals("KT40Q") || model.equals("KT40Q_O")) {
                    openAct(ButtonKt40qAct.class);
                } else if (model.equals("KT40")) {
                    openAct(ButtonKt40Act.class);
                } else if (model.equals("SD55") || model.equals("SD55L") || model.equals("SD55UHF") || model.equals("SD60")||model.equals("X2") || model.equals("X37")) {
                    openAct(ButtonSd55.class);
                } else if (model.equals("SK80H") || model.equals("SK80") || model.equals("SC80H") || model.equals("SC80") || model.equals("SC37") || model.equals("SC53")) {
                    openAct(ButtonSk80Act.class);
                } else if (model.equals("SD35")) {
                    openAct(ButtonSd35Act.class);
                } else if (model.equals("SD100T") || model.equals("X80") || model.equals("X47")) {
                    openAct(ButtonSd100tAct.class);
                } else {
                    openAct(ButtonAll.class);
                }
                break;

            case ACTION_FINGER_PRINT:
                openAct(FingerPrint.class);
                break;
            case ACTION_FLASH_LIGHT:
                if (getApiVersion() >= 23) {
                    openAct(FlashActivity.class);
                } else {
                    openAct(FlashLightAct.class);
                }
                break;
            case ACTION_CHARGE_NOLINE:      //无线充电
                break;
            case ACTION_CALL_PHONE:
                openAct(CallPhoneActi.class);
                break;
            case ACTION_TOUCH_SCREEN_MOR://多点触控
                openAct(MultitouchVisible.class);
                break;
            case ACTION_EEPROM:
                openAct(EepromAct.class);
                break;
            case ACTION_LASER:
                openAct(LaserAct.class);
                break;
            case ACTION_GPS_OUT:
                if (model.equals("S510")) {
                    openAct(OutGpsS510Act.class);
                } else if (model.equals("DB2_LVDS")) {
                    openAct(OutGpsDB2Act.class);
                } else if (model.equals("X300Q_X1") || model.equals("X300Q_P1") ||
                        model.equals("X300Q_OLED") || model.equals("X300Q_OLED_GPS")
                        || model.equals("X300Q")) {
                    openAct(OutGps_x300Act.class);
                } else if (model.equals("H500A")) {
                    openAct(OutGpsH500Act.class);
                } else if (model.equals("N55") || model.equals("X55") || model.equals("N55/X55")) {
                    openAct(OutGpsN55Act.class);
                } else if (model.equals("N80")) {
                    openAct(OutGpsN80Act.class);
                }

                break;
            case ACTION_VIBRATE:
                openAct(VibrateAct.class);
                break;
            case ACTION_BAROMETER:
                openAct(BarometerAct.class);
                break;
            case ACTION_ZHONGLI:
                if (model.equals("KT80") || model.equals("W6") || model.equals("DB2_LVDS")
                        || model.equals("mt6753") || model.equals("RT801")
                        || model.equals("T80") || model.equals("T800") || model.equals("FC-K80")
                        || model.equals("Biowolf LE") || model.equals("N800") || model.equals("FC-PK80")
                        || model.equals("DM-P80") || model.equals("SD-55")) {
                    openAct(Kt80Zhongli.class);
                } else {
                    openAct(ZhongLiGanYing.class);
                }
                break;
            case ACTION_MAGLEV:
                openAct(MaglevAct.class);
                break;
            case ACTION_SERIALPORT:
                if (model.equals("N80")) {
                    openAct(SerialportH80Act.class);
                } else {
                    openAct(SerialportAct.class);
                }
                break;
            case ACTION_USBPLATE:
                openAct(UsbPlateAct.class);
                break;
            case ACTION_R6:
                openAct(R6Act.class);
                break;
            case ACTION_UHF:
                openAct(UhfAct.class);
                break;
            case ACTION_GAS_SENSOR:
                openAct(GasSensorAct.class);
                break;
            case ACTION_CAMERA_USB:
                openAct(CameraUSBAct.class);
                break;
            case ACTION_EXPAND:
                openAct(ExpandAct.class);
                break;
            case ACTION_EXPORT:
                exportFile();
                break;
            case ACTION_ID2:
                openAct(Id2TestAct.class);
                break;
            case ACTION_RESET:
                openAct(ResetAct.class);

                break;
            case ACTION_PORT232:
                openAct(Rs232Serport.class);
                break;
            case ACTION_INTENET:
                openAct(CheckIntentAct.class);
                break;
            case ACTION_GPIOS:
                openAct(Tc01GpioAct.class);
                break;
            case ACTION_485:
                openAct(Tc01485Act.class);
                break;
            case ACTION_PRINT:
                openAct(PrintTestAct.class);
                break;
            case ACTION_WIFI_PROBE:
                openAct(WifiProbeTestAct.class);
                break;
            case ACTION_LAMP_MIC:
                openAct(LampMICAct.class);
                break;
            case ACTION_LAMP_LIGHT:
                openAct(MinerLampSC30Act.class);
                break;
            case ACTION_POSITION_KEY:
                openAct(PositionButtonSC30Act.class);
                break;
            case ACTION_POSITION_LIGHT:
                openAct(PositionLightSC30Act.class);
                break;
            case ACTION_POSITION_BUZZER:
                openAct(PositionBuzzerSC30Act.class);
                break;
            default:
                break;
        }
    }

    //安卓6.0获取权限
    public void permiss(int i, String[] s) {
        if (s.length < 0) {
            gotoNextPage(i);
        } else {
            AndPermission.with(MenuActivity.this).requestCode(i).permission(s)
                    .callback(permissionListener).rationale(new RationaleListener() {
                @Override
                public void showRequestPermissionRationale(int i, Rationale rationale) {
                    AndPermission.rationaleDialog(MenuActivity.this, rationale).show();
                }
            }).start();
        }
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int i, @NonNull List<String> list) {
            gotoNextPage(i);
//            Toast.makeText(MenuActivity.this, "成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(int i, @NonNull List<String> list) {
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(MenuActivity.this, list)) {
                AndPermission.defaultSettingDialog(MenuActivity.this, 300).show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initView();
        meneList = new String[]{getResources().getString(R.string.menu_version), getResources().getString(R.string.menu_sleep_wake), getResources().getString(R.string.menu_button), getResources().getString(R.string.menu_display), getResources().getString(R.string.menu_touch_screen),
                getResources().getString(R.string.menu_light), getResources().getString(R.string.menu_small_screen), getResources().getString(R.string.menu_indicator_light), getResources().getString(R.string.menu_sdcard), getResources().getString(R.string.menu_simcard),
                getResources().getString(R.string.menu_background_camera), getResources().getString(R.string.menu_flash_light), getResources().getString(R.string.menu_front_camera), getResources().getString(R.string.menu_bell), getResources().getString(R.string.menu_scan),
                getResources().getString(R.string.menu_erji_mic), getResources().getString(R.string.menu_phone_mic), getResources().getString(R.string.menu_wifi), getResources().getString(R.string.menu_bluetooth), getResources().getString(R.string.menu_gps),
                getResources().getString(R.string.menu_nfc), getResources().getString(R.string.menu_usb), getResources().getString(R.string.menu_otg), getResources().getString(R.string.menu_charge), getResources().getString(R.string.menu_gsensor),
                getResources().getString(R.string.menu_light_sensor), getResources().getString(R.string.menu_call_phone), getResources().getString(R.string.menu_eeprom), getResources().getString(R.string.menu_expan_gps), getResources().getString(R.string.menu_laser),
                getResources().getString(R.string.menu_vibrate), getResources().getString(R.string.menu_compass_sensor), getResources().getString(R.string.menu_spk), getResources().getString(R.string.menu_ris_camera), getResources().getString(R.string.menu_ris_psam),
                getResources().getString(R.string.menu_finger), getResources().getString(R.string.menu_notline_charge), getResources().getString(R.string.menu_touch_mor), getResources().getString(R.string.menu_barometer), getResources().getString(R.string.menu_zhongli)
                , getResources().getString(R.string.menu_usbplate), getResources().getString(R.string.menu_maglev), getResources().getString(R.string.menu_serialport), getResources().getString(R.string.menu_rfid), getResources().getString(R.string.menu_uhf),
                getResources().getString(R.string.menu_gas_sensor), getResources().getString(R.string.menu_camera_usb), getResources().getString(R.string.menu_expand), getResources().getString(R.string.menu_export), getResources().getString(R.string.menu_id2),
                getResources().getString(R.string.menu_reset), getResources().getString(R.string.menu_rs232), getResources().getString(R.string.menu_rj45), getResources().getString(R.string.menu_gpio), getResources().getString(R.string.menu_test485)
                , getResources().getString(R.string.menu_print), getResources().getString(R.string.menu_wifi_tanzhen), getResources().getString(R.string.menu_lamp_mic), getResources().getString(R.string.menu_lamp_light), getResources().getString(R.string.menu_position_button),
                getResources().getString(R.string.menu_position_indicator), getResources().getString(R.string.menu_position_buzzer)};
        //强制在线更新
//        UpdateVersion updateVersion = new UpdateVersion(mContext);
//        updateVersion.startUpdate();
        initList();
        mWifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        permissUtilses.clear();
        for (int i = 0; i < meneList.length; i++) {
            permissUtils permissUtils = null;
            if (i == 8 || i == 48) {
                permissUtils = new permissUtils(Permission.STORAGE);
                permissUtilses.add(permissUtils);
            } else if (i == 9) {
                permissUtils = new permissUtils(Permission.PHONE);
                permissUtilses.add(permissUtils);
            } else if (i == 10 || i == 11 || i == 12) {
                permissUtils = new permissUtils(Permission.CAMERA);
                permissUtilses.add(permissUtils);
            } else if (i == 15 || i == 16) {
                permissUtils = new permissUtils(Permission.MICROPHONE);
                permissUtilses.add(permissUtils);
            } else if (i == 17) {
                permissUtils = new permissUtils(wifi);
                permissUtilses.add(permissUtils);
            } else if (i == 18 || i == 19) {
                permissUtils = new permissUtils(Permission.LOCATION);
                permissUtilses.add(permissUtils);
            } else {
                permissUtils = new permissUtils(nulls);
                permissUtilses.add(permissUtils);
            }
        }
        //openWifi(this);
    }

    public void initView() {
        listMenu = (ListView) findViewById(R.id.list_menu);
    }
}
