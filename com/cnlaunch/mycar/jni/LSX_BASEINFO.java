package com.cnlaunch.mycar.jni;

import org.xmlpull.v1.XmlPullParser;

public class LSX_BASEINFO {
    public int codepage;
    public String creationtime;
    public String diagversion;
    public String langcode;
    public String langcode_en;
    public String langname;
    public short productid;
    public String serialno;

    public LSX_BASEINFO() {
        this.serialno = XmlPullParser.NO_NAMESPACE;
        this.productid = (short) 0;
        this.codepage = 0;
        this.langname = XmlPullParser.NO_NAMESPACE;
        this.langcode = XmlPullParser.NO_NAMESPACE;
        this.langcode_en = XmlPullParser.NO_NAMESPACE;
        this.diagversion = XmlPullParser.NO_NAMESPACE;
        this.creationtime = XmlPullParser.NO_NAMESPACE;
    }
}
