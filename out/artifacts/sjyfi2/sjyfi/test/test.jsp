<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 12/19/14
  Time: 7:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title></title>
<meta charset="UTF-8">

<link rel="stylesheet" type="text/css" href="uploadify/uploadify.css"/>
<script src="../js/jquery.js"></script>
<script src="../jsold/jquery.uploadify.js"></script>
<script src="../js/jupload.js"></script>
<script>
  $(document).ready(function(){
    //选择文件
    var $kmz_file = $("#kmz_file");
    $kmz_file.uploadify({
      swf: 'uploadify/uploadify.swf',
      uploader: '/UploadFileServlet',
      height: 24,
      width:64,
      queueID: "file_list",
      buttonText: "选择文件",
      removeCompleted: false,
      progressData: 'both',
      buttonImage: '',
      fileTypeDesc: 'kmz文件',
      fileTypeExts: '*.kmz',
      onQueueComplete: function () {
      },
      onSelect: function (file) {
        $.ajax({
          type    : 'POST',
          async   : false,
          url     : "/CheckFileExistServlet",
          data    : {filename: file.name},
          success : function(data) {
            if (data == 1) {

            }
          }
        });
      },
      onSelectError: function (file) {
        alert('The file ' + file.name + ' returned an error and was not added to the queue.');
      },
      onUploadStart: function (file) {

      },
      onUploadError: function (file, errorCode, errorMsg) {
        if (jupload.exceed(file.id)) {
          jupload.remove(file.id);
        } else {
          jupload.increase(file.id);

          //$kmz_file.uploadify('stop', file.id);
        }
      },
      onUploadComplete: function (file) {
        file.uploaded = false;
        $kmz_file.uploadify('upload', file.id);
        alert(file.uploaded);
      }
    });
  });
</script>
</head>
<body>
<input type="file" name="kmz_file" id="kmz_file" title="只允许上传kmz格式文件" />
<div id="file_list">
</div>
</body>
</html>