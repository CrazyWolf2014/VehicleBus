package com.ifoer.mine;

import java.io.Serializable;

public class ContactDetail implements Serializable {
    private static final long serialVersionUID = 1;
    private String birthday;
    private String city;
    private String continent;
    private String country;
    private float distance;
    private String email;
    private String face_path_large;
    private String facebook;
    private String mobile;
    private String position;
    private String province;
    private String purikuraUrl;
    private String qq;
    private String set_face_time;
    private int sex;
    private String time;
    private String token;
    private String twitter;
    private String user_name;
    private String weibo;

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPurikuraUrl() {
        return this.purikuraUrl;
    }

    public void setPurikuraUrl(String purikuraUrl) {
        this.purikuraUrl = purikuraUrl;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getContinent() {
        return this.continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
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

    public String getSet_face_time() {
        return this.set_face_time;
    }

    public void setSet_face_time(String set_face_time) {
        this.set_face_time = set_face_time;
    }

    public String getFace_path_large() {
        return this.face_path_large;
    }

    public void setFace_path_large(String face_path_large) {
        this.face_path_large = face_path_large;
    }
}
