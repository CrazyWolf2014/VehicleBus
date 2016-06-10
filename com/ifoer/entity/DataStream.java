package com.ifoer.entity;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class DataStream implements Serializable {
    private int count;
    private String name;
    private String streamState;
    private String streamStr;

    public DataStream() {
        this.streamStr = XmlPullParser.NO_NAMESPACE;
        this.streamState = XmlPullParser.NO_NAMESPACE;
    }

    public String getStreamStr() {
        return this.streamStr;
    }

    public void setStreamStr(String streamStr) {
        this.streamStr = streamStr;
    }

    public String getStreamState() {
        return this.streamState;
    }

    public void setStreamState(String streamState) {
        this.streamState = streamState;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
