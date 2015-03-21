package com.cn.util.File;

import com.cn.util.Config;
import com.cn.util.Json;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SNNU on 2015/3/20.
 */
public abstract class BaseFileParse {
    String parseFileUri;//需要解析的文件的完整路径

    /**
     *
     * @return 返回解析的数据，默认为null
     */
    public Object getParseObject() {
        return null ;
    }

    public String getParseFileUri() {
        return parseFileUri;
    }

    public void setParseFileUri(String parseFileUri) {
        this.parseFileUri = parseFileUri;
    }

    public void startDocument() throws SAXException {

    }

    public void endDocument() throws SAXException {

    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

    }

    public void characters(char[] ch, int start, int length) throws SAXException {

    }

    static public void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException {
        TrackDetailFileParse trackDetailFileParse = new TrackDetailFileParse();
        new JSAXParser().parse("C:\\Users\\SNNU\\Desktop\\routeRecord_20140331_172138\\TrackDetail.xml", trackDetailFileParse);
        System.out.println(trackDetailFileParse.getParseObject());
    }
}
