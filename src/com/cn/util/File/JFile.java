package com.cn.util.File;


import com.cn.bean.TTracksEntity;
import com.cn.bean.TTracksPointsEntity;
import com.cn.dao.DBHelper;
import com.cn.test.TestOutput;
import com.cn.util.Config;
import com.cn.util.DBUtil;
import com.cn.util.TableName;
import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by SNNU on 2014/11/5.
 */
/**
 * 对上传文件后文件的解压，读取以及保存。采用线程防止阻塞上传
 */
public class JFile extends Thread {
    static private Queue<String> uploadFilePathQueue = new LinkedList<String>();
    static private Boolean isQueueEmpty = true;

    static public void addUploadFilePath(String filePath)
    {
        uploadFilePathQueue.add(filePath);
        synchronized (isQueueEmpty) {
            if (isQueueEmpty) {
                isQueueEmpty = false;
                new JFile().start();
            }
        }
    }


    @Override
    public void run() {
        while (true) {
            //解压文件
            String kmzFileName = uploadFilePathQueue.poll();
            TestOutput.println("jfile:" + kmzFileName);
            String unZipFileName = FileUtil.getDirFromKmzName(kmzFileName);
            try {
                long upZipFileSize = new JUnZipFile().workAndReturnUnZipFileSize(kmzFileName, unZipFileName);
                try {
                    String nestDir = FileUtil.getNestDir(unZipFileName);
                    TTracksEntity tracksEntity = new XmlParser(nestDir + Config.KMZFileInfo.trackDetailFileName).getTrackEntity();
                    tracksEntity.setPath(FileUtil.removeLastSeparator(unZipFileName));
                    tracksEntity.setFilesize((int) upZipFileSize);
                    tracksEntity.setTrackid(DBUtil.insertAndReturnAutoIncreaseId(
                            DBHelper.getInsertSql(TableName.tracks, tracksEntity),
                            DBHelper.getSqlValues(tracksEntity)));

                    String sql = null;
                    List sqlValueList = new ArrayList();
                    List<List<TTracksPointsEntity>> pointList = new KmlParser(nestDir + Config.KMZFileInfo.routeRecordFileName).getPoints();
                    for (List<TTracksPointsEntity> pointsList : pointList) {
                        for (TTracksPointsEntity point : pointsList) {
                            point.setTrackid(tracksEntity.getTrackid());
                            sqlValueList.add(DBHelper.getSqlValues(point));
                            TestOutput.println(point);
                            if (sql == null) {
                                sql = DBHelper.getInsertSql(TableName.trackPoint, point);
                            }
                        }
                    }

                    TestOutput.println(sql);
                    if (sql != null) {
                        DBUtil.insertBatch(sql, sqlValueList);
                    }
                } catch (DocumentException e) {
                    TestOutput.println(e.getMessage());
                    e.printStackTrace();
                    new File(kmzFileName).delete();
                }
            } catch (IOException e) {
                TestOutput.println(e.getMessage());
                e.printStackTrace();
                new File(kmzFileName).delete();
            }

            synchronized (isQueueEmpty) {
                if (uploadFilePathQueue.isEmpty()) {
                    isQueueEmpty = true;
                    break;
                }
            }
        }
    }
}
