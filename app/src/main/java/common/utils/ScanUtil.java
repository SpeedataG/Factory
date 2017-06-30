package common.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
import android.os.SystemProperties;

public class ScanUtil {

    public interface OnScanListener {
        public void getBarcode(String data);
    }

    private OnScanListener listener;

    //解码广播
    private String RECE_DATA_ACTION = "com.se4500.onDecodeComplete";
    //调用扫描广播
    private String START_SCAN_ACTION = "com.geomobile.se4500barcode";
    //停止扫描广播
    private String STOP_SCAN = "com.geomobile.se4500barcodestop";

    private Context context;
    private boolean isFlag = false;

    public ScanUtil(Context context) {
        this.context = context;
        // 注册系统广播 接受扫描到的数据
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(RECE_DATA_ACTION);
        context.registerReceiver(receiver, iFilter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context,
                              Intent intent) {
            String action = intent.getAction();
            if (action.equals(RECE_DATA_ACTION)) {
                String data = intent.getStringExtra("se4500");
                if (listener != null) {
                    data = data.replace("\n\r", "");
                    data = data.replace("\n", "");
                    data = data.replace("\r", "");
                    data = data.replace("\u0000", "");
                    listener.getBarcode(data);
                    if (isFlag) {
                        if (isFlag) {
                            cancelRepeat();
                            handler.postDelayed(startTask, 0);
                        } else {
                            cancelRepeat();
                        }
                    }
                }
            }
        }
    };

    /**
     * 获取条码监听
     *
     * @param listener
     */
    public void setOnScanListener(OnScanListener listener) {
        this.listener = listener;
    }

    public void repeatScans() {
        isFlag=true;
        handler.removeCallbacks(startTask);
        handler.postDelayed(startTask, 0);
    }
    public  void firstScan(){
        isFlag=false;
        startScan();
    }

    Handler handler = new Handler();
    private Runnable startTask = new Runnable() {
        @Override
        public void run() {
            startScan();
            handler.postDelayed(startTask, 3000);
        }
    };


    /**
     * 发送广播  调用系统扫描
     */
    private void startScan() {
        Intent intent = new Intent();
        intent.setAction(STOP_SCAN);
        context.sendBroadcast(intent);
        SystemProperties.set("persist.sys.scanstopimme", "true");
        SystemClock.sleep(20);
        SystemProperties.set("persist.sys.scanstopimme", "false");
        intent.setAction(START_SCAN_ACTION);
        context.sendBroadcast(intent, null);
    }

    public void cancelRepeat() {
        handler.removeCallbacks(startTask);
        Intent intent = new Intent();
        intent.setAction("com.geomobile.se4500barcodestop");
        context.sendBroadcast(intent);
        SystemProperties.set("persist.sys.scanstopimme", "true");
    }

    public void unScan() {
        cancelRepeat();
        context.unregisterReceiver(receiver);
    }

}
