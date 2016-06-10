package com.ifoer.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SoftPackageDto implements Serializable {
    private static final long serialVersionUID = 4782847875942253445L;
    protected int amount;
    protected int currencyId;
    protected List<PackageDetailDto> packageDetailList;
    protected int packageFlag;
    protected int packageIsBuyed;
    protected String softPackageDesc;
    protected int softPackageId;
    protected String softPackageName;
    protected double totalPrice;

    public SoftPackageDto() {
        this.packageDetailList = new ArrayList();
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int value) {
        this.amount = value;
    }

    public int getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(int value) {
        this.currencyId = value;
    }

    public List<PackageDetailDto> getPackageDetailList() {
        return this.packageDetailList;
    }

    public void setPackageDetailList(List<PackageDetailDto> packageDetailList) {
        this.packageDetailList = packageDetailList;
    }

    public int getPackageFlag() {
        return this.packageFlag;
    }

    public void setPackageFlag(int value) {
        this.packageFlag = value;
    }

    public int getPackageIsBuyed() {
        return this.packageIsBuyed;
    }

    public void setPackageIsBuyed(int value) {
        this.packageIsBuyed = value;
    }

    public String getSoftPackageDesc() {
        return this.softPackageDesc;
    }

    public void setSoftPackageDesc(String value) {
        this.softPackageDesc = value;
    }

    public int getSoftPackageId() {
        return this.softPackageId;
    }

    public void setSoftPackageId(int value) {
        this.softPackageId = value;
    }

    public String getSoftPackageName() {
        return this.softPackageName;
    }

    public void setSoftPackageName(String value) {
        this.softPackageName = value;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(double value) {
        this.totalPrice = value;
    }
}
