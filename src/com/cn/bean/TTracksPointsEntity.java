package com.cn.bean;

/**
 * Created by jecyhw on 2014/10/12.
 */
public class TTracksPointsEntity {
    private Integer id;
    private Integer trackid;
    private Double longitude;
    private Double latitude;
    private Double altitude;

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getTrackid() {
        return trackid;
    }

    public void setTrackid(Integer trackid) {
        this.trackid = trackid;
    }

    @Override
    public String toString() {
        return "TTracksPointsEntity{" +
                "id=" + id +
                ", trackid=" + trackid +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                '}';
    }
}
