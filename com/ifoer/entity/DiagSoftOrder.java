package com.ifoer.entity;

import java.util.ArrayList;
import java.util.List;

public class DiagSoftOrder {
    protected int buyType;
    protected String cc;
    protected int currencyId;
    private int packageId;
    protected String serialNo;
    protected List<DiagSoftOrderInfo> softOrderList;
    protected String totalPrice;

    public DiagSoftOrder() {
        this.softOrderList = new ArrayList();
    }

    public int getPackageId() {
        return this.packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public List<DiagSoftOrderInfo> getSoftOrderList() {
        return this.softOrderList;
    }

    public void setSoftOrderList(List<DiagSoftOrderInfo> softOrderList) {
        this.softOrderList = softOrderList;
    }

    public int getBuyType() {
        return this.buyType;
    }

    public void setBuyType(int value) {
        this.buyType = value;
    }

    public String getCc() {
        return this.cc;
    }

    public void setCc(String value) {
        this.cc = value;
    }

    public int getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(int value) {
        this.currencyId = value;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String value) {
        this.serialNo = value;
    }

    public String getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
