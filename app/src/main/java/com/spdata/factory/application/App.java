/*
 *
 * @author yandeqing
 * @created 2016.6.3
 * @email 18612205027@163.com
 * @version $version
 *
 */

/*
 * @author yandeq
 * @version 1.0
 * @created 2016.6.3.
 *
 */

package com.spdata.factory.application;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.multidex.MultiDex;
//import android.support.multidex.MultiDex;

import common.base.BaseApplication;
import common.base.dialog.ToastUtils;
import common.utils.NetUtil;

public class App extends BaseApplication implements AppConst {

    private static App instance = null;
    public static final String KEY_DISPLAY = "display";
    public static final String KEY_VERSION = "version";
    public static final String KEY_USB = "usb";
    public static final String KEY_OTG = "otg";
    public static final String KEY_SCAN = "scan";
    public static final String KEY_SIM = "sim";
    public static final String KEY_SMALL_SCREEN = "small_screen";
    public static final String KEY_LIGHT_SENSOR = "light_sernsor";
    public static final String KEY_COMPASS_SENSOR = "compass_sernsor";//电子罗盘
    public static final String KEY_G_SENSOR = "g_sensor";//加速传感器
    public static final String KEY_SDCARD = "sdcard";
    public static final String KEY_CHANGE = "change";//无线充电
    public static final String KEY_BLUETOOTH = "bluetooth";//蓝牙
    public static final String KEY_LIGHT = "light";
    public static final String KEY_INDICATOR_LIGHT = "indicator_light";
    public static final String KEY_SLEEP_WAKE = "sleep_wake";
    public static final String KEY_TOUCH_SCREEN = "touch_screen";
    public static final String KEY_NFC = "nfc";
    public static final String KEY_GPS = "gps";
    public static final String KEY_WIFI = "wifi";
    public static final String KEY_CAMMAR_BACKGROUND = "cammar_background";
    public static final String KEY_CAMMAR_FRONT = "cammar_front";
    public static final String KEY_BELL = "bell";
    public static final String KEY_SPK = "spk";
    public static final String KEY_EIJI_MIC = "siji_mic";
    public static final String KEY_PSAM = "psam";
    public static final String KEY_FINISH = "finish";
    public static final String KEY_UNFINISH = "unfinish";
    public static final String KEY_IRIS_CAMERA = "iris_camera";
    public static final String KEY_FINGER_PRINT = "finger_print";
    public static final String KEY_BUTTON = "button";
    public static final String KEY_FLASH_LIGHT = "flash_light";
    public static final String KEY_PHONE_MIC = "phone_mic";
    public static final String KEY_CHARGE_NOLINE = "charge_noline";//无线充电
    public static final String KEY_ACTION_TOUCH_SCREEN_MOR = "touch_more";//duo点触控
    public static final String KEY_ACTION_CALL_PHONE = "call_phone";//打电话
    public static final String KEY_EEPROM = "eeprom";
    public static final String KEY_GPS_OUT = "gps_out";//外置gps
    public static final String KEY_LASER = "laser";//激光
    public static final String KEY_VIBRATE = "vibrate";//震动器
    public static final String KEY_BAROMETER = "barometer";//气压计
    public static final String KEY_ZHONGLI = "zhongli";//重力感应
    public static final String KEY_USBPLATE = "usbplate";//u盘
    public static final String KEY_MAGLEV = "maglev";//磁悬附充电
    public static final String KEY_SERIALPORT = "serrialport";//串口孔
    public static final String KEY_R6 = "r6";//高频
    public static final String KEY_UHF = "uhf";//超高频sensor
    public static final String KEY_GAS_SENSOR = "gassensor";//气体传感器
    public static final String KEY_CAMERA_USB = "camerausb";//dcd3相机
    public static final String KEY_EXPAND = "expand";//KT55触点测试
    public static final String KEY_EXPORT = "export";//导出测试结果
    public static final String KEY_ID2 = "export";//二代证测试
    public static final String KEY_RESET = "reset";//恢复出厂设置
    public static final String KEY_PORT232 = "port232";//232串口
    public static final String KEY_INTENET = "intenet";//RJ45网线接口测试
    public static final String KEY_GPIOS = "gpios";// tc01 主板gpio测试
    public static final String KEY_485 = "485";//tc01  485测试
    public static final String KEY_PRINT = "print";//打印机
    public static final String KEY_WIFI_PROBE = "wifiprobe";//WiFi探针
    public static final String KEY_LAMP_MIC = "lampmic";//矿灯MIC
    public static final String KEY_LAMP_LIGHT = "lamplight";//矿灯
    public static final String KEY_POSITION_KEY = "positionkey";//定位卡按键
    public static final String KEY_POSITION_LIGHT = "positionlight";//定位卡指示灯
    public static final String KEY_POSITION_BUZZER = "positionbuzzer";//定位卡蜂鸣器
    public static final String KEY_FLASH_CUSTOM = "positionbuzzer";//自定义手电筒

    public static App getInstance() {
        return instance;
    }

    public static String model;
    //    获取设备型号
    public static String getModel(){
        model = android.os.SystemProperties.get("ro.build.developer");
        if (model == null || "".equals(model)){
            model = Build.MODEL;
        }
        return model;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        NetUtil.init(this);
//        HttpModel.getInstance().init(this);
        ToastUtils.init(this, new Handler());

        MultiDex.install(this);
    }


    /**
     * 异常退出处理
     */
    @Override
    protected void onAppExceptionDestroy() {

    }

}
