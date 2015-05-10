/**
 * Created by SNNU on 2015/5/7.
 */
//gps坐标数组,格式为['114.21892734521,29.575429778924','114.21892734521,29.575429778924'],callback全部转换完之后的回调函数，参数为百度坐标数组
function gps2bd(gpsPoints, callback) {
    if (!gpsPoints || gpsPoints.length == 0) {//不需要转换
        callback();
        return;
    }

    var url = "http://api.map.baidu.com/geoconv/v1/?from=1&to=5&ak=kPEqHXhcmEDm9DhNYKbKfeer&callback=?&coords=",
        maxCnt = 50,//每次转换多少个坐标
        arr,//用来遍历gpsPoints
        baiDuPoints = [],//坐标转换的最终结果
        shouldAjaxCount = -1,//坐标转换应该要使用的次数
        actuallyAjaxCount = -1;//坐标转换实际使用的次数,用来判断坐标是否转换完

    while (gpsPoints.length > 0) {
        arr = gpsPoints.splice(0, maxCnt);
        (function(ajaxCount) {
            $.getJSON(url + arr.join(";"),
                function(data) {
                    ++actuallyAjaxCount;
                    if (data.status == 0) {
                        baiDuPoints[ajaxCount] = data.result;
                    } else {//该坐标解析失败
                        callback();//并且停止解析
                        return ;
                    }

                    if (actuallyAjaxCount == shouldAjaxCount && gpsPoints.length == 0) {//所有坐标转换完
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