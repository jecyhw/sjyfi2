package com.cn.dao;

import com.cn.util.TableName;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by SNNU on 2014/11/16.
 */
public class TTracksPointsDao extends AEntityDao {
    public TTracksPointsDao() {
        tableName = TableName.trackPoint;
    }
    
    @Override
    public Object getEntity(ResultSet set) throws SQLException {
        return null;
    }
}
