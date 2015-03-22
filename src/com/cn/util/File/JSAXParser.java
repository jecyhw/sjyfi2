package com.cn.util.File;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * Created by SNNU on 2015/3/20.
 */
public class JSAXParser {
    static SAXParserFactory factory = SAXParserFactory.newInstance();
    public void parse(String parseFileUri, BaseFileParse fileParse ) throws ParserConfigurationException, SAXException, IOException {
        fileParse.setParseFileUri(parseFileUri);
        factory.newSAXParser().parse(new File(parseFileUri), new DefaultSaxParse(fileParse));
    }
}
