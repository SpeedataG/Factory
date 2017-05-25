package com.spdata.factory;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.spdata.factory.application.App;
import com.spdata.factory.bean.ListItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import common.adapter.CommonAdapter;
import common.adapter.ViewHolder;
import common.base.act.FragActBase;
import common.event.ViewMessage;

@EActivity(R.layout.activity_menu)
public class MenuActivity extends FragActBase {

    @ViewById
    ListView listMenu;
    private String[] strings;
    private String model;
    private String models;
//    private String models2;
//    private String author;

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {

    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    private CommonAdapter<ListItem> adapter;
    private final static int ACTION_VERSION = 0;//版本
    private final static int ACTION_SLEEP_WAKE = 1;//休眠唤醒
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
    private String[] meneList = {"版本信息", "休眠唤醒", "按键", "屏幕显示",
            "触屏", "亮度调节", "小屏幕", "指示灯", "SD卡", "SIM卡", "后置相机/闪光",
            "手电筒", "前置相机", "喇叭", "扫描", "耳机MIC", "主机MIC", "wifi",
            "蓝牙", "GPS", "NFC", "USB", "OTG", "有线充电", "加速传感器", "光感",
            "打电话", "EEPROM", "外置GPS", "激光", "震动器", "电子罗盘", "听筒",
            "虹膜摄像头", "PSAM", "指纹&ID2", "无线充电", "多点触摸", "气压计", "重力感应"
            , "U盘", "磁吸附充电", "串口孔", "高频RFID", "超高频UHF", "气体传感器",
            "矿灯摄像头", "触点检测"};

    private List<ListItem> listItemList = new ArrayList<>();
    WifiManager mWifiManager;

    @AfterViews
    protected void main() {
        setSwipeEnable(false);
        //强制在线更新
//        UpdateVersion updateVersion = new UpdateVersion(mContext);
//        updateVersion.startUpdate();
        initList();
        mWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);


    }

