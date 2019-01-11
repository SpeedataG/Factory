package com.spdata.factory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;
import common.utils.SharedXmlUtil;

/**
 * Created by 42040 on 2018/11/2.
 */
public class ResetAct extends FragActBase implements View.OnClickListener {


    private CustomTitlebar titlebar;
    /**
     * 清除测试记录
     */
    private Button tvInfor;
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
        }, getResources().getString(R.string.menu_reset), null);
    }


    boolean is = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_reset_layout);
        initView();
        initTitlebar();
        setSwipeEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        tvInfor = (Button) findViewById(R.id.tv_infor);
        tvInfor.setOnClickListener(this);
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
            case R.id.tv_infor:
                SharedXmlUtil.getInstance(this).clearAll();
                showToast(getResources().getString(R.string.reste_msg));
                finish();
                break;
            case R.id.btn_pass:
                finish();
                break;
            case R.id.btn_not_pass:
                finish();
                break;
        }
    }
}
