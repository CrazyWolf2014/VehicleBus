package com.ifoer.entity;

import com.ifoer.expedition.BluetoothChat.DataStreamUtils;
import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class SptExDataStreamIdItem implements Serializable, Comparable {
    private static final long serialVersionUID = 1;
    private String streamState;
    private String streamStr;
    private String streamTextId;
    private String streamTextIdContent;
    private String streamTopTitle;

    public SptExDataStreamIdItem() {
        this.streamTopTitle = XmlPullParser.NO_NAMESPACE;
        this.streamTextIdContent = XmlPullParser.NO_NAMESPACE;
        this.streamStr = XmlPullParser.NO_NAMESPACE;
        this.streamState = XmlPullParser.NO_NAMESPACE;
    }

    public String getStreamTopTitle() {
        return this.streamTopTitle;
    }

    public void setStreamTopTitle(String streamTopTitle) {
        this.streamTopTitle = streamTopTitle;
    }

    public String getStreamState() {
        return this.streamState;
    }

    public void setStreamState(String streamState) {
        this.streamState = streamState;
    }

    public String getStreamTextIdContent() {
        return this.streamTextIdContent;
    }

    public void setStreamTextIdContent(String streamTextIdContent) {
        this.streamTextIdContent = streamTextIdContent;
    }

    public String getStreamTextId() {
        return this.streamTextId;
    }

    public void setStreamTextId(String streamTextId) {
        this.streamTextId = streamTextId;
    }

    public String getStreamStr() {
        return this.streamStr;
    }

    public double getStreamStrDouble() {
        if (this.streamStr == null || this.streamStr.equals(XmlPullParser.NO_NAMESPACE) || !DataStreamUtils.isNumeric(this.streamStr)) {
            return 0.0d;
        }
        return Double.parseDouble(this.streamStr);
    }

    public void setStreamStr(String streamStr) {
        this.streamStr = streamStr;
    }

    public int compareTo(Object arg0) {
        return getStreamTextIdContent().compareTo(((SptExDataStreamIdItem) arg0).getStreamTextIdContent());
    }
}
