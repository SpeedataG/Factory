package com.spdata.factory;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import common.base.act.FragActBase;
import common.event.ViewMessage;

/**
 * Created by xu on 2016/7/26.
 */

@EActivity(R.layout.act_bell)
public class BellAct extends FragActBase implements SeekBar.OnSeekBarChangeListener {

    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    SeekBar searchBar;
    AudioManager audioManager;
    int currentBell;
    int curSound;
    private static final String TAG = "AudioFxActivity";
    private static final float VISUALIZER_HEIGHT_DIP = 160f;
    private MediaPlayer mMediaPlayer;
    @Click
    void btnPass() {
        setXml(App.KEY_BELL, App.KEY_FINISH);
        finish();
    }

    @Click
    void btnNotPass() {
        setXml(App.KEY_BELL, App.KEY_UNFINISH);
        finish();
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
        }, "喇叭测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        btnPass.setVisibility(View.GONE);
        mMediaPlayer = MediaPlayer.create(this, R.raw.here);
        mMediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        setVolumeControlStream(AudioManager.STREAM_SYSTEM);
                    }
                });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mMediaPlayer.start();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentBell = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        searchBar.setProgress(currentBell);
        int max = audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        searchBar.setMax(max);
        searchBar.setOnSeekBarChangeListener(this);
        curSound = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
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
                        btnPass.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

}
