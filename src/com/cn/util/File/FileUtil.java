package com.cn.util.File;

import com.cn.test.TestOutput;

import java.io.File;
import java.io.IOException;

/**
 * Created by SNNU on 2014/11/13.
 */
public final class FileUtil {
    public static final String backslash = "/";
    public static File CreateDirIFNotExist(String dir) {
        File file = new File(dir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        return  file;
    }

    public static String addSeparator(String dir) {
        if (!dir.endsWith(backslash))
            dir += backslash;
        return dir;
    }

    public static String removeLastSeparator(String dir) {
        if (dir.endsWith(backslash)) {
            return dir.substring(0, dir.length() - 1);
        }
        return dir;
    }
    /**
     * 如果解压后的文件夹嵌套了文件夹，则返回嵌套的文件目录,文件路径最后带/
     * @param dir
     * @return
     */
    public static String getNestDir(String dir) {
        File dirFile = new File(dir);
        dir = dirFile.getAbsolutePath();
        if (dirFile.exists()) {
            String dirName = dir + backslash;
            String fileName = dirFile.getName();
            while (true) {
                File nestDir = new File(dirName + fileName);
                if (nestDir.exists() && nestDir.isDirectory()) {
                      dirName += fileName + backslash;
                } else {
                    return dirName;
                }
            }
        }
        try {
            new JUnZipFile().workAndReturnUnZipFileSize(dir + ".kmz", dir);
            return getNestDir(dir);
        } catch (IOException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
            return dir;
        }
    }

    /**
     * 去掉.kmz文件名的后缀名,并且以/结尾的路径名
     * @param kmzFileName
     * @return
     */
    public static String getDirFromKmzName(String kmzFileName) {
        return kmzFileName.substring(0, kmzFileName.lastIndexOf('.')) + backslash;
    }

    /**
     * 对合并后的文件提供删除功能
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return deleteFile(new File(fileName));
        }
        return false;
    }

    private static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (File child : fileList) {
                if (!deleteFile(child))
                    return false;
            }
        }
        return file.delete();
    }

    public static void main(String[] args) {
        //copyFile(new File("/home/acm/markers.png"), new File("/home/acm/1.png"));
    }
}
