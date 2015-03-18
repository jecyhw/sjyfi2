package com.cn.util.File;

import com.cn.bean.TTracksPointsEntity;
import com.cn.test.TestOutput;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.*;

/**
 * Created by acm on 12/3/14.
 */
public class KmlParser extends FileParser{
    public KmlParser(String fileName) throws DocumentException {
        super(fileName);
    }

    public Map getMapInfo() {
        Map<String, Object> result = new Hashtable<String, Object>();
        result.put("points", getPoints());
        result.put("placemark", getPlaceMark());
        return result;
    }

    public List<List<TTracksPointsEntity>> getPoints() {
        List<List<TTracksPointsEntity>> list = new ArrayList<List<TTracksPointsEntity>>();
        if (root != null) {
            String xPath = "Folder/Placemark/LineString/coordinates";
            List<Node> coordinates = root.selectNodes(xPath);
            String s = root.getPath();
            for (Node node : coordinates) {
                List<TTracksPointsEntity> entityList = new ArrayList<TTracksPointsEntity>();
                StringTokenizer tokenizer = new StringTokenizer(node.getText(), " ", false);
                while (tokenizer.hasMoreElements()) {
                    entityList.add(getCoordinate(tokenizer.nextToken()));
                }
                list.add(entityList);
            }
        }
        TestOutput.println(list);
        return list;
    }

    private TTracksPointsEntity getCoordinate(String coordinateText) {
        StringTokenizer tokenizer = new StringTokenizer(coordinateText, ",", false);
        TTracksPointsEntity pointsEntity = new TTracksPointsEntity();
        if (tokenizer.hasMoreElements())
        {
            pointsEntity.setLongitude(Double.parseDouble(tokenizer.nextToken()));
            pointsEntity.setLatitude(Double.parseDouble(tokenizer.nextToken()));
            pointsEntity.setAltitude(Double.parseDouble(tokenizer.nextToken()));

        }
        return pointsEntity;
    }

    public void append(KmlParser source) {
        Element rootSource = source.getElementRoot();
        Iterator it = rootSource.elementIterator("Placemark");
        while (it.hasNext()) {
            root.add((Element)((Element)it.next()).clone());
        }
        root.element("Folder").appendContent(rootSource.element("Folder"));
        save();
    }

    public  Object getPlaceMark() {
        List<Object> result = new ArrayList<Object>();
        if (root != null) {
            List<Element> eleList = root.elements("Placemark");
            for (Element ele : eleList) {
                Hashtable<String, Object> table = new Hashtable<String, Object>();
                table.put("name", ele.elementText("name"));
                table.put("description", getDescription(ele.elementText("description")));
                table.put("coordinates", getCoordinate(ele.selectSingleNode("Point/coordinates").getText()));
                result.add(table);
            }
        }
        return result;
    }

    private  Object getDescription(String description) {
        String dir = fileName.substring(0, fileName.lastIndexOf("/") + 1);//获取图片，音频的文件前缀
        StringTokenizer tokenizer = new StringTokenizer(description, "\"", false);
        List list = new ArrayList();
        while (tokenizer.hasMoreElements()) {
            String tmp = tokenizer.nextToken();
            if (tmp.contains("img") || tmp.contains("video") || tmp.contains("audio")) {
                //可能出现tmp1不包含img,video,audio, 包含下一个肯定就是地址
                list.add(dir + tokenizer.nextToken());
            }
        }
        return list;
    }

    static public void main(String[] args) {
        try {
            new KmlParser("C:\\Users\\SNNU\\Desktop\\routeRecord_20140723_052810\\RouteRecord.kml").getMapInfo();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
