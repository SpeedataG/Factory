package com.spdata.factory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import common.base.act.FragActBase;

import static android.R.attr.path;

/**
 * Created by xu on 2016/7/26.
 */

public class CammerFrontAct extends FragActBase implements SurfaceHolder.Callback, View.OnClickListener {

    private int count = 0;
    private int light = 0;
    private SurfaceView mySurfaceView;//surfaceView声明
    private SurfaceHolder holder;//surfaceHolder声明
    private Camera myCamera = null;//相机声明
    private String filePath = "/sdcard/qian.jpg";//照片保存路径
    boolean isClicked = false;//是否点击标识
    private int cammeraIndex;
    private CustomTitlebar titlebar;
    private SurfaceView camera;
    /**
     * 拍照
     */
    private Button btnPhoto;
    /**
     * 拍照
     */
    private Button btnPass;
    /**
     * 补光灯
     */
    private Button btnLight;
    /**
     * 失败
     */
    private Button btnNotPass;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_front_camera));
    }

    @Override
    protected void onStop() {
        super.onStop();
        SystemProperties.set("persist.sys.iscamera", "open");
        SystemProperties.set("persist.sys.scanstopimme", "false");
        Intent opencam = new Intent();
        opencam.setAction("com.se4500.closecamera");
        this.sendBroadcast(opencam, null);
//        if (SystemProperties.get("persist.sys.keyreport").equals("true")) {
//            if (SystemProperties.get("persist.sys.scanheadtype").equals("6603")) {
//                for (int waitCount = 0; waitCount < 20; waitCount++) {
//                    try {
//                        Thread.sleep(200);
//                    } catch (Exception e) {
//                    }
//                    if (SystemProperties.get("persist.sys.iscamera").equals("open")) {
//                        break;
//                    }
//                }
//            }
//        }
//        Intent closecam = new Intent();
//        closecam.setAction("com.se4500.closecamera");
//        this.sendBroadcast(closecam, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cammer_front);
        initView();
        if (SystemProperties.get("persist.sys.iscamera").equals("close")) {
            SystemProperties.set("persist.sys.scanstopimme", "true");
            Intent opencam = new Intent();
            opencam.setAction("com.se4500.opencamera");
            this.sendBroadcast(opencam, null);
        }
        if (SystemProperties.get("persist.sys.keyreport").equals("true")) {
            if (SystemProperties.get("persist.sys.scanheadtype").equals("6603")) {
                for (int waitCount = 0; waitCount < 20; waitCount++) {
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                    }
                    if (SystemProperties.get("persist.sys.iscamera").equals("open")) {
                        break;
                    }
                }
            }
        }
        initTitlebar();
        setSwipeEnable(false);
        //获得控件
        mySurfaceView = (SurfaceView) findViewById(R.id.camera);
        //获得句柄
        holder = mySurfaceView.getHolder();
        //添加回调
        holder.addCallback(this);
        //设置类型
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        String model = Build.MODEL;
        if (model.equals("KT80") || model.equals("W6") || model.equals("RT801")
                || model.equals("T80") || model.equals("T800") || model.equals("FC-K80")
                || model.equals("Biowolf LE") || model.equals("N800") || model.equals("FC-PK80")
                || model.equals("DM-P80") || model.equals("SD-55")) {
            titlebar.setAttrs(getResources().getString(R.string.camera_title7));
        } else {
            titlebar.setAttrs(getResources().getString(R.string.camera_title8));
            btnLight.setVisibility(View.GONE);
        }
    }

    //创建jpeg图片回调数据对象
    Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

                    // 首先保存图片
                    File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
                    if (!appDir.exists()) {
                        appDir.mkdir();
                    }
                    String fileName = System.currentTimeMillis() + ".jpg";
                    File file = new File(appDir, fileName);
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 其次把文件插入到系统图库
                    try {
                        MediaStore.Images.Media.insertImage(CammerFrontAct.this.getContentResolver(),
                                file.getAbsolutePath(), fileName, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 最后通知图库更新
                    CammerFrontAct.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
                }
            }).start();


        }
    };


    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    private int FindFrontCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;//无前置摄像头
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if (myCamera != null) {
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;
        }
        cammeraIndex = FindFrontCamera();
        if (cammeraIndex == -1) {
            showToast(getResources().getString(R.string.camera_msg));
            finish();
            return;
        } else {
            try {

                myCamera = Camera.open(1);
                setCameraDisplayOrientation(this, 1, myCamera);//设置预览方向,
                myCamera.setPreviewDisplay(holder);
                myCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //关闭预览并释放资源
        if (myCamera != null) {
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;
        }
    }

    /*
    设置方向
     */
    public static void setCameraDisplayOrientation(CammerFrontAct activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;   // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        camera = (SurfaceView) findViewById(R.id.camera);
        btnPhoto = (Button) findViewById(R.id.btn_photo);
        btnPhoto.setOnClickListener(this);
        btnPass = (Button) findViewById(R.id.btn_pass);
        btnPass.setOnClickListener(this);
        btnLight = (Button) findViewById(R.id.btn_light);
        btnLight.setOnClickListener(this);
        btnNotPass = (Button) findViewById(R.id.btn_not_pass);
        btnNotPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_photo:
                break;
            case R.id.btn_pass:
                if (count == 0) {
                    try {
                        myCamera.startPreview();
                        myCamera.takePicture(shutterCallback, null, jpeg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//            onAutoFocus(isClicked, myCamera);
                    titlebar.setTitlebarNameText(getResources().getString(R.string.camera_title4));
                    btnPass.setText(getResources().getString(R.string.camera_btn3));
                    count++;
                } else if (count == 1) {
                    setXml(App.KEY_CAMMAR_FRONT, App.KEY_FINISH);
                    finish();
                }
                break;
            case R.id.btn_light:
                if (light == 0) {
                    //打开补光灯
                    Camera.Parameters parameters = myCamera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    myCamera.setParameters(parameters);
                    titlebar.setAttrs(getResources().getString(R.string.camera_title8));
                    light = 1;
                } else if (light == 1) {
                    titlebar.setAttrs(getResources().getString(R.string.camera_title7));
                    //关闭闪光灯
                    Camera.Parameters parameters = myCamera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    myCamera.setParameters(parameters);
                    light = 0;
                }
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_CAMMAR_FRONT, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
