package com.linjun.projectsend.ui.main;

import android.os.Bundle;
import android.os.Handler;

import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.linjun.projectsend.R;

import com.linjun.projectsend.common.Const;
import com.linjun.projectsend.common.ShowcasePacket;
import com.linjun.projectsend.common.Type;
import com.linjun.projectsend.common.packets.LoginReqBody;
import com.linjun.projectsend.handler.LoginRespHandler;
import com.linjun.projectsend.handler.ShowcaseClientAioHandler;
import com.linjun.projectsend.handler.ShowcaseClientAioListener;
import com.linjun.projectsend.model.SendPacket;
import com.linjun.projectsend.ui.base.BaseActivity;
import com.linjun.projectsend.utils.HeartbeatTimer;
import com.linjun.projectsend.utils.Utils;
import com.txusballesteros.SnakeView;

import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.Node;
import org.tio.utils.json.Json;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements  AMapLocationListener{


    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.snake)
    SnakeView snake;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.btn_start)
    Button btnStart;

    private float[] values = new float[] { 60, 70, 80, 90, 100,
            150, 150, 160, 170, 175, 180,
            170, 140, 130, 110, 90, 80, 60};
    private AMapLocationClient locationClient = null;
    public StringBuffer sb = new StringBuffer();
    private int position = 0;
    private boolean stop = false;
   static String  serverIp= Const.ServerIP;
   static  int serverPort=Const.PORT;
    private static Node serverNode=new Node(serverIp,serverPort);
    private  static ReconnConf reconnConf=new ReconnConf(5000L);
    private static ClientAioHandler aioClientHandler = new ShowcaseClientAioHandler();
    private static ClientAioListener aioListener = new ShowcaseClientAioListener();
    private static ClientGroupContext clientGroupContext = new ClientGroupContext(aioClientHandler, aioListener, reconnConf);

    private static AioClient aioClient = null;

    static ClientChannelContext clientChannelContext;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
       locationClient=new AMapLocationClient(this);
       AMapLocationClientOption option=new AMapLocationClientOption();
       option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
       locationClient.setLocationOption(option);
       locationClient.setLocationListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null!=locationClient){
            locationClient.onDestroy();
        }
    }


    @OnClick(R.id.btn_start)
    public void onViewClicked() {
        if (btnStart.getText().equals("开始")) {
            btnStart.setText("停止");
            sb.append("开始定位...\n");
            locationClient.startLocation();


        } else {
            btnStart.setText("开始");
            sb.append("停止定位...\n");
            tvResult.setText(sb.toString());
            locationClient.stopLocation();
        }
    }

    private void generateValue() {
        if (position < (values.length - 1)) {
            position++;
        } else {
            position = 0;
        }
        float value = values[position];
        snake.addValue(value);
        text.setText(Integer.toString((int)value));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!stop) {
                    generateValue();
                }
            }
        }, 5000);
    }


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
            clientGroupContext.setHeartbeatTimeout(5000);
            try {
                aioClient = new AioClient(clientGroupContext);
                clientChannelContext = aioClient.connect(serverNode);
                SendPacket sendPacket = new SendPacket();
                sendPacket.setTime(System.currentTimeMillis());
                sendPacket.setJingdu(location.getLongitude());
                sendPacket.setWeidu(location.getLatitude());
                sendPacket.setSpeed(location.getSpeed());
                processCommand(sendPacket);
                sb.append(LoginRespHandler.getBody().getCode() + "\n");
                tvResult.setText(sb.toString());
                generateValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


public  static  void  processCommand(SendPacket packet) throws UnsupportedEncodingException {
        if (packet==null){
            return;
        }
    LoginReqBody loginReqBody=new LoginReqBody();
        loginReqBody.setDeviceID(packet.getDeviceid());
    ShowcasePacket showcasePacket=new ShowcasePacket();
    showcasePacket.setType(Type.LOGIN_REQ);
    showcasePacket.setBody(Json.toJson(loginReqBody).getBytes(ShowcasePacket.CHARSET));
    Aio.send(clientChannelContext,showcasePacket);
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

}
