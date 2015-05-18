/**
 * Created by SNNU on 2015/5/14.
 */
function Rtol(point, inf) {
    this.point = point;
    this.inf = inf;
}

Rtol.prototype = new BMap.Overlay();
Rtol.prototype.initialize = function (map) {
    this.map = map;
    var option = Rtol.DEFAULTS,
        $div = $(option.template),
        $inner = $div.find(".tooltip-inner"),
        $arrow = $div.find(".tooltip-arrow"),
        inf = this.inf,
        div = this.div = $div[0];
    $div.css({
        "z-index": BMap.Overlay.getZIndex(this.point.lat),
        "white-space": "nowrap"
    });

    $arrow.css({
        "border-left-width": 0,
        "left": option.al,
        "margin-left": 0,
        "border-top-color": inf.c
    });
    $inner
        .html(inf.n)
        .css({
            "background-color": inf.c
        }).hover(function () {
            $inner.css({
                cursor: "pointer",
                "text-decoration": "underline"
            }).html(inf.n + "(" + inf.t + ")");
        }, function () {
            $inner.css({
                cursor: "default",
                "text-decoration": "none"
            }).html(inf.n);
        }).click(function (ev) {
            console.log(ev);
        });
    mp.getPanes().labelPane.appendChild(div);
    div.height = $div.outerHeight();
    return div;
};

Rtol.prototype.draw = function () {
    var map = this.map,
        pixel = map.pointToOverlayPixel(this.point),
        $div = $(this.div),
        option = Rtol.DEFAULTS;
    $div.css({
        top: pixel.y - this.div.height,
        left: pixel.x - option.al
    });
};

Rtol.prototype.update = function (point, inf) {
    this.point = point;
    this.inf = inf;
    $(this.div).find(".tooltip-inner").html(inf.n);
};

Rtol.prototype.highlight = function () {
    $(this.div).find(".tooltip-inner").addClass("highlight");
};

Rtol.prototype.unHighlight = function () {
    $(this.div).find(".tooltip-inner").removeClass("highlight");
};

Rtol.DEFAULTS = {
    al: 4,
    ah: 5,
    template: '<div class="tooltip top in" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
};

/*
// 百度地图API功能
var mp = new BMap.Map("map");
mp.centerAndZoom(new BMap.Point(116.3964,39.9093), 15);
mp.enableScrollWheelZoom();

var myCompOverlay = new Rtol(new BMap.Point(116.407845,39.914101), {n: "银湖海岸城",t: "2015-5-15 10:23:12", c: "red"
});
console.log(myCompOverlay);
mp.addOverlay(myCompOverlay);*/
