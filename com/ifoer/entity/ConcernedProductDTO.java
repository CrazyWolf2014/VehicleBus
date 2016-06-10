package com.ifoer.entity;

import java.io.Serializable;

public class ConcernedProductDTO implements Serializable {
    private static final long serialVersionUID = 1;
    private String customerName;
    private String serialNo;
    private String unResolvedMessageCount;

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getUnResolvedMessageCount() {
        return this.unResolvedMessageCount;
    }

    public void setUnResolvedMessageCount(String unResolvedMessageCount) {
        this.unResolvedMessageCount = unResolvedMessageCount;
    }
}
