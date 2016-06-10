package com.ifoer.entity;

import com.car.result.WSResult;
import java.io.Serializable;

public class CardInfo extends WSResult implements Serializable {
    private static final long serialVersionUID = -8332138977867754517L;
    private String carNo;
    private String cardConsumeDate;
    private String cardName;
    private int cardRechargeYear;
    private int cardState;
    private boolean dtoisNull;
    private String serialNo;
    private String softConfName;

    public boolean isDtoisNull() {
        return this.dtoisNull;
    }

    public void setDtoisNull(boolean dtoisNull) {
        this.dtoisNull = dtoisNull;
    }

    public String getCardName() {
        return this.cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCarNo() {
        return this.carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCardConsumeDate() {
        return this.cardConsumeDate;
    }

    public void setCardConsumeDate(String cardConsumeDate) {
        this.cardConsumeDate = cardConsumeDate;
    }

    public int getCardRechargeYear() {
        return this.cardRechargeYear;
    }

    public void setCardRechargeYear(int cardRechargeYear) {
        this.cardRechargeYear = cardRechargeYear;
    }

    public int getCardState() {
        return this.cardState;
    }

    public void setCardState(int cardState) {
        this.cardState = cardState;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSoftConfName() {
        return this.softConfName;
    }

    public void setSoftConfName(String softConfName) {
        this.softConfName = softConfName;
    }
}
