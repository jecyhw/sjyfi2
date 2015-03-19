package com.cn.util.File;

import com.cn.bean.PlaceMark;
import com.cn.bean.RouteStyle;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SNNU on 2015/3/19.
 */
public class PlaceMarkSaxParse extends DefaultSaxParse {
    List<PlaceMark> placeMarkList = new ArrayList<PlaceMark>();
    PlaceMark placeMark;
    List<RouteStyle> routeStyleList = new ArrayList<RouteStyle>();//用来记录Style
    RouteStyle routeStyle;
    boolean isRoute = false;
    String parseFileUri;

    public String getParseFileUri() {
        return parseFileUri;
    }

    public PlaceMarkSaxParse(String parseFileUri) {
        this.parseFileUri = parseFileUri;
    }

    @Override
    public Object getParseObject() {
        return placeMarkList ;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        if (qName.equals("Placemark")) {
            placeMark = new PlaceMark();
        } else if (qName.equals("LineString")) {//  LineString/coordinates
            isRoute = true;
        } else if (qName.equals("Style")) {
            routeStyle = new RouteStyle();
            routeStyle.setId(attrs.getValue("id"));//获取Style的id值
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals("Placemark")) {//一个Placemark解析完
            placeMarkList.add(placeMark);
            isRoute = false;
            routeStyle = null;
        } else if (qName.equals("coordinates")) {
            if (isRoute)
                placeMark.setCoordinates(text);
            else
                placeMark.setCoordinate(text);
        } else if (qName.equals("name")) {//无子元素，所以可以这样用
            placeMark.setName(text);
        } else if (qName.equals("coordinates")) {
            builder.append(text);
        } else if (qName.equals("description")) {
            placeMark.setDescription(text, parseFileUri);
        }  else if (qName.equals("color")) {//路线样式颜色
            if (null == routeStyle)//有两种情况，一种路线是引用的样式，另一种是直接设置的样式
                placeMark.getPathStyle().setColor(text);
            else
                routeStyle.setColor(text);
        } else if (qName.equals("width")) {//路线样式宽度，
            if (null == routeStyle)
                placeMark.getPathStyle().setWidth(text);
            else
                routeStyle.setWidth(text);
        } else if (qName.equals("styleUrl")) {//说明:引用的样式要在该路线之前设置才能获取到
            text = text.substring(1);//获取该styleUrl的id，去掉开头的#
            for (RouteStyle style : routeStyleList) {
                if (routeStyle.getId().equals(text)) {
                    placeMark.setRouteStyle(style);
                    break;
                }
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
         builder.append(ch, start, length);
    }

    static public void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        PlaceMarkSaxParse handler = new PlaceMarkSaxParse("C:\\Users\\SNNU\\Desktop\\RouteRecord.kml");
        parser.parse(handler.getParseFileUri(), handler);
    }
}

