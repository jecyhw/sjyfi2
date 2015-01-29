package com.cn.action;

import com.cn.bean.TTracksEntity;
import com.cn.dao.AEntityDao;
import com.cn.dao.TTracksDao;
import com.cn.test.TestOutput;
import com.cn.util.Config;
import com.cn.util.DBUtil;
import com.cn.util.File.FileUtil;
import com.cn.util.File.KmlParser;
import com.cn.util.Out;
import org.dom4j.DocumentException;

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
 * Created by acm on 11/27/14.
 */
public class RouteRecordMapInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List values = new ArrayList();
        values.add(request.getParameter("id"));

        AEntityDao dao = new TTracksDao();
        String sql ="select * from " + dao.tableName + " where trackid = ?";

        TTracksEntity entity = (TTracksEntity) DBUtil.query(dao, sql, values);

        Out out = new Out(response);
        Map<String, Object> result = new Hashtable<String, Object>();
        if (entity != null)
        {
            String path = entity.getPath();
            String fileName = FileUtil.getNestDir(path) + Config.KMZFileInfo.routeRecordFileName;
            try {
                result = new KmlParser(fileName).getMapInfo();
            } catch (DocumentException e) {
                TestOutput.println(e.getMessage());
                e.printStackTrace();
            }
        }
        out.printJson(result);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
