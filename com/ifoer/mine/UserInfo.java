package com.ifoer.mine;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private String amount;
    private String city;
    private String code;
    private String country;
    private String date;
    private String email;
    private String face_thumb;
    private String face_url;
    private String facebook;
    private String find_by_tel;
    private String is_bind_email;
    private String is_bind_mobile;
    private String is_shock;
    private String is_verify;
    private String mobile;
    private String msg;
    private String nick_name;
    private String open_space;
    private String province;
    private String qq;
    private String recommend;
    private String set_face_time;
    private String sex;
    private String signature;
    private String sound;
    private String token;
    private String twitter;
    private String type_name;
    private String user_id;
    private String user_name;
    private String visible;
    private String weibo;

    public String getType_name() {
        return this.type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIs_shock() {
        return this.is_shock;
    }

    public void setIs_shock(String is_shock) {
        this.is_shock = is_shock;
    }

    public String getSound() {
        return this.sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getIs_verify() {
        return this.is_verify;
    }

    public void setIs_verify(String is_verify) {
        this.is_verify = is_verify;
    }

    public String getFind_by_tel() {
        return this.find_by_tel;
    }

    public void setFind_by_tel(String find_by_tel) {
        this.find_by_tel = find_by_tel;
    }

    public String getRecommend() {
        return this.recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getOpen_space() {
        return this.open_space;
    }

    public void setOpen_space(String open_space) {
        this.open_space = open_space;
    }

    public String getVisible() {
        return this.visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIs_bind_email() {
        return this.is_bind_email;
    }

    public void setIs_bind_email(String is_bind_email) {
        this.is_bind_email = is_bind_email;
    }

    public String getIs_bind_mobile() {
        return this.is_bind_mobile;
    }

    public void setIs_bind_mobile(String is_bind_mobile) {
        this.is_bind_mobile = is_bind_mobile;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getFace_thumb() {
        return this.face_thumb;
    }

    public void setFace_thumb(String face_thumb) {
        this.face_thumb = face_thumb;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public String toString() {
        return "LoginUserInfo [token=" + this.token + ", user_id=" + this.user_id + ", user_name=" + this.user_name + ", nick_name=" + this.nick_name + ", mobile=" + this.mobile + ", email=" + this.email + ", signature=" + this.signature + ", set_face_time=" + this.set_face_time + ", face_url=" + this.face_url + ", face_thumb=" + this.face_thumb + "]";
    }
}
