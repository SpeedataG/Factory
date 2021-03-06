package com.spdata.factory;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

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

/**
 * Created by lenovo_pc on 2016/9/3.
 */
public class MaglevAct extends FragActBase implements View.OnClickListener {


    private CustomTitlebar titlebar;
    private TextView tvVersionInfor;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_maglev) );
    }


    private int count = 0;
    private Timer timer;
    private remindTask task;
    private String streamToString;
    private String battVoltFile;
    private String battTempFile;
    private static final String CHARGER_CURRENT_NOW =
            "/sys/class/power_supply/battery/BatteryAverageCurrent";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_magle);
        initView();
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

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvVersionInfor = (TextView) findViewById(R.id.tv_version_infor);
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
                setXml(App.KEY_MAGLEV, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_MAGLEV, App.KEY_UNFINISH);
                finish();
                break;
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
                            tvVersionInfor.setText(getResources().getString(R.string.ChangeAct_full) );
                            break;
                        case 2:
                            first();
                            tvVersionInfor.append(getResources().getString(R.string.ChangeAct_dian_v) + Integer.parseInt(battVoltFile) / 1000.0 + "V");
                            tvVersionInfor.append(getResources().getString(R.string.ChangeAct_dian_c) + Integer.parseInt(battTempFile) / 10.0 + "℃");
                            try {
                                tvVersionInfor.append(getResources().getString(R.string.ChangeAct_dian_i) + readCurrentFile(new File(CHARGER_CURRENT_NOW)) + " mA");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            titlebar.setAttrs(getResources().getString(R.string.ChangeAct_state5));
                            break;
                        case 3:
                            tvVersionInfor.setText(getResources().getString(R.string.ChangeAct_state7));
                            titlebar.setAttrs(getResources().getString(R.string.ChangeAct_state8));
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
                tvVersionInfor.setText(getResources().getString(R.string.ChangeAct_state3));
            } else if (AC == 48) {
                tvVersionInfor.setText(getResources().getString(R.string.ChangeAct_state4));
            }

            InputStream usb = new FileInputStream("sys/class/power_supply/usb/online");
            InputStreamReader inputStreamReaderUsb = new InputStreamReader(usb);
            int USB = inputStreamReaderUsb.read();
            inputStreamReaderUsb.close();
            if (USB == 49) {
                tvVersionInfor.append(getResources().getString(R.string.ChangeAct_state9));
            } else if (USB == 48) {
                tvVersionInfor.append(getResources().getString(R.string.ChangeAct_state8));
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
