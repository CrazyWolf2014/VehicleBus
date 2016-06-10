package com.ifoer.entity;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class OperatingRecordInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private int recordId;
    private String serialNumber;
    private String testCar;
    private String testSite;
    private String testTime;

    public OperatingRecordInfo() {
        this.serialNumber = XmlPullParser.NO_NAMESPACE;
        this.testTime = XmlPullParser.NO_NAMESPACE;
        this.testSite = XmlPullParser.NO_NAMESPACE;
        this.testCar = XmlPullParser.NO_NAMESPACE;
    }

    public int getRecordId() {
        return this.recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTestTime() {
        return this.testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getTestSite() {
        return this.testSite;
    }

    public void setTestSite(String testSite) {
        this.testSite = testSite;
    }

    public String getTestCar() {
        return this.testCar;
    }

    public void setTestCar(String testCar) {
        this.testCar = testCar;
    }
}
