/**
 * Created by jecyhw on 2014/10/15.
 */
var mp;//地图实例
$(document).ready(function () {
    var clipMap,
        web_prefix = '/sjyfi2';//地图区域选择实例

    //加载地图
    mp = new BMap.Map('map');
    var point = new BMap.Point(116.331398,39.897445);
    mp.centerAndZoom(point,12);

    function myFun(result){
        var cityName = result.name;
        mp.setCenter(cityName);
    }
    var myCity = new BMap.LocalCity();
    myCity.get(myFun);

    mp.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP ]}));   //添加地图类型控件


    mp.enableScrollWheelZoom();
    mp.enableContinuousZoom();
    //

	var $tabs = $("#stabs").tabs({
		activate: function(e, ui) {
            var height = ($(".top").outerHeight() - 1) + "px"
			$(".bottom").css("top", height);
            $('.table-overlay').css("top", height);
            var regionCapture =  ui.oldPanel.find("button[name='region_capture']");
            if (regionCapture.length > 0 && regionCapture.html().indexOf("选择区域") == -1) {
                regionCapture.html('选择区域<span class="glyphicon glyphicon-screenshot"></span>');
                mp.setDefaultCursor("-moz-grab");
                mp.enableDragging();
                clipMap.setOptions({ hide: true, disable: true });
            }
		},
		create: function(e, ui) {
            var $tabSelf = $(this);
            var height = ($(".top").outerHeight() - 1) + "px"
            $(".bottom").css("top", height);
            $('.table-overlay').css("top", height);
            query($.toJSON({recorder: $("#account").html()}));

            //地图区域绑定
            clipMap = $("#map").imgAreaSelect({
                disable: true,
                instance: true,
                parent: "#map",
                onSelectChange: function (it, selection) {
                    var index = $("#stabs").tabs("option", "active"),
                        _topLeft = mp.pixelToPoint(new BMap.Pixel(selection.x1, selection.y1)),
                        _bottomRight = mp.pixelToPoint(new BMap.Pixel(selection.x2, selection.y2));
                    $("#left" + index).val(_topLeft.lng);
                    $("#top" + index).val(_bottomRight.lat);
                    $("#right" + index).val(_bottomRight.lng);
                    $("#bottom" + index).val(_topLeft.lat);
                }
            });

			$("button[name='search_submit']").each(function(index, btn) {
                $(btn).click(function () {
                    var tabsIndex = $tabSelf.tabs("option", "active"),
                        panel = $tabSelf.find("#stabs-" + tabsIndex),
                        regionCapture = $tabSelf.find("#region_capture" + tabsIndex),
                        count = 0,
                        cons = {};
                    if (regionCapture.length > 0 && regionCapture.html().indexOf("选择区域") == -1) {
                        regionCapture.html('选择区域<span class="glyphicon glyphicon-screenshot"></span>');
                        mp.setDefaultCursor("-moz-grab");
                        mp.enableDragging();
                        clipMap.setOptions({ hide: true, disable: true });
                    }

					panel.find("input[type='text']").each(function (_index, text) {
						if (!isNullOrEmpty(text.value)) {
							cons[$(text).attr("name")] = text.value;
							count++;
						}
					});
					if (count > 0) {
						query($.toJSON(cons));
					}
        		});
            });

            function query(conditions) {
                var queryResult =  $("#query-result");
                $.ajax({
                    url: web_prefix + "/QueryRecordServlet",
                    type: "post",
                    dataType: "json",
                    data: {
                        data: conditions
                    },
                    success: function (data) {
                        if (data.length == 0) {
                            queryResult.html('<tr><td colspan="4"  class="text-center">无搜索记录</td></tr>');
                        }
                        else {
                            var body = "";
                            $.each(data, function (k, v) {
                                if (v.trackid) {
                                    body += "<tr id='" + v.trackid + "'><td><input type='checkbox' title='显示到地图'/></td>"
                                    + "<td><a class='btn btn-link btn-xs' href='javascript:void(0)' title='查看详细信息'>"
                                    + (v.name == undefined ? '无' : v.name) + "</a></td><td>"
                                    + (v.starttime == undefined ? '无' : v.starttime) + "</td><td>"
                                    + calcFileSize(v.filesize) + "</td></tr>";
                                }
                            })
                            queryResult.html(body);
                        }
                        $tabs.find(".tabs-overlay").hide();
                        jmap.clear();
                        mp.clearOverlays();
                    },
                    error: function (a, b) {
                        queryResult.html('<tr><td colspan="4" class="text-center">查询出错</td></tr>');
                        if ($tabs)
                            $tabs.find(".tabs-overlay").hide();
                    },
                    beforeSend: function () {
                        queryResult.html('<tr><td colspan="4" class="text-center">正在查询中</td></tr>');
                        if ($tabs) {
                            $tabs.find(".tabs-overlay").show();
                            $('.table-overlay').hide();
                        }
                    }
                });
            }

            $("button[name='region_capture']").each(function(index, clip){
                $(clip).click(function () {
                    var $it = $(this);
                    if ($it.html().indexOf("选择区域") > -1) {
                        $it.html('取消截图<span class="glyphicon glyphicon-screenshot"></span>');
                        mp.setDefaultCursor("default");
                        mp.disableDragging();
                        clipMap.setOptions({ enable: true });
                    } else {
                        $it.html('选择区域<span class="glyphicon glyphicon-screenshot"></span>');
                        mp.setDefaultCursor("-moz-grab");
                        mp.enableDragging();
                        clipMap.setOptions({ hide: true, disable: true });
                    }
                });
            });
            //

            var  $startTime =  $("input[name='startTime']"),
                $endTime = $("input[name='endTime']");
            //日历绑定
            $startTime.each(function(index, time) {
                $(time).datepicker(
                    $.extend($.datepicker.regional['zh-CN'],{
                        dateFormat:"yy-mm-dd",
                        //changeMonth:true,
                        //changeYear:true,
                        maxDate: new Date(),
                        showButtonPanel: true,
                        numberOfMonths: 2,
                        //showOn: 'both',
                        onSelect: function (selectedDate ) {
                            $endTime.eq(index).datepicker("option", "minDate", selectedDate);
                            $endTime.eq(index).datepicker("option", "defaultDate", selectedDate);
                        }
                    })
                );
            });
            $endTime.each(function(index, time) {
                $(time).datepicker(
                    $.extend($.datepicker.regional['zh-CN'],{
                        dateFormat:"yy-mm-dd",
                        //changeMonth:true,
                        showButtonPanel: true,
                        numberOfMonths: 2,
                        //showOn: 'both',
                        onSelect: function (selectedDate ) {
                            $startTime.eq(index).datepicker("option", "maxDate", selectedDate);
                            $startTime.eq(index).datepicker("option", "defaultDate", selectedDate);
                        }
                    })
                );
            });
		}
	});

    //
    //

    //
    $("#query-result").on("click", "input", function(){//将对应选中的轨迹显示到地图
        var it = this,
            id = it.parentNode.parentNode.id;
        if (it.checked) {
            //先查看之前是否已经查询过
            if (!jmap(id).show()) {
                $('.table-overlay').show();
                $.getJSON(web_prefix + '/RouteRecordMapInfoServlet',
                    { id: id },//获取tr的id
                    function (data) { jmap.loadData(id, data); }
                );
            } else {
                jmap(id).setViewPort();
            }
            it.title = "取消显示";
        }
        else {
            //隐藏
            jmap(id).hide();
            it.title = "显示到地图";
        }
    }).
        on('click', 'a', function(){//查看详细信息
            var it = this;
            $.getJSON(web_prefix + '/ViewSingleRouteRecordInfoServlet',
                {
                    id: it.parentNode.parentNode.id
                },
                function(data) {
                    var name = data.name == undefined ? '无' : data.name,
                        author = data.author == undefined ? '无' : data.author,
                        starttime = data.starttime == undefined ? '无' : data.starttime,
                        endtime = data.endtime == undefined ? '无' : data.endtime,
                        length = data.length == undefined ? '无' : data.length,
                        maxaltitude = data.maxaltitude == undefined ? '无' : data.maxaltitude,
                        keysiteslist = data.keysiteslist == undefined ? '无' : data.keysiteslist,
                        annotation = data.annotation == undefined ? '无' : data.annotation,
                        path = data.path == undefined ? '无' : data.path,
                        filesize = data.filesize == undefined ? '无' : data.filesize;

                    var msg = '<table class="table table-bordered table-hover table-condensed table-striped">' +
                        '<tr>' +
                        '<td>名称:</td>' +
                        '<td><span>' + name + '</span></td>' +
                        '<td>提供者:</td>' +
                        '<td><span>' + author + '</span></td>' +
                        '<td rowspan="5" style="vertical-align: middle;"><img src="image/download.png" id="'+ data.trackid + '"></td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td>开始记录时间:</td>' +
                        '<td><span>' + starttime + '</span></td>' +
                        '<td>结束时间:</td>' +
                        '<td><span>' + endtime + '</span></td>' +
                        '</tr>' +
                        '<tr>'  +
                        '<td>轨迹长度:</td>' +
                        '<td><span>' + length + '米</span></td>' +
                        '<td>最大高度:</td>' +
                        '<td><span>' + maxaltitude + '米</span></td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td>关键点:</td>' +
                        '<td><span>' + keysiteslist + '</span></td>' +
                        '<td>描述:</td>' +
                        '<td><span>' + annotation + '</span></td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td>存放目录:</td>' +
                        '<td><span>' + path + '</span></td>' +
                        '<td>轨迹大小:</td>' +
                        '<td><span>' + calcFileSize(filesize) + '</span></td>' +
                        '</tr>' +
                        '</table>';
                    $.fancybox(msg);
                    /*$("#ui-dialog").html(msg);
                    $("#ui-dialog").dialog("open");*/
                }
            );
        });

    $("body").on("click", "div#ui-dialog img", function() {
        exportTrackRecord("[" + this.id + "]")
    });

    $(function() {
        var $fileList = $("#file-list"),
            $fileUploadList = $("#file-upload-list");
        // Setup html5 version
        $fileList.plupload({
            // General settings
            //runtimes : 'html5,flash,silverlight,html4',
            url : web_prefix +  '/UploadFileServlet',
            //chunk_size: '1mb',
            rename : false,
            dragdrop: true,
            max_retries: 3,
            autostart: false,
            sortable: true,
            filters : {
                // Maximum file size
                max_file_size : '1000mb',
                // Specify what files to browse for
                mime_types: [
                    {title : "kmz files", extensions : "kmz"}
                ],
                prevent_duplicates: true,
                checkExisting: web_prefix + "/CheckFileExistServlet"
            },
            /*views: {
                list: true,
                thumbs: true,
                active: 'thumbs'
            },*/
            // Flash settings
            flash_swf_url : 'js/plupload/Moxie.swf',

            // Silverlight settings
            silverlight_xap_url : 'js/plupload/Moxie.xap'
        });

        plupload.addFileFilter("checkExisting", function(checkUrl, file, cb) {
            var unDef;
            if (checkUrl != unDef) {
                $.getJSON(checkUrl,
                    {filename: file.name},
                    function(data) {
                        if (data == 1) {
                            $fileList.plupload('notify', 'error', file.name + '文件在服务器端已存在');
                            cb(false);
                        }
                        else {
                            cb(true);
                        }
                    }
                );
            }
        });

        $("#file_upload").click(function() {
            $fileUploadList.show();
        });
        //上传最小化,上传最大化,上传关闭

        $("#upload_minimize").click(function () {
            $fileList.hide();
            $(this).addClass("disabled").next().removeClass("disabled");

        }).next().click(function () {
            $fileList.show();
            $(this).addClass("disabled").prev().removeClass("disabled");
        }).next().click(function () {
            var queueLength = $fileList.plupload('getFiles').length;//$("#kmz_file").uploadify("settings", "queueLength");
            if (queueLength > 0) {
                var $tipMsg = $('<div class="text-info">提示!<hr/><p><strong>列表中有未上传完成的文件，\n确定要放弃上传吗？</strong>' +
                '</p><div class="text-right"><a class="btn btn-info btn-sm">确定</a><a class="btn btn-info btn-sm focus">取消</a></div></div>');
                $.fancybox($tipMsg, {
                    modal: true,
                    closeBtn: false,
                    afterShow: function () {
                        $tipMsg.find(".focus").click(function () {
                            $.fancybox.close(true);
                        })
                            .prev().click(function () {
                                $fileUploadList.hide();
                                $fileList.plupload('clearQueue');
                                $.fancybox.close(true);
                            });
                    }
                });
            } else {
                $fileUploadList.hide();
            }
        });

        $("#file-upload-error").bind('closed.bs.alert', function () {
            $fileUploadList.hide();
        });
    });

    $("#exit").click(function() {
        $.get(web_prefix + "/UserLogOutServlet");
        window.location.href = "../index.html";
    });

    //导出选中轨迹
    $("#export_track_record").click(function () {
        var ids = jmap.ids();
        if (ids.length > 0) {
            exportTrackRecord($.toJSON(ids));
        }
    });

    //导出轨迹
    function exportTrackRecord(ids) {
        var $export_track_record = $("#export_track_record");
        $export_track_record.attr({"disabled":"disabled"});
        var val = $export_track_record.html()
        $export_track_record.html("正在导出");
        var form = $("<form method='post' style='display: none;' action= '"
        + web_prefix
        +"/ExportRecordServlet' target='_blank'><input type='hidden' name='ids' value='"
        + ids +"'/></form>");   //定义一个form表单
        $(document.body).append(form);  //将表单放置在web中
        form.submit();
        form.remove();
        $export_track_record.html(val);
        $export_track_record.removeAttr("disabled");
    }
    //保存选中轨迹
    $("#save_track_record").click(function () {
        var ids = jmap.ids(),
            $it = $(this);
        if (ids.length > 1) {
            $it.attr({"disabled":"disabled"});
            var val = $it.html();
            $it.html("正在保存");
            $.getJSON(web_prefix + "/SaveRecordServlet",
                {
                    ids: $.toJSON(ids)
                },
                function(data) {
                    if (data.result == "true") {
                        $.fancybox('<div class="text-info">提示!<hr/><div class="text-center"><strong>保存成功。</strong></div></div>', {
                            modal: true
                        });
                    }
                    else {
                        $.fancybox('<div class="text-info"><strong>提示!</strong><hr/><div class="text-center"><strong>保存失败。</strong></div></div>', {
                            modal: true
                        });
                    }
                    //查询完后，恢复提交按钮
                    $it.removeAttr("disabled");
                    $it.html(val);
                }
            );
        }
    });

    $.ajaxSetup({
        cache: false,
        complete: function (xhr, textStatus) {
            var sessionstatus = xhr.getResponseHeader("sessionstatus"); // 通过XMLHttpRequest取得响应头，sessionstatus，
            if (sessionstatus == "timeout") {// 如果超时就处理 ，指定要跳转的页面
                var $timeOutMsg = $('<div class="text-danger"><strong>会话超时，请重新登陆!</strong><hr/><p><span class="text-primary">5</span>秒后自动跳转自登陆页面</p><p>也可点击<a class="btn  btn-danger btn-sm">此处</a>跳转</p></div>');
                $.fancybox($timeOutMsg, {
                    modal: true,
                    closeBtn: false,
                    afterShow: function() {
                        var $spanSec = $timeOutMsg.find('span');
                        var intervalId = setInterval(countDown, 1000);

                        function countDown() {
                            var num = parseInt($spanSec.html());
                            if (num) {
                                $spanSec.html(num - 1);
                            } else {
                                clearInterval(intervalId);
                                window.location.href = "../index.html";
                            }
                        }

                        $timeOutMsg.find('a').click(function () {
                            window.location.href = "../index.html";
                        });
                    }
                });
            }
        }
    });

    /*jwplayer.key = "m476PqaVB57UPPquVHfobNzMOH+z+knyJgpE+w==";
    jwplayer("preload").setup({file:"", height:0});
    jwplayer().onSetupError(function() {
        $("#preload").remove();
    });*/
});


function log(msg) {
    console.log(msg);
}