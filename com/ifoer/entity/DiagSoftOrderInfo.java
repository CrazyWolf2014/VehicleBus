package com.ifoer.entity;

public class DiagSoftOrderInfo {
    protected int currencyId;
    protected String price;
    protected String serialNo;
    protected int softId;
    protected String softName;
    protected String version;

    public int getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(int value) {
        this.currencyId = value;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String value) {
        this.serialNo = value;
    }

    public int getSoftId() {
        return this.softId;
    }

    public void setSoftId(int value) {
        this.softId = value;
    }

    public String getSoftName() {
        return this.softName;
    }

    public void setSoftName(String value) {
        this.softName = value;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String value) {
        this.version = value;
    }
}
