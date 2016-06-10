package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class LoginData extends BaseModel {
    private static final long serialVersionUID = 1037492000737111829L;
    private String token;
    private User user;
    private Xmpp xmpp;

    public Xmpp getXmpp() {
        return this.xmpp;
    }

    public void setXmpp(Xmpp xmpp) {
        this.xmpp = xmpp;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString() {
        return "LoginData [xmpp=" + this.xmpp + ", token=" + this.token + ", user=" + this.user + "]";
    }
}
