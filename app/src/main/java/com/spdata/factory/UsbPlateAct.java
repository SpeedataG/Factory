package com.spdata.factory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import common.base.act.FragActBase;
import common.event.ViewMessage;
import common.utils.DeviceControl;

/**
 * Created by lenovo_pc on 2016/9/3.
 */
@EActivity(R.layout.act_usbmlate)
public class UsbPlateAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvVersionInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private DeviceControl gpio;

    @Click
    void btnNotPass() {
        setXml(App.KEY_USBPLATE, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_USBPLATE, App.KEY_FINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("U盘");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }


    @AfterViews
    protected void main() {
        initTitlebar();


    }
   Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           String ss= (String) msg.obj;
           switch (msg.what){
               case 0:
                   tvVersionInfor.setText(ss);
                   break;
               case 1:
                   tvVersionInfor.setText(ss);
                   break;
               case 2:
                   tvVersionInfor.setText(ss);
                   setXml(App.KEY_USBPLATE, App.KEY_FINISH);
                   finish();
                   break;
           }
       }
   };
   private  BroadcastReceiver broadcastReceiver=new BroadcastReceiver(){
       @Override
       public void onReceive(Context context, Intent intent) {
           String action=intent.getAction();
           if (action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)){
               handler.sendMessage(handler.obtainMessage(0,"U盘以挂载,请拔出"));
           } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
              handler.sendMessage(handler.obtainMessage(1,"扫描U盘"));
           } else if (action.equals(Intent.ACTION_MEDIA_REMOVED)||action.equals(Intent.ACTION_MEDIA_EJECT)) {
               handler.sendMessage(handler.obtainMessage(2,"U盘以移除"));
           }
       }
   };


    @Override
    protected void onResume() {
        super.onResume();
        gpio = new DeviceControl("/sys/class/misc/mtgpio/pin");
        if (Build.MODEL.equals("M08")) {
            gpio.PowerOnDevice72();
        } else {
            gpio.PowerOffDevice63();
            gpio.PowerOnDevice99();
            gpio.PowerOnDevice98();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_CHECKING);//检查正在检查
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);//以挂载
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);//:表明SDCard 被卸载前己被移除
        filter.addDataScheme("file");
        registerReceiver(broadcastReceiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        if (Build.MODEL.equals("M08")) {
            gpio.PowerOffDevice72();
        } else {
            gpio.PowerOffDevice98();
            gpio.PowerOffDevice63();
            gpio.PowerOffDevice99();
        }
    }
}
