/**
 * Created by SNNU on 2015/5/7.
 */
var web_prefix = "/ynwls";

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