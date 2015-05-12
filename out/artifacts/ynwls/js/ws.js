/**
 * Created by SNNU on 2015/5/10.
 */
$(document).ready(function(){
    function getLbs(type, name) {
        return re = '<span class="label label-' + type + '">' + name + "</span>";
    }

    function createMk(uid, bd) {//����ͼ��
        if (rt.color_index >= color_list.length) {
            rt.color_index = 0;
        }
        rt.ci[uid] = ++rt.color_index;

        var mk =  new BMap.Marker(bd, {
            // ָ��Marker��icon����ΪSymbol
            icon: new BMap.Symbol(BMap_Symbol_SHAPE_POINT, {
                //scale: 1,//ͼ�����Ŵ�С
                fillColor: color_list[rt.color_index],//�����ɫ
                fillOpacity: 0.8//���͸����
            })
        });
        rt.mk[uid] = mk;
        mp.addOverlay(mk);//��ӵ���ͼ
        addEventOnMk();

        //��marker����¼�
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

    var rt = {//����û��������Ϣ
            gps: [],//gps����
            bd: [],//ת���İٶ�����
            mk: [],//��ͼ�ϵ�marker
            lb: [],//��Ӧmarker��label��ǩ
            ci: [],//��Ӧmkʹ�õ�ͼ����ɫ
            color_index : -1,
            lpt: 5000,//Ĭ��ÿ�������һ�Σ����ĳ������δ��ȡ����������,����ʱ�佫�������ӣ��������ݸ��£�����ʱ��
            maxLpt: 60000,//��ĸ���ʱ��Ϊ1����
            minLpt: 5000,//��̵ĸ���ʱ��Ϊ5��
            upc: 0,//ÿ�θ��µ�����
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
        gps = {};//gps�������ռ�
    gps.point = function(j, w){//gps������
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
                var urt = eval(event.data);//���µ��û��б�
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

        //���ݸ�����Ϣ����̬�����ֻ�ʱ��
        function adjustUpdateLongPoolTime() {
            if (rt.updateUserCount == 0 && rt.lpt < rt.maxLpt) {//��ǰ����û�и��£�����С�������ʱ��
                rt.lpt++;
            } else {
                rt.updateUserCount = 0;
                if (rt.lpt > rt.minLpt) {
                    rt.lpt--;
                }
            }
        }
    }



    //��ȡgpsת���ٶ�����ĵ����������ʽ,���ҹ��˵��ڵ�ͼ������ʾ������δ���µ��û�����
    function resultToGpsPoints(urt) {
        var gpsPoints = [];
        if (urt) {
            var len = urt.length, _urt;
            for (var i = 0; i < len; i++) {
                _urt = urt[i];
                if (rt.gps[_urt.uid] && rt.gps[_urt.uid].equals(new gps.point(_urt.longitude, _urt.latitude))) {
                    continue;//������û�����ʾ,��������δ��
                }
                gpsPoints.push(_urt.longitude + "," + _urt.latitude);
            }
        }
        return gpsPoints;
    }

    //�����û������������µ�Marker��λ��
    function updateMarkerPosition(urt, bdPoints) {
        if (!bdPoints || bdPoints.length == 0)//û����Ҫ���µ��û�
            return false;
        var len = bdPoints.length;
        for (var i = 0; i < len; i++) {
            var _urt = urt[i],
                bdPoint = bdPoints[i];
            if ( rt.gps[_urt.uid]) {//���û�����ʾ,�����û�����
                rt.update(_urt, bdPoint);
            } else {
                rt.add(_urt, bdPoint);
            }
            rt.updateUserCount++;//��������1
        }
        mp.setViewport(rt.bd);//�������������Ұ
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
                                $(this).attr("data-original-title", "δ���ҵ������Ա�ĵ�ǰ����λ����Ϣ")
                                    .tooltip("show");
                            } else {
                                var bd = [], uid, gps = [];
                                for (var i = 0; i < len; i++) {
                                    uid = re[i].uid;
                                    if (rt.gps[uid]) {//�ڵ�ͼ������ʾ
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