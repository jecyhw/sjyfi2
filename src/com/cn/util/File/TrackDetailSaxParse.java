package com.cn.util.File;

import com.cn.bean.TTracksEntity;
import com.cn.util.DateUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.Date;

/**
 * Created by SNNU on 2015/3/19.
 */
public class TrackDetailSaxParse extends DefaultSaxParse{
    TTracksEntity track = new TTracksEntity();

    @Override
    public Object getParseObject() {
        return track;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if ("name".equals(qName)) {
            track.setName(text);
        } else if ("author".equals(qName)) {
            track.setAuthor(text);
        } else if ("starttime".equals(qName)) {
            track.setStarttime(new Date(DateUtil.string2Date(text).getTime()));
        } else if ("endtime".equals(qName)) {
            track.setEndtime(new Date(DateUtil.string2Date(text).getTime()));
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
}
