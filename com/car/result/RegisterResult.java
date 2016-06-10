package com.car.result;

public class RegisterResult extends WSResult {
    protected String cc;
    private int code;
    protected String initPassword;

    public String getCc() {
        return this.cc;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setCc(String value) {
        this.cc = value;
    }

    public String getInitPassword() {
        return this.initPassword;
    }

    public void setInitPassword(String value) {
        this.initPassword = value;
    }
}
