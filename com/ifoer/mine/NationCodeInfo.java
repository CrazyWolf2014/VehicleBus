package com.ifoer.mine;

public class NationCodeInfo {
    private String display;
    private String is_sms;
    private String ncode;
    private String pre_code;

    public String getNcode() {
        return this.ncode;
    }

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setNcode(String ncode) {
        this.ncode = ncode;
    }

    public String getIs_sms() {
        return this.is_sms;
    }

    public void setIs_sms(String is_sms) {
        this.is_sms = is_sms;
    }

    public String getPre_code() {
        return this.pre_code;
    }

    public void setPre_code(String pre_code) {
        this.pre_code = pre_code;
    }
}
