package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class City extends BaseModel {
    private static final long serialVersionUID = -3317250513605695444L;
    private String ccode;
    private String display;

    public String getCcode() {
        return this.ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String toString() {
        return "City [ccode=" + this.ccode + ", display=" + this.display + "]";
    }
}
