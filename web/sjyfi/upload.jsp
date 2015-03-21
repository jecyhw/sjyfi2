<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@ page import="com.cn.util.Config" %>
<%@ page import="com.cn.test.TestOutput" %>
<%@ page import="java.io.File" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.io.FileUtils" %>
<%@ page import="java.io.BufferedOutputStream" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="com.cn.util.File.*" %>
<%@ page import="java.io.BufferedWriter" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: SNNU
  Date: 2015/3/18
  Time: 14:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%


    String file = "/usr/local/apache-tomcat/webapps/sjyfi2/sjyfi/1.xml";
    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
    outputStream.write(("<trackdetail>\n" +
            "<name>玛多到了。</name>\n" +
            "<author>王吉祥</author>\n" +
            "<starttime>2014-05-22 17:58:24</starttime>\n" +
            "<endtime>2014-05-22 18:12:23</endtime>\n" +
            "<length>143845.39759596568</length>\n" +
            "<maxaltitude>4409.157513681916</maxaltitude>\n" +
            "<keysiteslist></keysiteslist>\n" +
            "<annotation></annotation>\n" +
            "</trackdetail>\n").getBytes("utf-8"));
    outputStream.close();
    BaseFileParse fileParse = new TrackDetailFileParse();
    new JSAXParser().parse(file, fileParse);
    out.println(fileParse.getParseObject());

    List<String> list = new ArrayList<String>();
    String t = FileUtil.getNestDir("/var/gpstracks/routeRecord_20140408_122841/");
    if (t != null)
        list.add(t);
    t = FileUtil.getNestDir("/var/gpstracks/routeRecord_20140408_122844/");
    if (t != null)
        list.add(t);
    out.println(list);
    long start = System.currentTimeMillis();
    FileMerge merge = new FileMerge();
    merge.work(list, Config.unZipFileDir + "test");
    PlaceMarkFileParse placeMarkFileParse = new PlaceMarkFileParse();
    new JSAXParser().parse(Config.unZipFileDir + "test/" + Config.KMZFileInfo.routeRecordFileName, placeMarkFileParse);
    out.println(placeMarkFileParse.getParseObject());
//    ServletFileUpload upload = null;
//    DiskFileItemFactory factory = new DiskFileItemFactory();
//    upload = new ServletFileUpload(factory);
//    int chunk = 0, chunks = 0;
//    String fileName = null;
//    try {
//        String filePath = null;
//        List<FileItem> fileItems = upload.parseRequest(request);
//        Iterator<FileItem> it = fileItems.iterator();
//        while (it.hasNext()) {
//            FileItem fileItem = it.next();
//            if (fileItem.isFormField()) {
//                String fieldName = fileItem.getFieldName();
//                if (fieldName.equals("chunk"))
//                    chunk = Integer.parseInt(fileItem.getString());
//                else if (fieldName.equals("chunks"))
//                    chunks = Integer.parseInt(fileItem.getString());
//                else
//                    fileName = fileItem.getString();
//            } else {
//                fileItem.write(new File(Config.fileUploadDir + chunk + fileName));
//            }
//        }
//        if (chunks != 0 && chunks == chunk + 1) {
//            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(Config.fileUploadDir + fileName)));
//            for (chunk = 0; chunk < chunks; chunk++) {
//                File tempFile = new File(Config.fileUploadDir + chunk + fileName);
//                outputStream.write(FileUtils.readFileToByteArray(tempFile));
//                outputStream.flush();
//                tempFile.delete();
//            }
//            outputStream.close();
//            TestOutput.println(fileName + " upload success");
//        }
//    } catch (Exception e) {
//        TestOutput.println(e.getMessage());
//        e.printStackTrace();
//    }
%>
