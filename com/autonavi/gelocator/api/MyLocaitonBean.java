package com.autonavi.gelocator.api;

import org.xmlpull.v1.XmlPullParser;

public class MyLocaitonBean {
    private String f776a;
    private String f777b;
    private double f778c;
    private double f779d;
    private double f780e;
    private String f781f;
    private String f782g;

    public MyLocaitonBean() {
        this.f776a = XmlPullParser.NO_NAMESPACE;
        this.f777b = XmlPullParser.NO_NAMESPACE;
        this.f778c = 0.0d;
        this.f779d = 0.0d;
        this.f780e = 0.0d;
        this.f781f = XmlPullParser.NO_NAMESPACE;
        this.f782g = XmlPullParser.NO_NAMESPACE;
    }

    public double getCenx() {
        return this.f778c;
    }

    public double getCeny() {
        return this.f779d;
    }

    public String getCityCode() {
        return this.f781f;
    }

    public String getDes() {
        return this.f782g;
    }

    public double getRadius() {
        return this.f780e;
    }

    public String getResult() {
        return this.f776a;
    }

    public String getrDesc() {
        return this.f777b;
    }

    public void setCenx(double d) {
        this.f778c = d;
    }

    public void setCeny(double d) {
        this.f779d = d;
    }

    public void setCityCode(String str) {
        this.f781f = str;
    }

    public void setDes(String str) {
        this.f782g = str;
    }

    public void setRadius(double d) {
        this.f780e = d;
    }

    public void setResult(String str) {
        this.f776a = str;
    }

    public void setrDesc(String str) {
        this.f777b = str;
    }

    public final String toString() {
        return new StringBuffer().append("resutl=" + this.f776a).append(" rDes=" + this.f777b).append(" cenx=" + this.f778c).append(" ceny=" + this.f779d).append(" radius=" + this.f780e).append(" cityCode=" + this.f781f).append(" des=" + this.f782g).toString();
    }
}
