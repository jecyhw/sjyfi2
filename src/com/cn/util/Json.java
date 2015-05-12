package com.cn.util;

import com.cn.bean.ConditionEntity;
import com.cn.dao.TrtGpsPointDao;
import com.cn.dao.UserHistoryDao;
import com.cn.test.TestOutput;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jecyhw on 2014/10/18.
 */
public class Json {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(DateUtil.dateFormat);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static void write(Writer writer, Object value)
    {
        try {
            objectMapper.writeValue(writer, value);
        } catch (IOException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void write(OutputStream outputStream, Object value) {
        try {
            objectMapper.writeValue(outputStream, value);

        } catch (IOException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
    }


    public static String writeAsString(Object value) {
        String result = "[]";
        try {
            result = objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object read(String jsonString, Class clsName)
    {

        Object obj = null;
        try {
            obj = objectMapper.readValue(jsonString, clsName);
        } catch (IOException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    public static void main(String[] args) throws IOException {
        String sql = "select " + TableName.tRtGpsPoint + ".*, " + TableName.sjyfiUser + ".name from " + TableName.tRtGpsPoint
                + "," + TableName.sjyfiUser + " where " + TableName.sjyfiUser + ".name like  ? and "
                + TableName.tRtGpsPoint + ".time >= \"1997-05-08\" and "
                + TableName.sjyfiUser + ".uid=" + TableName.tRtGpsPoint + ".uid";
        List uidList = new ArrayList();
        uidList.add("%" + 1 + "%");
        System.out.println(writeAsString(DBUtil.queryMulti(new UserHistoryDao(), sql, uidList)));
    }
}
