package com.cnlaunch.mycar.jni;

import org.xmlpull.v1.XmlPullParser;

public class LSX_USERINFO {
    public String license;
    public String name;
    public String phone;

    public LSX_USERINFO() {
        this.name = XmlPullParser.NO_NAMESPACE;
        this.phone = XmlPullParser.NO_NAMESPACE;
        this.license = XmlPullParser.NO_NAMESPACE;
    }
}
