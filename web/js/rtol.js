/**
 * Created by SNNU on 2015/5/14.
 */
function rtol(point, text) {
    this._point = point;
    this._text = text;
}

rtol.prototype = new BMap.Overlay();
rtol.prototype.initialize = function (map) {
    this._map = map;
    var $div = $(rtol.DEFAULTS.template),
        $inner = $div.find(".tooltip-inner"),
        $arrow = $div.find(".tooltip-arrow"),
        div = this._div = $div[0];
    $div.css({
        "z-index": BMap.Overlay.getZIndex(this._point.lat),
        "white-space": "nowrap"
    });

    $arrow.css({
        "border-left-width": 0,
        "left": rtol.DEFAULTS.al,
        "margin-left": 0,
        "border-top-color": "#1c94c4"
    });
    $inner
        .html(this._text)
        .css({
            "background-color": "#1c94c4"
        }).hover(function () {
            $inner.css({
                cursor: "pointer",
                "text-decoration": "underline"
            })
        }, function () {
            $inner.css({
                cursor: "default",
                "text-decoration": "none"
            })
        }).click(function (ev) {
            console.log(ev);
        });
    mp.getPanes().labelPane.appendChild(div);
    return div;
};

rtol.prototype.draw = function () {
    var map = this._map,
        pixel = map.pointToOverlayPixel(this._point),
        $div = $(this._div);
    $div.css({
        top: pixel.y - rtol.DEFAULTS.ah - $div.outerWidth(),
        left: pixel.x - rtol.DEFAULTS.al
    });
};



rtol.DEFAULTS = {
    al: 4,
    ah: 5,
    template: '<div class="tooltip top in" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
};

// 百度地图API功能
var mp = new BMap.Map("map");
mp.centerAndZoom(new BMap.Point(116.3964,39.9093), 15);
mp.enableScrollWheelZoom();

var myCompOverlay = new rtol(new BMap.Point(116.407845,39.914101), "银湖海岸城");

mp.addOverlay(myCompOverlay);