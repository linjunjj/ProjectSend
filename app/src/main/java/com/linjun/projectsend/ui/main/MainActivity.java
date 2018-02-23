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

import com.linjun.projectsend.ui.base.BaseActivity;
import com.linjun.projectsend.utils.HeartbeatTimer;
import com.linjun.projectsend.utils.Utils;
import com.txusballesteros.SnakeView;

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
    //
    private float[] values = new float[] { 60, 70, 80, 90, 100,
            150, 150, 160, 170, 175, 180,
            170, 140, 130, 110, 90, 80, 60};
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private StringBuffer sb = new StringBuffer();
    private HeartbeatTimer heartbeatTimer;
    private ExecutorService mThreadPool;
    private String city;
    private String country;
    private String province;
    private double weidu;
    private double jingdu;

    private int position = 0;
    private boolean stop = false;
    AMapLocationClient aMapLocationClient=null;

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
        }, 500);
    }


    @Override
    public void onLocationChanged(AMapLocation location) {
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if(location.getErrorCode() == 0){
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
        sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启":"关闭").append("\n");
        sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
        sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
        sb.append("****************").append("\n");
        //定位之后的回调时间
        sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
        tvResult.setText(sb.toString());
        if (location.getErrorCode()==0){
            sb.append("开始向后端传送数据"+"\n");
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

}
