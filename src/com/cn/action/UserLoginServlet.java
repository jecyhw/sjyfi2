package com.cn.action;

import com.cn.bean.SjyfiUserEntity;
import com.cn.dao.AEntityDao;
import com.cn.dao.SjyfiUserDao;
import com.cn.util.DBUtil;
import com.cn.util.Out;
import com.cn.util.TableName;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by jecyhw on 2014/10/20.
 */
public class UserLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取
        List<Object> values = new ArrayList<Object>();
        values.add(request.getParameter("account"));
        values.add(request.getParameter("pwd"));

        AEntityDao dao = new SjyfiUserDao();
        String sql = "select name, role from " + TableName.sjyfiUser + " where account = ? and password = ?";

        SjyfiUserEntity entity = (SjyfiUserEntity) DBUtil.query(dao, sql, values);

        Map<String, String> result = new Hashtable<String, String>();
        if (entity == null) {
            result.put("result", "false");
        } else {
            result.put("result", "true");
            HttpSession session = request.getSession();
            session.setAttribute("userName", entity.getName());
            session.setAttribute("role", entity.getRole());
        }
        Out out = new Out(response);
        out.printJson(result);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
