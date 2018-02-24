package com.linjun.projectsend.common;

/**
 * Created by linjun on 2018/2/24.
 */

public interface Type {
    /**
     * 登录消息请求
     */
    byte LOGIN_REQ = 1;
    /**
     * 登录消息响应
     */
    byte LOGIN_RESP = 2;


    byte HEART_BEAT_REQ = 99;

}
