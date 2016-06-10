package com.cnlaunch.mycar.jni;

import org.xmlpull.v1.XmlPullParser;

public class LSX_AUTOINFO {
    public String chassis;
    public String displacement;
    public String enginemodel;
    public String madein;
    public String make;
    public String model;
    public String vin;
    public String year;

    public LSX_AUTOINFO() {
        this.vin = XmlPullParser.NO_NAMESPACE;
        this.make = XmlPullParser.NO_NAMESPACE;
        this.model = XmlPullParser.NO_NAMESPACE;
        this.year = XmlPullParser.NO_NAMESPACE;
        this.madein = XmlPullParser.NO_NAMESPACE;
        this.chassis = XmlPullParser.NO_NAMESPACE;
        this.enginemodel = XmlPullParser.NO_NAMESPACE;
        this.displacement = XmlPullParser.NO_NAMESPACE;
    }
}
