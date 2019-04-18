package com.spdata.factory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemProperties;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import common.base.act.FragActBase;

import static android.R.attr.path;
import static com.spdata.factory.view.CameraPreview.setCameraDisplayOrientation;

public class CammerBackgroundAct extends FragActBase implements SurfaceHolder.Callback,
        View.OnClickListener, Camera.AutoFocusCallback, View.OnTouchListener {

    FrameLayout layout;//surfaceView声明;

    private int count = 0;
    private Camera.AutoFocusCallback myAutoFocusCallback = null;

    SurfaceHolder holder;//surfaceHolder声明
    Camera myCamera;//相机声明
    String filePath = "/sdcard/hou.jpg";//照片保存路径
    boolean isClicked = true;//是否点击标识
    Camera.Parameters parameters;
    private CameraPreview preview;
    private Handler handler = new Handler();
    private CustomTitlebar titlebar;
    private SurfaceView camera_surface;
    private View focus_index;
    /**
     * 拍照
     */
    private Button btn_pass;
    /**
     * 失败
     */
    private Button btn_not_pass;

    @Override
    protected void initTitlebar() {
        titlebar.setTitlebarStyle(CustomTitlebar.TITLEBAR_STYLE_NORMAL);
        titlebar.setAttrs(getResources().getString(R.string.menu_background_camera));
    }


    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
        }
    };
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
                        MediaStore.Images.Media.insertImage(CammerBackgroundAct.this.getContentResolver(),
                                file.getAbsolutePath(), fileName, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 最后通知图库更新
                    CammerBackgroundAct.this.sendBroadcast(new Intent(Intent.
                            ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
                }
            }).start();
//            mcamera2.cancelAutoFocus(); //这一句很关键
//            mcamera.cancelAutoFocus(); //这一句很关键

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cammar_background);
        initView();
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
        btn_pass.setEnabled(false);
        //获得控件
        camera_surface = (SurfaceView) findViewById(R.id.camera_surface);
        //获得句柄
        holder = camera_surface.getHolder();
        //添加回调
        holder.addCallback(this);
        //设置类型
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //设置监听
        camera_surface.setOnClickListener(this);
        titlebar.setTitlebarNameText(getResources().getString(R.string.camera_title));
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
        preview = new CameraPreview(this, camera_surface);
        preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.layout)).addView(preview);
        preview.setKeepScreenOn(true);
        camera_surface.setOnTouchListener(this);
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
    protected void onStop() {
        super.onStop();
        SystemProperties.set("persist.sys.iscamera", "open");
        SystemProperties.set("persist.sys.scanstopimme", "false");
        Intent opencam = new Intent();
        opencam.setAction("com.se4500.closecamera");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
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
                try {
                    myCamera = Camera.open(0);
                    setCameraDisplayOrientation(CammerBackgroundAct.this, 0, myCamera);//设置预览方向,
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(getResources().getString(R.string.camera_faild));
                }
                if("SD100T".equals(App.getModel())){
                    myCamera.setDisplayOrientation(90);//设置预览方向,
                }
//                if (Build.MODEL.equals("SD80") || Build.MODEL.equals("AQUARIUS Cmp NS208") || Build.MODEL.equals("N80")
//                        || Build.MODEL.equals("S550")) {
//                    myCamera.setDisplayOrientation(270);//设置预览方向,
//                } else {
//                    myCamera.setDisplayOrientation(90);//设置预览方向,
//
//                }
                myCamera.setPreviewDisplay(holder);
                myCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == camera_surface) {
            try {
                myCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            initCamera();//实现相机的参数初始化
                            camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                            titlebar.setAttrs(getResources().getString(R.string.camera_title2));
                            btn_pass.setEnabled(true);
                        } else {
                            titlebar.setAttrs(getResources().getString(R.string.camera_title3));
                        }
                    }
                }); //自动对焦
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v == btn_pass) {
            if (count == 0) {
                count++;
                isClicked = false;
                myCamera.takePicture(shutterCallback, null, jpeg);
                titlebar.setTitlebarNameText(getResources().getString(R.string.camera_title4));
                btn_pass.setEnabled(false);
                btn_pass.setText(getResources().getString(R.string.camera_btn2));
                myCamera.stopPreview();
                myCamera.startPreview();  //开启预览
                titlebar.setAttrs(getResources().getString(R.string.camera_title5));
            } else if (count == 1) {//闪光拍照
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                myCamera.setParameters(parameters);
                myCamera.takePicture(shutterCallback, null, jpeg);
                titlebar.setTitlebarNameText(getResources().getString(R.string.camera_title6));
                btn_pass.setText(getResources().getString(R.string.camera_btn3));
                myCamera.stopPreview();
                myCamera.startPreview();
                count++;
            } else if (count == 2) {
//            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//            myCamera.setParameters(parameters);
                setXml(App.KEY_CAMMAR_BACKGROUND, App.KEY_FINISH);
                finish();
            }

        } else if (v == btn_not_pass) {
            setXml(App.KEY_CAMMAR_BACKGROUND, App.KEY_UNFINISH);
            finish();
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


    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        camera_surface = (SurfaceView) findViewById(R.id.camera_surface);
        focus_index = (View) findViewById(R.id.focus_index);
        layout = (FrameLayout) findViewById(R.id.layout);
        btn_pass = (Button) findViewById(R.id.btn_pass);
        btn_pass.setOnClickListener(this);
        btn_not_pass = (Button) findViewById(R.id.btn_not_pass);
        btn_not_pass.setOnClickListener(this);
    }
}
