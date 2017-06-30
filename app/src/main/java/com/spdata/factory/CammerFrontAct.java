package com.spdata.factory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import common.base.act.FragActBase;
import common.event.ViewMessage;

import static android.R.attr.path;

/**
 * Created by xu on 2016/7/26.
 */

@EActivity(R.layout.act_cammer_front)
public class CammerFrontAct extends FragActBase implements SurfaceHolder.Callback, Camera.AutoFocusCallback {

    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    Button btn_light;

    private int CAMERA_RESULT = -1;
    int cameraCount = 0;
    private int count = 0;
    private int light = 0;
    private SurfaceView mySurfaceView;//surfaceView声明
    private SurfaceHolder holder;//surfaceHolder声明
    private Camera myCamera = null;//相机声明
    private String filePath = "/sdcard/qian.jpg";//照片保存路径
    boolean isClicked = false;//是否点击标识
    private int cammeraIndex;

    @Click
    void btnPass() {
        if (count == 0) {
            try {
                myCamera.startPreview();
                myCamera.takePicture(shutterCallback, null, jpeg);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            onAutoFocus(isClicked, myCamera);
            titlebar.setTitlebarNameText("拍照成功！");
            btnPass.setText("成功");
            count++;
        } else if (count == 1) {
            setXml(App.KEY_CAMMAR_FRONT, App.KEY_FINISH);
            finish();
        }
    }

    @Click
    void btnNotPass() {
        setXml(App.KEY_CAMMAR_FRONT, App.KEY_UNFINISH);
        finish();
    }

    @Click
    void btn_light() {
        if (light == 0) {
            //打开补光灯
            Camera.Parameters parameters = myCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            myCamera.setParameters(parameters);
            titlebar.setAttrs("请点击拍照按钮进行拍照！");
            light = 1;
        } else if (light == 1) {
            titlebar.setAttrs("请先打开前置补关灯");
            //关闭闪光灯
            Camera.Parameters parameters = myCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            myCamera.setParameters(parameters);
            light = 0;
        }
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("前置摄像头测试");
    }

    @AfterViews
    protected void main() {
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
        if (model.equals("KT80") || model.equals("W6")) {
            titlebar.setAttrs("请先打开前置补关灯");
        } else {
            titlebar.setAttrs("请点击拍照按钮进行拍照！");
            btn_light.setVisibility(View.GONE);
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

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
        }
    };

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if (cammeraIndex == -1) {
            showToast("无前置摄像头");
            finish();
            return;
        }
        //设置参数并开始预览
        Camera.Parameters params = myCamera.getParameters();
        params.setPictureFormat(PixelFormat.JPEG);
        params.setPreviewSize(640, 480);
        // 设置预览照片时每秒显示多少帧的最小值和最大值
        params.setPreviewFpsRange(4, 10);
        // 设置图片格式
        params.setPictureFormat(ImageFormat.JPEG);
        // 设置JPG照片的质量
        params.set("jpeg-quality", 85);
        myCamera.setParameters(params);
        myCamera.startPreview();

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
        } else {
            cammeraIndex = FindFrontCamera();
            if (cammeraIndex == -1) {
                showToast("无前置摄像头");
                finish();
                return;
            } else {
                try {
                    myCamera = Camera.open(cammeraIndex);
                    myCamera.stopPreview();
//                myCamera = Camera.open(CAMERA_RESULT);
                    setCameraDisplayOrientation(this, 1, myCamera);//设置预览方向,
                    //设置显示
                    myCamera.setPreviewDisplay(holder);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //关闭预览并释放资源
        if (cammeraIndex != -1) {
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;
        }
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        // TODO Auto-generated method stub
        if (success) {
            //设置参数,并拍照
            Camera.Parameters params = myCamera.getParameters();
            params.setPictureFormat(PixelFormat.JPEG);
            params.setPreviewSize(640, 480);
            myCamera.setParameters(params);
            myCamera.takePicture(null, null, jpeg);
        }

    }

    /*
    设置方向
     */
    public static void setCameraDisplayOrientation(CammerFrontAct activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
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
}
