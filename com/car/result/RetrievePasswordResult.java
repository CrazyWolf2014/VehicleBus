package com.car.result;

public class RetrievePasswordResult extends WSResult {
    protected String cc;
    protected String password;

    public String getCc() {
        return this.cc;
    }

    public void setCc(String value) {
        this.cc = value;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }
}
