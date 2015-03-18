<%--
  Created by IntelliJ IDEA.
  User: SNNU
  Date: 2015/3/17
  Time: 14:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>文件下载</title>
    <link rel="stylesheet" type="text/css" href="js/bootstrap/css/bootstrap.css" media="screen"/>
</head>
<body>
<div class="alert alert-success text-center" role="alert" id="downloadTip">
    <strong>正在努力合并要下载的文件，请耐心等待</strong>
    <img src="js/fancybox/fancybox_loading.gif"/>
</div>
<iframe name="fileMerge" style="display: none" id="fileMerge"></iframe>
<form name="frm" id="frm" target="fileMerge" method="post" action="/sjyfi2/ExportRecordServlet">
    <input type="hidden" name="ids" id="ids">
</form>
<form name="frmFile" id="frmFile" method="post" action="/sjyfi2/DownloadFileServlet">
    <input type="hidden" name="downloadFile" id="downloadFile">
    <input type="hidden" name="isDeleted" id="isDeleted">
</form>
</body>

<script>
    window.onload = function() {
        var ids = document.getElementById("ids");
        ids.value = '<%=request.getParameter("ids")%>';
        if (ids.value == 'null') {
            window.close();
        }
        document.getElementById("frm").submit();
    }

    function callback(downloadFileName, isDeleted) {//iframe回调函数
        if (downloadFileName != "") {
            document.getElementById("downloadTip").innerHTML = "<strong>合并完成,正在下载</strong>";
            document.getElementById("downloadFile").value = downloadFileName;
            document.getElementById("isDeleted").value = isDeleted;
            document.getElementById("frmFile").submit();
        } else {
            alert("该文件不存在");
            window.close();
        }
    }
</script>
</html>
