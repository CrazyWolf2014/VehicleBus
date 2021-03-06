package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class User extends BaseModel {
    private static final long serialVersionUID = -4463342326567570589L;
    private String email;
    private String face_url;
    private String mobile;
    private String nick_name;
    private String set_face_time;
    private String signature;
    private String token;
    private String user_id;
    private String user_name;

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

    public String getNick_name() {
        return this.nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
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

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSet_face_time() {
        return this.set_face_time;
    }

    public void setSet_face_time(String set_face_time) {
        this.set_face_time = set_face_time;
    }

    public String getFace_url() {
        return this.face_url;
    }

    public void setFace_url(String face_url) {
        this.face_url = face_url;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toString() {
        return "User [user_id=" + this.user_id + ", user_name=" + this.user_name + ", nick_name=" + this.nick_name + ", mobile=" + this.mobile + ", email=" + this.email + ", signature=" + this.signature + ", set_face_time=" + this.set_face_time + ", face_url=" + this.face_url + ", token=" + this.token + "]";
    }
}
