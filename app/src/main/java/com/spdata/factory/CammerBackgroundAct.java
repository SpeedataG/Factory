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
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CameraPreview;
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

@EActivity(R.layout.act_cammar_background)
public class CammerBackgroundAct extends FragActBase implements SurfaceHolder.Callback,
        View.OnClickListener, Camera.AutoFocusCallback, View.OnTouchListener {

    @ViewById
    CustomTitlebar titlebar;
    @ViewById
    Button btnPass;
    @ViewById
    Button btnNotPass;
    @ViewById
    View focus_index;

    private int count = 0;
    private Camera.AutoFocusCallback myAutoFocusCallback = null;
    SurfaceView mySurfaceView;//surfaceView声明
    SurfaceHolder holder;//surfaceHolder声明
    Camera myCamera;//相机声明
    String filePath = "/sdcard/hou.jpg";//照片保存路径
    boolean isClicked = true;//是否点击标识
    Camera.Parameters parameters;
    private CameraPreview preview;
    private Handler handler = new Handler();

    @Click
    void btnPass() {
        if (count == 0) {
            if (isClicked) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            myCamera.startPreview();   //开启预览
                            myCamera.takePicture(shutterCallback, null, jpeg);

                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isClicked = false;
            }
            if (!isClicked) {
                btnPass.setText("闪光拍照");
                titlebar.setTitlebarNameText("拍照成功!");
                btnPass.setEnabled(false);
                count++;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            myCamera.startPreview();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                titlebar.setAttrs("请点击屏幕对焦！");
                                isClicked = true;
                            }
                        });
                    }
                }).start();
            }
        } else if (count == 1) {//闪光拍照
            if (isClicked) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Camera.Parameters parameters = myCamera.getParameters();//打开闪光灯
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                             myCamera.setParameters(parameters);
                            myCamera.takePicture(shutterCallback, null, jpeg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                isClicked = false;
            }
            if (!isClicked) {
                titlebar.setTitlebarNameText("闪光灯拍照成功！");
                count++;
                btnPass.setText("成功");
            }
        } else if (count == 2) {
            setXml(App.KEY_CAMMAR_BACKGROUND, App.KEY_FINISH);
            finish();
        }
    }

    @Click
    void btnNotPass() {
        setXml(App.KEY_CAMMAR_BACKGROUND, App.KEY_UNFINISH);
        finish();
    }

    @Override
    protected Context regieterBaiduBaseCount() {
        return null;
    }

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs("后置摄像头/闪光灯测试");
    }

    @Override
    public void onEventMainThread(ViewMessage viewMessage) {
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
        }
    };
    //创建jpeg图片回调数据对象
    Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

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
                MediaStore.Images.Media.insertImage(CammerBackgroundAct.this.getContentResolver(),
                        file.getAbsolutePath(), fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            CammerBackgroundAct.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
//            mcamera2.cancelAutoFocus(); //这一句很关键
//            mcamera.cancelAutoFocus(); //这一句很关键

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @AfterViews
    protected void main() {
//        if(SystemProperties.get("persist.sys.iscamera").equals("close")){
//            Intent opencamera = new Intent();
//            //SystemProperties.set("persist.sys.iscamera","open");
//            opencamera.setAction("com.se4500.opencamera");
//            this.sendOrderedBroadcast(opencamera,null);
//        }

//        if(SystemProperties.get("persist.sys.keyreport").equals("true")){
//            if(SystemProperties.get("persist.sys.se4500").equals("true")){
//                for(waitCount = 0; waitCount < 20; waitCount++){
//                    try{
//                        Thread.sleep(200);
//                    }catch(Exception e){
//                    }
//                    if(SystemProperties.get("persist.sys.iscamera").equals("open"))
//                        break;
//                }
//            }
//        }


        initTitlebar();
        btnPass.setEnabled(false);
        //获得控件
        mySurfaceView = (SurfaceView) findViewById(R.id.camera_surface);
        //获得句柄
        holder = mySurfaceView.getHolder();
        //添加回调
        holder.addCallback(this);
        //设置类型
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //设置监听
        mySurfaceView.setOnClickListener(this);
        titlebar.setTitlebarNameText("请点击屏幕进行对焦!");
        myAutoFocusCallback = new Camera.AutoFocusCallback() {

            public void onAutoFocus(boolean success, Camera camera) {
                // TODO Auto-generated method stub
                if (success)//success表示对焦成功
                {
                    myCamera.autoFocus(null); //自动对焦
                } else {
                    //未对焦成功
                }
            }
        };
        InitData();
    }

    private void InitData() {
        preview = new CameraPreview(this, mySurfaceView);
        preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.layout)).addView(preview);
        preview.setKeepScreenOn(true);
        mySurfaceView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                preview.pointFocus(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
                focus_index.getLayoutParams());
        layout.setMargins((int) event.getX() - 60, (int) event.getY() - 60, 0, 0);

        focus_index.setLayoutParams(layout);
        focus_index.setVisibility(View.VISIBLE);

        ScaleAnimation sa = new ScaleAnimation(3f, 1f, 3f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(800);
        focus_index.startAnimation(sa);
        handler.postAtTime(new Runnable() {
            @Override
            public void run() {
                focus_index.setVisibility(View.INVISIBLE);
            }
        }, 800);
        return false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        //设置参数并开始预览
        //实现自动对焦
        SystemClock.sleep(500);
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

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        //开启相机
        if (myCamera != null) {
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;
        }
        if (myCamera == null) {
            try {
                myCamera = Camera.open();
                myCamera.setDisplayOrientation(90);//设置预览方向,
                myCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        try {
            myCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        initCamera();//实现相机的参数初始化
                        camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                        titlebar.setAttrs("对焦成功！");
                        btnPass.setEnabled(true);
                    }else {
                        titlebar.setAttrs("对焦失败！");
                    }
                }
            }); //自动对焦
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //相机参数的初始化设置
    private void initCamera() {
        parameters = myCamera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        myCamera.setParameters(parameters);
        myCamera.startPreview();
        myCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //关闭预览并释放资源
        myCamera.stopPreview();
        myCamera.release();
        myCamera = null;

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
//            myCamera.takePicture(null, null, jpeg);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
