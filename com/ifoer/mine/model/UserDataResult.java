package com.ifoer.mine.model;

import java.io.Serializable;

public class UserDataResult extends BaseCode implements Serializable {
    private static final long serialVersionUID = 4989848284926897857L;
    private String email;
    private String facebook;
    private String isBindEmali;
    private String isBindMobile;
    private String mobile;
    private String qq;
    private String twitter;
    private String uEmail;
    private String uMobile;
    private String weibo;

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

    public String getuEmail() {
        return this.uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuMobile() {
        return this.uMobile;
    }

    public void setuMobile(String uMobile) {
        this.uMobile = uMobile;
    }

    public String getIsBindEmali() {
        return this.isBindEmali;
    }

    public void setIsBindEmali(String isBindEmali) {
        this.isBindEmali = isBindEmali;
    }

    public String getIsBindMobile() {
        return this.isBindMobile;
    }

    public void setIsBindMobile(String isBindMobile) {
        this.isBindMobile = isBindMobile;
    }

    public String getQq() {
        return this.qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeibo() {
        return this.weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
