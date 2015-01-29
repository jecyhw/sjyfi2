package com.cn.util.File;

import com.cn.bean.TTracksEntity;
import com.cn.bean.TTracksPointsEntity;
import com.cn.test.TestOutput;
import com.cn.util.Config;
import com.cn.util.DateUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by SNNU on 2014/11/15.
 */
public class FileParser {
    protected Element root;
    protected String fileName;

    protected FileParser(String fileName) throws DocumentException {
        this.fileName = fileName;
        root = new SAXReader().read(fileName).getRootElement();
    }

    public void save() {
        try {
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(fileName));
            xmlWriter.write(root.getDocument());
        } catch (UnsupportedEncodingException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public Element getElementRoot() {
        return root;
    }

    static public void main(String[] args) {
        File file = new File("/home/acm/routeRecord_20140408_122844/routeRecord_20140408_122844");
        TestOutput.println(file.getAbsolutePath());

    }
}
