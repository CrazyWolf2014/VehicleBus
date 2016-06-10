package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class UserBaseInfo extends BaseModel {
    private static final long serialVersionUID = 8940705740713334716L;
    private String city;
    private String country;
    private String email;
    private String identity_tag;
    private int is_bind_email;
    private int is_bind_mobile;
    private String mobile;
    private String nick_name;
    private String province;
    private int sex;
    private String signature;
    private String url;
    private String user_name;

    public int getIs_bind_mobile() {
        return this.is_bind_mobile;
    }

    public void setIs_bind_mobile(int is_bind_mobile) {
        this.is_bind_mobile = is_bind_mobile;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIs_bind_email() {
        return this.is_bind_email;
    }

    public void setIs_bind_email(int is_bind_email) {
        this.is_bind_email = is_bind_email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNick_name() {
        return this.nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getIdentity_tag() {
        return this.identity_tag;
    }

    public void setIdentity_tag(String identity_tag) {
        this.identity_tag = identity_tag;
    }
}
