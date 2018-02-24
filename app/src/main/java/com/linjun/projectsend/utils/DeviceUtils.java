package com.linjun.projectsend.utils;

/**
 * Created by linjun on 2018/2/23.
 */

public class DeviceUtils {
//    获取手机品牌
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }
//手机型号
public static String getPhoneModel() {
    return android.os.Build.MODEL;
}


}
