package com.cn.bean;

import com.cn.util.Config;
import com.cn.util.File.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SNNU on 2015/3/19.
 */
public class PlaceMark {
    static Pattern descPatten = Pattern.compile("(img|video|audio)\\s+src=\"(?:[^/]+/)?(.+?)\"");
    private String name;//两者都有
    private List desc;//关键点的描述
    private TTracksPointsEntity coordinate;//关键点的坐标
    private List<TTracksPointsEntity> route;//轨迹的坐标
    private RouteStyle routeStyle = new RouteStyle();;//轨迹的样式

    public String getName() {
        return name;
    }

    public List getDesc() {
        return desc;
    }

    public TTracksPointsEntity getCoordinate() {
        return coordinate;
    }

    public RouteStyle getRouteStyle() {
        return routeStyle;
    }

    public List<TTracksPointsEntity> getRoute() {
        return route;
    }

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
        Matcher matcher = descPatten.matcher(descText);
        String dir = kmlFilePath.substring(0, kmlFilePath.lastIndexOf("/") + 1);//获取图片，音频的文件父目录
        String type, prefix;
        File file;
        while (matcher.find()) {
            type = matcher.group(1);
            String[] hrefArr = matcher.group(2).split(",");
            prefix = dir;
            if ("img".equals(type)) {
                prefix += Config.KMZFileInfo.photoDirectoryName;
            } else if ("video".equals(type)) {
                prefix += Config.KMZFileInfo.videoDirectoryName;
            } else if ("audio".equals(type)) {
                prefix += Config.KMZFileInfo.audioDirectoryName;
            }
            for (String href : hrefArr) {
                file = new File(prefix + href);//判断下该文件是否存在
                if (file.exists()) {
                    desc.add(prefix + href);
                }
            }
        }
    }

    public void setRouteStyle(RouteStyle routeStyle) {
        this.routeStyle = routeStyle;
    }

    public static void main(String[] args) {
        Pattern descPatten = Pattern.compile("(img|video|audio) src=\"(?:[^/]+/)?(.+?)\"");
        Matcher matcher = descPatten.matcher("<description>img src=\"20140723061827_0.png,20140723061827_1.png,20140723061827_2.png,20140723061827_3.png,20140723061827_5.png,20140723061827_4.png,\";video src=\"20140723061827.mp4\";audio src=\"20140723061827.aac\"</description>\n" +
                "\t\t\t<Point>");
        while (matcher.find()) {
            String[] t = matcher.group(2).split(",");

            for (String tt : t) {
                System.out.println(tt);
            }
        }
    }
}
