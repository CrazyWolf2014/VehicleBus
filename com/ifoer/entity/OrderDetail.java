package com.ifoer.entity;

public class OrderDetail {
    protected int currencyId;
    private String icon;
    protected int orderDetailId;
    protected double price;
    protected int softId;
    protected String softName;
    private String softPackageID;
    protected String version;

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(int value) {
        this.currencyId = value;
    }

    public int getOrderDetailId() {
        return this.orderDetailId;
    }

    public void setOrderDetailId(int value) {
        this.orderDetailId = value;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double value) {
        this.price = value;
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

    public String getSoftPackageID() {
        return this.softPackageID;
    }

    public void setSoftPackageID(String softPackageID) {
        this.softPackageID = softPackageID;
    }
}
