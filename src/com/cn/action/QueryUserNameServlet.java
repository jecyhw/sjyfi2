package com.cn.action;

import com.cn.dao.GetUserNameDao;
import com.cn.dao.TrtGpsPointDao;
import com.cn.util.DBUtil;
import com.cn.util.Out;
import com.cn.util.TableName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by SNNU on 2015/5/11.
 */
public class QueryUserNameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msg = null;
        int msgCode = 0;
        Map<String, Object> result = new Hashtable<String, Object>();
        GetUserNameDao dao = new GetUserNameDao();
        String sql = "select name from " + TableName.gettRtGpsPointNew();//默认获取全部用户的更新信息
        List uidList = new ArrayList();
        String name = request.getParameter("name");
        if (name != null) {
            sql += " where name like ?";
            uidList.add("%" + name + "%");
        } else {
            msg = "name不能为空";
            msgCode = 1;
        }
        if (msg == null) {
            result.put("result", DBUtil.queryMulti(dao, sql, uidList));
        } else {
            result.put("result", msg);
        }
        result.put("status", msgCode);

        Out out = new Out(response);
        out.printJson(result);
        out.close();
    }
}
