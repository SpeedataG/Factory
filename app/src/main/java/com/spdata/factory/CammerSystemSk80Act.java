package com.spdata.factory;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import common.base.act.FragActBase;

/**
 * SK80调用系统相机
 */
public class CammerSystemSk80Act extends FragActBase implements View.OnClickListener {

    private CustomTitlebar titlebar;
    private Button successBtn, failBtn;
    private ImageView mIvCameraImg;
    private String mFilePath;
    private FileInputStream is;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_background_camera));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_camera_sk80);
        initData();
        initView();
    }

    private void initData() {
        mFilePath = Environment.getExternalStorageDirectory().getPath();
        mFilePath = mFilePath + "/" + "mytest.png";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            takePhotoBiggerThan7((new File(mFilePath)).getAbsolutePath());
        } else {
            Intent intent = new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri mUri = Uri.fromFile(new File(mFilePath));
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            startActivityForResult(intent, 1);
        }
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        successBtn = findViewById(R.id.btn_success);
        failBtn = findViewById(R.id.btn_fail);
        mIvCameraImg = findViewById(R.id.iv_cameraimg);
        successBtn.setOnClickListener(this);
        failBtn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 0){
                mIvCameraImg.setVisibility(View.INVISIBLE);
            }else {
                mIvCameraImg.setVisibility(View.VISIBLE);
                try {
                    is = new FileInputStream(mFilePath);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    mIvCameraImg.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    // 关闭流
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void takePhotoBiggerThan7(String absolutePath) {
        Uri mCameraTempUri;
        try {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put(MediaStore.Images.Media.DATA, absolutePath);
            mCameraTempUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if (mCameraTempUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTempUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            }
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            e.printStackTrace();
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
