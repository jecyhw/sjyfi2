package com.cn.dao;

import com.cn.bean.TrtGpsPointEntity;
import com.cn.util.TableName;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by SNNU on 2015/5/6.
 */
public class TrtGpsPointDao extends AEntityDao {
    public TrtGpsPointDao() {
        this.tableName = TableName.tRtGpsPoint;
    }
    @Override
    public Object getEntity(ResultSet set) throws SQLException {
        TrtGpsPointEntity entity = new TrtGpsPointEntity();
        entity.setUid(set.getInt("uid"));
        entity.setLongitude(set.getDouble("longitude"));
        entity.setLatitude(set.getDouble("latitude"));
        entity.setAltitude(set.getDouble("altitude"));
        entity.setTime(set.getTimestamp("time"));
        entity.setName(set.getString("name"));
        return entity;
    }
}
