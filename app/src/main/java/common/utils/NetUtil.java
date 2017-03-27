/*
 *
 * @author yandeqing
 * @created 2016.6.3
 * @email 18612205027@163.com
 * @version $version
 *
 */

package common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络请求相关参数类
 */
public class NetUtil {

    public static Context mContext;

    private NetUtil() {

    }

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * @return 是否为wifi连接，一般用于数据较大的下载或者更新时候， 使用该方法检测网络链接，并提示用户！
     */
    public static boolean checkWifi() {
        boolean isWifiConnect = false;
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // check the networkInfos numbers
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for (int i = 0; i < networkInfos.length; i++) {
            if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_MOBILE) {
                    isWifiConnect = false;
                }
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifiConnect = true;
                }
            }
        }
        return isWifiConnect;
    }

    /**
     * <hr>
     * http://www.oschina.net/question/54100_34632
     * </hr>
     *
     * @return 判断网络是否连接上了！
     */
    public static boolean isNetworkConnected() {
        /* 根据系统服务获取手机连接管理对象 */
        ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 判断当前网络是否连接
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

}
