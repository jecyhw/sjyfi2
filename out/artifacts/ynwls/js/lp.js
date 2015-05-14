/**
 * Created by SNNU on 2015/5/10.
 */
$(document).ready(function() {
    mp = new BMap.Map('map');//地图实例
    mp.centerAndZoom(new BMap.Point(116.331398,39.897445),12);
    mp.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP ]}));   //添加地图类型控件
    mp.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT}));// 左上角，添加比例尺
    mp.addControl(new BMap.NavigationControl());  //左上角，添加默认缩放平移控件
    mp.enableScrollWheelZoom();
    mp.enableContinuousZoom();

    //根据ip来定位
    $.ajax({
        url: "http://api.map.baidu.com/location/ip?ak=QT9ntk6IBtEHGSy4BG7zOXoU&coor=bd09ll",
        dataType: "jsonp",
        success: function (data) {
            if  (data.status == 0) {
                var p = data.content.point;
                mp.setCenter(new BMap.Point(p.x, p.y));
            }
        },
        complete : function () {
            if (window.WebSocket){
                ws();
            } else{
                longPoll();//采用ajax长连接
            }
            rt.start();
        }
    });

    $(".panel").each(function(index, panel) {
        var $bc = $(panel).find("button.clear"),
            $bs = $(panel).find("button.search"),
            id = $bc.attr("data-for");
        $bs.click(function() {
            var $in = $("#" + id);
            if ($in.val() == "") {
                tooltipShow($in, 1000, "用户名不能为空");
            } else {
                eval(id + '($bs,  $in)');//根据字符串来动态执行upi或者uhi函数
            }
        });

        $bc.click(function() {
            eval("c_" + id + "()");
            $("#" + id).val("");
        });
    });
});
var mp;
var rt = {
    /**
     * 用户的跟新信息
     */
    us: [],
    /**
     * 转换的百度坐标
     */
    bd: [],
    /**
     * 地图上的marker
     */
    mk: [],
    /**
     * 对应marker的label标签
     */
    lb: [],
    /**
     * 对应mk使用的图标颜色
     */
    ci: [],
    /**
     * 记录查询出来的的uid
     */
    qd: [],
    /**
     * 查询的历史轨迹
     */
    py: [],
    /**
     * 颜色索引
     */
    color_index : -1,
    /**
     * 默认每五秒更新一次，如果某次请求未获取到更新数据,更新时间将会逐渐增加，如有数据更新，更新时间
     */
    lpt: 5000,
    /**
     * 最长的更新时间为1分钟
     */
    maxLpt: 60000,
    /**
     * 最短的更新时间为5秒
     */
    minLpt: 5000,
    /**
     * 每次更新的人数
     */
    upc: 0,
    /**
     * 默认离当前时间相差5分钟,用来检查用户是否离线
     */
    dit: 5,
    /**
     * 每个dit时间检查离线用户
     */
    olt: 1000 * 5,
    /**
     * 离线用户检查的计时器
     */
    timer: undefined,
    /**
     * 在线人数
     */
    olc: 0,
    /**
     * 根据uid来删除用户相关的信息
     * @param uid
     */
    del: function(uid) {
        mp.removeOverlay(rt.mk[uid]);
        mp.removeOverlay(rt.lb[uid]);
        rt.us[uid] = rt.bd[uid] = rt.mk[uid] = rt.lb[uid] = rt.ci[uid] = undefined;
        rt.online(-1);
    },
    /**
     *
     * @param inf us.inf对象
     * @param bd 百度地图坐标
     */
    update: function (inf, bd) {
        var uid = inf.i;
        rt.us[uid] = inf;
        rt.bd[uid] = bd;
        rt.mk[uid].setPosition(bd);
        rt.lb[uid].setPosition(bd);
    },
    /**
     *
     * @param inf us.inf对象
     * @param bd 百度地图坐标
     */
    add: function (inf, bd) {
        var uid = inf.i;
        rt.us[uid] = inf;
        rt.bd[uid] = bd;
        createMk();//创建marker
        createLabel();//创建label
        rt.online(1);
        function createMk() {//创建图标
            rt.ci[uid] = rt.getci();

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

        function createLabel() {
            var lb = new BMap.Label(getLbs("primary", inf.n), {
                position: bd,
                offset: new BMap.Size(5, -40)
            });
            lb.setStyle({
                border: "0px",
                margin: "0px",
                padding: "0px"
            });
            rt.lb[uid] = lb;
            mp.addOverlay(lb);
        }
    },
    clear: function() {
        rt.us = rt.bd = rt.ci = rt.li = rt.mk = [];
        rt.color_index = -1;
    },
    exists: function (uid) {
        return rt.us[uid];
    },
    /**
     * 离线用户检查
     */
    offline: function () {
        var di = [];
        //先获取离线用户
        for (var i in rt.us) {
            var uid = rt.us[i].i;
            if (!rt.qd[uid] && rt.us[i].comByT()) {//当前uid不在查询的uid中，且时间和现在超过rt.olt分钟
                di.push(uid);
            }
        }
        //执行删除
        for (var i = di.length - 1; i >= 0; i--) {
            rt.del(di[i]);
        }
    },
    /**
     * 启动离线用户检查
     */
    start: function() {
        rt.timer = setInterval(rt.offline, rt.olt);//每隔五分钟检查一次
    },
    /**
     * 停止离线用户检查
     */
    stop: function () {
        clearInterval(rt.timer);
    },
    getci: function () {
        if (rt.color_index >= color_list.length) {
            rt.color_index = 0;
        } else {
            rt.color_index++;
        }
        return rt.color_index;
    },
    setViewport: function () {
        var bd = [];
        for (var i in rt.bd) {
            bd.push(rt.bd[i]);
        }
        if (bd.length > 0) {
            mp.setViewport(bd);
        }
    },
    online: function (c) {
        rt.olc += c;
        $(".online").html(rt.olc);
    }
};

var us = {};
/**
 *
 * @param urt 用户的更新数据
 */
us.inf = function(urt){
    /**
     * 用户的gps的经度
     * @type {Number}
     */
    this.j = urt.longitude;
    /**
     * 用户的gps的纬度
     * @type {Number}
     */
    this.w = urt.latitude;
    /**
     * 用户的更新时间
     * @type {Date String}
     */
    this.t = urt.time;
    /**
     * 用户的名字
     * @type {String}
     */
    this.n = urt.name;
    /**
     * 用户的uid
     * @type {int}
     */
    this.i = urt.uid;
    /**
     * 判断两个gps是否一致
     * @param a us.inf对象
     * @returns {boolean}
     */
    this.eqGps = function(a){
        return this.j == a.j && this.w == a.w;
    };
    /**
     * 将gps的经纬度用逗号连接
     * @returns {string}
     */
    this.ts = function () {
        return this.j + "," + this.w;
    };
    /**
     * 和当前时间比较,判断差是否大于5分钟
     * @returns {boolean}
     */
    this.comByT = function() {
        return (new Date().getTime() - Date.parse(this.t.replace(/-/g, '/'))) / (1000 * 60) > rt.dit;
    }
};

/**
 * 获取gps转换百度坐标的的请求参数形式,并且过滤掉在地图上已显示但坐标未更新的用户数据
 * @param inf us.inf对象
 * @returns {Array}
 */
function formatGps(inf) {
    var re = [],
        len = inf.length;
    for (var i = 0; i < len; i++) {
        re.push(inf[i].ts());
    }
    return re;
}

/**
 * 过滤已显示用户的信息且获取的更新数据未变 ,根据rt.us,并转换为us.inf对象集合
 * @param urt 所有的用户更新信息
 * @returns {Array}
 */
function filterInf(urt) {
    var re = [];
    if (urt) {
        for (var i = urt.length - 1; i >= 0; i--) {
            var inf = new us.inf(urt[i]);
            if (rt.us[inf.i] && rt.us[inf.i].eqGps(inf)) {
                continue;
            }
            re.push(inf);
        }
    }
    return re;
}

/**
 * 将用户的更新信息转换成us.inf对象集合
 * @param urt
 * @returns {Array}
 */
function convertToInf(urt) {
    var re = [];
    if (urt) {
        for (var i = urt.length - 1; i >= 0; i--) {
            re.push(new us.inf(urt[i]));
        }
    }
    return re;
}


/**
 *根据用户的坐标来更新的Marker的位置
 * @param inf us.inf对象集合
 * @param bdPoints 百度坐标集合,一一对应
 * @returns {boolean}
 */
function updateMk(inf, bds) {
    if (!bds || bds.length == 0)//没有需要更新的用户
        return false;

    var len = bds.length;
    for (var i = 0; i < len; i++) {
        if (rt.exists(inf[i].i)) {
            rt.update(inf[i], bds[i])
        } else {
            rt.add(inf[i], bds[i]);
        }
    }
    rt.setViewport();//重新设置最佳视野
    return true;
}

/**
 *
 * @param type default|primary|success|info|warning|danger
 * @param name
 * @returns {string}
 */
function getLbs(type, name) {
    return '<span class="label label-' + type + '">' + name + "</span>";
}


/**
 *
 * @param $tag 标签的jq对象
 * @param st 显示时间
 */
function tooltipShow($tag, st, msg) {
    $tag.attr("data-original-title", msg).tooltip({
        placement: 'auto'
    }).tooltip("show");
    setTimeout(function () {
        $tag.tooltip("destroy");
    }, st);
}

function longPoll() {
    $.ajax({
        type: "post",
        url: web_prefix + "/QueryUserPosition.do",
        dataType: "json",
        success: function (data) {
            if (data.status == 0) {
                var inf = filterInf(data.result);
                gps2bd(formatGps(inf), function(bds) {
                    updateMk(inf, bds);
                    setTimeout(longPoll, rt.lpt);
                });
            }
        },
        error: function () {
            setTimeout(longPoll, rt.lpt);
        }
    });
}

/**
 * webSocket
 */
function ws() {
    $.getJSON(web_prefix + "/QueryUserPosition.do", function (data) {
        if (data.status == 0) {
            var inf = filterInf(data.result);
            gps2bd(formatGps(inf), function(bds) {
                updateMk(inf, bds);
            });
        }
    });

    var wsUrl = "ws://" + window.location.host + web_prefix + "/ShowUserByRealTime";
    var ws = new window.WebSocket(wsUrl);
    ws.onerror = function() {
        longPoll();
    };

    ws.onmessage = function (event) {
        var inf = filterInf(eval(event.data));
        gps2bd(formatGps(inf), function(bds) {
            updateMk(inf, bds);
        });
    };
}

/**
 * 查询用户的最新地理位置信息
 */
function upi($btn, $in) {
    $.getJSON( web_prefix + '/QueryUserPosition.do', {name: $in.val()}, function (data) {
        if (data.status == 0) {
            //先清空rt.qd
            c_upi();
            var urt = data.result, i, uid;
            if (urt.length == 0) {
                tooltipShow($btn, 2000, "未查找到相关人员的当前地理位置信息");
            } else {
                var bds = [],
                    inf = convertToInf(urt);

                for (i = 0; i < inf.length;) {//先过滤掉在地图上已显示的用户信息
                    uid = inf[i].i;//获取uid
                    if (rt.us[uid]) {//在地图上已显示
                        rt.lb[uid].setContent(getLbs("success ", rt.us[uid].n));//将lable标记
                        bds.push(rt.bd[uid]);//添加百度坐标
                        rt.qd[uid] = uid;//添加查询用户的uid
                        inf.splice(i, 1);
                    } else {
                        i++;
                    }
                }

                //下一步将未在地图上显示的用户信息显示出来
                gps2bd(formatGps(inf), function (_bds) {
                    if (_bds) {
                        for (var i = 0; i < inf.length; ) {
                            uid = inf[i].i;//获取uid
                            rt.qd[uid] = uid;
                            if (!rt.us[uid]) {//在地图上未显示,因为是异步，还是需要再次判断
                                rt.add(inf[i], _bds[i]);
                            }
                            rt.lb[uid].setContent(getLbs("success ", inf[i].n));//将lable标记
                            bds.push(rt.bd[uid]);
                        }
                    }
                    mp.setViewport(bds);
                });

                tooltipShow($btn, 2000, "查找到" + bds.length + "个用户地理位置信息");
            }
        }
    });
}
/**
 * 清除查询的用户实时位置
 */
function c_upi() {
    var uid, i;
    for (i in rt.qd) {
        uid = rt.qd[i];
        rt.lb[uid].setContent(getLbs("primary ", rt.us[uid].n));
    }
    rt.qd = [];
}

/**
 * 查询用户的历史轨迹
 */
function uhi($btn, $in) {
    $.getJSON( web_prefix + '/QueryUserHistory.do', {name: $in.val()}, function (data) {
        if (data.status == 0) {
            c_uhi();
            var uh = data.result;
            if (uh.length == 0) {
                tooltipShow($btn, 2000, "未查找到相关人员的历史地理位置信息");
            } else {
                var rc = 0, sc = 0, bdArr = [];
                $.each(uh, function (index, uhi) {
                    (function(uhis) {
                        var gps = [];
                        $.each(uhis.history, function(_index, his) {
                            gps.push(his.longitude + "," + his.latitude);
                        });
                        gps2bd(gps, function (bds) {
                            if (bds) {
                                var options = {}, uid = uhis.uid;
                                options.strokeColor = rt.ci[uid] ? color_list[rt.ci[uid]] : rt.getci();
                                rt.py.push(new BMap.Polyline(bds, lineStyle(options)));
                                bdArr = bdArr.concat(bds);
                                sc++;
                            }
                            rc++;
                            if (rc == uh.length) {//全部解析完
                                for (var i = rt.py.length - 1; i >= 0; i--) {
                                    mp.addOverlay(rt.py[i]);
                                }
                                mp.setViewport(bdArr);
                                tooltipShow($btn, 2000, "查找到" + sc + "个用户地理位置信息");
                            }
                        });
                    })(uhi);
                });
            }
        }
    });
}
/**
 * 移除查询的历史记录
 */
function c_uhi() {
    for (var i = rt.py.length - 1; i >= 0; i--) {
        mp.removeOverlay(rt.py[i]);
    }
    rt.py = [];
}

function lineStyle(options) {
    return $.extend({
        strokeColor: "#8A2BE2",
        fillColor: "",
        strokeWeight: 3,
        strokeOpacity: 0,
        fillOpacity: 0
    }, options);
}
