/**
 * Created by SNNU on 2015/5/14.
 */
function rtol(point) {
    this._point = point;
}

rtol.prototype = new Bmap.Overlay();
rtol.prototype.initialize = function (map) {
    this._map = map;

};