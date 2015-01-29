package com.cn.bean;

import java.sql.Timestamp;

/**
 * Created by jecyhw on 2014/10/12.
 */
public class TSjyfiVssbEntity {
    private int vssbId;
    private int sampleSiteId;
    private String site;
    private String siteCode;
    private float altitude;
    private float longitude;
    private float latitude;
    private String adminRegion;
    private String adminCode;
    private String subReserve;
    private String funcArea;
    private String projType;
    private String vegeType;
    private Float ssArea;
    private Float coverage;
    private String soileroType;
    private String soileroDegree;
    private String surveyIndex;
    private String surveyMethod;
    private String imageClose;
    private String imageLong;
    private String remark;
    private String org;
    private String investigator;
    private int uid;
    private String geomorphicType;
    private String geologicalMatrix;
    private String humaneffectType;
    private String humaneffectDegree;
    private String animaleffectType;
    private String animaleffectDegree;
    private String soilType;
    private String soilFeature;
    private Timestamp time;

    @Override
    public String toString() {
        return "TSjyfiVssbEntity{" +
                "vssbId=" + vssbId +
                ", sampleSiteId=" + sampleSiteId +
                ", site='" + site + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", altitude=" + altitude +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", adminRegion='" + adminRegion + '\'' +
                ", adminCode='" + adminCode + '\'' +
                ", subReserve='" + subReserve + '\'' +
                ", funcArea='" + funcArea + '\'' +
                ", projType='" + projType + '\'' +
                ", vegeType='" + vegeType + '\'' +
                ", ssArea=" + ssArea +
                ", coverage=" + coverage +
                ", soileroType='" + soileroType + '\'' +
                ", soileroDegree='" + soileroDegree + '\'' +
                ", surveyIndex='" + surveyIndex + '\'' +
                ", surveyMethod='" + surveyMethod + '\'' +
                ", imageClose='" + imageClose + '\'' +
                ", imageLong='" + imageLong + '\'' +
                ", remark='" + remark + '\'' +
                ", org='" + org + '\'' +
                ", investigator='" + investigator + '\'' +
                ", uid=" + uid +
                ", geomorphicType='" + geomorphicType + '\'' +
                ", geologicalMatrix='" + geologicalMatrix + '\'' +
                ", humaneffectType='" + humaneffectType + '\'' +
                ", humaneffectDegree='" + humaneffectDegree + '\'' +
                ", animaleffectType='" + animaleffectType + '\'' +
                ", animaleffectDegree='" + animaleffectDegree + '\'' +
                ", soilType='" + soilType + '\'' +
                ", soilFeature='" + soilFeature + '\'' +
                ", time=" + time +
                '}';
    }
}
