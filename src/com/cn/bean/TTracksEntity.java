package com.cn.bean;

import com.cn.util.DateUtil;

import java.sql.Date;

/**
 * Created by jecyhw on 2014/10/12.
 */

public class TTracksEntity {
    private Integer trackid;
    private String name;
    private String author;
    private Date starttime;
    private Date endtime;
    private Double length;
    private Double maxaltitude;
    private String keysiteslist;
    private String annotation;
    private String path;
    private Integer filesize;

    public Integer getTrackid() {
        return trackid;
    }

    public void setTrackid(Integer trackid) {
        this.trackid = trackid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getMaxaltitude() {
        return maxaltitude;
    }

    public void setMaxaltitude(Double maxaltitude) {
        this.maxaltitude = maxaltitude;
    }

    public String getKeysiteslist() {
        return keysiteslist;
    }

    public void setKeysiteslist(String keysiteslist) {
        this.keysiteslist = keysiteslist;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getFilesize() {
        return filesize;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    @Override
    public String toString() {
        return "TTracksEntity{" +
                "trackid=" + trackid +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", length=" + length +
                ", maxaltitude=" + maxaltitude +
                ", keysiteslist='" + keysiteslist + '\'' +
                ", annotation='" + annotation + '\'' +
                ", path='" + path + '\'' +
                ", filesize=" + filesize +
                '}';
    }
}
