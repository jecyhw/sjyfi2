package com.cn.util.File;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Created by SNNU on 2015/3/19.
 */
public abstract class DefaultSaxParse extends DefaultHandler {
    StringBuilder builder = new StringBuilder();
    String text;
    public abstract Object getParseObject();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        text = builder.toString().trim();
        builder.delete(0, builder.length());
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        builder.append(ch, start, length);
    }
}
