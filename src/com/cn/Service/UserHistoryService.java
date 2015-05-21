package com.cn.service;

import com.cn.dao.TrtGpsPointDao;
import com.cn.dao.UserHistoryDao;
import com.cn.util.DBUtil;
import com.cn.util.Out;
import com.cn.util.TableName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by SNNU on 2015/5/6.
 */
public class UserHistoryService extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msg;
        int msgCode = 0;
        Map<String, Object> result = new Hashtable<String, Object>();
        String name = request.getParameter("name");
        if (name != null) {
            String sql = "select " + TableName.gettRtGpsPoint() + ".*, " + TableName.getUser() + ".name from " + TableName.gettRtGpsPoint()
                    + "," + TableName.getUser() + " where " + TableName.getUser() + ".name like  ? and "
                    + TableName.gettRtGpsPoint() + ".time >= curdate() and "
                    + TableName.getUser() + ".uid=" + TableName.gettRtGpsPoint() + ".uid"
                    +" order by " + TableName.gettRtGpsPoint() + ".uid, " + TableName.gettRtGpsPoint() + ".time";
            List uidList = new ArrayList();
            uidList.add("%" + name + "%");
            result.put("result", DBUtil.queryMulti(new UserHistoryDao(), sql, uidList));
        }
        else {
            msg = "name参数不能为空";
            msgCode = 1;
            result.put("result", msg);
        }

        result.put("status", msgCode);
        Out out = new Out(response);
        out.printJson(result);
        out.close();
    }
}
