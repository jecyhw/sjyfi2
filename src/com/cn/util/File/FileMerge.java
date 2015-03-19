package com.cn.util.File;

import com.cn.test.TestOutput;
import org.dom4j.DocumentException;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;

/**
 * Created by acm on 12/8/14.
 */
public class FileMerge {
    /**
     * 合并轨迹文件，kml和xml文件则一定格式进行内容合并
     *
     * @param fileNameList  要合并的文件名列表
     * @param mergeFileName 合并后的新文件所在目录
     */
    public void work(List<String> fileNameList, String mergeFileName) {
        for (String fileName : fileNameList) {
            copyFolder(fileName, mergeFileName);
        }
    }

    private void copyFolder(String source, String dest) {
        File fileSource = new File(source);
        if (fileSource.exists()) {
            File fileDest = new File(dest);
            copyFolder(fileSource, fileDest);
        }
    }

    private void copyFolder(File source, File dest) {
        if (source.isDirectory()) {
            dest.mkdirs();
            File[] fileList = source.listFiles();
            for (File file : fileList) {
                copyFolder(file, new File(dest.getAbsolutePath() + FileUtil.backslash + file.getName()));
            }
        } else {
            if (dest.exists()) {
                appendFile(source, dest);
            } else {
                copyFile(source, dest);
            }
        }
    }

    private void copyFile(File source, File dest) {
        try {
            BufferedInputStream inputBuffered = new BufferedInputStream(new FileInputStream(source));
            BufferedOutputStream outputBuffered = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputBuffered.read(bytes)) > -1) {
                outputBuffered.write(bytes, 0, len);
            }
            outputBuffered.close();
            inputBuffered.close();
        } catch (FileNotFoundException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void appendFile(File source, File dest) {
        if (source.getName().contains(".kml")) {
            kmlAppend(source, dest);
        } else if (source.getName().contains(".xml")) {
            xmlAppend(source, dest);
        }
    }

    private void kmlAppend(File source, File dest) {
        try {
            Result resultXml = new StreamResult(new FileOutputStream(new File("")));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            new KmlParser(dest.getAbsolutePath()).append(new KmlParser(source.getAbsolutePath()));
        } catch (DocumentException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void xmlAppend(File source, File dest) {
        try {

            new XmlParser(dest.getAbsolutePath()).append(new XmlParser(source.getAbsolutePath()));
        } catch (DocumentException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
    }

    static public void main(String[] args) {
        Object obj = null;

        String s = (String) obj;

    }
}
