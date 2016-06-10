package com.ifoer.entity;

import java.io.Serializable;

public class CustomerDTO implements Serializable {
    private static final long serialVersionUID = 1;
    private String customerMobile;
    private String customerName;

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return this.customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }
}
