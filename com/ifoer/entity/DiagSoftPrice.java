package com.ifoer.entity;

public class DiagSoftPrice {
    protected int currencyId;
    protected double price;

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
}
