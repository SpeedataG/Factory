package com.spdata.factory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gpio.MainActivity;
import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;
import com.speedata.libuhf.utils.SharedXmlUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import common.base.act.FragActBase;
import common.utils.MsgEvent;
import common.utils.SearchTagDialog;

/**
 * Created by suntianwei on 2016/12/6.
 */
public class UhfAct extends FragActBase implements View.OnClickListener {
    private CustomTitlebar titlebar;
    private TextView tvGreen;
    private TextView tvRed;
    /**
     * 盘点测试
     */
    private Button pandian;

    /**
     * 成功
     */
    private Button btnPass;
    /**
     * 失败
     */
    private Button btnNotPass;

    private IUHFService iuhfService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_uhf);
        initView();
        initTitlebar();
        UHFManager.setStipulationLevel(0);
        try {
            iuhfService = UHFManager.getUHFService(UhfAct.this);
        } catch (Exception e) {
            e.printStackTrace();
            boolean cn = getApplicationContext().getResources().getConfiguration().locale.getCountry().equals("CN");
            if (cn) {
                tvRed.setText(getResources().getString(R.string.uhf_nonexist));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.uhf_nonexist), Toast.LENGTH_SHORT).show();
            } else {
                tvRed.setText(getResources().getString(R.string.uhf_nonexist));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.uhf_nonexist), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        String modle = SharedXmlUtil.getInstance(UhfAct.this).read("modle", "");
        tvGreen.setText(getResources().getString(R.string.uhf_model) + modle);
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_uhf));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mEventBus(MsgEvent msgEvent) {
        String type = msgEvent.getType();
        String msg = (String) msgEvent.getMsg();
        if ("failed".equals(type)) {
            tvRed.setVisibility(View.VISIBLE);
            tvRed.setText(msg + " ");
        } else if ("success".equals(type)) {
            tvGreen.setVisibility(View.VISIBLE);
            tvGreen.setText(msg + " ");
        }
    }


    private PowerManager pM = null;
    private PowerManager.WakeLock wK = null;
    private int init_progress = 0;

    @SuppressLint("InvalidWakeLockTag")
    private void newWakeLock() {
        init_progress++;
        pM = (PowerManager) getSystemService(POWER_SERVICE);
        if (pM != null) {
            wK = pM.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, "lock3992");
            if (wK != null) {
                wK.acquire();
                init_progress++;
            }
        }

        if (init_progress == 1) {
            Log.w("3992_6C", "wake lock init failed");
        }
    }

    private boolean openDev() {
        if (iuhfService.openDev() != 0) {
            new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.uhf_msg_title)).setMessage(getResources().getString(R.string.uhf_msg)).setPositiveButton(getResources().getString(R.string.uhf_msg_ok), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).show();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        UHFManager.closeUHFService();
        try {
            UHFManager.closeUHFService();
            wK.release();
            EventBus.getDefault().unregister(this);
            iuhfService = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (openDev()) return;
        } catch (Exception e) {
            e.printStackTrace();
            tvRed.setVisibility(View.VISIBLE);
            pandian.setEnabled(false);
            btnPass.setVisibility(View.GONE);
            tvRed.setText(getResources().getString(R.string.uhf_msg_error));
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            iuhfService.closeDev();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvGreen = (TextView) findViewById(R.id.tvGreen);
        tvRed = (TextView) findViewById(R.id.tvRed);
        pandian = (Button) findViewById(R.id.pandian);
        pandian.setOnClickListener(this);
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
            case R.id.pandian:
                SearchTagDialog searchTag = new SearchTagDialog(this, iuhfService, "");
                searchTag.show();
                break;

            case R.id.btn_read_card:
                break;
            case R.id.btn_pass:
                setXml(App.KEY_UHF, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_UHF, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
