package com.cn.bean;

import java.sql.Timestamp;

/**
 * Created by jecyhw on 2014/10/12.
 */

class TSjyfiAibEntity {
    private int aibid;
    private int sampLineNo;
    private String weather;
    private Timestamp time;
    private String observer;
    private String recorder;
    private int uid;
    private String place;

    @Override
    public String toString() {
        return "TSjyfiAibEntity{" +
                "aibid=" + aibid +
                ", sampLineNo=" + sampLineNo +
                ", weather='" + weather + '\'' +
                ", time=" + time +
                ", observer='" + observer + '\'' +
                ", recorder='" + recorder + '\'' +
                ", uid=" + uid +
                ", place='" + place + '\'' +
                '}';
    }
}
