package com.ifoer.entity;

import java.io.Serializable;

public class CardConsumeDTO implements Serializable {
    private static final long serialVersionUID = -4983028326846351697L;
    private String cardNo;
    private String date;
    private String freeEndTime;
    private String oldFreeEndTime;
    private int years;

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCardNo() {
        return this.cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getYears() {
        return this.years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public String getOldFreeEndTime() {
        return this.oldFreeEndTime;
    }

    public void setOldFreeEndTime(String oldFreeEndTime) {
        this.oldFreeEndTime = oldFreeEndTime;
    }

    public String getFreeEndTime() {
        return this.freeEndTime;
    }

    public void setFreeEndTime(String freeEndTime) {
        this.freeEndTime = freeEndTime;
    }
}
