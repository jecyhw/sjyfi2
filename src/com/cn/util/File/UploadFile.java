package com.cn.util.File;

import com.cn.test.TestOutput;
import com.cn.util.Config;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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

    public void work(HttpServletRequest request) {
        upload = new ServletFileUpload(factory);
        try {
            String filePath;
            List<FileItem> fileItems = upload.parseRequest(request);
            Iterator<FileItem> it = fileItems.iterator();
            while (it.hasNext()) {
                FileItem fileItem = it.next();
                TestOutput.println(fileItem.getName() + ":" + fileItem.getFieldName());
                if (fileItem.getName() != null) {
                    filePath = Config.fileUploadDir + fileItem.getName();
                    fileItem.write(new File(filePath));//保存文件
                    JFile.addUploadFilePath(filePath);//上传成功加入到上传文件路径列表,以便解压
                }
            }
        } catch (Exception e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
