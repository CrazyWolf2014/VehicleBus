package com.ifoer.entity;

import java.io.Serializable;

public class Report implements Serializable {
    private static final long serialVersionUID = 1;
    private String creationTime;
    private String id;
    private String reportName;
    private String reportPath;
    private String serialNo;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportName() {
        return this.reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getReportPath() {
        return this.reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }
}
