package com.cnlaunch.x431pro.module.mine.model;

import com.cnlaunch.x431pro.module.base.BaseModel;
import java.util.Date;

public class ReportFileInfo extends BaseModel {
    private static final long serialVersionUID = -1604584234577915297L;
    private String reportName;
    private String reportSerialnumber;
    private Date reportTime;

    public String getReportName() {
        return this.reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportSerialnumber() {
        return this.reportSerialnumber;
    }

    public void setReportSerialnumber(String reportSerialnumber) {
        this.reportSerialnumber = reportSerialnumber;
    }

    public Date getReportTime() {
        return this.reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String toString() {
        return "ReportFileInfo [reportName=" + this.reportName + ", reportSerialnumber=" + this.reportSerialnumber + ", reportTime=" + this.reportTime + "]";
    }
}
