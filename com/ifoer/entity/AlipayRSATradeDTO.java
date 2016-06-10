package com.ifoer.entity;

public class AlipayRSATradeDTO {
    protected String sign;
    protected String signData;
    protected String signType;

    public String getSign() {
        return this.sign;
    }

    public void setSign(String value) {
        this.sign = value;
    }

    public String getSignData() {
        return this.signData;
    }

    public void setSignData(String value) {
        this.signData = value;
    }

    public String getSignType() {
        return this.signType;
    }

    public void setSignType(String value) {
        this.signType = value;
    }
}
