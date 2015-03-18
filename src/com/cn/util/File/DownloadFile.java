package com.cn.util.File;

import com.cn.test.TestOutput;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by acm on 12/13/14.
 */
public class DownloadFile {
    /**
     *
     * @param response
     * @param fileName 要下载的文件完整路径
     * @param isDeleteFile 是否要删除下载的源文件
     * @return
     */
    public void work(HttpServletResponse response, String fileName, boolean isDeleteFile) {
        File file = new File(fileName);
        if (file.exists()) {
            response.setContentType("application/x-download");
            try {
                response.setHeader("Content-Length", file.length() + "");
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(
                        fileName.substring(fileName.lastIndexOf("/") + 1).getBytes("UTF-8"), "ISO8859-1"));
                BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                byte[] b = new byte[2048];
                int len;
                while ((len = inputStream.read(b)) != -1) {
                    outputStream.write(b, 0, len);
                }
                inputStream.close();
                outputStream.close();
            } catch (UnsupportedEncodingException e) {
                TestOutput.println(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                TestOutput.println(e.getMessage());
                e.printStackTrace();
            } finally {
                if (isDeleteFile) {
                    FileUtil.deleteFile(fileName);
                }
            }
        }
    }
}
