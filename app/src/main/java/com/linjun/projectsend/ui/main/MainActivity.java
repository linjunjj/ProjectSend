package com.linjun.projectsend.ui.main;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;

import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.linjun.projectsend.R;
import com.linjun.projectsend.common.packets.SendPacket;
import com.linjun.projectsend.service.LocationService;
import com.linjun.projectsend.service.TestService;
import com.linjun.projectsend.ui.base.BaseActivity;
import com.linjun.projectsend.utils.Utils;
import com.txusballesteros.SnakeView;

import butterknife.BindView;
import butterknife.OnClick;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;


public class MainActivity extends BaseActivity implements  AMapLocationListener{
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.snake)
    SnakeView snake;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.btn_start)
    Button btnStart;
   private NioEventLoopGroup group;
    private  Channel channel;
    private  ChannelFuture channelFuture;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    public StringBuffer sb = new StringBuffer();
    private Messenger sMessenger;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
//        if (null == locationOption) {
//            locationOption = new AMapLocationClientOption();
//        }
//       locationClient=new AMapLocationClient(this);
//        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        locationOption=getDefaultOption();
//       locationClient.setLocationListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        destroyLocation();
    }


    @OnClick(R.id.btn_start)
    public void onViewClicked() {
        if (btnStart.getText().equals("开始")) {
            btnStart.setText("停止");
            sb.append("开始定位...\n");
//            startLocation();
            Intent intet1 = new Intent(MainActivity.this, TestService.class);
//            startService(intet1);
            bindService(intet1,conn, Context.BIND_AUTO_CREATE);
            tvResult.setText("启动服务成功");

        } else {
            sb.append("停止定位...\n");
            btnStart.setText("开始");
            tvResult.setText(sb.toString());
//            stopLocation();

        }
    }
//    @SuppressLint("HandlerLeak")
//    Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            String m=msg+"";
//           switch (msg.what){
//               case 0:
//                   SendPacket packet=new SendPacket();
//                   packet.setSend_time(System.currentTimeMillis());
//                   packet.setCountPackage(1);
//                   channel.writeAndFlush(packet);
//                   break;
//               case 1:
//                   tvResult.setText(tvResult.getText()+m+"\r\n");
//               case 2:
//                   tvResult.setText("");
//                   break;
//               case 3:
//                   String mm=String.valueOf(tvResult.getText()+"'");
//                    byte[] bb=mm.getBytes();
//                   SendPacket packet1=new SendPacket();
//                   packet1.setSend_time(System.currentTimeMillis());
//                   packet1.setBytes(bb);
//                   packet1.setCountPackage(1);
//                   channel.writeAndFlush(mm).addListener(new ChannelFutureListener() {
//                       @Override
//                       public void operationComplete(ChannelFuture future) throws Exception {
//                           handler.obtainMessage(2).sendToTarget();
//                       }
//                   });
//                   break;
//               case 4:
//
//
//           }
//        }
//    };

    private Handler handler=new Handler() {
        // 获取Service发送过来的消息进行处理
        @Override
        public void handleMessage(Message msg) {
            Log.i("回传的消息是", "----->Client processes message.");
            switch (msg.what) {
                case 0:
                    // 此为示例，仅仅显示获取到的消息
                    tvResult.setText(tvResult.getText() + "\n" + msg.obj);
                    break;
            }
        }
    };

    //定义Connection，作为启动Services的参数
    private ServiceConnection conn=new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            TestService.Binder binder = (TestService.Binder) service;
            TestService myService = binder.getService();
            myService.setCallback(new TestService.Callback() {
                @Override
                public void onDataChange(String data) {
                    Message msg = new Message();
                    msg.obj = data;
                    handler.sendMessage(msg);
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            sMessenger=null;
            Log.i("对方水电费", "----->ServiceConnection isConn is flase");
        }
    };


    @Override
    public void onLocationChanged(AMapLocation location) {
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
            sb.append("定位成功" + "\n");
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
        tvResult.setText(sb.toString());
        if (location.getErrorCode() == 0) {
            sb.append("开始向后端传送数据" + "\n");
            tvResult.append(sb.toString());

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
