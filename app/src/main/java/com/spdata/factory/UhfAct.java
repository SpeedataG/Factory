package com.spdata.factory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;
import com.speedata.libuhf.utils.SharedXmlUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.MsgEvent;
import common.utils.SearchTagDialog;

/**
 * Created by suntianwei on 2016/12/6.
 */
@EActivity(R.layout.act_uhf)
public class UhfAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvGreen;
    @ViewById
    TextView tvRed;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button pandian;
    @ViewById
    Button read;


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
    void pandian() {
        SearchTagDialog searchTag = new SearchTagDialog(this, iuhfService, "");
        searchTag.show();
    }
    @Click
    void read() {
        String read_area = iuhfService.read_area(3, "0", "6", "0");
        if (read_area == null) {
            EventBus.getDefault().post(new MsgEvent("failed", "读失败"));
        } else {
            int length = read_area.length();
            if (length < 24) {
                EventBus.getDefault().post(new MsgEvent("failed", "读失败"));
            } else {
                EventBus.getDefault().post(new MsgEvent("success", "读成功"));
            }
        }

        int writeArea = iuhfService.write_area(3, "0", "0", "6", read_area);
        if (writeArea != 0) {
            EventBus.getDefault().post(new MsgEvent("failed", "写失败"));
        } else {
            EventBus.getDefault().post(new MsgEvent("success", "写成功"));
        }
    }

    private IUHFService iuhfService;

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


    @AfterViews
    protected void main() {
        initTitlebar();
        SharedXmlUtil.getInstance(UhfAct.this).write("modle", "");
        try {
            iuhfService = UHFManager.getUHFService(UhfAct.this);
            String s=SharedXmlUtil.getInstance(UhfAct.this).read("modle","");
            if (s.equals("3992")){
                tvRed.setVisibility(View.VISIBLE);
                tvRed.setText("*无uhf模块*");
            }
            newWakeLock();
            org.greenrobot.eventbus.EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
            tvRed.setVisibility(View.VISIBLE);
            tvRed.setText("*模块不识别*");
        }

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
        if (iuhfService.OpenDev() != 0) {
            new AlertDialog.Builder(this).setTitle("警告！").setMessage("上电失败").setPositiveButton("确定", new DialogInterface.OnClickListener() {

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
            tvRed.setText("*模块不识别*");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            iuhfService.CloseDev();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
