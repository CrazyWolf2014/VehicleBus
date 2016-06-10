package com.ifoer.entity;

import java.io.Serializable;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;

public class SptActiveTestStream implements Serializable {
    private static final long serialVersionUID = 1;
    private String dataStreamContent;
    private String dataStreamId;
    private String dataStreamStr;
    private String unit;

    public SptActiveTestStream() {
        this.dataStreamContent = XmlPullParser.NO_NAMESPACE;
        this.dataStreamStr = XmlPullParser.NO_NAMESPACE;
        this.unit = XmlPullParser.NO_NAMESPACE;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDataStreamContent() {
        return this.dataStreamContent;
    }

    public void setDataStreamContent(String dataStreamContent) {
        this.dataStreamContent = dataStreamContent;
    }

    public String getDataStreamId() {
        return this.dataStreamId;
    }

    public void setDataStreamId(String dataStreamId) {
        this.dataStreamId = dataStreamId;
    }

    public String getDataStreamStr() {
        return this.dataStreamStr;
    }

    public void setDataStreamStr(String dataStreamStr) {
        this.dataStreamStr = dataStreamStr;
    }

    public double getStreamStrDouble() {
        Pattern pat = Pattern.compile("^[0-9.]+$");
        if (this.dataStreamStr.equals(XmlPullParser.NO_NAMESPACE) || !pat.matcher(this.dataStreamStr).matches()) {
            return 0.0d;
        }
        return Double.parseDouble(this.dataStreamStr);
    }
}
