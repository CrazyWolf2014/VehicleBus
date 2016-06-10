package com.ifoer.entity;

import org.xmlpull.v1.XmlPullParser;

public class AnalysisData {
    private byte[] pReceiveBuffer;
    private byte[] pRequestBuffer;
    private String requestWordStr;
    private Boolean state;

    public AnalysisData() {
        this.requestWordStr = XmlPullParser.NO_NAMESPACE;
        this.pRequestBuffer = null;
        this.pReceiveBuffer = null;
        this.state = Boolean.valueOf(false);
    }

    public String getRequestWordStr() {
        return this.requestWordStr;
    }

    public void setRequestWordStr(String requestWordStr) {
        this.requestWordStr = requestWordStr;
    }

    public byte[] getpRequestBuffer() {
        return this.pRequestBuffer;
    }

    public void setpRequestBuffer(byte[] pRequestBuffer) {
        this.pRequestBuffer = pRequestBuffer;
    }

    public byte[] getpReceiveBuffer() {
        return this.pReceiveBuffer;
    }

    public void setpReceiveBuffer(byte[] pReceiveBuffer) {
        this.pReceiveBuffer = pReceiveBuffer;
    }

    public Boolean getState() {
        return this.state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
