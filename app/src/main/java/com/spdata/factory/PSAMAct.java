package com.spdata.factory;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
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
import common.utils.CardOperation;

@EActivity(R.layout.activity_psam)
public class PSAMAct extends FragActBase {
    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    TextView tvInfor;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;

    @Click
    void btnNotPass() {
        setXml(App.KEY_PSAM, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btnPass() {
        setXml(App.KEY_PSAM, App.KEY_FINISH);
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
        }, "SIM测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }


    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        CardOperation cardOperation = new CardOperation(this);
        byte[] psam = cardOperation.activePSAM();
        if (psam == null) {
            showToast("NO PSAM");
            setXml(App.KEY_PSAM, App.KEY_UNFINISH);
            finish();
        }else {
            showToast("PSAM 测试成功");
            setXml(App.KEY_PSAM, App.KEY_FINISH);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
