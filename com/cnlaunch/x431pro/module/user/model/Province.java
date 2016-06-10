package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class Province extends BaseModel {
    private static final long serialVersionUID = 5200559937107324792L;
    private String display;
    private String pcode;

    public String getPcode() {
        return this.pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String toString() {
        return "Province [pcode=" + this.pcode + ", display=" + this.display + "]";
    }
}
