package com.linjun.projectsend.common.packets;

import java.io.Serializable;

public class SendPacket implements Serializable {
    private  String deviceid;
    private  double jingdu;
    private  double weidu;
    private  float speed;
    private  String context;
    private byte[] bytes;
    private String send_uid;
    private String receive_uid;
    private long send_time;
    private long receive_time;
    private int sumCountPackage;
    private int countPackage;

    public int getSumCountPackage() {
        return sumCountPackage;
    }

    public void setSumCountPackage(int sumCountPackage) {
        this.sumCountPackage = sumCountPackage;
    }

    public int getCountPackage() {
        return countPackage;
    }

    public void setCountPackage(int countPackage) {
        this.countPackage = countPackage;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getSend_uid() {
        return send_uid;
    }

    public void setSend_uid(String send_uid) {
        this.send_uid = send_uid;
    }

    public String getReceive_uid() {
        return receive_uid;
    }

    public void setReceive_uid(String receive_uid) {
        this.receive_uid = receive_uid;
    }

    public long getSend_time() {
        return send_time;
    }

    public void setSend_time(long send_time) {
        this.send_time = send_time;
    }

    public long getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(long receive_time) {
        this.receive_time = receive_time;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public double getJingdu() {
        return jingdu;
    }

    public void setJingdu(double jingdu) {
        this.jingdu = jingdu;
    }

    public double getWeidu() {
        return weidu;
    }

    public void setWeidu(double weidu) {
        this.weidu = weidu;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
