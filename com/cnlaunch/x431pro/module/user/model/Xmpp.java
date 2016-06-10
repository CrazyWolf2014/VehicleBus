package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class Xmpp extends BaseModel {
    private static final long serialVersionUID = 3380228491641321497L;
    private String domain;
    private String ip;
    private String port;

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String toString() {
        return "XmppInfo [ip=" + this.ip + ", port=" + this.port + ", domain=" + this.domain + "]";
    }
}
