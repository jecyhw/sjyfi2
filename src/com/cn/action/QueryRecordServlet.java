package com.cn.action;

import com.cn.bean.ConditionEntity;
import com.cn.dao.TTracksDao;
import com.cn.test.TestOutput;
import com.cn.util.DBUtil;
import com.cn.util.Json;
import com.cn.util.Out;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jecyhw on 2014/10/16.
 */
public class QueryRecordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConditionEntity conditionEntity = (ConditionEntity) Json.read(request.getParameter("data"), ConditionEntity.class);
        Out out = new Out(response);
        out.printJson(DBUtil.queryMulti(new TTracksDao(), conditionEntity.getSql(), conditionEntity.getSqlValues()));
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
