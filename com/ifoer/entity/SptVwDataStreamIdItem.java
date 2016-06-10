package com.ifoer.entity;

import com.ifoer.expedition.BluetoothChat.DataStreamUtils;
import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class SptVwDataStreamIdItem implements Serializable {
    private static final long serialVersionUID = 1;
    private String streamStr;
    private String streamTextId;
    private String streamTextIdContent;
    private String streamUnitId;
    private String streamUnitIdContent;

    public SptVwDataStreamIdItem() {
        this.streamTextIdContent = XmlPullParser.NO_NAMESPACE;
        this.streamStr = XmlPullParser.NO_NAMESPACE;
        this.streamUnitId = XmlPullParser.NO_NAMESPACE;
        this.streamUnitIdContent = XmlPullParser.NO_NAMESPACE;
    }

    public String getStreamUnitId() {
        return this.streamUnitId;
    }

    public void setStreamUnitId(String streamUnitId) {
        this.streamUnitId = streamUnitId;
    }

    public String getStreamUnitIdContent() {
        return this.streamUnitIdContent;
    }

    public void setStreamUnitIdContent(String streamUnitIdContent) {
        this.streamUnitIdContent = streamUnitIdContent;
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
}
