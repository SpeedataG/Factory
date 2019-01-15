package com.spdata.factory;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;
import com.speedata.r6lib.IMifareManager;
import com.speedata.r6lib.R6Manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;

import static com.speedata.r6lib.R6Manager.CardType.MIFARE;

/**
 * Created by suntianwei on 2016/12/6.
 */
public class R6Act extends FragActBase implements View.OnClickListener {

    private CustomTitlebar titlebar;
    private TextView tvInfor;
    /**
     * 读卡
     */
    private Button btnReadCard;
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
        titlebar.setAttrs(getResources().getString(R.string.menu_rfid));
    }


    private IMifareManager dev1;
    private static byte[] RawID;
    private static String HID = null;
    private Timer timer;
    private static final int TIME_TO_READDATA = 500;
    ReadTimerTask readTimerTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sct_r6);
        initView();
        initTitlebar();
        initSoundPool();
        dev1 = R6Manager.getMifareInstance(MIFARE);
        timer = new Timer();
        readTimerTask = new ReadTimerTask();
        timer.schedule(readTimerTask, 10, TIME_TO_READDATA);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String hids = (String) msg.obj;
            if (!hids.equals("")) {
                play(2, 0);
            }
            tvInfor.append(getResources().getString(R.string.R6Act_info) + hids + "\n");
        }
    };

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
        btnReadCard = (Button) findViewById(R.id.btn_read_card);
        btnReadCard.setOnClickListener(this);
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
            case R.id.btn_read_card:
                break;
            case R.id.btn_pass:
                setXml(App.KEY_R6, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_R6, App.KEY_UNFINISH);
                finish();
                break;
        }
    }

    private class ReadTimerTask extends TimerTask {
        @Override
        public void run() {
            if (getHID1()) {
                Log.e("abc", HID);
                String hid = HID;
                if (!hid.equals("")) {
                    Message msg = new Message();
                    msg.obj = hid;
                    handler.sendMessage(msg);
                }
            }

        }
    }

    private SoundPool sp; //声音池
    private Map<Integer, Integer> mapSRC;

    //初始化声音池
    private void initSoundPool() {
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mapSRC = new HashMap<Integer, Integer>();
        mapSRC.put(1, sp.load(this, R.raw.error, 0));
        mapSRC.put(2, sp.load(this, R.raw.welcome, 0));

    }

    /**
     *
     */
    private void play(int sound, int number) {
        sp.play(mapSRC.get(sound),//播放的声音资源
                1.0f,//左声道，范围为0--1.0
                1.0f,//右声道，范围为0--1.0
                0, //优先级，0为最低优先级
                number,//循环次数,0为不循环
                0);//播放速率，0为正常速率
    }

    @Override
    public void onDestroy() {
        dev1.ReleaseDev();
        timer.cancel();
        if (sp != null) {
            sp.release();
        }
        readTimerTask.cancel();
        super.onDestroy();
    }

    private boolean getHID1() {
        if (dev1.InitDev() != 0) {
            return false;
        } else {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException var6) {
            }
            byte[] cd = dev1.SearchCard();
            if (cd == null) {
                dev1.ReleaseDev();
                return false;
            } else {
                RawID = cd;
                String IDString = new String("");
                byte[] var5 = cd;
                int var4 = cd.length;

                for (int var3 = 0; var3 < var4; ++var3) {
                    byte a = var5[var3];
                    IDString = IDString + String.format("%02X", new Object[]{Byte.valueOf(a)});
                }
                HID = IDString;
                dev1.ReleaseDev();
                return true;
            }
        }
    }


}
