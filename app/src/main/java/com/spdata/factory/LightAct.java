package com.spdata.factory;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
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

@EActivity(R.layout.act_light)
public class LightAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    SeekBar searchBar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_LIGHT, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_LIGHT, App.KEY_FINISH);
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
        }, "屏幕亮度测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    int Max_Brightness = 100;
    float fBrightness = 0.0f;
    WindowManager.LayoutParams lp = null;

    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        int normal = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 255);
        // 进度条绑定当前亮度
        searchBar.setProgress(normal);
        searchBar.setOnSeekBarChangeListener(seekListener);
        searchBar.setMax(254);
        lp = getWindow().getAttributes();

    }

    SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            changeAppBrightness(mContext, progress);
        }
    };

    // 根据亮度值修改当前window亮度
    public void changeAppBrightness(Context context, int brightness) {
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
        Settings.System.putInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, brightness);
    }

}
