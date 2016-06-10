package com.ifoer.entity;

public class RCUDataDTO {
    private String dataId;
    private String dataName;
    private String dataUnitId;
    private String dataUnitName;
    private String dataValue;
    private String systemName;

    public String getDataId() {
        return this.dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataName() {
        return this.dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataValue() {
        return this.dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getDataUnitId() {
        return this.dataUnitId;
    }

    public void setDataUnitId(String dataUnitId) {
        this.dataUnitId = dataUnitId;
    }

    public String getDataUnitName() {
        return this.dataUnitName;
    }

    public void setDataUnitName(String dataUnitName) {
        this.dataUnitName = dataUnitName;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
