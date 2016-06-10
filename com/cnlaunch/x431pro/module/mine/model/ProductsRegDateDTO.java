package com.cnlaunch.x431pro.module.mine.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class ProductsRegDateDTO extends BaseModel {
    private static final long serialVersionUID = 3915347426207964865L;
    private String regDate;
    private String serialNo;
    private String snState;

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSnState() {
        return this.snState;
    }

    public void setSnState(String snState) {
        this.snState = snState;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String toString() {
        return "ProductsRegDateDTO [serialNo=" + this.serialNo + ", snState=" + this.snState + ", regDate=" + this.regDate + "]";
    }
}
