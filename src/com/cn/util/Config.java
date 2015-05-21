package com.cn.util;

import com.cn.util.File.BaseFileParse;
import com.cn.util.File.JSAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by SNNU on 2014/11/5.
 */
public final class Config extends BaseFileParse{
    static String uploadDir = "/var/gpstracks/jecyhw/";
    static String uploadTempDir = uploadDir;
    static String unZipFileDir = uploadDir;
    static String zipFileDir = uploadDir;
    static String thumbnailDir = uploadDir;

    static {
        try {
            new JSAXParser().parse(new Config());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUploadDir() {
        return uploadDir;
    }

    public static String getZipFileDir() {
        return zipFileDir;
    }

    public static String getUnZipFileDir() {
        return unZipFileDir;
    }

    public static String getUploadTempDir() {
        return uploadTempDir;
    }

    public static String getThumbnailDir() {
        return thumbnailDir;
    }

    protected Config() {
        parseFileUri = this.getClass().getResource("/").getPath() + "configfile/upload.xml";
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        if ("config".equals(qName)) {
            String temp = attrs.getValue("uploadDir");
            if (temp != null) {
                uploadDir = temp;
                temp = attrs.getValue("uploadTempDir");
                uploadTempDir = temp == null ? uploadDir : temp;

                temp = attrs.getValue("zipFileDir");
                zipFileDir = temp == null ? uploadDir : temp;

                temp = attrs.getValue("unZipFileDir");
                unZipFileDir = temp == null ? uploadDir : temp;

                temp = attrs.getValue("thumbnailDir");
                thumbnailDir = temp == null ? uploadDir : temp;
            }
        }
    }

    public class KMZFileInfo{
        static final public String routeRecordFileName = "RouteRecord.kml";
        static final public String trackDetailFileName = "TrackDetail.xml";
        static final public String videoDirectoryName = "video/";
        static final public String photoDirectoryName = "photo/";
        static final public String audioDirectoryName = "audio/";
    }
}
