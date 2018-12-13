package com.spdata.factory;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.SDUtils;

@EActivity(R.layout.activity_change)
public class ChangeAct extends FragActBase {

    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_next;
    @ViewById
    Button btn_second;
    private String streamToString;
    private String battVoltFile = "";
    private String battTempFile;

    @Click
    void btnNotPass() {
        setXml(App.KEY_CHANGE, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_CHANGE, App.KEY_FINISH);
        finish();
    }

    @Click
    void btn_next() {
        first();
    }

    @Click
    void btn_second() {
    }


    private final int INPIT = 0;
    private final int OUT = 1;

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, "有线充电测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishTimer();
    }

    @Override
    protected void onPause() {
        finishTimer();
        super.onPause();
    }

    private int count = 0;

    private Timer timer;
    private remindTask task;

    private void yesorno() {
        try {
            InputStream inputStream = new FileInputStream("sys/class/power_supply/battery/status");
            streamToString = convertStreamToString(inputStream);
            streamToString = streamToString.replace("\n", "");
            InputStream batt_volt_file = new FileInputStream("sys/class/power_supply/battery/batt_vol");
            battVoltFile = convertStreamToString(batt_volt_file);
            battVoltFile = battVoltFile.replace("\n", "");
            InputStream batt_temp_file = new FileInputStream("sys/class/power_supply/battery/batt_temp");
            battTempFile = convertStreamToString(batt_temp_file);
            battTempFile = battTempFile.replace("\n", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (streamToString.equals("Full")) {
            count = 1;
        } else if (streamToString.equals("Charging")) {
            count = 2;
        } else {
            count = 3;
        }
    }

    private void init() {
        task = new remindTask();
        remind(task);
    }

    private static final String CHARGER_CURRENT_NOW =
            "/sys/class/power_supply/battery/BatteryAverageCurrent";

    private class remindTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    yesorno();
                    remindTask task = new remindTask();
                    remind(task);
                    Log.i("sss", count + "");
                    switch (count) {
                        case 1:
                            tvInfor.setText("电池状态：Full(已满)，换块电池重新试下");
                            break;
                        case 2:
                            first();
                            if (Build.MODEL.equals("SD55") || Build.MODEL.equals("SD60")) {
                                tvInfor.append("\n电池电压：" + Integer.parseInt(battVoltFile) / 1000000.0 + "V");
                            }else {
                                tvInfor.append("\n电池电压：" + Integer.parseInt(battVoltFile) / 1000.0 + "V");

                            }
                            tvInfor.append("\n电池温度：" + Integer.parseInt(battTempFile) / 10.0 + "℃");
//                            try {
//                                tvInfor.append("\n充电电流：" + readCurrentFile(new File(CHARGER_CURRENT_NOW)) + " mA");
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            tvInfor.append("\n充电电流：" + bufferRead() + " mA");
                            titlebar.setAttrs("正在充电");
                            break;
                        case 3:
                            tvInfor.setText("电池未充电，请连接充电器！");
                            titlebar.setAttrs("有线充电测试");
                            break;
                        case 0:

                            break;
                        default:
                            break;

                    }
                }
            });
        }
    }

    private void remind(TimerTask task) {
        timer = new Timer();
        timer.schedule(task, 100);
    }

    public void finishTimer() {
        timer.cancel();
        task.cancel();
    }

    private void first() {
        try {
            InputStream ac = new FileInputStream("sys/class/power_supply/ac/online");
            InputStreamReader inputStreamReader = new InputStreamReader(ac);
            int AC = inputStreamReader.read();
            inputStreamReader.close();
            if (AC == 49) {
                tvInfor.setText("\n直流电源充电");
            } else if (AC == 48) {
                tvInfor.setText("\n非直流电源充电");
            }

            InputStream usb = new FileInputStream("sys/class/power_supply/usb/online");
            InputStreamReader inputStreamReaderUsb = new InputStreamReader(usb);
            int USB = inputStreamReaderUsb.read();
            inputStreamReaderUsb.close();
            if (USB == 49) {
                tvInfor.append("\nUSB充电");
            } else if (USB == 48) {
                tvInfor.append("\n非USB充电");
            }
        } catch (IOException e) {
            e.printStackTrace();
            tvInfor.append(e.getMessage());
        }
    }


    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String readCurrentFile(File file) throws IOException {
        InputStream input = new FileInputStream(file);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    input));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            input.close();
        }
    }

    //读取文件：这里需要做判断，如果没有这个文件会报错。
    public String bufferRead() {
        try {
            BufferedReader bfr;
            if (Build.MODEL.equals("SD55") || Build.MODEL.equals("SD60")) {
                bfr = new BufferedReader(new FileReader("/sys/class/power_supply/battery/current_now"));
            } else {
                bfr = new BufferedReader(new FileReader(CHARGER_CURRENT_NOW));
            }
            String line = bfr.readLine();
            StringBuilder sb = new StringBuilder();
            sb.append(line);
//            while (line != null) {
//                sb.append(line);
//                Log.d("buffer", "bufferRead: " + sb.toString());
////                sb.append("\n");
//                line = bfr.readLine();
//            }
            bfr.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
