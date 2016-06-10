package com.paypal.android.MEP;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Iterator;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public class PayPalPayment implements Serializable {
    private static final long serialVersionUID = 1;
    private String f1424a;
    private String f1425b;
    private BigDecimal f1426c;
    private PayPalInvoiceData f1427d;
    private int f1428e;
    private int f1429f;
    private String f1430g;
    private String f1431h;
    private String f1432i;
    private String f1433j;

    public PayPalPayment() {
        this.f1424a = null;
        this.f1425b = null;
        this.f1426c = null;
        this.f1427d = null;
        this.f1428e = 3;
        this.f1429f = 22;
        this.f1430g = null;
        this.f1431h = null;
        this.f1432i = null;
        this.f1433j = null;
    }

    public PayPalPayment(String str, Currency currency, int i, PayPalInvoiceData payPalInvoiceData, String str2, String str3, String str4, String str5) {
        this.f1425b = str.replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE);
        this.f1424a = currency.getCurrencyCode();
        this.f1427d = payPalInvoiceData;
        this.f1428e = i;
        this.f1429f = 22;
        this.f1430g = str3;
        this.f1431h = str2;
        this.f1432i = str4;
        this.f1433j = str5;
        this.f1426c = new BigDecimal(0);
        Iterator it = payPalInvoiceData.f1416a.iterator();
        while (it.hasNext()) {
            this.f1426c = this.f1426c.add(((PayPalInvoiceItem) it.next()).f1419a);
        }
        this.f1426c = this.f1426c.setScale(2, 4);
    }

    public String getCurrencyType() {
        return this.f1424a;
    }

    public String getCustomID() {
        return this.f1430g;
    }

    @Deprecated
    public String getDescription() {
        return this.f1433j;
    }

    public PayPalInvoiceData getInvoiceData() {
        return this.f1427d;
    }

    public String getIpnUrl() {
        return this.f1432i;
    }

    public String getMemo() {
        return this.f1433j;
    }

    public String getMerchantName() {
        return this.f1431h;
    }

    public int getPaymentSubtype() {
        return this.f1429f;
    }

    public int getPaymentType() {
        return this.f1428e;
    }

    public String getRecipient() {
        return this.f1425b;
    }

    public BigDecimal getSubtotal() {
        return this.f1426c;
    }

    public void setCurrencyType(String str) {
        this.f1424a = str.toUpperCase();
    }

    public void setCurrencyType(Currency currency) {
        this.f1424a = currency.getCurrencyCode();
    }

    public void setCustomID(String str) {
        this.f1430g = str;
    }

    @Deprecated
    public void setDescription(String str) {
        this.f1433j = str;
    }

    public void setInvoiceData(PayPalInvoiceData payPalInvoiceData) {
        this.f1427d = payPalInvoiceData;
    }

    public void setIpnUrl(String str) {
        this.f1432i = str;
    }

    public void setMemo(String str) {
        this.f1433j = str;
    }

    public void setMerchantName(String str) {
        this.f1431h = str;
    }

    public void setPaymentSubtype(int i) {
        this.f1429f = i;
    }

    public void setPaymentType(int i) {
        this.f1428e = i;
    }

    public void setRecipient(String str) {
        this.f1425b = str.replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE);
    }

    public void setSubtotal(BigDecimal bigDecimal) {
        this.f1426c = bigDecimal.setScale(2, 4);
    }
}
