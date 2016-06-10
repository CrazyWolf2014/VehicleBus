package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class ContactData extends BaseModel {
    private static final long serialVersionUID = 3929376880557813604L;
    private String email;
    private String facebook;
    private String mobile;
    private String qq;
    private String twitter;
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

    public String toString() {
        return "ContactData [email=" + this.email + ", mobile=" + this.mobile + ", qq=" + this.qq + ", weibo=" + this.weibo + ", facebook=" + this.facebook + ", twitter=" + this.twitter + "]";
    }
}
