package com.ifoer.entity;

public class PayKeyAll {
    private String cc;
    private String orderSN;
    private String paykey;
    private String serialNo;
    private String status;

    public String getOrderSN() {
        return this.orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }

    public String getCc() {
        return this.cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPaykey() {
        return this.paykey;
    }

    public void setPaykey(String paykey) {
        this.paykey = paykey;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
