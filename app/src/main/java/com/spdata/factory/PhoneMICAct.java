package com.spdata.factory;

import android.app.Service;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import common.base.act.FragActBase;

/**
 * Created by xu on 2016/7/26.
 */

/**
 * 该实例中，我们使用AudioRecord类来完成我们的音频录制程序 AudioRecord类，我们可以使用三种不同的read方法来完成录制工作，
 * 每种方法都有其实用的场合 一、实例化一个AudioRecord类我们需要传入几种参数
 * 1、AudioSource：这里可以是MediaRecorder.AudioSource.MIC
 * 2、SampleRateInHz:录制频率，可以为8000hz或者11025hz等，不同的硬件设备这个值不同
 * 3、ChannelConfig:录制通道，可以为udioFormat
 * .CHANNEL_CONFIGURATION_MONO和udioFormat.CHANNEL_CONFIGURATION_STEREO
 * 4、AudioFormat :录制编码格式，可以为AudioFormat.ENCODING_16BIT和BIT,其中16BIT的仿真性比8BIT好，
 * 但是需要消耗更多的电量和存储空间 5、BufferSize:录制缓冲大小：可以通过getMinBufferSize来获取
 * 这样我们就可以实例化一个AudioRecord对象了 二、创建一个文件，用于保存录制的内容 同上篇 三、打开一个输出流，指向创建的文件
 * DataOutputStream dos = new DataOutputStream(new BufferedOutputSream(new
 * FileOutputStream(file))) 四、现在就可以开始录制了，我们需要创建一个字节数组来存储从udioRecorder中返回的音频数据，但是
 * 注意，我们定义的数组要小于定义AudioRecord时指定的那个ufferSize short[]buffer = new
 * short[BufferSize/4]; startRecording(); 然后一个循环，调用AudioRecord的read方法实现读取
 * 另外使用MediaPlayer是无法播放使用AudioRecord录制的音频的，为了实现播放，我们需要 使用AudioTrack类来实现
 * AudioTrack类允许我们播放原始的音频数据
 * <p>
 * <p>
 * 一、实例化一个AudioTrack同样要传入几个参数 1、StreamType:在AudioManager中有几个常量，其中一个是TREAM_MUSIC;
 * 2、SampleRateInHz：最好和AudioRecord使用的是同一个值 3、ChannelConfig：同上 4、AudioFormat：同上
 * 5、BufferSize：通过AudioTrack的静态方法getMinBufferSize来获取
 * 6、Mode：可以是AudioTrack.MODE_STREAM和MODE_STATIC，关于这两种不同之处，可以查阅文档
 * 二、打开一个输入流，指向刚刚录制内容保存的文件，然后开始播放，边读取边播放
 * <p>
 * 实现时，音频的录制和播放分别使用两个AsyncTask来完成
 */

public class PhoneMICAct extends FragActBase implements View.OnClickListener {


    private RecordTask recorder;
    private PlayTask player;
    private File fpath;
    private File audioFile;
    private boolean isRecording = true, isPlaying = false; // 标记
    private int frequence = 16000;// 8000;
    // //录制频率，单位hz.这里的值注意了，写的不好，可能实例化AudioRecord对象的时候，会出错。我开始写成11025就不行。这取决于硬件设备
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private boolean isPlay = false;
    private boolean isStart = false;
    private CustomTitlebar titlebar;
    private TextView tvInfor;
    /**
     * 开始录音
     */
    private Button btnSoundRecording;
    /**
     * 播放录音
     */
    private Button btnPlay;
    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    //存储计时
    private int numTime;
    //存储倒计时
    private int reTime;


    @Override
    protected void initTitlebar() {

        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.menu_phone_mic), null);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_earmic);
        initView();
        initTitlebar();
        setSwipeEnable(false);
        AudioManager audioManager = (AudioManager) this.getSystemService(Service.AUDIO_SERVICE);
        int max = audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                max, 0);
        btnPlay.setOnClickListener(this);
        btnPlay.setEnabled(false);
        tvInfor.setText(getResources().getString(R.string.sound_phonemsg));
        btnSoundRecording.setText(getResources().getString(R.string.sound_start_record));
        btnSoundRecording.setOnClickListener(this);
        btnPlay.setText(getResources().getString(R.string.sound_play));

