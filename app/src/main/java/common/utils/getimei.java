package common.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class getimei {
    Context context;
    /**
     * 无卡或者获取失败返回000000
     * @param context
     * @return
     */
    @SuppressLint("UseValueOf")
    public static String getImsiAll (Context context) {
        String imsi = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.
                    getSystemService(Context.TELEPHONY_SERVICE);
            //普通方法获取
            imsi = tm.getSubscriberId();
            if (imsi==null || "".equals(imsi)) imsi = tm.getSimOperator();
            Class<?>[] resources = new Class<?>[] {int.class};
            Integer resourcesId = new Integer(1);
            if (imsi==null || "".equals(imsi)) {
                try {
                    //反射展讯
                    Method addMethod = tm.getClass().getDeclaredMethod("getSubscriberIdGemini", resources);
                    addMethod.setAccessible(true);
                    imsi = (String) addMethod.invoke(tm, resourcesId);
                } catch (Exception e) {
                    imsi = null;
                }
            }
            if (imsi==null || "".equals(imsi)) {
                try {
                    //反射mtk
                    Class<?> c = Class
                            .forName("com.android.internal.telephony.PhoneFactory");
                    Method m = c.getMethod("getServiceName", String.class, int.class);
                    String spreadTmService = (String) m.invoke(c, Context.TELEPHONY_SERVICE, 1);
                    TelephonyManager tm1 = (TelephonyManager) context.getSystemService(spreadTmService);
                    imsi = tm1.getSubscriberId();
                } catch (Exception e) {
                    imsi = null;
                }
            }
            if (imsi==null || "".equals(imsi)) {
                try {
                    //反射高通    这个没测过
                    Method addMethod2 = tm.getClass().getDeclaredMethod("getSimSerialNumber", resources);
                    addMethod2.setAccessible(true);
                    imsi = (String) addMethod2.invoke(tm, resourcesId);
                } catch (Exception e) {
                    imsi = null;
                }
            }
            if (imsi==null || "".equals(imsi)) {
                imsi = "000000";
            }
            return imsi;
        } catch (Exception e) {
            return "000000";
        }
    }
}