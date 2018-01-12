package com.linjun.projectsend.common;

/**
 * Created by linjun on 2018/1/5.
 */

public class DataModel {
    private  long timestamp;
    private  Double longitude;
    private  Double dimensionality;
    private String country;
    private String city;
    private  String province;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getDimensionality() {
        return dimensionality;
    }

    public void setDimensionality(Double dimensionality) {
        this.dimensionality = dimensionality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
