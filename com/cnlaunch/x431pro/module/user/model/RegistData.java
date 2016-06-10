package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class RegistData extends BaseModel {
    private static final long serialVersionUID = -7819662502352444788L;
    private String email;
    private String is_bind_mobile;
    private String mobile;
    private String nick_name;
    private String reg_source;
    private String token;
    private String user_id;
    private String user_name;
    private String zipcode;

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIs_bind_mobile() {
        return this.is_bind_mobile;
    }

    public void setIs_bind_mobile(String is_bind_mobile) {
        this.is_bind_mobile = is_bind_mobile;
    }

    public String getNick_name() {
        return this.nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getReg_source() {
        return this.reg_source;
    }

    public void setReg_source(String reg_source) {
        this.reg_source = reg_source;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getZipcode() {
        return this.zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String toString() {
        return "RegistData [user_id=" + this.user_id + ", user_name=" + this.user_name + ", mobile=" + this.mobile + ", email=" + this.email + ", is_bind_mobile=" + this.is_bind_mobile + ", nick_name=" + this.nick_name + ", reg_source=" + this.reg_source + ", token=" + this.token + "]";
    }
}
