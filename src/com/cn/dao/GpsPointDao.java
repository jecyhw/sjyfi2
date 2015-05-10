package com.cn.dao;

import com.cn.bean.GpsPoint;
import com.cn.bean.TrtGpsPointEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by SNNU on 2015/5/7.
 */
public class GpsPointDao extends AEntityDao {
    @Override
    public Object getEntity(ResultSet set) throws SQLException {
        GpsPoint entity = new GpsPoint();
        entity.setLongitude(set.getDouble("longitude"));
        entity.setLatitude(set.getDouble("latitude"));
        entity.setAltitude(set.getDouble("altitude"));
        return entity;
    }
}
