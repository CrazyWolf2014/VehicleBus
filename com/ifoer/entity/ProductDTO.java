package com.ifoer.entity;

public class ProductDTO {
    protected Integer pdtState;
    protected String productConfigName;
    protected String serialNo;
    protected String venderName;

    public Integer getPdtState() {
        return this.pdtState;
    }

    public void setPdtState(Integer value) {
        this.pdtState = value;
    }

    public String getProductConfigName() {
        return this.productConfigName;
    }

    public void setProductConfigName(String value) {
        this.productConfigName = value;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String value) {
        this.serialNo = value;
    }

    public String getVenderName() {
        return this.venderName;
    }

    public void setVenderName(String value) {
        this.venderName = value;
    }
}
