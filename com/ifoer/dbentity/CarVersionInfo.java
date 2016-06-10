package com.ifoer.dbentity;

import java.io.Serializable;

public class CarVersionInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private String carId;
    private String lanName;
    private String serialNo;
    private String softId;
    private String versionDir;
    private String versionNo;

    public String getCarId() {
        return this.carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getVersionNo() {
        return this.versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getVersionDir() {
        return this.versionDir;
    }

    public void setVersionDir(String versionDir) {
        this.versionDir = versionDir;
    }

    public String getLanName() {
        return this.lanName;
    }

    public void setLanName(String lanName) {
        this.lanName = lanName;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSoftId() {
        return this.softId;
    }

    public void setSoftId(String softId) {
        this.softId = softId;
    }
}
