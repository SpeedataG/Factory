package com.spdata.factory;

import android.app.smallscreen.SmallScreenManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.UnsupportedEncodingException;

import common.base.act.FragActBase;

public class SmallScreenAct extends FragActBase implements View.OnClickListener {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private SmallScreenManager mSmallScreenManager;
    private CustomTitlebar titlebar;
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
        }, "小屏幕测试", null);
    }

    private void Test() {
        if (mSmallScreenManager == null) {
            tvInfor.setText("启动服务失败");
            return;
        }
//        try {
//            smallScreenManager.writeBuffer(12, 0);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            tvInfor.setText("写入失败");
//            return;
//        }
//        try {
//            mSmallScreenManager.clearScreen();
//            mSmallScreenManager.writeAsciiBuffer("SPEEDATA".getBytes("gb2312"),4);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        try {
            mSmallScreenManager.writeGb2312Buffer("你好思必拓".getBytes("gb2312"), 10);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            tvInfor.setText("写入失败");
        }


        tvInfor.setTextSize(30);
        tvInfor.setText("请确认右上角小屏幕时间是否显示 你好思必拓");
    }

    private static final String SMALL_SCREEN = "smallscreen";

    private void initService() {
        //获取系统服务
        mSmallScreenManager = (SmallScreenManager)
                mContext.getSystemService(SMALL_SCREEN);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mSmallScreenManager != null) {
//            //判断系统设置中是否勾选显示时钟
//            String isCheck = SystemProperties.get("persist.sys.smallscreen");
//            if (isCheck.equals("true")) {
//                try {
//                    mSmallScreenManager.startClock();
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_small_screen);
        initView();
        initTitlebar();
        initService();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Test();
            }
        }, 0);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SmallScreenAct Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
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
                setXml(App.KEY_SMALL_SCREEN, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_SMALL_SCREEN, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
