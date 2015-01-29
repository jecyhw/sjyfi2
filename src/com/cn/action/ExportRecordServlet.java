package com.cn.action;

import com.cn.bean.TTracksEntity;
import com.cn.dao.AEntityDao;
import com.cn.dao.TTracksDao;
import com.cn.util.*;
import com.cn.util.File.DownloadFile;
import com.cn.util.File.FileMerge;
import com.cn.util.File.FileUtil;
import com.cn.util.File.JZipFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by acm on 12/8/14.
 */
public class ExportRecordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List ids = (List) Json.read(request.getParameter("ids"), List.class);

        AEntityDao dao = new TTracksDao();
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("select * from ").append(dao.tableName).append(" where trackid in (?");
        for (int i = ids.size() - 1; i > 0; i--) {//生成sql语句
            sbSql.append(", ?");
        }
        sbSql.append(");");

        List entityList = DBUtil.queryBatch(dao, sbSql.toString(), ids);//查询
        String zipFileName;
        DownloadFile downloadFile = new DownloadFile();
        if (entityList.size() > 0) {
            if (entityList.size() == 1) {
                String tmp = ((TTracksEntity) entityList.get(0)).getPath();
                zipFileName = FileUtil.removeLastSeparator(tmp) + ".kmz";
                downloadFile.work(response, zipFileName);
            } else {
                List<String> paths = new ArrayList<String>();
                for (Object entity : entityList) {
                    paths.add(FileUtil.getNestDir(((TTracksEntity) entity).getPath()));
                }

                String fileName = "routeRecord_" + DateUtil.date2String(new Date(),
                        new SimpleDateFormat("yyyyMMdd_HHmmss"));//生成合并文件所在的文件名
                String mergeFileName = Config.unZipFileDir + fileName;//完整路径，和解压kmz的文件目录一致
                FileMerge merge = new FileMerge();
                merge.work(paths, mergeFileName);

                zipFileName = Config.zipFileDir + fileName + ".kmz";
                new JZipFile().work(mergeFileName, zipFileName);
                downloadFile.work(response, zipFileName);
                FileUtil.deleteFile(mergeFileName);//压缩完毕后删除合并的文件,以及压缩文件
                FileUtil.deleteFile(zipFileName);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
