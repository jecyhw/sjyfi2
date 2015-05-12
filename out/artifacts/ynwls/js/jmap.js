/**
 * Created by acm on 12/4/14.
 */
(function () {
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

        remove: function () {
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

    jmap.loadData = function (id, data) {
        new Draw(id, data).work();
    };

    jmap.ids = function () {
        var ids = [];
        for (var index in mapData) {
            try {
                if (mapData[index][0].isVisible() == true) {
                    ids.push(index);
                }
            } catch (e) {
            }
        }
        return ids;
    }

    jmap.clear = function () {
        mapData = [];
    }

    function Draw(id, data) {
        function lineStyle(options) {
            return $.extend({
                strokeColor: "#8A2BE2",
                fillColor: "",
                strokeWeight: 3,
                strokeOpacity: 0,
                fillOpacity: 0
            }, options);
        }

        var size = new BMap.Size(42, 34),//折线的起点和终点的图标大小
            overlay = [];

        this.work = function () {
            polyLine();
        }

        function polyLine() {//由于路线存在多条，坐标转换需要一条一条转换，也就是第一组gsp坐标转换完成百度坐标，才能进行后一组转换,并且先对路线解析完，再解析关键点
            var routePlaceMark, routePlaceMarks = data.routePlaceMarks;
            if (routePlaceMarks && routePlaceMarks.length > 0) {
                getNextRoute();
            } else {//获取信息失败
                showFail();
            }

            function getNextRoute() {
                if (routePlaceMarks.length > 0) {
                    routePlaceMark = routePlaceMarks.pop();
                    var gpsPoints = [], route = routePlaceMark.route;
                    for (var i in route) {
                        gpsPoints.push(route[i].longitude + "," + route[i].latitude);
                    }
                    gps2bd(gpsPoints, function (bdPoints) {
                        if (!bdPoints || bdPoints.length == 0) {//路线解析失败,停止解析
                            showFail();
                        } else {
                            if (routePlaceMark.routeStyle) {
                                var routeStyle = routePlaceMark.routeStyle;
                                overlay.push(new BMap.Polyline(baiDuPoints, lineStyle({
                                    strokeColor: routeStyle.color,
                                    strokeWeight: routeStyle.width
                                })));
                            } else {
                                overlay.push(new BMap.Polyline(bdPoints, lineStyle()));
                            }
                            overlay.push(marker(bdPoints[0], 0, 0));//起点
                            overlay.push(marker(bdPoints[bdPoints.length - 1], 0, -34));//终点

                            getNextRoute();
                        }
                    });
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
            if (keyPointPlaceMarks && keyPointPlaceMarks.length > 0) {
                $.each(keyPointPlaceMarks, function (index, keyPointPlaceMark) {
                    gpsPoints.push(keyPointPlaceMark.coordinate.longitude + "," + keyPointPlaceMark.coordinate.latitude);
                });
            }
            gps2bd(gpsPoints, function (bdPoints) {
                if (bdPoints && bdPoints.length > 0) {
                    for (var index in bdPoints) {
                        (function (keyPointPlaceMark) {
                            var marker = new BMap.Marker(bdPoints[index]);
                            overlay.push(marker);
                            marker.addEventListener("click", function () {
                                this.openInfoWindow(slide(keyPointPlaceMark.desc, keyPointPlaceMark.name));//图片，视频展示
                            });
                        })(keyPointPlaceMarks[index]);
                    }
                }
                showInMap();
            });

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
                            + '<p><a class="btn btn-primary btn-sm" href="javascript:videoDownLoad('+ val + ')">'
                            + '点击下载<span class="glyphicon glyphicon-download"></span></a></p>';
                        }
                        msg.push(child);
                        $.fancybox(msg, {
                            title: name,
                            loop: false,
                            closeBtn: false,
                            modal: true,
                            helpers: {
                                title: {type: 'inside'},
                                buttons: {}
                            }
                        });
                    });
                } else {
                    $.fancybox(name, {
                        autoCenter: true
                    })
                }
            }

            function videoDownLoad(href) {
                var form = $("<form method='post' style='display: none;' action='"
                + web_prefix + "/DownloadFile.do'><input type='hidden' name='downloadFile' value='"
                + href +"'/></form>");   //定义一个form表单
                $(document.body).append(form);  //将表单放置在web中
                form.submit();
                form.remove();
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
    };

    init.prototype = jmap.fn;
    window.jmap = jmap;
})();


