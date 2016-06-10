package com.cnlaunch.framework.network.download;

import java.io.Serializable;

public class BreakpointURL implements Serializable {
    private static final long serialVersionUID = -5894386056739439741L;
    private int code;
    private Data data;
    private String message;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return this.data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "BreakpointURL [code=" + this.code + ", message=" + this.message + "]";
    }
}
