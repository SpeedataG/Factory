package com.spdata.factory;

import android.os.Build;
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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;

public class ChangeAct extends FragActBase implements View.OnClickListener {


    private String streamToString;
    private String battVoltFile = "";
    private String battTempFile;
    private CustomTitlebar titlebar;
    private TextView tvInfor;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;
    /**
     * 下一步
     */
    private Button btnNext;
    /**
     * 下一步
     */
    private Button btnSecond;

    private final int INPIT = 0;
    private final int OUT = 1;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_charge), null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        initView();
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

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
        btnPass = (Button) findViewById(R.id.btn_pass);
        btnPass.setOnClickListener(this);
        btnNotPass = (Button) findViewById(R.id.btn_not_pass);
        btnNotPass.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        btnSecond = (Button) findViewById(R.id.btn_second);
        btnSecond.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_pass:
                setXml(App.KEY_CHANGE, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_CHANGE, App.KEY_UNFINISH);
                finish();
                break;
            case R.id.btn_next:
                first();
                break;
            case R.id.btn_second:
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
                            tvInfor.setText(getResources().getString(R.string.ChangeAct_full));
                            break;
                        case 2:
                            first();
                            if (Build.MODEL.equals("SD55") || Build.MODEL.equals("SD60")) {
                                tvInfor.append(getResources().getString(R.string.ChangeAct_dian_v) + Integer.parseInt(battVoltFile) / 1000000.0 + "V");
                            } else {
                                tvInfor.append(getResources().getString(R.string.ChangeAct_dian_v) + Integer.parseInt(battVoltFile) / 1000.0 + "V");

                            }
                            tvInfor.append(getResources().getString(R.string.ChangeAct_dian_c) + Integer.parseInt(battTempFile) / 10.0 + "℃");
//                            try {
//                                tvInfor.append("\n充电电流：" + readCurrentFile(new File(CHARGER_CURRENT_NOW)) + " mA");
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            tvInfor.append(getResources().getString(R.string.ChangeAct_dian_i) + bufferRead() + " mA");
                            titlebar.setAttrs(getResources().getString(R.string.ChangeAct_state5));
                            break;
                        case 3:
                            tvInfor.setText(getResources().getString(R.string.ChangeAct_state6));
                            titlebar.setAttrs(getResources().getString(R.string.ChangeAct_state5));
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
        timer.schedule(task, 50);
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
                tvInfor.setText(getResources().getString(R.string.ChangeAct_state3));
            } else if (AC == 48) {
                tvInfor.setText(getResources().getString(R.string.ChangeAct_state4));
            }

            InputStream usb = new FileInputStream("sys/class/power_supply/usb/online");
            InputStreamReader inputStreamReaderUsb = new InputStreamReader(usb);
            int USB = inputStreamReaderUsb.read();
            inputStreamReaderUsb.close();
            if (USB == 49) {
                tvInfor.append(getResources().getString(R.string.ChangeAct_state));
            } else if (USB == 48) {
                tvInfor.append(getResources().getString(R.string.ChangeAct_state2));
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
