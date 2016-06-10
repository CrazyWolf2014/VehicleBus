package com.paypal.android.MEP;

public class MEPAddress {
    private String f1361a;
    private String f1362b;
    private String f1363c;
    private String f1364d;
    private String f1365e;
    private String f1366f;

    public String getCity() {
        return this.f1363c;
    }

    public String getCountrycode() {
        return this.f1366f;
    }

    public String getPostalcode() {
        return this.f1365e;
    }

    public String getState() {
        return this.f1364d;
    }

    public String getStreet1() {
        return this.f1361a;
    }

    public String getStreet2() {
        return this.f1362b;
    }

    public void setCity(String str) {
        this.f1363c = str;
    }

    public void setCountrycode(String str) {
        this.f1366f = str;
    }

    public void setPostalcode(String str) {
        this.f1365e = str;
    }

    public void setState(String str) {
        this.f1364d = str;
    }

    public void setStreet1(String str) {
        this.f1361a = str;
    }

    public void setStreet2(String str) {
        this.f1362b = str;
    }
}
