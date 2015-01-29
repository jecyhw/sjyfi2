package com.cn.util.File;

import com.cn.bean.TTracksEntity;
import com.cn.test.TestOutput;
import com.cn.util.DateUtil;
import org.dom4j.DocumentException;

import java.sql.Date;

/**
 * Created by acm on 12/3/14.
 */
public class XmlParser  extends FileParser{
    public XmlParser(String fileName) throws DocumentException {
        super(fileName);
    }

    public TTracksEntity getTrackEntity() {
        TTracksEntity tracksEntity = new TTracksEntity();
        if (root != null) {
            String tmp;
            tracksEntity.setName(root.elementText("name"));
            tracksEntity.setAuthor(root.elementText("author"));

            tmp = root.elementText("starttime");
            if (!tmp.isEmpty()) {
                tracksEntity.setStarttime(new Date(DateUtil.string2Date(tmp).getTime()));
            }

            tmp = root.elementText("endtime");
            if (!tmp.isEmpty()) {
                tracksEntity.setEndtime(new Date(DateUtil.string2Date(tmp).getTime()));
            }

            tmp = root.elementText("length");
            if (!tmp.isEmpty()) {
                tracksEntity.setLength(Double.parseDouble(tmp));
            }

            tmp = root.elementText("maxaltitude");
            if (!tmp.isEmpty()) {
                tracksEntity.setMaxaltitude(Double.parseDouble(tmp));
            }

            tracksEntity.setKeysiteslist(root.elementText("keysiteslist"));
            tracksEntity.setAnnotation(root.elementText("annotation"));
            TestOutput.println(tracksEntity.toString());
        }
        return tracksEntity;
    }

    public void append(XmlParser source) {
        if (root != null) {
            TTracksEntity sourceTrackEntity = source.getTrackEntity();
            TTracksEntity destTrackEntity = this.getTrackEntity();
            String tmpSource, tmpDest;

            root.element("name").addText("," + sourceTrackEntity.getName());

            tmpSource = sourceTrackEntity.getAuthor();
            tmpDest = destTrackEntity.getAuthor();
            if (!isContains(tmpDest, tmpSource)) {
                root.element("author").addText("," + tmpSource);
            }

            if (sourceTrackEntity.getStarttime().before(destTrackEntity.getStarttime())) {
                root.element("starttime").setText(DateUtil.date2String(sourceTrackEntity.getStarttime()));
            }
            if (sourceTrackEntity.getEndtime().after(destTrackEntity.getEndtime())) {
                root.element("endtime").setText(DateUtil.date2String(sourceTrackEntity.getEndtime()));
            }
            root.element("length").setText(String.valueOf(sourceTrackEntity.getLength() + destTrackEntity.getLength()));
            if (sourceTrackEntity.getMaxaltitude() > destTrackEntity.getMaxaltitude()) {
                root.element("maxaltitude").setText(String.valueOf(sourceTrackEntity.getMaxaltitude()));
            }

            tmpSource = trimComma(sourceTrackEntity.getKeysiteslist().trim());
            tmpDest = trimComma(destTrackEntity.getKeysiteslist().trim());
            if (tmpDest.isEmpty()) {
                root.element("keysiteslist").addText(tmpSource);
            }
            else {

                root.element("keysiteslist").setText(tmpDest +  "," + tmpSource);
            }

            tmpSource = trimComma(sourceTrackEntity.getAnnotation().trim());
            tmpDest = trimComma(destTrackEntity.getAnnotation().trim());
            if (tmpDest.isEmpty()) {
                root.element("annotation").addText(tmpSource);
            }
            else {
                root.element("annotation").setText(tmpDest + "," + tmpSource);
            }
        }
        save();
    }


    private boolean isContains(String source, String dest) {
        String[] str = source.split(",");
        for (String t : str) {
            if (t.equals(dest))
                return true;
        }
        return  false;
    }

    private String trimComma(String str) {
        if (str.isEmpty())
            return str;
        char[] chs = str.toCharArray();
        int start = 0, end = chs.length;
        for (; start < end; start++) {
            if (chs[start] != ',') {
                break;
            }
        }

        for (; end > start ; end--) {
            if (chs[end - 1] != ',') {
                break;
            }
        }
        return str.substring(start, end);
    }
}
