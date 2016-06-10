package com.ifoer.entity;

import java.io.Serializable;

public class X431PadSoftDTO implements Serializable {
    private static final long serialVersionUID = 1;
    private int code;
    private String diagVehicleType;
    private int downloadSize;
    private String downloadStatus;
    private String fileSize;
    private String lanId;
    private String maxOldVersion;
    private int purchased;
    private String softApplicableArea;
    private String softId;
    private String softName;
    private String softPackageID;
    private String softUpdateTime;
    private int type;
    private String versionDetailId;
    private String versionNo;

    public int getPurchased() {
        return this.purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }

    public String getSoftName() {
        return this.softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getVersionNo() {
        return this.versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
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

    public String getSoftApplicableArea() {
        return this.softApplicableArea;
    }

    public void setSoftApplicableArea(String softApplicableArea) {
        this.softApplicableArea = softApplicableArea;
    }

    public String getSoftUpdateTime() {
        return this.softUpdateTime;
    }

    public void setSoftUpdateTime(String softUpdateTime) {
        this.softUpdateTime = softUpdateTime;
    }

    public String getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getSoftId() {
        return this.softId;
    }

    public void setSoftId(String softId) {
        this.softId = softId;
    }

    public String getLanId() {
        return this.lanId;
    }

    public void setLanId(String lanId) {
        this.lanId = lanId;
    }

    public String getVersionDetailId() {
        return this.versionDetailId;
    }

    public void setVersionDetailId(String versionDetailId) {
        this.versionDetailId = versionDetailId;
    }

    public String getDownloadStatus() {
        return this.downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public int getDownloadSize() {
        return this.downloadSize;
    }

    public void setDownloadSize(int downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getMaxOldVersion() {
        return this.maxOldVersion;
    }

    public void setMaxOldVersion(String maxOldVersion) {
        this.maxOldVersion = maxOldVersion;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
