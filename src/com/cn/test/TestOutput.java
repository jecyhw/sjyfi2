package com.cn.test;

import java.io.*;
import java.util.Date;

/**
 * Created by jecyhw on 2014/10/16.
 */
public class TestOutput {
    static private String outPutFileName = System.getProperty("user.dir") + "/log.txt";
    static private Boolean isOutPutFile = true;
    static private String lineSeparator = System.getProperty("line.separator");
    static {
        File file = new File(outPutFileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    static public void println(Object object)
    {
        if (isOutPutFile) {
            try {
                FileWriter writer = new FileWriter(outPutFileName, true);
                writer.write(new Date().toString() + "::" + object + lineSeparator);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(object);
        }
    }
}
