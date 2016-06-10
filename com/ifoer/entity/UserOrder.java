package com.ifoer.entity;

import java.io.Serializable;

public class UserOrder implements Serializable {
    private static final long serialVersionUID = 1818259198829515254L;
    protected int currencyId;
    protected int orderId;
    protected String orderName;
    protected String orderSN;
    protected String orderTime;
    protected String payTime;
    protected int payType;
    private boolean selected;
    protected String serialNo;
    protected int status;
    protected double totalPrice;

    public UserOrder() {
        this.selected = false;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(int value) {
        this.currencyId = value;
    }

    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int value) {
        this.orderId = value;
    }

    public String getOrderName() {
        return this.orderName;
    }

    public void setOrderName(String value) {
        this.orderName = value;
    }

    public String getOrderSN() {
        return this.orderSN;
    }

    public void setOrderSN(String value) {
        this.orderSN = value;
    }

    public String getOrderTime() {
        return this.orderTime;
    }

    public void setOrderTime(String value) {
        this.orderTime = value;
    }

    public String getPayTime() {
        return this.payTime;
    }

    public void setPayTime(String value) {
        this.payTime = value;
    }

    public int getPayType() {
        return this.payType;
    }

    public void setPayType(int value) {
        this.payType = value;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String value) {
        this.serialNo = value;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int value) {
        this.status = value;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(double value) {
        this.totalPrice = value;
    }
}
