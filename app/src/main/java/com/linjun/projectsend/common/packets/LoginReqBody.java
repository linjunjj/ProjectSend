package com.linjun.projectsend.common.packets;

/**
 * Created by linjun on 2018/2/24.
 */

public class LoginReqBody extends  BaseBody {
    private String deviceID;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
