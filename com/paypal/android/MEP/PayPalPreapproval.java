package com.paypal.android.MEP;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayPalPreapproval implements Serializable {
    public static final int DAY_FRIDAY = 5;
    public static final int DAY_MONDAY = 1;
    public static final int DAY_NONE = 7;
    public static final int DAY_SATURDAY = 6;
    public static final int DAY_SUNDAY = 0;
    public static final int DAY_THURSDAY = 4;
    public static final int DAY_TUESDAY = 2;
    public static final int DAY_WEDNESDAY = 3;
    public static final int PERIOD_ANNUALLY = 5;
    public static final int PERIOD_BIWEEKLY = 2;
    public static final int PERIOD_DAILY = 0;
    public static final int PERIOD_MONTHLY = 4;
    public static final int PERIOD_NONE = 6;
    public static final int PERIOD_SEMIMONTHLY = 3;
    public static final int PERIOD_WEEKLY = 1;
    public static final int TYPE_AGREE = 0;
    public static final int TYPE_AGREE_AND_PAY = 1;
    private static final long serialVersionUID = 7;
    private String f1434a;
    private String f1435b;
    private int f1436c;
    private BigDecimal f1437d;
    private BigDecimal f1438e;
    private String f1439f;
    private String f1440g;
    private boolean f1441h;
    private boolean f1442i;
    private int f1443j;
    private int f1444k;
    private int f1445l;
    private int f1446m;
    private String f1447n;
    private String f1448o;
    private int f1449p;

    public PayPalPreapproval() {
        this.f1434a = null;
        this.f1435b = null;
        this.f1436c = TYPE_AGREE;
        this.f1437d = null;
        this.f1438e = null;
        this.f1439f = null;
        this.f1440g = null;
        this.f1441h = false;
        this.f1442i = false;
        this.f1443j = PERIOD_NONE;
        this.f1444k = DAY_NONE;
        this.f1445l = TYPE_AGREE;
        this.f1446m = TYPE_AGREE;
        this.f1447n = null;
        this.f1448o = null;
        this.f1449p = TYPE_AGREE_AND_PAY;
    }

    public String getCurrencyType() {
        return this.f1434a;
    }

    public int getDayOfMonth() {
        return this.f1445l;
    }

    public int getDayOfWeek() {
        return this.f1444k;
    }

    public int getDayOfWeekInt(String str) {
        return str.equals("SUNDAY") ? TYPE_AGREE : str.equals("MONDAY") ? TYPE_AGREE_AND_PAY : str.equals("TUESDAY") ? PERIOD_BIWEEKLY : str.equals("WEDNESDAY") ? PERIOD_SEMIMONTHLY : str.equals("THURSDAY") ? PERIOD_MONTHLY : str.equals("FRIDAY") ? PERIOD_ANNUALLY : str.equals("SATURDAY") ? PERIOD_NONE : DAY_NONE;
    }

    public String getEndDate() {
        return this.f1440g;
    }

    public String getIpnUrl() {
        return this.f1447n;
    }

    public boolean getIsApproved() {
        return this.f1441h;
    }

    public BigDecimal getMaxAmountPerPayment() {
        return this.f1437d;
    }

    public int getMaxNumberOfPayments() {
        return this.f1436c;
    }

    public int getMaxNumberOfPaymentsPerPeriod() {
        return this.f1446m;
    }

    public BigDecimal getMaxTotalAmountOfAllPayments() {
        return this.f1438e;
    }

    public String getMemo() {
        return this.f1448o;
    }

    public String getMerchantName() {
        return this.f1435b;
    }

    public int getPaymentPeriod() {
        return this.f1443j;
    }

    public int getPaymentPeriodInt(String str) {
        return str.equals("DAILY") ? TYPE_AGREE : str.equals("WEEKLY") ? TYPE_AGREE_AND_PAY : str.equals("BIWEEKLY") ? PERIOD_BIWEEKLY : str.equals("SEMIMONTHLY") ? PERIOD_SEMIMONTHLY : str.equals("MONTHLY") ? PERIOD_MONTHLY : str.equals("ANNUALLY") ? PERIOD_ANNUALLY : PERIOD_NONE;
    }

    public boolean getPinRequired() {
        return this.f1442i;
    }

    public String getStartDate() {
        return this.f1439f;
    }

    public int getType() {
        return this.f1449p;
    }

    public boolean isValid() {
        return this.f1435b != null && this.f1435b.length() > 0;
    }

    public void setCurrencyType(String str) {
        this.f1434a = str;
    }

    public void setDayOfMonth(int i) {
        this.f1445l = i;
    }

    public void setDayOfWeek(int i) {
        this.f1444k = i;
    }

    public void setEndDate(String str) {
        this.f1440g = str;
    }

    public void setIpnUrl(String str) {
        this.f1447n = str;
    }

    public void setIsApproved(boolean z) {
        this.f1441h = z;
    }

    public void setMaxAmountPerPayment(BigDecimal bigDecimal) {
        this.f1437d = bigDecimal;
    }

    public void setMaxNumberOfPayments(int i) {
        this.f1436c = i;
    }

    public void setMaxNumberOfPaymentsPerPeriod(int i) {
        this.f1446m = i;
    }

    public void setMaxTotalAmountOfAllPayments(BigDecimal bigDecimal) {
        this.f1438e = bigDecimal;
    }

    public void setMemo(String str) {
        this.f1448o = str;
    }

    public void setMerchantName(String str) {
        this.f1435b = str;
    }

    public void setPaymentPeriod(int i) {
        this.f1443j = i;
    }

    public void setPinRequired(boolean z) {
        this.f1442i = z;
    }

    public void setStartDate(String str) {
        this.f1439f = str;
    }

    public void setType(int i) {
        if (i <= TYPE_AGREE_AND_PAY && i >= 0) {
            this.f1449p = i;
        }
    }
}
