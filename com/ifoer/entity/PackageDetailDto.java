package com.ifoer.entity;

import java.io.Serializable;

public class PackageDetailDto implements Serializable {
    private static final long serialVersionUID = -4298855785036052590L;
    protected int buyed;
    private boolean clickFlag;
    protected int currencyId;
    private String icon;
    protected double price;
    protected int softId;
    protected String softName;
    private String softPackageID;
    protected String version;

    public PackageDetailDto() {
        this.clickFlag = false;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isClickFlag() {
        return this.clickFlag;
    }

    public void setClickFlag(boolean clickFlag) {
        this.clickFlag = clickFlag;
    }

    public int getBuyed() {
        return this.buyed;
    }

    public void setBuyed(int value) {
        this.buyed = value;
    }

    public int getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(int value) {
        this.currencyId = value;
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
