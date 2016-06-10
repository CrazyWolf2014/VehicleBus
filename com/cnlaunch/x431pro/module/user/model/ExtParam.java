package com.cnlaunch.x431pro.module.user.model;

public class ExtParam {
    private String facebook;
    private String hobby;
    private String qq;
    private String twitter;
    private String weibo;

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

    public String getHobby() {
        return this.hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String toString() {
        return "ExtInfo [qq=" + this.qq + ", weibo=" + this.weibo + ", facebook=" + this.facebook + ", twitter=" + this.twitter + ", hobby=" + this.hobby + "]";
    }
}
