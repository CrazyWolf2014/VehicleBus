package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class Country extends BaseModel {
    private static final long serialVersionUID = 8173885715167729056L;
    private String display;
    private int is_sms;
    private String ncode;
    private int pre_code;

    public String getNcode() {
        return this.ncode;
    }

    public void setNcode(String ncode) {
        this.ncode = ncode;
    }

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public int getIs_sms() {
        return this.is_sms;
    }

    public void setIs_sms(int is_sms) {
        this.is_sms = is_sms;
    }

    public int getPre_code() {
        return this.pre_code;
    }

    public void setPre_code(int pre_code) {
        this.pre_code = pre_code;
    }

    public String toString() {
        return "Country [ncode=" + this.ncode + ", display=" + this.display + ", is_sms=" + this.is_sms + ", pre_code=" + this.pre_code + "]";
    }
}
