<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@ page import="com.cn.util.File.FileUtil" %>
<%@ page import="com.cn.util.Config" %>
<%@ page import="com.cn.test.TestOutput" %>
<%@ page import="com.cn.util.File.JFile" %>
<%@ page import="java.io.File" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.io.FileUtils" %>
<%@ page import="java.io.BufferedOutputStream" %>
<%@ page import="java.io.FileOutputStream" %>
<%--
  Created by IntelliJ IDEA.
  User: SNNU
  Date: 2015/3/18
  Time: 14:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ServletFileUpload upload = null;
    DiskFileItemFactory factory = new DiskFileItemFactory();
    upload = new ServletFileUpload(factory);
    int chunk = 0, chunks = 0;
    String fileName = null;
    try {
        String filePath = null;
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
                fileItem.write(new File(Config.fileUploadDir + chunk + fileName));
            }
        }
        if (chunks != 0 && chunks == chunk + 1) {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(Config.fileUploadDir + fileName)));
            for (chunk = 0; chunk < chunks; chunk++) {
                File tempFile = new File(Config.fileUploadDir + chunk + fileName);
                outputStream.write(FileUtils.readFileToByteArray(tempFile));
                outputStream.flush();
                tempFile.delete();
            }
            outputStream.close();
            TestOutput.println(fileName + " upload success");
        }
    } catch (Exception e) {
        TestOutput.println(e.getMessage());
        e.printStackTrace();
    }
%>
