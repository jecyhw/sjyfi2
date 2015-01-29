package com.cn.bean;

import java.sql.Timestamp;

/**
 * Created by jecyhw on 2014/10/12.
 */
public class TSjyfiVtpEntity {
    private int id;
    private int vssbId;
    private String sampleSiteId;
    private String siteCode;
    private String vegeName;
    private String level;
    private Float avgheight;
    private Integer number;
    private Float dbh;
    private Float projectCoverage;
    private String lifeForm;
    private String branchForm;
    private String lifeStatus;
    private String phenophase;
    private String remark;
    private Timestamp time;

    @Override
    public String toString() {
        return "TSjyfiVtpEntity{" +
                "id=" + id +
                ", vssbId=" + vssbId +
                ", sampleSiteId='" + sampleSiteId + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", vegeName='" + vegeName + '\'' +
                ", level='" + level + '\'' +
                ", avgheight=" + avgheight +
                ", number=" + number +
                ", dbh=" + dbh +
                ", projectCoverage=" + projectCoverage +
                ", lifeForm='" + lifeForm + '\'' +
                ", branchForm='" + branchForm + '\'' +
                ", lifeStatus='" + lifeStatus + '\'' +
                ", phenophase='" + phenophase + '\'' +
                ", remark='" + remark + '\'' +
                ", time=" + time +
                '}';
    }
}
