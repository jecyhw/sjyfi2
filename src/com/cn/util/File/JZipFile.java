package com.cn.util.File;

import com.cn.test.TestOutput;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by SNNU on 2014/11/13.
 */
public class JZipFile {
    /**
     *
     * @param inputFileName 要压缩的文件名
     * @param zipFileName  压缩后的文件名
     */
    public void work(String inputFileName, String zipFileName) {
        work(zipFileName, new File(inputFileName));
    }

    private void work(String inputFileName, File file) {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(inputFileName));
            work(out, file, "");
        } catch (FileNotFoundException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    TestOutput.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void work(ZipOutputStream out, File file, String base) {
        if (file.isDirectory()) {
            File[] zipFiles = file.listFiles();
            try {
                out.putNextEntry(new ZipEntry(base + FileUtil.backslash));
                base = base.length() == 0 ? "" : base + FileUtil.backslash;
                for (File zipFile : zipFiles) {
                    work(out, zipFile, base + zipFile.getName());
                }
            } catch (IOException e) {
                TestOutput.println(e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            FileInputStream in = null;
            try {
                out.putNextEntry(new ZipEntry(base));
                in = new FileInputStream(file);
                int len;
                while ((len = in.read()) > -1) {
                    out.write(len);
                }

            } catch (IOException e) {
                TestOutput.println(e.getMessage());
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        TestOutput.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static public void main(String[] args) {
        new JZipFile().work("/var/gpstracks/routeRecord_20140408_122844", "/home/acm/routeRecord_20140408_122844.kmzz");
    }
}
