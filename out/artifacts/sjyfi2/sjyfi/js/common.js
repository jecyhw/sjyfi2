/**
 * Created by acm on 12/14/14.
 */
function isNullOrEmpty(val) {
    return  val == null || val == "";
}


function calcFileSize(fileSize) {
	if (!fileSize)
		return fileSize;
	var unit = "b";
	fileSize = parseFloat(fileSize);
    if (fileSize > 1023) {
    	fileSize /= 1024;
    	unit = "K";
        if (fileSize > 1023) {
        	fileSize /= 1024;
        	unit = "M";
        	if (fileSize > 1023) {
        		fileSize /= 1024;
            	unit = "G";
        	}
        }
        fileSize = fileSize.toFixed(2);
    }
	return fileSize + unit;
}

function bsAlertDanger($parent, id, msg) {
	var $error = $("#" + id);
	if ($error.length == 0)
	{
		var text = '<div id="' + id + '" class="alert alert-danger">' 
		+ '<a href="#" class="close" data-dismiss="alert">&times;</a>'
		+ '<strong>' + msg + '</strong>'
		+ '</div>';
		$parent.inse
		$parent.append(text);
	}
}

function videoDownLoad(href) {
    var form = $("<form method='post' style='display: none;' action='"
    +"/sjyfi2/DownloadFileServlet'><input type='hidden' name='downloadFile' value='"
    + href +"'/></form>");   //定义一个form表单
    $(document.body).append(form);  //将表单放置在web中
    form.submit();
    form.remove();
}