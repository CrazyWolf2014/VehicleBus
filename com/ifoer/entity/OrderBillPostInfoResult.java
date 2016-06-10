package com.ifoer.entity;

import com.car.result.WSResult;
import java.io.Serializable;

public class OrderBillPostInfoResult extends WSResult implements Serializable {
    private String addressId;
    private String billId;
    private String billSerialNum;
    private String isBilled;
    private String isPosted;
    private String postSerialNum;

    public String getBillId() {
        return this.billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getIsBilled() {
        return this.isBilled;
    }

    public void setIsBilled(String isBilled) {
        this.isBilled = isBilled;
    }

    public String getBillSerialNum() {
        return this.billSerialNum;
    }

    public void setBillSerialNum(String billSerialNum) {
        this.billSerialNum = billSerialNum;
    }

    public String getAddressId() {
        return this.addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getIsPosted() {
        return this.isPosted;
    }

    public void setIsPosted(String isPosted) {
        this.isPosted = isPosted;
    }

    public String getPostSerialNum() {
        return this.postSerialNum;
    }

    public void setPostSerialNum(String postSerialNum) {
        this.postSerialNum = postSerialNum;
    }
}
