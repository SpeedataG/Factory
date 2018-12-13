package com.spdata.factory;

import android.content.Context;
import android.os.Build;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.event.ViewMessage;

@EActivity(R.layout.act_otg)
public class OTGAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_OTG, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
    }

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
        }, "OTG测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    private Timer timer;
    private remindTask task;
    private int len;

    @AfterViews
    protected void main() {
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
                            if (getApiVersion() >=23) {
                                tvInfor.setText("OTG模式\n");
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
                                    tvInfor.setText("读取中……");
                                } else {
                                    tvInfor.setText("OTG模式\n" + "已用空间：" + usedSize + "\n可用空间：" + availableSize);

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
                            tvInfor.setText("非OTG模式\n" + "请插入USB OTG并连接U盘");
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
    public static String getUSBStorage(Context context) {
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
        return "已用空间：" + usedSize + "\n可用空间：" + availableSize;//空间:已使用/可用的
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishTimer();
    }

    private void init() {
        tvInfor.setText("插入USB OTG转接的U盘\n");
        tvInfor.append("插入后点击下一步\n");
    }
}
