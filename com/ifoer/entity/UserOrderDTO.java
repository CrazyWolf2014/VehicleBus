package com.ifoer.entity;

public class UserOrderDTO {
    private String borderTime;
    private int currencyId;
    private int orderId;
    private String orderName;
    private String orderSN;
    private String payTime;
    private int payType;
    private String serialNo;
    private int status;
    private double totalPrice;

    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderSN() {
        return this.orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }

    public String getOrderName() {
        return this.orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPayType() {
        return this.payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getBorderTime() {
        return this.borderTime;
    }

    public void setBorderTime(String borderTime) {
        this.borderTime = borderTime;
    }

    public String getPayTime() {
        return this.payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }
}
