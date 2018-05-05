package com.linjun.projectsend.ui.main;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.linjun.SendPacket;
import com.linjun.projectsend.utils.DeviceUtils;
import com.linjun.projectsend.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service implements AMapLocationListener {
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    public final String TAG="LocationService";

    private Timer mTimer;


    @Override
    public void onCreate() {
        Notification noti = new Notification();
        noti.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
        startForeground(1, noti);
        Log.i(TAG,"定位成功");
       System.out.println("服务启动了");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

                initLocation();
        startLocation();
        return START_STICKY;
    }



    private CountBinder echoBinder = new CountBinder();

    public class CountBinder extends Binder {
        public LocationService getService(){
            return LocationService.this;
        }
    }
    @Override
    public void onDestroy() {
        stopLocation();
        destroyLocation();
        Log.i(TAG, "onDestroy: 服务关闭");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return echoBinder;
    }
   private  void initLocation(){
       if (null == locationOption) {
           locationOption = new AMapLocationClientOption();
       }
       locationClient=new AMapLocationClient(this);
       locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
       locationOption=getDefaultOption();
       locationClient.setLocationListener(this);
   }

    @Override
    public void onLocationChanged(AMapLocation location) {
        StringBuffer sb = new StringBuffer();
        if (location.getErrorCode() == 0) {
            sb.append("定位成功" + "\n");
            Log.i(TAG,"定位成功");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");

            sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
            sb.append("角    度    : " + location.getBearing() + "\n");
            //定位完成的时间
            sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        sb.append("***定位质量报告***").append("\n");
        sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
        sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
        sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
        sb.append("****************").append("\n");
        //定位之后的回调时间
        sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
        if (location.getErrorCode() == 0) {
            sb.append("dsfsafasd");
            Message msg = new Message();
            Log.i(TAG, "onLocationChanged: 服务没有正常关闭");
//            msg.obj="发送数据成功"+Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n";
            msg.obj=sb;
            msg.what = 1;
            MainActivity.handler.sendMessage(msg);
            Message msg1 = new Message();
            SendPacket sendPacket=new SendPacket();
            sendPacket.setJingdu(Math.abs(location.getLongitude()));
            sendPacket.setWeidu(location.getLatitude());
            sendPacket.setSpeed(location.getSpeed());
//            sendPacket.setBytes();
            sendPacket.setDeviceid(DeviceUtils.getPhoneBrand()+DeviceUtils.getPhoneModel());
            sendPacket.setSend_time(System.currentTimeMillis());
            msg1.obj=sendPacket;
            msg1.what = 2;
            MainActivity.handler.sendMessage(msg1);
        }
    }
    private String getGPSStatusString(int statusCode){
        String str = "";
        switch (statusCode){
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    private void startLocation(){
        //根据控件的选择，重新设置定位参数
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }
    private void destroyLocation(){
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}
