package com.paypal.android.MEP;

import java.math.BigDecimal;

public class MEPAmounts {
    private String f1367a;
    private BigDecimal f1368b;
    private BigDecimal f1369c;
    private BigDecimal f1370d;

    public String getCurrency() {
        return this.f1367a;
    }

    public BigDecimal getPaymentAmount() {
        return this.f1368b;
    }

    public BigDecimal getShipping() {
        return this.f1370d;
    }

    public BigDecimal getTax() {
        return this.f1369c;
    }

    public void setCurrency(String str) {
        this.f1367a = str;
    }

    public void setPaymentAmount(String str) {
        try {
            this.f1368b = new BigDecimal(str);
        } catch (NumberFormatException e) {
            this.f1368b = new BigDecimal("0.0");
        }
    }

    public void setPaymentAmount(BigDecimal bigDecimal) {
        this.f1368b = bigDecimal;
    }

    public void setShipping(String str) {
        try {
            this.f1370d = new BigDecimal(str);
        } catch (NumberFormatException e) {
            this.f1370d = new BigDecimal("0.0");
        }
    }

    public void setShipping(BigDecimal bigDecimal) {
        this.f1370d = bigDecimal;
    }

    public void setTax(String str) {
        try {
            this.f1369c = new BigDecimal(str);
        } catch (NumberFormatException e) {
            this.f1369c = new BigDecimal("0.0");
        }
    }

    public void setTax(BigDecimal bigDecimal) {
        this.f1369c = bigDecimal;
    }
}
