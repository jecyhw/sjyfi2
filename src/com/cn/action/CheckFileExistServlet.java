package com.cn.action;

import com.cn.dao.CheckFileExistDao;
import com.cn.util.DBUtil;
import com.cn.util.Out;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 12/19/14.
 */
public class CheckFileExistServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CheckFileExistDao dao = new CheckFileExistDao();
        List list = new ArrayList();
        String fileName = request.getParameter("filename");
        list.add("?" + fileName.substring(0, fileName.length() - 4) + "?");//去电后缀 .kmz
        String sql = "select count(*) from t_tracks where path like ?";
        Out out = new Out(response);
        if (null != DBUtil.query(dao, sql, list)) {
            out.printText("1");
        } else {
            out.printText("0");
        }
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
