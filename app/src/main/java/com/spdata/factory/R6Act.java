package com.spdata.factory;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.abc.TVS.TVSHwAPI;
import com.android.hflibs.Mifare_native;
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
@EActivity(R.layout.sct_r6)
public class R6Act extends FragActBase {
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
        setXml(App.KEY_R6, App.KEY_FINISH);
        finish();
    }

    @Click
    void btnNotPass() {
        setXml(App.KEY_R6, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btn_read_card() {
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("高频RFID");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {

    }

    private Mifare_native dev = new Mifare_native();
    private Timer timer;
    private static final int TIME_TO_READDATA = 500;
    ReadTimerTask readTimerTask;
    @AfterViews
    protected void main() {
        initTitlebar();
        initSoundPool();
        timer = new Timer();
        readTimerTask = new ReadTimerTask();
        timer.schedule(readTimerTask, 10, TIME_TO_READDATA);
    }

    public void ReadRfid() {
        //初始化读卡设备
        if (dev.InitDev() != 0)////0 代表初始化设备成功，-1 代表失败
        {
            tvInfor.setText(R.string.msg_error_dev);//打开设备错误
            return;
        }

        int block = 5;
        //搜卡，处理冲突并选卡。返回选中卡片的 SN,搜卡失败，
        // 返回 null；成功返回 byte array 引用。其内容为卡片的 SN
        //search a valid card
        byte[] ID = dev.SearchCard();
        if (ID == null) {
            tvInfor.setText(R.string.msg_mifare_error_nocard);
            return;
        }
        String IDString = new String(" 0x");
        for (byte a : ID) {
            IDString += String.format("%02X", a);
        }
        tvInfor.setText(R.string.msg_mifare_ok_findcard);
        tvInfor.append(IDString);
        tvInfor.append("\n\n");

        //auth the block to read/write
        byte[] key = new byte[6];
        for (int i = 0; i < 6; i++) {
            key[i] = (byte) 0xff;
        }
        if (dev.AuthenticationCardByKey(Mifare_native.AUTH_TYPEA, ID, block, key) != 0) {
            tvInfor.append(getString(R.string.msg_mifare_error_auth));//身份验证块失败
            return;
        }
        tvInfor.append(getString(R.string.msg_mifare_ok_auth));//身份验证块好了
        tvInfor.append("\n\n");

        byte[] getdata = dev.ReadBlock(block);
        if (getdata == null) {
            tvInfor.append(getString(R.string.msg_mifare_error_readblock));
            return;
        }
        String getdataString = new String();
        for (byte i : getdata) {
            getdataString += String.format(" 0x%02x", i);
        }
        tvInfor.append(getString(R.string.msg_mifare_ok_readblock));
        tvInfor.append(getdataString);
        tvInfor.append("\n\n");


        //halt current card
        if (dev.HaltCard() != 0) {
            tvInfor.append(getString(R.string.msg_mifare_error_haltcard));
            return;
        }
        tvInfor.append(getString(R.string.msg_mifare_ok_haltcard));
        tvInfor.append("\n\n");

        tvInfor.append(getString(R.string.msg_mifare_ok_allok));
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String hids = (String) msg.obj;
            if (!hids.equals("")) {
                play(2,0);
            }
            tvInfor.append("Card ID:" + hids+"\n");
        }
    };
    private class ReadTimerTask extends TimerTask {
        @Override
        public void run() {
            if (TVSHwAPI.getHID()) {
                Log.e("abc", TVSHwAPI.HID);
                String hid = TVSHwAPI.HID;
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
//        dev.ReleaseDev();
        timer.cancel();
        if (sp != null) {
            sp.release();
        }
        readTimerTask.cancel();
        super.onDestroy();
    }
}
