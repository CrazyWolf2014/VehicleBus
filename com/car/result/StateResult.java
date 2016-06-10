package com.car.result;

public class StateResult extends WSResult {
    protected String productInfo;
    protected Integer state;

    public String getProductInfo() {
        return this.productInfo;
    }

    public void setProductInfo(String value) {
        this.productInfo = value;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer value) {
        this.state = value;
    }
}
