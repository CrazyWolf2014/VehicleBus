package com.cnlaunch.x431pro.module.user.model;

public class UserParam {
    private String email;
    private String identity_tag;
    private String lang;
    private String mobile;
    private String name;
    private String sex;
    private String signature;
    private String uname;
    private String vcode;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUname() {
        return this.uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
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

    public String getVcode() {
        return this.vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getIdentity_tag() {
        return this.identity_tag;
    }

    public void setIdentity_tag(String identity_tag) {
        this.identity_tag = identity_tag;
    }

    public String toString() {
        return "UserInfo [name=" + this.name + ", sex=" + this.sex + ", signature=" + this.signature + ", uname=" + this.uname + ", email=" + this.email + ", mobile=" + this.mobile + ", vcode=" + this.vcode + ", lang=" + this.lang + ", identity_tag=" + this.identity_tag + "]";
    }
}
