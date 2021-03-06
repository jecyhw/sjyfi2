package com.cn.util.File;

import com.cn.bean.TTracksEntity;
import com.cn.test.TestOutput;
import com.cn.util.DateUtil;
import org.xml.sax.SAXException;

import java.sql.Timestamp;


/**
 * Created by SNNU on 2015/3/20.
 */
public class TrackDetailFileParse extends BaseFileParse{
    TTracksEntity track = new TTracksEntity();
    StringBuilder builder = new StringBuilder();
    String text;

    public Object getParseObject() {
        TestOutput.println("TrackDetailFileParse: " + track);
        return track;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        text = builder.toString().trim();
        builder.delete(0, builder.length());
        if ("name".equals(qName)) {
            track.setName(text);
        } else if ("author".equals(qName)) {
            track.setAuthor(text);
        } else if ("starttime".equals(qName)) {
            track.setStarttime(Timestamp.valueOf(text));
        } else if ("endtime".equals(qName)) {
            track.setEndtime(Timestamp.valueOf(text));
        } else if ("length".equals(qName)) {
            track.setLength(Double.parseDouble(text));
        } else if ("maxaltitude".equals(qName)) {
            track.setMaxaltitude(Double.parseDouble(text));
        } else if ("keysiteslist".equals(qName)) {
            track.setKeysiteslist(text);
        } else if ("annotation".equals(qName)) {
            track.setAnnotation(text);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        builder.append(ch, start, length);
    }

}
