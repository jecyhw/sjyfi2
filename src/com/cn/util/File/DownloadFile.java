package com.cn.util.File;

import com.cn.test.TestOutput;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by acm on 12/13/14.
 */
public class DownloadFile {
    public boolean work(HttpServletResponse response, String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            response.setContentType("application/x-download");
            try {
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(
                        fileName.substring(fileName.lastIndexOf("/") + 1).getBytes("UTF-8"), "ISO8859-1"));
                OutputStream outputStream = response.getOutputStream();
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] b = new byte[1024];
                int len;
                while ((len = fileInputStream.read(b)) > -1) {
                    outputStream.write(b, 0, len);
                }
                outputStream.flush();
                outputStream.close();
                return true;
            } catch (UnsupportedEncodingException e) {
                TestOutput.println(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                TestOutput.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }
}
