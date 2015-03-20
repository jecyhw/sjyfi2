package com.cn.util;

import com.cn.test.TestOutput;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by jecyhw on 2014/10/18.
 */
public class Json {
    private static ObjectMapper objectMapper;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    static {
        objectMapper = new ObjectMapper();
        objectMapper.getSerializationConfig().setDateFormat(dateFormat);
        objectMapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
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

    public static void main(String[] args)
    {
        List list = (List<Integer>)Json.read("[1,3]", List.class);
        TestOutput.println(list.get(1));
    }
}