    // 打开WIFI
    public void openWifi(Context context) {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        } else if (mWifiManager.getWifiState() == 2) {
//            Toast.makeText(context,"Wifi正在开启", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(context,"Wifi已经开启", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        listItemList.clear();
        model = Build.MODEL;
        if (model.equals("T450") || model.equals("KT55") || model.equals("T550")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
                    "23", "24", "25", "26", "30", "31", "32", "37", "39", "47"};
        } else if (model.equals("KT80") || model.equals("W6")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
                    "24", "25", "26", "30", "31", "37", "39", "40", "41", "42"};
        } else if (model.equals("S510")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "12", "13", "15", "16", "17", "18", "21", "22", "23", "24", "25", "26",
                    "28", "30", "31", "32", "38", "37", "39"};
        } else if (model.equals("DB2_LVDS")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "8", "9", "10", "11", "13",
                    "15", "16", "17", "18", "20", "21", "22", "23", "24", "25", "26", "27",
                    "28", "30", "31", "37", "38", "39"};
        } else if (model.equals("KT50") || model.equals("KT50_B2")
                || model.equals("R40") || model.equals("T50") || model.equals("KT50_YQ")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
                    "26", "30","31", "32","37", "39", "43", "44"};
        } else if (model.equals("X300Q_X1") || model.equals("X300Q_P1") ||
                model.equals("X300Q_OLED") || model.equals("X300Q_OLED_GPS")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
                    "29", "30", "32", "31", "37", "38", "42", "39"};
        } else if (model.equals("H500A")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "12", "13", "16", "17", "18", "20", "21", "23", "24", "25", "26",
                    "28", "30", "32", "31", "37", "39"};
        } else if (model.equals("N80")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11",
                    "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
                    "24", "25", "26", "30", "31", "37", "39", "40", "42"};
        } else if (model.equals("N55") || model.equals("X55") || model.equals("N55/X55")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "20", "21", "22",
                    "23", "24", "25", "26", "28", "30", "31", "32", "37", "39"};
        } else if (model.equals("spda6735") || model.equals("DCD3")) {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "7", "8", "9",
                    "13", "16", "17", "18", "21", "24",
                    "23", "26", "30", "37", "45", "46"};
        } else if (model.equals("mt6753")) {
            strings = new String[]{"0", "1", "3", "4", "5", "7", "8", "9", "10",
                    "11", "12", "15", "17", "18", "19", "21", "22",
                    "23", "25", "26", "31", "34", "35", "37", "39"};
        } else if (model.equals("M08")) {
            strings = new String[]{"0", "2", "3", "4", "5", "7", "8", "9", "10",
                   "11","13","16", "17", "18", "19", "20","21","40","23","26","30","34","37" };
        } else {
            strings = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
                    "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "37",
                    "39", "43", "42", "44"};
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
                            .drawable.right);
                    helper.setBackground(R.id.relative, getResources()
                            .getDrawable(R.drawable.selector_item));
                } else if (state.equals(App.KEY_UNFINISH)) {
                    helper.setImageResource(R.id.image, R.drawable.error);
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
                gotoNextPage(Integer.parseInt(strings[position]));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUI();
        openWifi(this);
//        if (model.equals("T450") || model.equals("KT55") || model.equals("KT80")
//                || model.equals("W6") || model.equals("N80") ||
//                model.equals("N55") || model.equals("X55") ||
//                model.equals("N55/X55") || model.equals("T550")) {
//            String result = SystemProperties.get("persist.sys.keyreport", "true");
//            if (result.equals("true")) {
//                SystemProperties.set("persist.sys.keyreport", "false");
//            }
//        }
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
        }
        return result;
    }


    private int selectPosition = 0;

    /**
     * 跳转到对应的测试界面
     *
     * @param position
     */
    private void gotoNextPage(int position) {
        selectPosition = position;
        switch (position) {
            case ACTION_VERSION:
                openAct(VersionAct.class, true);
                break;
            case ACTION_SLEEP_WAKE:
                openAct(SleepWakeAct.class, true);
                break;
            case ACTION_DISPLAY:
                openAct(DisplayTest.class, false);
                break;
            case ACTION_USB:
                openAct(USBAct.class, true);
                break;
            case ACTION_OTG:
                openAct(OTGAct.class, true);
                break;
            case ACTION_SD_SCARD:
                if (model.equals("KT80") || model.equals("W6")) {
                    openAct(SDCardkt80Act.class, true);
                } else {
                    openAct(SDCardAct.class, true);
                }
                break;
            case ACTION_WIFI:
                openAct(WifiAct.class, true);
                break;
            case ACTION_TOUCH_SCREEN:
                openAct(TsHandWriting.class, false);
                break;
            case ACTION_LIGHT:
                openAct(LightAct.class, true);
                break;
            case ACTION_INDICATOR_LIGHT:
                openAct(IndicatorLightAct.class, true);
                break;
            case ACTION_LIGHT_SENSOR:
                openAct(LightSeneorAct.class, true);
                break;
            case ACTION_COMPASS_SENSOR:
                openAct(CompassSeneorAct.class, true);
                break;
            case ACTION_GSENSOR:
                openAct(GSeneorAct.class, true);
                break;
            case ACTION_NFC:
                openAct(NFCAct.class, true);
                break;
            case ACTION_GPS:
                openAct(GPSTest.class, false);
                break;
            case ACTION_SCAN:
                if (model.equals("X300Q_X1") || model.equals("X300Q_P1") ||
                        model.equals("X300Q_OLED") || model.equals("X300Q_OLED_GPS")) {
                    openAct(ScanX300qAct.class, true);
                } else {
                    openAct(ScanAct.class, true);
                }
                break;
            case ACTION_CAMMAR_BACKGROUND:
                if (model.equals("spda6735") || model.equals("DCD3")) {
                    openAct(DCD3CammerBackgroundAct.class, true);
                } else {
                    openAct(CammerBackgroundAct.class, true);
                }

                break;
            case ACTION_SIM:
                openAct(SimAct.class, true);
                break;
            case ACTION_SMALL_SCREEN:
                openAct(SmallScreenAct.class, true);
                break;
            case ACTION_CAMMAR_FRONT:
                // TODO 跳转到前置相机测试界面
                openAct(CammerFrontAct.class, true);
                break;
            case ACTION_BELL:
                // TODO 跳转到喇叭测试界面
                openAct(BellAct.class, true);
                break;
            case ACTION_SPK:
                // TODO 跳转到听筒测试界面
                openAct(SpeakerAct.class, true);
                break;
            case ACTION_EIJI_MIC:
                // TODO 跳转到耳机MIC测试界面
                openAct(EarMICAct.class, true);
                break;
            case ACTION_PHONE_MIC:
                //手机mic
                openAct(PhoneMICAct.class, true);
                break;

            case ACTION_PSAM:
                // TODO 跳转到PSAM测试界面
                openAct(PSAMAct.class, true);
                break;
            case ACTION_BLUETOOTH:
                // TODO 跳转到蓝牙测试界面
                openAct(BluetoothAct.class, true);
                break;

            case ACTION_CHARGE:
                openAct(ChangeAct.class, true);
                break;

            case ACTION_BUTTON:
                if (model.equals("T450") || model.equals("KT55")
                        || model.equals("N55") || model.equals("X55")
                        || model.equals("N55/X55") || model.equals("T550")) {
                    openAct(ButtonAct.class, true);
                } else if (model.equals("KT80") || model.equals("W6")) {
                    openAct(ButtonKT80Act.class, true);
                } else if (model.equals("S510")) {
                    openAct(ButtonS150Act.class, true);
                } else if (model.equals("DB2_LVDS")) {
                    openAct(ButtonDb2Act.class, true);
                } else if (model.equals("KT50") || model.equals("KT50_B2")
                        || model.equals("R40") || model.equals("T50")|| model.equals("KT50_YQ")) {
                    openAct(ButtonKT50Act.class, true);
                } else if (model.equals("X300Q_X1") || model.equals("X300Q_P1") ||
                        model.equals("X300Q_OLED") || model.equals("X300Q_OLED_GPS")) {
                    openAct(ButtonTwoAct.class, true);
                } else if (model.equals("H500A")) {
                    openAct(ButtonH500Act.class, true);
                } else if (model.equals("spda6735") || model.equals("DCD3")) {
                    openAct(DCD3ButtonAct.class, true);
                } else if (model.equals("N80")) {
                    openAct(ButtonN80Act.class, true);
                } else if (model.equals("M08")) {
                    openAct(ButtonM08Act.class,true);
                }
                break;

            case ACTION_FINGER_PRINT:
                openAct(FingerPrint.class, true);
                break;
            case ACTION_FLASH_LIGHT:
                openAct(FlashLightAct.class, true);
                break;
            case ACTION_CHARGE_NOLINE:      //无线充电
                break;
            case ACTION_CALL_PHONE:
                openAct(CallPhoneActi.class, true);
                break;
            case ACTION_TOUCH_SCREEN_MOR://多点触控
                openAct(MultitouchVisible.class, false);
                break;
            case ACTION_EEPROM:
                openAct(EepromAct.class, true);
                break;
            case ACTION_LASER:
                openAct(LaserAct.class, true);
                break;
            case ACTION_GPS_OUT:
                if (model.equals("S510")) {
                    openAct(OutGpsS510Act.class, true);
                } else if (model.equals("DB2_LVDS")) {
                    openAct(OutGpsDB2Act.class, true);
                } else if (model.equals("X300Q_X1") || model.equals("X300Q_P1") ||
                        model.equals("X300Q_OLED") || model.equals("X300Q_OLED_GPS")) {
                    openAct(OutGpsAct.class, true);
                } else if (model.equals("H500A")) {
                    openAct(OutGpsH500Act.class, true);
                } else if (model.equals("N55") || model.equals("X55") || model.equals("N55/X55")) {
                    openAct(OutGpsN55Act.class, true);
                }
                break;
            case ACTION_VIBRATE:
                openAct(VibrateAct.class, true);
                break;
            case ACTION_BAROMETER:
                openAct(BarometerAct.class, true);
                break;
            case ACTION_ZHONGLI:
                if (model.equals("KT80") || model.equals("W6") ||
                        model.equals("DB2_LVDS") || model.equals("N80")
                        || model.equals("mt6753")) {
                    openAct(Kt80Zhongli.class, true);
                } else {
                    openAct(ZhongLiGanYing.class, true);
                }
                break;
            case ACTION_MAGLEV:
                openAct(MaglevAct.class, true);
                break;
            case ACTION_SERIALPORT:
                if (model.equals("N80")) {
                    openAct(SerialportH80Act.class, true);
                } else {
                    openAct(SerialportAct.class, true);
                }
                break;
            case ACTION_USBPLATE:
                openAct(UsbPlateAct.class, true);
                break;
            case ACTION_R6:
                openAct(R6Act.class, true);
                break;
            case ACTION_UHF:
                openAct(UhfAct.class, true);
                break;
            case ACTION_GAS_SENSOR:
                openAct(GasSensorAct.class, true);
                break;
            case ACTION_CAMERA_USB:
                openAct(CameraUSBAct.class, true);
                break;
            case ACTION_EXPAND:
                openAct(ExpandAct.class, true);
                break;
        }
    }
}
