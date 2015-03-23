/**
 * Created by acm on 12/4/14.
 */
(function() {
    var mapData = [];
    var jmap = function (id) {
        return new jmap.fn.init(id);
    };
    jmap.fn = jmap.prototype = {
        show: function () {
            return show(this.id);
        },

        hide: function () {
            return hide(this.id);
        },

        remove : function () {
            return remove(this.id);
        },

        setViewPort: function () {
            var overlay = mapData[this.id];
            if (overlay) {
                var points = [];
                for (var i in overlay) {
                    if (overlay[i].getPath) {
                        points = points.concat(overlay[i].getPath());
                    }
                }
                if (points.length > 0)
                    mp.setViewport(points);
            }
        }
    };

    function show(id) {
        var overlay = mapData[id];
        if (overlay) {
            for (var i in overlay) {
                overlay[i].show();
            }
            return true;
        }
        return false;
    };

    function hide(id) {
        var overlay = mapData[id];
        if (overlay) {
            for (var i in overlay) {
                overlay[i].hide();
            }
            return true;
        }
        return false;
    };

    function remove(id) {
        if (mapData[id]) {
            delete mapData[id];
            return true;
        }
        return false;
    };

    var init = jmap.fn.init = function (id) {
        if (id) {
            this.id = id;
        }
        return this;
    };

    jmap.loadData = function(id, data) {
        new Draw(id, data).work();
    };

    jmap.ids = function () {
        var ids = [];
        for (var index in mapData) {
            try {
                if (mapData[index][0].isVisible() == true) {
                    ids.push(index);
                }
            } catch (e) {}
        }
        return ids;
    }

    jmap.clear = function () {
        mapData = [];
    }

    function Draw(id, data) {
        var lineStyle = {
                strokeColor: "#8A2BE2",
                fillColor: "",
                strokeWeight: 3,
                strokeOpacity: 0,
                fillOpacity: 0
            },
            circleStyle = {
                strokeColor: "#DBE22B",
                fillColor: "",
                strokeWeight: 2,
                strokeOpacity: 0.5,
                fillOpacity: 0.5
            },
            size = new BMap.Size(42, 34),//折线的起点和终点的图标大小
            overlay = [];

        this.work = function() {
            if (data.routePlaceMarks.length > 0)
                polyLine();
            else {
               showFail();
            }
        }

        function polyLine() {//由于路线存在多条，坐标转换需要一条一条转换，也就是第一组gsp坐标转换完成百度坐标，才能进行后一组转换,并且先对路线解析完，再解析关键点
            var routePlaceMark, routePlaceMarks = data.routePlaceMarks;
            callback();
            function callback(baiDuPoints){
                if (baiDuPoints && baiDuPoints.length > 0) {
                    if (routePlaceMark.routeStyle) {
                        var routeStyle = routePlaceMark.routeStyle;
                        overlay.push(new BMap.Polyline(baiDuPoints, {
                            strokeColor: routeStyle.color,
                            fillColor: "",
                            strokeWeight: routeStyle.width,
                            strokeOpacity: 0,
                            fillOpacity: 0
                        }));
                    } else {
                        overlay.push(new BMap.Polyline(baiDuPoints, lineStyle));
                    }
                    overlay.push(marker(baiDuPoints[0], 0, 0));//起点
                    overlay.push(marker(baiDuPoints[baiDuPoints.length - 1], 0, -34));
                }
                if (routePlaceMarks.length > 0) {
                    routePlaceMark = routePlaceMarks.pop();
                    var gpsPoints = [], route = routePlaceMark.route;
                    for (var i in route) {
                        gpsPoints.push(route[i].longitude + "," + route[i].latitude);
                    }
                    gps2BaiDuPoints(gpsPoints, callback);
                } else {
                    placeMark();//对应折线中的点描述(标注)
                }
            }

            //生成折线的起点终点覆盖物
            function marker(point, x, y) {//x,y表示图片偏移
                var marker = new BMap.Marker(point,
                    {
                        icon: new BMap.Icon("image/baidu/markers.png", size,
                            {
                                imageOffset: new BMap.Size(x, y)
                            })
                    });
                return marker;
            };
        }

        function placeMark() {//对应折线中的点描述
            var gpsPoints = [], keyPointPlaceMarks = data.keyPointPlaceMarks;
            if (keyPointPlaceMarks.length > 0) {
                $.each(keyPointPlaceMarks, function (index, keyPointPlaceMark) {
                    gpsPoints.push(keyPointPlaceMark.coordinate.longitude + "," + keyPointPlaceMark.coordinate.latitude);
                });
                gps2BaiDuPoints(gpsPoints, callback);
            } else {
                showInMap();
            }

            function callback(baiDuPoints){
                for(var index in baiDuPoints){
                    (function(keyPointPlaceMark){
                        var marker = new BMap.Marker(baiDuPoints[index]);
                        overlay.push(marker);
                        marker.addEventListener("click", function () {
                            this.openInfoWindow(slide(keyPointPlaceMark.desc, keyPointPlaceMark.name));//图片，视频展示
                        });
                    })(keyPointPlaceMarks[index]);
                }
                showInMap();
            }

            function slide(desc, name) {
                if (desc && desc.length > 0) {
                    var msg = [];
                    $.each(desc, function (index, val) {
                        var child = {};
                        if (val.indexOf('photo') > -1) {
                            child.type = 'image';
                            child.href = val;
                        } else {
                            child.type = 'html';
                            child.scrolling = "no";
                            child.href = '<video controls="controls" width="640" height="360">'
                            + '<source src="' + val + '" type="video/mp4" />'
                            + '<object type="application/x-shockwave-flash" data="http://player.longtailvideo.com/player.swf" width="640" height="360">'
                            + '<param name="movie" value="http://player.longtailvideo.com/player.swf" />'
                            + '<param name="allowFullScreen" value="true" />'
                            + '<param name="wmode" value="transparent" />'
                            + '<param name="flashVars" value="controlbar=over&amp;file=' + val + '" />'
                            + '<span class="text-danger"><strong>该视频无法播放,请点击下面按钮进行下载</strong></span>'
                            + '</object>'
                            + '</video>'
                            + '<p><button class="btn btn-primary btn-sm" onclick="videoDownLoad(\'' + val + '\')">'
                            + '点击下载<span class="glyphicon glyphicon-download"></span></button></p>';
                        }
                        msg.push(child);
                        $.fancybox(msg, {
                            title: name,
                            loop: false,
                            closeBtn: false ,
                            modal: true,
                            helpers:  {
                                title	: { type : 'inside' },
                                buttons : {
                                }
                            }
                        });
                    });
                } else {
                    $.fancybox(name, {
                        autoCenter: true
                    })
                }


            }
        }

        function showInMap() {
            mapData[id] = overlay;
            for (var i in overlay) {
                mp.addOverlay(overlay[i]);
            }
            jmap(id).setViewPort();
            $('.table-overlay').hide();
            $('#' + id).addClass('success');
        }

        function showFail() {
            $('.table-overlay').hide();
            $('#' + id).addClass('danger');
        }

        function gps2BaiDuPoints(gpsPoints, callback) {//gps坐标数组,格式为['114.21892734521,29.575429778924','114.21892734521,29.575429778924'],callback全部转换完之后的回调函数，参数为百度坐标数组
            var url = "http://api.map.baidu.com/geoconv/v1/?from=1&to=5&ak=kPEqHXhcmEDm9DhNYKbKfeer&callback=?&coords=",
                maxCnt = 50,//每次转换多少个坐标
                arr,//用来遍历gpsPoints
                baiDuPoints = [],//坐标转换的最终结果
                shouldAjaxCount = -1,//坐标转换应该要使用的次数
                actuallyAjaxCount = -1;//坐标转换实际使用的次数,用来判断坐标是否转换完

            log("gpsPoints:" + gpsPoints);
            while (gpsPoints.length > 0) {
                arr = gpsPoints.splice(0, maxCnt);
                (function(ajaxCount) {
                    $.getJSON(url + arr.join(";"),
                        function(data) {
                            ++actuallyAjaxCount;
                            log("ajaxCount" + ajaxCount  + "," + actuallyAjaxCount + ":" + data);
                            if (data.status == 0) {
                                baiDuPoints[ajaxCount] = data.result;
                            } else {//坐标转换失败
                               showFail();
                            }
                            if (actuallyAjaxCount == shouldAjaxCount && gpsPoints.length == 0) {//所有坐标转换完
                                log(baiDuPoints);
                                var resultPoints = [], result;
                                for (var i = 0; i <= actuallyAjaxCount; i++) {//
                                    result = baiDuPoints[i];
                                    for (var j in result) {
                                        resultPoints.push(new BMap.Point(result[j].x, result[j].y));
                                    }
                                }
                                callback(resultPoints);
                            }
                        }
                    );
                })(++shouldAjaxCount);
            }
        }
    };

    init.prototype = jmap.fn;
    window.jmap = jmap;
})();

