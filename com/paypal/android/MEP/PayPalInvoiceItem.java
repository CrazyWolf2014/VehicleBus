package com.paypal.android.MEP;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayPalInvoiceItem implements Serializable {
    private static final long serialVersionUID = 5;
    BigDecimal f1419a;
    private String f1420b;
    private String f1421c;
    private BigDecimal f1422d;
    private int f1423e;

    public PayPalInvoiceItem() {
        this.f1420b = null;
        this.f1421c = null;
        this.f1419a = null;
        this.f1422d = null;
        this.f1423e = 0;
    }

    public PayPalInvoiceItem(String str, String str2, BigDecimal bigDecimal, int i) {
        this.f1420b = str;
        this.f1421c = str2;
        this.f1422d = bigDecimal;
        this.f1423e = i;
        this.f1419a = bigDecimal.multiply(new BigDecimal(i).setScale(2, 4));
    }

    public String getID() {
        return this.f1421c;
    }

    public String getName() {
        return this.f1420b;
    }

    public int getQuantity() {
        return this.f1423e;
    }

    public BigDecimal getTotalPrice() {
        return this.f1419a;
    }

    public BigDecimal getUnitPrice() {
        return this.f1422d;
    }

    public boolean isValid() {
        return (this.f1420b == null || this.f1420b.length() <= 0) ? (this.f1421c == null || this.f1421c.length() <= 0) ? (this.f1419a == null || this.f1419a.compareTo(BigDecimal.ZERO) <= 0) ? (this.f1422d != null && this.f1422d.compareTo(BigDecimal.ZERO) > 0) || this.f1423e > 0 : true : true : true;
    }

    public void setID(String str) {
        this.f1421c = str;
    }

    public void setName(String str) {
        this.f1420b = str;
    }

    public void setQuantity(int i) {
        this.f1423e = i;
    }

    public void setTotalPrice(BigDecimal bigDecimal) {
        this.f1419a = bigDecimal.setScale(2, 4);
    }

    public void setUnitPrice(BigDecimal bigDecimal) {
        this.f1422d = bigDecimal.setScale(2, 4);
    }
}