//        registerHeadsetPlugReceiver();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))// 手机有SD卡的情况
        {
            // 在这里我们创建一个文件，用于保存录制内容
            fpath = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/data/files/");
            fpath.mkdirs();// 创建文件夹
        } else// 手机无SD卡的情况
        {
            fpath = this.getCacheDir();
        }

        try {
            // 创建临时文件,注意这里的格式为.pcm
            audioFile = File.createTempFile("recording", ".pcm", fpath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fpath = null;
        audioFile = null;
        if (recorder != null) {
            recorder.cancel(true);
            recorder = null;
        }
        if (player != null) {
            player.cancel(true);
            player = null;
        }
    }

    @Override
    protected void onPause() {
        //如果异步任务不为空 并且状态是 运行时  ，就把他取消这个加载任务
        if (player != null && player.getStatus() == AsyncTask.Status.RUNNING) {
            player.cancel(true);

        }
        super.onPause();
    }

    //添加计时
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            numTime++;
            tvInfor.setText(getResources().getString(R.string.sound_record_time) + numTime + "s");
            handler.postDelayed(this, 1000);
        }
    };
    //添加倒计时
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            reTime--;
            tvInfor.setText(getResources().getString(R.string.sound_remaining_time) + reTime + "s");
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onClick(View v) {
        if (v == btnSoundRecording) {
            if (!isStart) {
                recorder = new RecordTask();
                recorder.execute();//执行计时
                isStart = true;
                btnSoundRecording.setText(getResources().getString(
                        R.string.sound_stop_record));
                numTime = 0;
                tvInfor.setText(getResources().getString(R.string.sound_record_time) + numTime + "s");
            } else {
                this.isRecording = false;
                btnSoundRecording.setText(getResources().getString(
                        R.string.sound_start_record));
                isStart = false;
            }

        } else if (v == btnPlay) {
            btnNotPass.setVisibility(View.VISIBLE);
            btnPass.setVisibility(View.VISIBLE);
            if (!isPlay) {
                player = new PlayTask();
                btnPlay.setText(getResources().getString(
                        R.string.sound_play_stop));
                player.execute();
                isPlay = true;
                reTime = numTime;
                tvInfor.setText(getResources().getString(R.string.sound_remaining_time) + reTime + "s");
            } else {
                isPlay = false;
                btnPlay.setText(getResources().getString(R.string.sound_play));
                this.isPlaying = false;
                handler.removeCallbacks(runnable2);
            }
        } else if (v == btnNotPass) {
            setXml(App.KEY_PHONE_MIC, App.KEY_UNFINISH);
            finish();

        } else if (v == btnPass) {
            setXml(App.KEY_PHONE_MIC, App.KEY_FINISH);
            finish();
        }

    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
        btnSoundRecording = (Button) findViewById(R.id.btn_sound_recording);
        btnSoundRecording.setOnClickListener(this);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
        btnPass = (Button) findViewById(R.id.btn_pass);
        btnPass.setOnClickListener(this);
        btnNotPass = (Button) findViewById(R.id.btn_not_pass);
        btnNotPass.setOnClickListener(this);
    }


    class RecordTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            isRecording = true;
            if (isCancelled()) {
                return null;
            }
            try {
                // 开通输出流到指定的文件
                DataOutputStream dos = new DataOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(audioFile)));
                // 根据定义好的几个配置，来获取合适的缓冲大小
                int bufferSize = AudioRecord.getMinBufferSize(frequence,
                        channelConfig, audioEncoding);
                AudioRecord record = null;
                if (Build.MODEL.equals("spda6735") || Build.MODEL.equals("DCD3")) {
                    // 实例化AudioRecord
                    record = new AudioRecord(
                            5, frequence,
                            channelConfig, audioEncoding, bufferSize);
                } else {
                    // 实例化AudioRecord
                    record = new AudioRecord(
                            MediaRecorder.AudioSource.MIC, frequence,
                            channelConfig, audioEncoding, bufferSize);
                }

                // 定义缓冲
                short[] buffer = new short[bufferSize];

                // 开始录制
                record.startRecording();
                handler.postDelayed(runnable, 0);
                int r = 0; // 存储录制进度
                // 定义循环，根据isRecording的值来判断是否继续录制
                while (isRecording) {
                    // 从bufferSize中读取字节，返回读取的short个数
                    // 这里老是出现buffer overflow，不知道是什么原因，试了好几个值，都没用，TODO：待解决
                    int bufferReadResult = record
                            .read(buffer, 0, buffer.length);
                    // 循环将buffer中的音频数据写入到OutputStream中
                    for (int i = 0; i < bufferReadResult; i++) {
                        dos.writeShort(buffer[i]);
                    }
                    publishProgress(new Integer(r)); // 向UI线程报告当前进度
                    r++; // 自增进度值
                }
                // 录制结束
                record.stop();
                handler.removeCallbacks(runnable);
                Log.v("The DOS available:", "::" + audioFile.length());
                dos.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        // 当在上面方法中调用publishProgress时，该方法触发,该方法在I线程中被执行
        @Override
        protected void onProgressUpdate(Integer... progress) {
//            tvInfor.setText(progress[0].toString());
        }

        @Override
        protected void onPostExecute(Void result) {
            btnPlay.setEnabled(true);
        }

        @Override
        protected void onPreExecute() {
            btnPlay.setEnabled(false);
        }
    }

    class PlayTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            if (isCancelled()) {
                return null;
            }
            isPlaying = true;
            int bufferSize = AudioTrack.getMinBufferSize(frequence,
                    channelConfig, audioEncoding);
            short[] buffer = new short[bufferSize / 4];
            try {
                // 定义输入流，将音频写入到AudioTrack类中，实现播放
                DataInputStream dis = new DataInputStream(
                        new BufferedInputStream(new FileInputStream(audioFile)));
                // 实例AudioTrack
                AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,
                        frequence, channelConfig, audioEncoding, bufferSize,
                        AudioTrack.MODE_STREAM);
                // 开始播放
                track.play();
                //循环写入似乎比循环播放时间稍长，为了计时器能减到0，所以将第一次提前开始计时
                handler.postDelayed(runnable2, 0);
                // 由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
                while (isPlaying && dis.available() > 0) {
                    int i = 0;
                    while (dis.available() > 0 && i < buffer.length) {
                        buffer[i] = dis.readShort();
                        i++;
                    }
                    // 然后将数据写入到AudioTrack中
                    track.write(buffer, 0, buffer.length);
                }

                // 播放结束
                track.stop();
                handler.removeCallbacks(runnable2);
                dis.close();

            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            isPlay = false;
            btnPlay.setText(getResources().getString(R.string.sound_play));
            btnSoundRecording.setEnabled(true);
        }

        @Override
        protected void onPreExecute() {
            btnSoundRecording.setEnabled(false);
        }
    }

}
