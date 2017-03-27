package com.spdata.factory;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.event.ViewMessage;

/**
 * Created by lenovo_pc on 2016/9/3.
 */
@EActivity(R.layout.act_magle)
public class MaglevAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvVersionInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_MAGLEV, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_MAGLEV, App.KEY_FINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("磁吸附充电");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    private int count = 0;
    private Timer timer;
    private remindTask task;
    private String streamToString;
    private String battVoltFile;
    private String battTempFile;
    private static final String CHARGER_CURRENT_NOW =
            "/sys/class/power_supply/battery/BatteryAverageCurrent";
    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        task = new remindTask();
        remind(task);
    }

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
                            tvVersionInfor.setText("电池状态：Full(已满)，换块电池重新试下");
                            break;
                        case 2:
                            first();
                            tvVersionInfor.append("\n电池电压：" +Integer.parseInt(battVoltFile)/1000.0+"V" );
                            tvVersionInfor.append("\n电池温度：" + Integer.parseInt(battTempFile)/10.0+"℃");
                            try {
                                tvVersionInfor.append("\n充电电流：" +readCurrentFile(new File(CHARGER_CURRENT_NOW))+" mA");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            titlebar.setAttrs("正在充电");
                            break;
                        case 3:
                            tvVersionInfor.setText("电池未充电，请连接磁吸附充电线！");
                            titlebar.setAttrs("磁吸附充电");
                            break;
                        case 0:
                            break;
                    }
                }
            });
        }
    }

    private void remind(TimerTask task) {
        timer = new Timer();
        timer.schedule(task, 600);
    }

    public void finishTimer() {
        timer.cancel();
        task.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        task = new remindTask();
        remind(task);
    }

    private void first() {
        try {
            InputStream ac = new FileInputStream("sys/class/power_supply/ac/online");
            InputStreamReader inputStreamReader = new InputStreamReader(ac);
            int AC = inputStreamReader.read();
            inputStreamReader.close();
            if (AC == 49) {
                tvVersionInfor.setText("\n直流电源充电");
            } else if (AC == 48) {
                tvVersionInfor.setText("\n非直流电源充电");
            }

            InputStream usb = new FileInputStream("sys/class/power_supply/usb/online");
            InputStreamReader inputStreamReaderUsb = new InputStreamReader(usb);
            int USB = inputStreamReaderUsb.read();
            inputStreamReaderUsb.close();
            if (USB == 49) {
                tvVersionInfor.append("\nUSB充电,请接入磁吸附充电器");
            } else if (USB == 48) {
                tvVersionInfor.append("\n磁吸附充电");
            }
        } catch (IOException e) {
            e.printStackTrace();
            tvVersionInfor.append(e.getMessage());
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
}
