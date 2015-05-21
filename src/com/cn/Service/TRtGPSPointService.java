package com.cn.service;

import com.cn.bean.SjyfiUserEntity;
import com.cn.util.*;
import com.cn.websocket.ShowUserByRealTime;
import com.cn.bean.TrtGpsPointEntity;
import com.cn.dao.DBHelper;
import com.cn.dao.SjyfiUserDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by SNNU on 2015/5/5.
 */
public class TRtGPSPointService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msg = null;
        int msgCode = 0;
        Map<String, Object> result = new HashMap<String, Object>();

        TrtGpsPointEntity entity = new TrtGpsPointEntity();
        try {
            entity.setUid(Integer.valueOf(request.getParameter("uid")));
            entity.setLongitude(Double.valueOf(request.getParameter("longitude")));
            entity.setLatitude(Double.valueOf(request.getParameter("latitude")));
            entity.setAltitude(Double.valueOf(request.getParameter("altitude")));
            entity.setTime(Timestamp.valueOf(request.getParameter("time")));
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.MILLISECOND, 0);
            if (entity.getTime().getTime() <= cal.getTimeInMillis()) {
                msg = "time日期已经过时";
            }

        } catch (Exception e) {
            if (entity.getUid() == null) {
                msg = "uid值缺失或者格式不对(必须为整型)";
            } else if (entity.getLongitude() == null) {
                msg = "longitude值缺失或者格式不对(必须为数值型)";
            } else if (entity.getLatitude() == null) {
                msg = "latitude值缺失或者格式不对(必须为数值型)";
            } else if (entity.getAltitude() == null) {
                msg = "altitude值缺失或者格式不对(必须为数值型)";
            } else if (entity.getTime() == null) {
                msg = "time值缺失或者格式不对(必须为日期类型，格式为yyyy-MM-dd HH:mm:ss)";
            }
        }

        if (msg == null) {
            SjyfiUserDao userDao = new SjyfiUserDao();
            String sql = "select name, role from " + TableName.getUser() + " where uid = ?";
            List valList = new ArrayList();
            valList.add(entity.getUid());
            Object userEntity = DBUtil.query(new SjyfiUserDao(), sql, valList);
            if (userEntity == null) {
                msg = "uid对应的用户不存在";
                msgCode = 1;
            }
            else if (DBUtil.insert(DBHelper.getInsertSql(TableName.gettRtGpsPoint(), entity), DBHelper.getSqlValues(entity)) > 0) {
                msg = "success";
                List updateGpsList = new ArrayList();
                entity.setName(((SjyfiUserEntity)userEntity).getName());
                updateGpsList.add(entity);
                ShowUserByRealTime.broadcast(Json.writeAsString(updateGpsList));
            } else {
                msg = "数据库执行出错";
            }
        } else {
            msgCode = 1;
        }
        result.put("status", msgCode);
        result.put("result", msg);

        Out out = new Out(response);
        out.printJson(result);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
