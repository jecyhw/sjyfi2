package com.cn.util.File;

import com.cn.test.TestOutput;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by SNNU on 2014/11/13.
 */
public class JUnZipFile {
    /**
     *
     * @param inputFileName 要解压的文件名
     * @param unZipFileName 解压的目录
     * @return 返回解压后文件的大小
     */
    public void work(String inputFileName, String unZipFileName) {
        try {
        unZipFileName = FileUtil.addSeparator(unZipFileName);
        InputStream in = new BufferedInputStream(new FileInputStream(inputFileName));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    FileUtil.CreateDirIFNotExist(unZipFileName + entry.getName());
                } else {
                    unZip(zin, unZipFileName + entry.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            TestOutput.println(e.getMessage());
        }

    }

    public void unZip(ZipInputStream zin, String filePath) throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] b = new byte[2048];
        int len;
        while ((len = zin.read(b)) != -1) {
            out.write(b, 0, len);
        }
        out.close();
    }
}
