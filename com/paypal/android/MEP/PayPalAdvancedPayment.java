package com.paypal.android.MEP;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

public class PayPalAdvancedPayment implements Serializable {
    private static final long serialVersionUID = 2;
    private String f1411a;
    private ArrayList<PayPalReceiverDetails> f1412b;
    private String f1413c;
    private String f1414d;
    private String f1415e;

    public PayPalAdvancedPayment() {
        this.f1411a = null;
        this.f1412b = new ArrayList();
        this.f1413c = null;
        this.f1414d = null;
        this.f1415e = null;
    }

    public String getCurrencyType() {
        return this.f1411a;
    }

    public String getIpnUrl() {
        return this.f1413c;
    }

    public String getMemo() {
        return this.f1414d;
    }

    public String getMerchantName() {
        return this.f1415e;
    }

    public PayPalReceiverDetails getPrimaryReceiver() {
        for (int i = 0; i < this.f1412b.size(); i++) {
            if (((PayPalReceiverDetails) this.f1412b.get(i)).getIsPrimary()) {
                return (PayPalReceiverDetails) this.f1412b.get(i);
            }
        }
        return null;
    }

    public ArrayList<PayPalReceiverDetails> getReceivers() {
        return this.f1412b;
    }

    public BigDecimal getTotal() {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (int i = 0; i < this.f1412b.size(); i++) {
            bigDecimal = bigDecimal.add(((PayPalReceiverDetails) this.f1412b.get(i)).getTotal());
        }
        return bigDecimal;
    }

    public BigDecimal getTotalShipping() {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        int i = 0;
        while (i < this.f1412b.size()) {
            if (!(((PayPalReceiverDetails) this.f1412b.get(i)).getInvoiceData() == null || ((PayPalReceiverDetails) this.f1412b.get(i)).getInvoiceData().getShipping() == null)) {
                bigDecimal = bigDecimal.add(((PayPalReceiverDetails) this.f1412b.get(i)).getInvoiceData().getShipping());
            }
            i++;
        }
        return bigDecimal;
    }

    public BigDecimal getTotalSubtotal() {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (int i = 0; i < this.f1412b.size(); i++) {
            if (((PayPalReceiverDetails) this.f1412b.get(i)).getSubtotal() != null) {
                bigDecimal = bigDecimal.add(((PayPalReceiverDetails) this.f1412b.get(i)).getSubtotal());
            }
        }
        return bigDecimal;
    }

    public BigDecimal getTotalTax() {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        int i = 0;
        while (i < this.f1412b.size()) {
            if (!(((PayPalReceiverDetails) this.f1412b.get(i)).getInvoiceData() == null || ((PayPalReceiverDetails) this.f1412b.get(i)).getInvoiceData().getTax() == null)) {
                bigDecimal = bigDecimal.add(((PayPalReceiverDetails) this.f1412b.get(i)).getInvoiceData().getTax());
            }
            i++;
        }
        return bigDecimal;
    }

    public boolean hasPrimaryReceiever() {
        for (int i = 0; i < this.f1412b.size(); i++) {
            if (((PayPalReceiverDetails) this.f1412b.get(i)).getIsPrimary()) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid() {
        if (this.f1412b.size() <= 0) {
            return false;
        }
        int i = 0;
        while (i < this.f1412b.size()) {
            if (((PayPalReceiverDetails) this.f1412b.get(i)).getRecipient() == null || ((PayPalReceiverDetails) this.f1412b.get(i)).getRecipient().length() <= 0 || ((PayPalReceiverDetails) this.f1412b.get(i)).getSubtotal().compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }
            i++;
        }
        boolean z = (!this.f1411a.equals("AUD") || getTotal().compareTo(new BigDecimal("11000")) < 0) ? (!this.f1411a.equals("CAD") || getTotal().compareTo(new BigDecimal("11000")) < 0) ? (!this.f1411a.equals("CHF") || getTotal().compareTo(new BigDecimal("11000")) < 0) ? (!this.f1411a.equals("CZK") || getTotal().compareTo(new BigDecimal("200000")) < 0) ? (!this.f1411a.equals("DKK") || getTotal().compareTo(new BigDecimal("55000")) < 0) ? (!this.f1411a.equals("EUR") || getTotal().compareTo(new BigDecimal("7500")) < 0) ? (!this.f1411a.equals("GBP") || getTotal().compareTo(new BigDecimal("7500")) < 0) ? (!this.f1411a.equals("HKD") || getTotal().compareTo(new BigDecimal("80000")) < 0) ? (!this.f1411a.equals("HUF") || getTotal().compareTo(new BigDecimal("2000000")) < 0) ? (!this.f1411a.equals("ILS") || getTotal().compareTo(new BigDecimal("40000")) < 0) ? (!this.f1411a.equals("JPY") || getTotal().compareTo(new BigDecimal("1000000")) < 0) ? (!this.f1411a.equals("MXN") || getTotal().compareTo(new BigDecimal("130000")) < 0) ? (!this.f1411a.equals("MYR") || getTotal().compareTo(new BigDecimal("35000")) < 0) ? (!this.f1411a.equals("NOK") || getTotal().compareTo(new BigDecimal("60000")) < 0) ? (!this.f1411a.equals("NZD") || getTotal().compareTo(new BigDecimal("15000")) < 0) ? (!this.f1411a.equals("PHP") || getTotal().compareTo(new BigDecimal("500000")) < 0) ? (!this.f1411a.equals("PLN") || getTotal().compareTo(new BigDecimal("30000")) < 0) ? (!this.f1411a.equals("SEK") || getTotal().compareTo(new BigDecimal("75000")) < 0) ? (!this.f1411a.equals("SGD") || getTotal().compareTo(new BigDecimal("15000")) < 0) ? (!this.f1411a.equals("THB") || getTotal().compareTo(new BigDecimal("350000")) < 0) ? (!this.f1411a.equals("TWD") || getTotal().compareTo(new BigDecimal("350000")) < 0) ? !this.f1411a.equals("USD") || getTotal().compareTo(new BigDecimal("10000")) < 0 : false : false : false : false : false : false : false : false : false : false : false : false : false : false : false : false : false : false : false : false : false;
        return z;
    }

    public void setCurrencyType(String str) {
        this.f1411a = str.toUpperCase();
    }

    public void setCurrencyType(Currency currency) {
        this.f1411a = currency.getCurrencyCode();
    }

    public void setIpnUrl(String str) {
        this.f1413c = str;
    }

    public void setMemo(String str) {
        this.f1414d = str;
    }

    public void setMerchantName(String str) {
        this.f1415e = str;
    }

    public void setReceivers(ArrayList<PayPalReceiverDetails> arrayList) {
        this.f1412b = arrayList;
    }
}
