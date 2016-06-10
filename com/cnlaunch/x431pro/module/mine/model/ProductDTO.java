package com.cnlaunch.x431pro.module.mine.model;

import java.io.Serializable;

public class ProductDTO implements Serializable {
    private static final long serialVersionUID = 1;
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

    public String toString() {
        return "ProductDTO [pdtState=" + this.pdtState + ", productConfigName=" + this.productConfigName + ", serialNo=" + this.serialNo + ", venderName=" + this.venderName + "]";
    }
}
