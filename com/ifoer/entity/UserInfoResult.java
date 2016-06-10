package com.ifoer.entity;

import java.io.Serializable;

public class UserInfoResult implements Serializable {
    private static final long serialVersionUID = 1;
    private String UserName;
    private String cc;
    private int code;
    private String email;
    private String message;
    private String mobile;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCc() {
        return this.cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
