package com.cn.util;

/**
 * Created by SNNU on 2014/11/5.
 */
public final class Config {
    //static final public String fileUploadDir = "http://159.226.15.215/var/gpstracks/";
    static final public String fileUploadDir = "/var/gpstracks/";
    static final public String fileUploadTempDir = fileUploadDir;
    static final public String unZipFileDir = fileUploadDir;
    static final public String zipFileDir = fileUploadDir;

    public class KMZFileInfo{
        static final public String routeRecordFileName = "RouteRecord.kml";
        static final public String trackDetailFileName = "TrackDetail.xml";
        static final public String videoDirectoryName = "video/";
        static final public String photoDirectoryName = "photo/";
        static final public String audioDirectoryName = "audio/";
    }
}
