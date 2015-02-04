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
        log(data);
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
            polyLine();
        }

        function polyLine() {//由于路线存在多条，坐标转换需要一条一条转换，也就是第一组gsp坐标转换完成百度坐标，才能进行后一组转换
            var pointsArray = data.points;
            callback();
            function callback(baiDuPoints){
                if (baiDuPoints && baiDuPoints.length > 0) {
                    overlay.push(new BMap.Polyline(baiDuPoints, lineStyle));
                    overlay.push(marker(baiDuPoints[0], 0, 0));//起点
                    overlay.push(marker(baiDuPoints[baiDuPoints.length - 1], 0, -34));
                }
                if (pointsArray.length > 0) {
                    var tPoints = pointsArray.pop(), gpsPoints = [];
                    for (var i in tPoints) {
                        gpsPoints.push(tPoints[i].longitude + "," + tPoints[i].latitude);
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
            var gpsPoints = [], placeMarks = data.placemark;
            if (placeMarks.length > 0) {
                $.each(placeMarks, function (index, placeMark) {
                    gpsPoints.push(placeMark.coordinates.longitude + "," + placeMark.coordinates.latitude);
                });
                gps2BaiDuPoints(gpsPoints, callback);
            } else {
                showInMap();
            }

            function callback(baiDuPoints){
                for(var index in baiDuPoints){
                    var placeMark = placeMarks[index];
                    var marker = new BMap.Marker(baiDuPoints[index]);
                    overlay.push(marker);
                    marker.addEventListener("click", function () {
                        this.openInfoWindow(slide(placeMark.description, placeMark.name));//图片，视频展示
                    });
                }
                showInMap();
            }

            function slide(desc, name) {


                var msg = [], playerObj = {};
                $.each(desc, function (index, val) {
                    var child = {};
                    child.href = val;
                    if (val.indexOf('photo') < 0) {
                        child.type = 'image';
                    } else {
                        child.type = 'html';
                        child.scrolling = "no";

                    }
                    msg.push(val);
                });
                $.fancybox(msg, {
                    title: name,
                    afterLoad: function (current, previous) {
                        if (current.type == 'html') {
                            playerObj.addr = current.href;
                            playerObj.id = new Date().getTime();
                            current.content = '<div id="' + playerObj.id + '">+</div><div class="player-overlay"><div class="sjyfi-loading"><div></div></div></div>'
                        } else {
                            playerObj = {};
                        }
                    },
                    afterShow: function () {
                        if (playerObj.id) {
                            jwplayer(playerObj.id).setup({
                                file: playerObj.addr,
                                events: {
                                    onReady: function() {
                                        $(".player-overlay").remove();
                                    }
                                }
                            });
                        }
                    },
                    loop: false,
                    closeBtn: false ,
                    modal: true,
                    helpers:  {
                        title	: { type : 'inside' },
                        buttons : {

                        }
                    }
                });
            }
        }

        function showInMap() {
            mapData[id] = overlay;
            for (var i in overlay) {
                mp.addOverlay(overlay[i]);
            }
            jmap(id).setViewPort();
            $('.table-overlay').hide();
        }

        function gps2BaiDuPoints(gpsPoints, callback) {//gps坐标数组,格式为['114.21892734521,29.575429778924','114.21892734521,29.575429778924'],callback全部转换完之后的回调函数，参数为百度坐标数组
            var url = "http://api.map.baidu.com/geoconv/v1/?from=1&to=5&ak=kPEqHXhcmEDm9DhNYKbKfeer&callback=?&coords=",
                maxCnt = 80,
                arr,
                baiDuPoints = [],
                shouldAjaxCount = 0,
                actuallyAjaxCount = 0;
            log(gpsPoints);
            while (gpsPoints.length > 0) {
                arr = gpsPoints.splice(0, 80);
                shouldAjaxCount++;
                $.getJSON(url + arr.join(";"),
                    function(data) {
                        log(data);
                        actuallyAjaxCount++;
                        if (data.status == 0) {
                            var result;
                            for (var i in data.result) {
                                result = data.result[i];
                                baiDuPoints.push(new BMap.Point(result.x, result.y));
                            }
                        }
                        if (actuallyAjaxCount == shouldAjaxCount && gpsPoints.length == 0) {
                            log(baiDuPoints);
                            callback(baiDuPoints);
                        }
                    }
                );
            }
        }
    };

    init.prototype = jmap.fn;
    window.jmap = jmap;
})();


