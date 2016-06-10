package com.ifoer.entity;

import java.io.Serializable;

public class CountryInfo implements Serializable {
    private static final long serialVersionUID = 3613642249705017672L;
    private String country;
    private int iSsms;
    private int nCode;
    private String preCode;

    public int getnCode() {
        return this.nCode;
    }

    public void setnCode(int nCode) {
        this.nCode = nCode;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getiSsms() {
        return this.iSsms;
    }

    public void setiSsms(int iSsms) {
        this.iSsms = iSsms;
    }

    public String getPreCode() {
        return this.preCode;
    }

    public void setPreCode(String preCode) {
        this.preCode = preCode;
    }
}
