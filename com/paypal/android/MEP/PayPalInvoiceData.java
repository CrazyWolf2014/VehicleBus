package com.paypal.android.MEP;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class PayPalInvoiceData implements Serializable {
    private static final long serialVersionUID = 4;
    ArrayList<PayPalInvoiceItem> f1416a;
    private BigDecimal f1417b;
    private BigDecimal f1418c;

    public PayPalInvoiceData() {
        this.f1416a = new ArrayList();
        this.f1417b = null;
        this.f1418c = null;
    }

    public PayPalInvoiceData(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
        this.f1416a = new ArrayList();
        this.f1417b = bigDecimal;
        this.f1418c = bigDecimal2;
    }

    public void add(PayPalInvoiceItem payPalInvoiceItem) {
        this.f1416a.add(payPalInvoiceItem);
    }

    public ArrayList<PayPalInvoiceItem> getInvoiceItems() {
        return this.f1416a;
    }

    public BigDecimal getShipping() {
        return this.f1418c;
    }

    public BigDecimal getTax() {
        return this.f1417b;
    }

    public void setInvoiceItems(ArrayList<PayPalInvoiceItem> arrayList) {
        this.f1416a = arrayList;
    }

    public void setShipping(BigDecimal bigDecimal) {
        this.f1418c = bigDecimal.setScale(2, 4);
    }

    public void setTax(BigDecimal bigDecimal) {
        this.f1417b = bigDecimal.setScale(2, 4);
    }
}
