package com.spdata.factory;

import android.content.Context;
import android.os.Bundle;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;

public class OTGAct extends FragActBase implements View.OnClickListener {

    private CustomTitlebar titlebar;
    /**  */
    private TextView tvInfor;
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
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_otg), null);
    }


    private Timer timer;
    private remindTask task;
    private int len;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_otg);
        initView();
        initTitlebar();
        setSwipeEnable(false);
//        init();
    }

    public static final String POWER_EXTERNAL = "/sys/class/misc/aw9523/gpio";

    @Override
    protected void onResume() {
        super.onResume();
        task = new remindTask();
        remind(task);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
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
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_OTG, App.KEY_UNFINISH);
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
                    try {
                        InputStream inputStream = new FileInputStream("sys/class/switch/otg_state/state");
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        len = inputStreamReader.read();
                        inputStreamReader.close();
                        if (len == 49) {
                            if (getApiVersion() >= 23) {
                                tvInfor.setText(getResources().getString(R.string.otg_msg));
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                setXml(App.KEY_OTG, App.KEY_FINISH);
                                                finish();
                                            }
                                        });
                                    }
                                }).start();
                            } else {
                                File path = new File("/storage/usbotg");
                                StatFs stat = new StatFs(path.getPath());
                                long blockSize = stat.getBlockSize();
                                long totalBlocks = stat.getBlockCount();
                                long availableBlocks = stat.getAvailableBlocks();
                                final String usedSize = Formatter.formatFileSize(mContext, (totalBlocks - availableBlocks) * blockSize);
                                final String availableSize = Formatter.formatFileSize(mContext, availableBlocks * blockSize);
//                            tvInfor.setText("OTG模式\n"+getUSBStorage(mContext));
                                if (usedSize.equals("0.00B") && availableSize.equals("0.00B")) {
                                    tvInfor.setText(getResources().getString(R.string.otg_msg2));
                                } else {
                                    tvInfor.setText(getResources().getString(R.string.otg_msg) + getResources().getString(R.string.otg_msg3) + usedSize + getResources().getString(R.string.otg_msg4) + availableSize);

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(4000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    setXml(App.KEY_OTG, App.KEY_FINISH);
                                                    finish();
                                                }
                                            });
                                        }
                                    }).start();
                                }
                            }
                        } else if (len == 48) {
                            tvInfor.setText(getResources().getString(R.string.otg_msg5));
                        } else {
                            tvInfor.append("bs0=" + len + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        tvInfor.append(e.getMessage());
                    }
                    task = new remindTask();
                    remind(task);


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

    //获得挂载的USB设备的存储空间使用情况
    public String getUSBStorage(Context context) {
        // USB Storage
        //storage/usbotg为USB设备在Android设备上的挂载路径.不同厂商的Android设备路径不同。
        //这样写同样适合于SD卡挂载。
        File path = new File("/storage/usbotg");
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();
        String usedSize = Formatter.formatFileSize(context, (totalBlocks - availableBlocks) * blockSize);
        String availableSize = Formatter.formatFileSize(context, availableBlocks * blockSize);
        return getResources().getString(R.string.otg_msg3) + usedSize + getResources().getString(R.string.otg_msg4) + availableSize;//空间:已使用/可用的
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishTimer();
    }

}
