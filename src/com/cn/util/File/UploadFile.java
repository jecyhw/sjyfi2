package com.cn.util.File;

import com.cn.test.TestOutput;
import com.cn.util.Config;
import com.cn.util.Out;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SNNU on 2014/11/5.
 */
public class UploadFile {
    private ServletFileUpload upload = null;
    static DiskFileItemFactory factory = new DiskFileItemFactory();

    static {
        factory.setRepository(FileUtil.CreateDirIFNotExist(Config.fileUploadTempDir));
    }

    static {
        FileUtil.CreateDirIFNotExist(Config.fileUploadDir);
    }

    public void work(HttpServletRequest request, HttpServletResponse response) {
        Out out = new Out(response);
        if (ServletFileUpload.isMultipartContent(request)) {
            upload = new ServletFileUpload(factory);
            try {
                int chunk = -1, chunks = -1;//chunks表示分割的总数,chunk表示当前是哪个分割
                String fileName = null;
                List<FileItem> fileItems = upload.parseRequest(request);
                Iterator<FileItem> it = fileItems.iterator();
                while (it.hasNext()) {
                    FileItem fileItem = it.next();
                    if (fileItem.isFormField()) {
                        String fieldName = fileItem.getFieldName();
                        if (fieldName.equals("chunk"))
                            chunk = Integer.parseInt(fileItem.getString());
                        else if (fieldName.equals("chunks"))
                            chunks = Integer.parseInt(fileItem.getString());
                        else
                            fileName = fileItem.getString();
                    } else {
                        if (chunks == -1) {//没有分割文件
                            String uploadFileName = Config.fileUploadDir + fileItem.getName();
                            File uploadFile = new File(uploadFileName);
                            fileItem.write(uploadFile);
                            JFile.addUploadFilePath(uploadFileName);//上传成功加入到上传文件路径列表,以便解压
                            out.printText("文件上传成功");
                        } else {
                            fileItem.write(new File(Config.fileUploadDir + chunk + fileName));//保存分割文件
                        }
                    }
                }
                if (chunks == chunk + 1) {//合并文件
                    String uploadFileName = Config.fileUploadDir + fileName;
                    File uploadFile = new File(uploadFileName);
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(uploadFile));
                    for (chunk = 0; chunk < chunks; chunk++) {
                        File tempFile = new File(Config.fileUploadDir + chunk + fileName);
                        outputStream.write(FileUtils.readFileToByteArray(tempFile));
                        outputStream.flush();
                        tempFile.delete();
                    }
                    outputStream.close();
                    JFile.addUploadFilePath(uploadFileName);//上传成功加入到上传文件路径列表,以便解压
                    out.printText("文件上传成功");
                }
            } catch (Exception e) {
                TestOutput.println(e.getMessage());
                e.printStackTrace();
                out.printText("文件上传失败");
            }
        } else {
            out.printText("非文件上传");
        }
        out.close();
    }
}
