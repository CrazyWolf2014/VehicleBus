package com.cnlaunch.x431pro.module.upgrade.model;

import java.io.Serializable;

public class X431PadDtoSoft implements Serializable {
    private static final long serialVersionUID = 6706044786739517801L;
    private int code;
    private String diagVehicleType;
    private long downloadSize;
    private String fileName;
    private long fileSize;
    private String freeUseEndTime;
    private boolean isChecked;
    private boolean isExpired;
    private boolean isMust;
    private String lanId;
    private String mRemarks;
    private String maxOldVersion;
    private int progress;
    private int purchased;
    private String serverCurrentTime;
    private String softApplicableArea;
    private String softId;
    private String softName;
    private String softPackageID;
    private String softUpdateTime;
    private int state;
    private int type;
    private String url;
    private String versionDetailId;
    private String versionNo;

    public X431PadDtoSoft() {
        this.type = 3;
        this.isMust = false;
        this.isExpired = false;
    }

    public long getDownloadSize() {
        return this.downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getSoftName() {
        return this.softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getSoftId() {
        return this.softId;
    }

    public void setSoftId(String softId) {
        this.softId = softId;
    }

    public String getVersionDetailId() {
        return this.versionDetailId;
    }

    public void setVersionDetailId(String versionDetailId) {
        this.versionDetailId = versionDetailId;
    }

    public String getVersionNo() {
        return this.versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getSoftUpdateTime() {
        return this.softUpdateTime;
    }

    public void setSoftUpdateTime(String softUpdateTime) {
        this.softUpdateTime = softUpdateTime;
    }

    public String getSoftApplicableArea() {
        return this.softApplicableArea;
    }

    public void setSoftApplicableArea(String softApplicableArea) {
        this.softApplicableArea = softApplicableArea;
    }

    public String getServerCurrentTime() {
        return this.serverCurrentTime;
    }

    public void setServerCurrentTime(String serverCurrentTime) {
        this.serverCurrentTime = serverCurrentTime;
    }

    public String getFreeUseEndTime() {
        return this.freeUseEndTime;
    }

    public void setFreeUseEndTime(String freeUseEndTime) {
        this.freeUseEndTime = freeUseEndTime;
    }

    public String getSoftPackageID() {
        return this.softPackageID;
    }

    public void setSoftPackageID(String softPackageID) {
        this.softPackageID = softPackageID;
    }

    public String getDiagVehicleType() {
        return this.diagVehicleType;
    }

    public void setDiagVehicleType(String diagVehicleType) {
        this.diagVehicleType = diagVehicleType;
    }

    public String getLanId() {
        return this.lanId;
    }

    public void setLanId(String lanId) {
        this.lanId = lanId;
    }

    public int getPurchased() {
        return this.purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }

    public String getMaxOldVersion() {
        return this.maxOldVersion;
    }

    public void setMaxOldVersion(String maxOldVersion) {
        this.maxOldVersion = maxOldVersion;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isMust() {
        return this.isMust;
    }

    public void setMust(boolean isMust) {
        this.isMust = isMust;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean getExpired() {
        return this.isExpired;
    }

    public void setExpired(boolean expired) {
        this.isExpired = expired;
    }

    public String getRemarks() {
        return this.mRemarks;
    }

    public void setRemarks(String remarks) {
        this.mRemarks = remarks;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String toString() {
        return "X431PadDtoSoft [softName=" + this.softName + ", softId=" + this.softId + ", versionDetailId=" + this.versionDetailId + ", versionNo=" + this.versionNo + ", softUpdateTime=" + this.softUpdateTime + ", softApplicableArea=" + this.softApplicableArea + ", serverCurrentTime=" + this.serverCurrentTime + ", freeUseEndTime=" + this.freeUseEndTime + ", softPackageID=" + this.softPackageID + ", diagVehicleType=" + this.diagVehicleType + ", lanId=" + this.lanId + ", purchased=" + this.purchased + ", maxOldVersion=" + this.maxOldVersion + ", type=" + this.type + ", isChecked=" + this.isChecked + "]";
    }
}
