package com.cn.util.File;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by SNNU on 2015/3/19.
 */
public class RouteRecordSaxParse extends DefaultHandler {
    TransformerHandler transformerHandle = null;
    public RouteRecordSaxParse() {
//创建保存xml的结果流对象
        try {
            //获取sax生产工厂对象实例
            SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            //获取sax生产处理者对象实例
            transformerHandle = saxTransformerFactory.newTransformerHandler();
            transformerHandle.setResult(new StreamResult(new FileOutputStream(new File("c:\\user.xml"))));
            //获取sax生产器

            Transformer transformer = transformerHandle.getTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void startDocument() throws SAXException {
        transformerHandle.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        transformerHandle.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        transformerHandle.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        transformerHandle.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        transformerHandle.characters(ch, start, length);
    }

    static public void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        RouteRecordSaxParse saxParse = new RouteRecordSaxParse();
        parser.parse("C:\\Users\\SNNU\\Desktop\\RouteRecord.kml", saxParse);
    }

}
