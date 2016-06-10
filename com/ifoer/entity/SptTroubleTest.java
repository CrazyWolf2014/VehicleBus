package com.ifoer.entity;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class SptTroubleTest implements Serializable {
    private static final long serialVersionUID = 1;
    private String troubleCodeContent;
    private String troubleDescribeContent;
    private String troubleGAGname;
    private String troubleHelp;
    private String troubleId;
    private String troubleSpareDescribeContent;
    private String troubleStateContent;
    private String troubleStateId;

    public SptTroubleTest() {
        this.troubleCodeContent = XmlPullParser.NO_NAMESPACE;
        this.troubleDescribeContent = XmlPullParser.NO_NAMESPACE;
        this.troubleStateId = XmlPullParser.NO_NAMESPACE;
        this.troubleStateContent = XmlPullParser.NO_NAMESPACE;
        this.troubleSpareDescribeContent = XmlPullParser.NO_NAMESPACE;
        this.troubleGAGname = XmlPullParser.NO_NAMESPACE;
    }

    public String getTroubleHelp() {
        return this.troubleHelp;
    }

    public void setTroubleHelp(String troubleHelp) {
        this.troubleHelp = troubleHelp;
    }

    public String getTroubleId() {
        return this.troubleId;
    }

    public void setTroubleId(String troubleId) {
        this.troubleId = troubleId;
    }

    public String getTroubleCodeContent() {
        return this.troubleCodeContent;
    }

    public void setTroubleCodeContent(String troubleCodeContent) {
        this.troubleCodeContent = troubleCodeContent;
    }

    public String getTroubleDescribeContent() {
        return this.troubleDescribeContent;
    }

    public void setTroubleDescribeContent(String troubleDescribeContent) {
        this.troubleDescribeContent = troubleDescribeContent;
    }

    public String getTroubleStateId() {
        return this.troubleStateId;
    }

    public void setTroubleStateId(String troubleStateId) {
        this.troubleStateId = troubleStateId;
    }

    public String getTroubleStateContent() {
        return this.troubleStateContent;
    }

    public void setTroubleStateContent(String troubleStateContent) {
        this.troubleStateContent = troubleStateContent;
    }

    public String getTroubleSpareDescribeContent() {
        return this.troubleSpareDescribeContent;
    }

    public void setTroubleSpareDescribeContent(String troubleSpareDescribeContent) {
        this.troubleSpareDescribeContent = troubleSpareDescribeContent;
    }

    public void setTroubleGAGname(String troubleGAGname) {
        this.troubleGAGname = troubleGAGname;
    }

    public String getTroubleGAGname() {
        return this.troubleGAGname;
    }
}
