package com.spdata.factory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import common.base.act.FragActBase;

/**
 * SK80调用系统相机
 */
public class CammerSystemSk80Act extends FragActBase implements View.OnClickListener {

    private CustomTitlebar titlebar;
    private Intent intent;
    private Button successBtn,failBtn;
    private ImageView mIvCameraImg;
    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_background_camera));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_camera_sk80);

        intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(intent,1);
        initView();

    }

    private void initView(){
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        successBtn = findViewById(R.id.btn_success);
        failBtn = findViewById(R.id.btn_fail);
        mIvCameraImg = findViewById(R.id.iv_cameraimg);
        successBtn.setOnClickListener(this);
        failBtn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            Bundle b = data.getExtras();
            Bitmap thumbBitmap = (Bitmap) b.get("data");
            mIvCameraImg.setImageBitmap(thumbBitmap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_success:
                setXml(App.KEY_CAMMAR_BACKGROUND, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_fail:
                setXml(App.KEY_CAMMAR_BACKGROUND, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
