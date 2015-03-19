package com.cn.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by SNNU on 2015/3/19.
 */
public class PlaceMark {
    String name;//两者都有
    List desc;//关键点的描述
    TTracksPointsEntity coordinate;//关键点的坐标
    List<TTracksPointsEntity> route;//轨迹的坐标
    RouteStyle routeStyle;//轨迹的样式

    public void setName(String name) {
        this.name = name;
    }

    public TTracksPointsEntity setCoordinate(String coordinateText) {
        coordinate = new TTracksPointsEntity();
        StringTokenizer tokenizer = new StringTokenizer(coordinateText, ",", false);
        if (tokenizer.hasMoreElements())
        {
            coordinate.setLongitude(Double.parseDouble(tokenizer.nextToken()));
            coordinate.setLatitude(Double.parseDouble(tokenizer.nextToken()));
            coordinate.setAltitude(Double.parseDouble(tokenizer.nextToken()));

        }
        return coordinate;
    }

    public void setCoordinates(String coordinatesText) {
        route = new ArrayList<TTracksPointsEntity>();
        StringTokenizer tokenizer = new StringTokenizer(coordinatesText, " ", false);
        while (tokenizer.hasMoreElements()) {
            route.add(setCoordinate(tokenizer.nextToken()));
        }
        coordinate = null;//因为在setCoordinate中使用了coordinate，所以最后要设置为null
    }

    public void setDescription(String descText, String kmlFilePath){
        desc = new ArrayList();
        String dir = kmlFilePath.substring(0, kmlFilePath.lastIndexOf("/") + 1);//获取图片，音频的文件前缀
        StringTokenizer tokenizer = new StringTokenizer(descText, "\"", false);
        while (tokenizer.hasMoreElements()) {
            String tmp = tokenizer.nextToken();
            if (tmp.contains("img") || tmp.contains("video") || tmp.contains("audio")) {
                //可能出现tmp1不包含img,video,audio, 包含下一个肯定就是地址
                desc.add(dir + tokenizer.nextToken());
            }
        }
    }

    public RouteStyle getPathStyle() {
        if (routeStyle == null)
            routeStyle = new RouteStyle();
        return routeStyle;
    }

    public void setRouteStyle(RouteStyle routeStyle) {
        this.routeStyle = routeStyle;
    }
}
