package com.cn.util;

import com.cn.test.TestOutput;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SNNU on 2014/11/15.
 */
public class DateUtil {
    static private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static public Date string2Date(String dateString) {
        Date date = new Date();
        try {
             date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
        return date;
    }

    static public Date string2Date(String dateString, SimpleDateFormat dateFormat) {
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
        return date;
    }

    static public String date2String(Date date){
        return dateFormat.format(date);
    }

    static public String date2String(Date date, SimpleDateFormat dateFormat) {
        return dateFormat.format(date);
    }
}
