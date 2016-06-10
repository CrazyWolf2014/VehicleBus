package com.ifoer.dbentity;

public class ShoppingCar {
    private String buyType;
    private String cc;
    private String currencyId;
    private String date;
    private int id;
    private String price;
    private String serialNo;
    private String softId;
    private String softName;
    private String totalPrice;
    private String version;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCc() {
        return this.cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
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

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getBuyType() {
        return this.buyType;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    public String getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
