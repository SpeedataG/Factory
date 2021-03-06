package com.spdata.factory;

import android.app.Service;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.lang.reflect.Method;

import common.base.act.FragActBase;

/**
 * Created by xu on 2016/7/26.
 */

public class SpeakerAct extends FragActBase implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {


    private int max;
    private CustomTitlebar titlebar;
    private SeekBar searchBar;
    /**
     * 听筒正在播放音乐……
     */
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_speaker);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        btnPass.setVisibility(View.GONE);
        audioManager = (AudioManager) this.getSystemService(Service.AUDIO_SERVICE);
//5.0以下调用这个可用
//       audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);// 把模式调成听筒放音模式
//        audioManager.setRingerMode(AudioManager.MODE_IN_COMMUNICATION);
//        //这里的资源文件要求要小点的文件，要注意
        try {
            mMediaPlayer = MediaPlayer.create(SpeakerAct.this, R.raw.spectre);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        } catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        setSpeakerphoneOn(false);

        currentBell = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        searchBar.setProgress(currentBell);
        max = audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                max, 0);
        searchBar.setMax(max);
        searchBar.setOnSeekBarChangeListener(this);
        curSound = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnPass.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }


    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_spk), null);
    }

    AudioManager audioManager;
    MediaPlayer mMediaPlayer = null;
    int currentBell;
    int curSound;


    private void setSpeakerphoneOn(boolean on) {
        try {
            //播放音频流类型
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
//            audioManager.adjustStreamVolume (AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.ADJUST_LOWER);
            //获得当前类
            Class audioSystemClass = Class.forName("android.media.AudioSystem");
            //得到这个方法
            Method setForceUse = audioSystemClass.getMethod("setForceUse", int.class, int.class);
            if (on) {
                audioManager.setMicrophoneMute(false);
                audioManager.setSpeakerphoneOn(true);
                audioManager.setMode(AudioManager.MODE_NORMAL);
//				setForceUse.invoke(null, 1, 1);
            } else {//听筒
                audioManager.setSpeakerphoneOn(false);
                audioManager.setMode(AudioManager.MODE_NORMAL);
                setForceUse.invoke(null, 0, 0);
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                curSound, 0);
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            System.gc();
        }
        audioManager.setSpeakerphoneOn(false);//扬声器
        audioManager.setMode(AudioManager.MODE_NORMAL);
        super.onDestroy();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        currentBell = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        curSound = searchBar.getProgress();
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                curSound, 0);
        int result = currentBell - progress;
        System.out.println("---progress_music_result:" + result);
        if (result > 0) {
            for (int i = 0; i < result; i++) {
                audioManager.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND);
                int current = audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);
                setVolumeControlStream(AudioManager.STREAM_MUSIC);
                curSound = searchBar.getProgress();
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        curSound, 0);
                System.out.println("---progress_music:" + i
                        + " ---change_after=" + current);
            }
        } else {
            for (int i = 0; i > result; i--) {
                audioManager.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND);
                setVolumeControlStream(AudioManager.STREAM_MUSIC);
                curSound = searchBar.getProgress();
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        curSound, 0);
                int current = audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);
                System.out.println("---progress_music:" + progress
                        + " ---change_after=" + current);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        searchBar = (SeekBar) findViewById(R.id.search_bar);
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
                setXml(App.KEY_SPK, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_SPK, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
