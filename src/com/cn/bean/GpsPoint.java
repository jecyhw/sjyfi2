package com.cn.bean;

/**
 * Created by SNNU on 2015/5/7.
 */
public class GpsPoint {
    Double longitude;
    Double latitude;
    Double altitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "GpsPoint{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                '}';
    }
}
