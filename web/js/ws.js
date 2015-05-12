/**
 * Created by SNNU on 2015/5/10.
 */
$(document).ready(function(){
    function getLbs(type, name) {
        return re = '<span class="label label-' + type + '">' + name + "</span>";
    }

    function createMk(uid, bd) {//创建图标
        if (rt.color_index >= color_list.length) {
            rt.color_index = 0;
        }
        rt.ci[uid] = ++rt.color_index;

        var mk =  new BMap.Marker(bd, {
            // 指定Marker的icon属性为Symbol
            icon: new BMap.Symbol(BMap_Symbol_SHAPE_POINT, {
                //scale: 1,//图标缩放大小
                fillColor: color_list[rt.color_index],//填充颜色
                fillOpacity: 0.8//填充透明度
            })
        });
        rt.mk[uid] = mk;
        mp.addOverlay(mk);//添加到地图
        addEventOnMk();

        //给marker添加事件
        function addEventOnMk() {
            mk.addEventListener("click", function (e) {
                console.log(e);
            });
            mk.addEventListener("mouseover", function (e) {
                console.log(e);
            });
            mk.addEventListener("mouseout", function (e) {
                console.log(e);
            });
        }
    }

    function createLabel(uid, name, bd) {
        var lb = new BMap.Label(getLbs("primary", name), {
            position: bd,
            offset: new BMap.Size(10, 10)
        });
        rt.lb[uid] = lb;
        mp.addOverlay(lb);
    }

    var rt = {//存放用户的相关信息
            gps: [],//gps坐标
            bd: [],//转换的百度坐标
            mk: [],//地图上的marker
            lb: [],//对应marker的label标签
            ci: [],//对应mk使用的图标颜色
            color_index : -1,
            lpt: 5000,//默认每五秒更新一次，如果某次请求未获取到更新数据,更新时间将会逐渐增加，如有数据更新，更新时间
            maxLpt: 60000,//最长的更新时间为1分钟
            minLpt: 5000,//最短的更新时间为5秒
            upc: 0,//每次更新的人数
            update: function (urt, bd) {
                var uid = urt.uid;
                rt.gps[uid] = new gps.point(urt.longitude, urt.latitude);
                rt.bd[uid] = bd;
                rt.mk[uid].setPosition(bd);
                rt.lb[uid].setPosition(bd);
            },
            add: function (urt, bd) {
                var uid = urt.uid;
                rt.gps[uid] = new gps.point(urt.longitude, urt.latitude);;
                rt.bd[uid] = bd;
                createMk(uid, bd);
                createLabel(uid, urt.name, bd);
            }
        },
        gps = {};//gps的命名空间
    gps.point = function(j, w){//gps点坐标
        this.j = j;
        this.w = w;
        this.equals = function (a) {
            return a.j == this.j && a.w == this.w;
        };
    };



    var webSocket = window.WebSocket || window.MozWebSocket;
    if (webSocket){
        $.getJSON(web_prefix + "/QueryUserPosition.do", function (data) {
            if (data.status == 0) {
                gps2bd(resultToGpsPoints(data.result), function (bdPoints) {
                    updateMarkerPosition( result, bdPoints);
                });
            }
        });

        var wsUrl = "ws://" + window.location.host + web_prefix + "/ShowUserByRealTime";
        var ws = new webSocket(wsUrl);
        ws.onerror = function() {
            console.log("onerror");
        };
        ws.onopen = function () {
            console.log("onopen");
        };
        ws.onmessage = function (event) {
            if (event.data) {
                var urt = eval(event.data);//更新的用户列表
                gps2bd(resultToGpsPoints(urt), function (bdPoints) {
                    updateMarkerPosition( result, bdPoints);
                });
            }
            console.log(event);
        };
        ws.onclose = function () {
            console.log("onclose");
        }
    } else{
        longPoll();
    }

    function longPoll() {
        $.ajax({
            type: "post",
            url: web_prefix + "/QueryUserPosition.do",
            dataType: "json",
            success: function (data) {
                if (data.status == 0) {
                    updateUserPosition(data.result);
                }
            },
            error: function() {
                setTimeout(longPoll, rt.lpt);
            }
        });

        //根据更新信息来动态调整轮回时间
        function adjustUpdateLongPoolTime() {
            if (rt.updateUserCount == 0 && rt.lpt < rt.maxLpt) {//当前请求没有更新，并且小于最长更新时间
                rt.lpt++;
            } else {
                rt.updateUserCount = 0;
                if (rt.lpt > rt.minLpt) {
                    rt.lpt--;
                }
            }
        }
    }



    //获取gps转换百度坐标的的请求参数形式,并且过滤掉在地图上已显示但坐标未更新的用户数据
    function resultToGpsPoints(urt) {
        var gpsPoints = [];
        if (urt) {
            var len = urt.length, _urt;
            for (var i = 0; i < len; i++) {
                _urt = urt[i];
                if (rt.gps[_urt.uid] && rt.gps[_urt.uid].equals(new gps.point(_urt.longitude, _urt.latitude))) {
                    continue;//如果该用户已显示,并且坐标未变
                }
                gpsPoints.push(_urt.longitude + "," + _urt.latitude);
            }
        }
        return gpsPoints;
    }

    //根据用户的坐标来更新的Marker的位置
    function updateMarkerPosition(urt, bdPoints) {
        if (!bdPoints || bdPoints.length == 0)//没有需要更新的用户
            return false;
        var len = bdPoints.length;
        for (var i = 0; i < len; i++) {
            var _urt = urt[i],
                bdPoint = bdPoints[i];
            if ( rt.gps[_urt.uid]) {//该用户已显示,更新用户坐标
                rt.update(_urt, bdPoint);
            } else {
                rt.add(_urt, bdPoint);
            }
            rt.updateUserCount++;//更新数加1
        }
        mp.setViewport(rt.bd);//重新设置最佳视野
        return true;
    }
    $("button").click(function() {
        var id = $(this).attr("data-for");
        var $in = $("#" + id);
        if ($in.val() == "") {
            $in.tooltip("show");
            setTimeout(function() {
                $in.tooltip("hide");
            }, 1000);
        } else {
            switch(id) {
                case "upi":
                    $.getJSON("url: web_prefix + '/QueryUserPosition.do'", { name: $in.val() }, function (data) {
                        if (data.status == 0) {
                            var re = data.result, len = re.length;
                            if (len == 0) {
                                $(this).attr("data-original-title", "未查找到相关人员的当前地理位置信息")
                                    .tooltip("show");
                            } else {
                                var bd = [], uid, gps = [];
                                for (var i = 0; i < len; i++) {
                                    uid = re[i].uid;
                                    if (rt.gps[uid]) {//在地图上已显示
                                        bd.push(rt.bd[uid]);
                                        re.lb[uid].setContent(getLbs("success ", re[i].name));
                                    }
                                }
                            }
                        }
                        gps2bd(resultToGpsPoints(urt), function (bdPoints) {

                        });
                    });
                    break;
                case "uhi":

                    break;
            }
        }
    });
});