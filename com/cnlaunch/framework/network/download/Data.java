package com.cnlaunch.framework.network.download;

import java.io.Serializable;

public class Data implements Serializable {
    private static final long serialVersionUID = 2367519982636659174L;
    private String downUrl;
    private String md5SignStr;
    private long softLength;

    public String getDownUrl() {
        return this.downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getMd5SignStr() {
        return this.md5SignStr;
    }

    public void setMd5SignStr(String md5SignStr) {
        this.md5SignStr = md5SignStr;
    }

    public long getSoftLength() {
        return this.softLength;
    }

    public void setSoftLength(long softLength) {
        this.softLength = softLength;
    }

    public String toString() {
        return "Data [downUrl=" + this.downUrl + ", md5SignStr=" + this.md5SignStr + ", softLength=" + this.softLength + "]";
    }
}
