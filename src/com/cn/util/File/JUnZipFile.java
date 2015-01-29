package com.cn.util.File;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.Enumeration;

/**
 * Created by SNNU on 2014/11/13.
 */
public class JUnZipFile {

    private OutputStream os = null;
    private InputStream is = null;
    private ZipFile zipFile = null;
    private ZipEntry zipEntry = null;
    private long unZipFileSize = 0;
    private String unZipFilePath;

    /**
     *
     * @param inputFileName 要解压的文件名
     * @param unZipFileName 解压的目录
     * @return 返回解压后文件的大小
     */
    public long workAndReturnUnZipFileSize(String inputFileName, String unZipFileName) throws IOException {
        unZipFileName = FileUtil.addSeparator(unZipFileName);
        return work(inputFileName, unZipFileName);
    }

    private long work(String inputFileName, String unZipFileName) throws IOException {
        zipFile = new ZipFile(inputFileName, "gbk");
        Enumeration<ZipEntry> entryEnum = zipFile.getEntries();
        while (entryEnum.hasMoreElements()) {
            zipEntry = entryEnum.nextElement();
            unZipFileSize += zipEntry.getSize();
            unZipFilePath = unZipFileName + zipEntry.getName();
            if (zipEntry.isDirectory()) {
                directory();
            } else {
                file();
            }
        }
        zipFile.close();

        return unZipFileSize;
    }

    private void directory() {
        FileUtil.CreateDirIFNotExist(unZipFilePath);
    }

    private void file() throws IOException {
        File newFile = new File(unZipFilePath);
        os = new BufferedOutputStream(new FileOutputStream(newFile));
        is = zipFile.getInputStream(zipEntry);
        byte[] buffer = new byte[1024];
        int readLen = 0;
        while ((readLen = is.read(buffer)) >= 0) {
            os.write(buffer, 0, readLen);
        }
        is.close();
        os.close();

    }

    static public void main(String[] args) {
        try {
            new JUnZipFile().workAndReturnUnZipFileSize("/var/gpstracks/routeRecord_20140408_122844.kmz",
                    "/home/acm/routeRecord_20140408_122844");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
