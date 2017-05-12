package com.spdata.factory;

import android.content.Context;
import android.view.View;
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
    TextView btn_psam1;
    @ViewById
    TextView btn_psam2;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    private CardOperation cardOperation;
    private byte[] psam2;
    private byte[] psam1;

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

    @Click
    void btn_psam1() {
        psam1 = cardOperation.activeIC();
        if (psam1 == null) {
            tvInfor.append("No Psam1\n");
        }else {
            tvInfor.append("Psam1 Succeed \n");
        }
    }
    @Click
    void btn_psam2() {
        psam2 = cardOperation.activePSAM();
        if (psam2 == null) {
            tvInfor.append("No Psam2\n");
        }else {
            tvInfor.append("Psam2 Succeed \n");
        }
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
        }, "PSAM测试", null);
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }


    @AfterViews
    protected void main() {
        initTitlebar();
        setSwipeEnable(false);
        cardOperation = new CardOperation(this);

//        if (psam1==null&&psam2==null){
//            showToast("No Psam1 and Psam2");
//            setXml(App.KEY_PSAM, App.KEY_UNFINISH);
//            finish();
//        }else if (psam1==null){
//            showToast("No Psam1 ");
//            setXml(App.KEY_PSAM, App.KEY_UNFINISH);
//            finish();
//        }else if (psam2==null){
//            showToast("No Psam2 ");
//            setXml(App.KEY_PSAM, App.KEY_UNFINISH);
//            finish();
//        }else {
//            showToast("Psam1 Succeed and Psam2 Succeed ");
//            setXml(App.KEY_PSAM, App.KEY_UNFINISH);
//            finish();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
