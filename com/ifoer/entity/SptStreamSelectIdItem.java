package com.ifoer.entity;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class SptStreamSelectIdItem implements Serializable, Comparable {
    private static final long serialVersionUID = 1;
    private int position;
    private int site;
    private String streamSelectId;
    private String streamSelectStr;

    public SptStreamSelectIdItem() {
        this.streamSelectStr = XmlPullParser.NO_NAMESPACE;
        this.site = 0;
    }

    public String getStreamSelectId() {
        return this.streamSelectId;
    }

    public void setStreamSelectId(String streamSelectId) {
        this.streamSelectId = streamSelectId;
    }

    public String getStreamSelectStr() {
        return this.streamSelectStr;
    }

    public void setStreamSelectStr(String streamSelectStr) {
        this.streamSelectStr = streamSelectStr;
    }

    public int getSite() {
        return this.site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public int compareTo(Object arg0) {
        return getStreamSelectStr().compareTo(((SptStreamSelectIdItem) arg0).getStreamSelectStr());
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
