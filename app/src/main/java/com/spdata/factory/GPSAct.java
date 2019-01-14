package com.spdata.factory;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.south.SDKMethod;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.spdata.factory.application.App;
import com.spdata.factory.view.CustomTitlebar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import common.base.act.FragActBase;

public class GPSAct extends FragActBase implements View.OnClickListener {

    private String gpsState;
    private CustomTitlebar titlebar;
    private EditText editText;
    /**
     * 无数据
     */
    private TextView tvInfor;
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
        }, getResources().getString(R.string.menu_gps), null);
    }


    private SDKMethod gpsnet_device;
    boolean is = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gps);
        initView();
        initTitlebar();
        setSwipeEnable(false);
//        x300qgpsDate();
        initGPS();

    }

    private void x300qgpsDate() {
        String model = Build.MODEL;
        if (model.equals("X300Q_X1") || model.equals("X300Q_P1") ||
                model.equals("X300Q_OLED") || model.equals("X300Q_OLED_GPS")) {
            gpsnet_device = new SDKMethod();
            gpsnet_device.setExtGps(1);
            try {
                BufferedReader reader = new BufferedReader(new FileReader
                        ("sys/devices/virtual/gpsdrv/gps/gpsctl"));
                gpsState = reader.readLine();
                if (gpsState.equals("0")) {
                    BufferedWriter writer = new BufferedWriter(new FileWriter
                            ("sys/devices/virtual/gpsdrv/gps/gpsctl"));
                    writer.write("1");
                    writer.close();
                    is = false;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(GPSAct.this, getResources().getString(R.string.gps_open),
                    Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getResources().getString(R.string.gps_open));
            dialog.setPositiveButton(getResources().getString(R.string.alert_dialog_ok),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                        }
                    });
            dialog.setNeutralButton(getResources().getString(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();
        } else {
            showToast(getResources().getString(R.string.gps_start));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopGPSLoction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (!is) {
//            BufferedWriter writer = null;
//            try {
//                writer = new BufferedWriter(new FileWriter
//                        ("sys/devices/virtual/gpsdrv/gps/gpsctl"));
//                writer.write("0");
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            gpsnet_device.setExtGps(0);
//        }
    }

    // gps
    LocationManager locationManager;

    private void init() {
        startGPSLoction();
    }

    private void stopGPSLoction() {
//        showToast("停止");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
        locationManager.removeGpsStatusListener(gpsStausListener);
    }

    // Gps是否可用
    private boolean isGpsEnable() {
        locationManager = ((LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE));
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    Calendar caledar;
    //位置监听
    LocationListener locationListener = new LocationListener() {
        /**
         * GPS状态变化时触发
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            System.out.println("-----");
            switch (status) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
//                    Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
//                    Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            updateView(location);
        }

        /**
         * GPS禁用时触发
         */
        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
            updateView(null);
        }

        /**
         * 位置信息变化时触发
         */
        @Override
        public void onLocationChanged(Location location) {
            updateView(location);
        }
    };
    //状态监听
    GpsStatus.Listener gpsStausListener = new GpsStatus.Listener() {

        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_FIRST_FIX: {//当GPS系统模块接收到第一次GPS定位信息之后发送此事件码。调用getTimeToFirstFix()方法获取自GPS系统模块被开启至第一次定位所消耗的时间。
                    // fixed = true;
                    break;
                }
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS: {
                    if (ActivityCompat.checkSelfPermission(GPSAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    GpsStatus currentgpsStatus = locationManager.getGpsStatus(null);
                    List<GpsSatellite> satelliteList = new ArrayList<GpsSatellite>();
                    int maxSatellites = currentgpsStatus.getMaxSatellites();// //获取卫星颗数的默认最大值
//                    logger.d("当前所有卫星数目为" + maxSatellites);
                    Iterator<GpsSatellite> iters = currentgpsStatus.getSatellites()
                            .iterator();
                    mSatelliteCount = 0;
                    tvInfor.setText(getResources().getString(R.string.gps_searched));
                    mSatellitesStrenthList = new ArrayList<Float>();
                    String result = "";
                    while (iters.hasNext() && mSatelliteCount <= maxSatellites) {
                        // satelliteList.add(iters.next());
                        result += iters.next().getSnr();
//                        tvInfor.append("," + iters.next().getSnr());
                        mSatelliteCount++;
                    }
                    tvInfor.append(mSatelliteCount + getResources().getString(R.string.gps_count) + "\n");
                    tvInfor.append(result + "\n");
                    printTime();
                    break;
                }
                case GpsStatus.GPS_EVENT_STARTED: {//当GPS系统模块被开启之后发送此事件码
                    break;
                }
                case GpsStatus.GPS_EVENT_STOPPED: {//当GPS系统模块被关闭之后发送此事件码。
                    break;
                }
            }
        }
    };

    private void printTime() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);//获取年份
        int month = ca.get(Calendar.MONTH);//获取月份
        int day = ca.get(Calendar.DATE);//获取日
        int minute = ca.get(Calendar.MINUTE);//分
        int hour = ca.get(Calendar.HOUR);//小时
        int second = ca.get(Calendar.SECOND);//秒
        int WeekOfYear = ca.get(Calendar.DAY_OF_WEEK);
        tvInfor.append(getResources().getString(R.string.gps_now_date) + year + getResources().getString(R.string.gps_year) + month + getResources().getString(R.string.gps_month) + day + getResources().getString(R.string.gps_day) + hour + getResources().getString(R.string.gps_hour) + minute + getResources().getString(R.string.gps_minute) + second + getResources().getString(R.string.gps_second));
    }

    /**
     * 实时更新文本内容
     *
     * @param location
     */
    private void updateView(Location location) {
        if (location != null) {
            editText.setText(getResources().getString(R.string.gps_update_lon));
            editText.append(String.valueOf(location.getLongitude()));
            editText.append(getResources().getString(R.string.gps_update_lat));
            editText.append(String.valueOf(location.getLatitude()));
        } else {
            //清空EditText对象
            editText.getEditableText().clear();
        }
    }

    private int mSatelliteCount = 0;
    private List<Float> mSatellitesStrenthList;

    private void startGPSLoction() {
        // 获取位置管理服务
        // LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) this.getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.ACCURACY_HIGH); // 高功耗

        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
        updateView(location);
        // updateToNewLocation(location);
        // 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
        locationManager.requestLocationUpdates(provider, 1 * 1000, 1,
                locationListener);
        locationManager.addGpsStatusListener(gpsStausListener);  //监听状态
    }

    private void initView() {
        titlebar = (CustomTitlebar) findViewById(R.id.titlebar);
        editText = (EditText) findViewById(R.id.editText);
        tvInfor = (TextView) findViewById(R.id.tv_infor);
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
            case R.id.btn_pass:
                setXml(App.KEY_GPS, App.KEY_FINISH);
                finish();
                break;
            case R.id.btn_not_pass:
                setXml(App.KEY_GPS, App.KEY_UNFINISH);
                finish();
                break;
        }
    }
}
