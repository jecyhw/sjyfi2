/**
 * Created by SNNU on 2015/5/10.
 */
$(document).ready(function() {
    mp = new BMap.Map('map');//地图实例
    mp.centerAndZoom(new BMap.Point(116.331398,39.897445),12);
    new BMap.LocalCity().get(function(result) {//根据ip来定位
        mp.setCenter(result.name);
    });
    mp.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP ]}));   //添加地图类型控件
    mp.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT}));// 左上角，添加比例尺
    mp.addControl(new BMap.NavigationControl());  //左上角，添加默认缩放平移控件
    mp.enableScrollWheelZoom();
    mp.enableContinuousZoom();

/*    $('input').on('input propertychange',function(){
        $("input[data-toggle=dropdown]").dropdown('hide');
        var $this = $(this);
        if ($this.val()  != "") {
            $.getJSON(web_prefix + "/QueryUserName.do", { name: $this.val() }, function (data) {
                var $next = $this.next("ul");
                $next.html("");
                if (data.status == 0) {
                    var re = data.result;
                    if (re.length > 0) {
                        for (var i = re.length - 1; i >= 0; i--) {
                            $next.append(getLi(upi, re[i]));
                        }
                        $this.attr('data-toggle', 'dropdown');//添加下拉菜单
                        return;
                    }
                }
                $this.attr('data-toggle', '');//去除下拉菜单
            });
        }
    });*/

    $("button").click(function () {
        var id = $(this).attr("data-for");
        var $in = $("#" + id);
        if ($in.val() == "") {
            tooltipShow($in, 1000, "用户名不能为空");
        } else {
            /*if (id == "upi") {
                upi($(this), $in.val());
            } else if (id == "uhi") {
                uhi($(this), $in.val());
            }*/
            eval(id + '($(this), "' + $in.val() +'")');//根据字符串来动态执行upi或者uhi函数
        }
    });

    longPoll();//采用ajax长连接
    rt.start();
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
    olt: 1000 * 60 * 5,
    /**
     * 离线用户检查的计时器
     */
    timer: undefined,

    /**
     * 根据uid来删除用户相关的信息
     * @param uid
     */
    del: function(uid) {
        mp.removeOverlay(rt.mk[uid]);
        mp.removeOverlay(rt.lb[uid]);
        rt.us[uid] = rt.bd[uid] = rt.mk[uid] = rt.lb[uid] = rt.ci[uid] = undefined;
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
                offset: new BMap.Size(10, 10)
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
        return re.us[uid];
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
        return (new Date().getTime() - Date.parse(this.t)) / (1000 * 60) > rt.dit;
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
            if (rt.us[inf.i] && re.us[inf.i].eqGps(inf)) {
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
    mp.setViewport(rt.bd);//重新设置最佳视野
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
 * @param callback 点击的回调函数
 * @param name 要显示的标签值
 * @returns {string}
 */
function getLi(callback, name) {
    return '<li role="presentation"><a role="menuitem" tabindex="-1" href="javascript:'+ callback + '(' + name +')">' + name + '</a></li>';
}

/**
 *
 * @param $tag 标签的jq对象
 * @param st 显示时间
 */
function tooltipShow($tag, st, msg) {
    $tag.attr("data-original-title", msg).tooltip("show");
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
 * 查询用户的最新地理位置信息
 */
function upi(name) {
    $.getJSON( web_prefix + '/QueryUserPosition.do', {name: name}, function (data) {
        if (data.status == 0) {
            //先清空rt.qd
            rt.qd = [];
            var urt = data.result;
            if (urt.length == 0) {
                tooltipShow($(this), 2000, "未查找到相关人员的当前地理位置信息");
            } else {
                var bds = [],
                    uid,
                    inf = convertToInf(urt);

                for (var i = 0; i < inf.length;) {//先过滤掉在地图上已显示的用户信息
                    uid = inf[i].i;//获取uid
                    if (rt.us[uid]) {//在地图上已显示
                        rt.lb[uid].setContent(getLbs("success ", inf[i].n));//将lable标记
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
                            if (!rt.us[uid]) {//在地图上已显示,因为是异步，还是还需要再次判断
                                rt.add(inf[i], _bds[i]);
                            }
                            rt.lb[uid].setContent(getLbs("success ", inf[i].n));//将lable标记
                            bds.push(rt.bd[uid]);
                        }
                    }
                    mp.setViewport(bds);
                });

                tooltipShow($(this), 2000, "查找到" + bds.length + "个用户地理位置信息");
            }
        }
    });
}

/**
 * 查询用户的历史轨迹
 */
function uhi() {
    $.getJSON( web_prefix + '/QueryUserPosition.do', {name: name}, function (data) {
        if (data.status == 0) {

            var uh = data.result;
            if (uh.length == 0) {
                tooltipShow($(this), 2000, "未查找到相关人员的历史地理位置信息");
            } else {
                var rc = 0, sc = 0;
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
                                new BMap.Polyline(bds, lineStyle(options));
                                sc++;
                            }

                            rc++;
                            if (rc == uh.length) {//全部解析完
                                tooltipShow($(this), 2000, "查找到" + bds.length + "个用户地理位置信息");
                            }
                        });
                    })(uhi);
                });

                var bds = [],
                    uid,
                    inf = convertToInf(urt);

                for (var i = 0; i < inf.length;) {//先过滤掉在地图上已显示的用户信息
                    uid = inf[i].i;//获取uid
                    if (rt.us[uid]) {//在地图上已显示
                        rt.lb[uid].setContent(getLbs("success ", inf[i].n));//将lable标记
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
                            if (!rt.us[uid]) {//在地图上已显示,因为是异步，还是还需要再次判断
                                rt.add(inf[i], _bds[i]);
                            }
                            rt.lb[uid].setContent(getLbs("success ", inf[i].n));//将lable标记
                            bds.push(rt.bd[uid]);
                        }
                    }
                    mp.setViewport(bds);
                });

                tooltipShow($(this), 2000, "查找到" + bds.length + "个用户地理位置信息");
            }
        }
    });
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
