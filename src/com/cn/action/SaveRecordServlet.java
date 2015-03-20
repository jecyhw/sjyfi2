package com.cn.action;

import com.cn.bean.TTracksEntity;
import com.cn.dao.AEntityDao;
import com.cn.dao.DBHelper;
import com.cn.dao.TTracksDao;
import com.cn.test.TestOutput;
import com.cn.util.*;
import com.cn.util.File.*;
import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by acm on 12/8/14.
 */
public class SaveRecordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List ids = (List)Json.read(request.getParameter("ids"), List.class);

        AEntityDao dao = new TTracksDao();
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("select * from ").append(dao.tableName).append(" where trackid in (?");
        for (int i = ids.size() - 1; i > 0; i--) {//生成sql语句
            sbSql.append(", ?");
        }
        sbSql.append(")");

        List entityList = DBUtil.queryBatch(dao, sbSql.toString(), ids);//查询

        int mergeFileSize = 0;
        List<String> paths = new ArrayList<String>();
        for (Object entity : entityList) {
            paths.add(FileUtil.getNestDir(((TTracksEntity) entity).getPath()));//再获取下一层路径，因为轨迹文件在解压是包多含了一层目录
            mergeFileSize += ((TTracksEntity) entity).getFilesize();
        }

        String fileName = "routeRecord_" + DateUtil.date2String(new Date(),
                new SimpleDateFormat("yyyyMMdd_HHmmss"));//生成合并文件所在的文件名

        String mergeFileName = Config.unZipFileDir + fileName;
        FileMerge merge = new FileMerge();
        merge.work(paths, mergeFileName);

        String zipFileName = Config.zipFileDir + fileName + ".kmz";
        new JZipFile().work(mergeFileName, zipFileName);
        Map<String, String> result = new Hashtable<String, String>();
        try {
            TrackDetailFileParse fileParse = new TrackDetailFileParse();
            new JSAXParser().parse(FileUtil.getNestDir(mergeFileName) + Config.KMZFileInfo.trackDetailFileName, fileParse);
            TTracksEntity entity = (TTracksEntity) fileParse.getParseObject();
            entity.setFilesize(mergeFileSize);
            entity.setPath(mergeFileName);
            DBUtil.insert(DBHelper.getInsertSql(TableName.tracks, entity), DBHelper.getSqlValues(entity));
            result.put("result", "true");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        Out out = new Out(response);
        out.printJson(result);
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
