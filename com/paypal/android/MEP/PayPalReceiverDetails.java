package com.paypal.android.MEP;

import com.paypal.android.p022a.C0839h;
import java.io.Serializable;
import java.math.BigDecimal;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public class PayPalReceiverDetails implements Serializable {
    private static final long serialVersionUID = 3;
    private String f1450a;
    private BigDecimal f1451b;
    private PayPalInvoiceData f1452c;
    private int f1453d;
    private int f1454e;
    private String f1455f;
    private String f1456g;
    private String f1457h;
    private boolean f1458i;

    public PayPalReceiverDetails() {
        this.f1450a = null;
        this.f1451b = null;
        this.f1452c = null;
        this.f1453d = 3;
        this.f1454e = 22;
        this.f1455f = null;
        this.f1456g = null;
        this.f1457h = null;
        this.f1458i = false;
    }

    public String getCustomID() {
        return this.f1456g;
    }

    public String getDescription() {
        return this.f1455f;
    }

    public PayPalInvoiceData getInvoiceData() {
        return this.f1452c;
    }

    public boolean getIsPrimary() {
        return this.f1458i;
    }

    public String getMerchantName() {
        return this.f1457h;
    }

    public int getPaymentSubtype() {
        return this.f1454e;
    }

    public int getPaymentType() {
        return this.f1453d;
    }

    public String getRecipient() {
        return this.f1450a;
    }

    public BigDecimal getSubtotal() {
        return this.f1451b;
    }

    public BigDecimal getTotal() {
        BigDecimal add = BigDecimal.ZERO.add(this.f1451b);
        if (this.f1452c == null) {
            return add;
        }
        if (this.f1452c.getTax() != null) {
            add = add.add(this.f1452c.getTax());
        }
        return this.f1452c.getShipping() != null ? add.add(this.f1452c.getShipping()) : add;
    }

    public boolean isEmailRecipient() {
        return C0839h.m1574d(this.f1450a);
    }

    public boolean isPhoneRecipient() {
        return C0839h.m1575e(this.f1450a);
    }

    public void setCustomID(String str) {
        this.f1456g = str;
    }

    public void setDescription(String str) {
        this.f1455f = str;
    }

    public void setInvoiceData(PayPalInvoiceData payPalInvoiceData) {
        this.f1452c = payPalInvoiceData;
    }

    public void setIsPrimary(boolean z) {
        this.f1458i = z;
    }

    public void setMerchantName(String str) {
        this.f1457h = str;
    }

    public void setPaymentSubtype(int i) {
        this.f1454e = i;
    }

    public void setPaymentType(int i) {
        this.f1453d = i;
    }

    public void setRecipient(String str) {
        this.f1450a = str.replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE);
    }

    public void setSubtotal(BigDecimal bigDecimal) {
        this.f1451b = bigDecimal.setScale(2, 4);
    }
}
