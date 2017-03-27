package com.spdata.factory;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.abc.TVS.TVSHwAPI;
import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import common.base.act.FragActBase;
import common.event.ViewMessage;

/**
 * Created by suntianwei on 2016/12/6.
 */
@EActivity(R.layout.act_uhf)
public class UhfAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_read_card;

    @Click
    void btnPass() {
        setXml(App.KEY_UHF, App.KEY_FINISH);
        finish();
    }

    @Click
    void btnNotPass() {
        setXml(App.KEY_UHF, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btn_read_card() {
        if (TVSHwAPI.OpenDev() && TVSHwAPI.getTID(30, 10000)) {
            tvInfor.append(TVSHwAPI.TID + "");
            Log.e("abc", TVSHwAPI.TID);
        }
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("超高频UHF");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }

    private Timer timer;
    private static final int TIME_TO_READDATA = 500;
    ReadTimerTask readTimerTask;

    @AfterViews
    protected void main() {
        initTitlebar();
        initSoundPool();//初始化声音池
        timer = new Timer();
        readTimerTask = new ReadTimerTask();
        timer.schedule(readTimerTask, 10, TIME_TO_READDATA);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String tids = (String) msg.obj;
            if (!tids.equals("")) {
                play(2, 0);
                tvInfor.append("Card ID:" + tids + "\n");
            }
        }
    };

    private class ReadTimerTask extends TimerTask {
        @Override
        public void run() {
            if (TVSHwAPI.OpenDev() && TVSHwAPI.getTID(30, 10000)) {
                Log.e("abc", TVSHwAPI.TID);
                String tid = TVSHwAPI.TID;
                if (!tid.equals("")) {
                    Message msg = new Message();
                    msg.obj = tid;
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
     * 播放声音池的声音
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
        timer.cancel();
        if (sp != null) {
            sp.release();
        }
        readTimerTask.cancel();
        super.onDestroy();
    }
}
