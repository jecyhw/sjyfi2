package com.cn.bean;

import java.sql.Timestamp;

/**
 * Created by jecyhw on 2014/10/12.
 */
public class TSjyfiAidEntity {
    private int id;
    private int aibid;
    private String animalName;
    private Integer quantity;
    private String behavior;
    private String enviroment;
    private String site;
    private float longitude;
    private float latitude;
    private float altitude;
    private String remark;
    private Timestamp time;

    @Override
    public String toString() {
        return "TSjyfiAidEntity{" +
                "id=" + id +
                ", aibid=" + aibid +
                ", animalName='" + animalName + '\'' +
                ", quantity=" + quantity +
                ", behavior='" + behavior + '\'' +
                ", enviroment='" + enviroment + '\'' +
                ", site='" + site + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                ", remark='" + remark + '\'' +
                ", time=" + time +
                '}';
    }
}
