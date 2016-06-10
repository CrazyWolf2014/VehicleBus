package com.ifoer.mine.model;

import android.text.TextUtils;
import java.io.Serializable;

public class MsgHeader implements Serializable {
    private static final long serialVersionUID = -621271271112283654L;
    private int code;
    private String data;
    private String debug_msg;
    private boolean isError;
    private String msg;

    public MsgHeader() {
        this.isError = false;
    }

    public boolean isError() {
        return this.isError;
    }

    public void setError(boolean isError) {
        this.isError = isError;
    }

    public String getContentData() {
        return this.data;
    }

    public void setContentData(String data) {
        this.data = data;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        if (TextUtils.isEmpty(this.msg)) {
            this.msg = getDebug_msg();
        }
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDebug_msg() {
        return this.debug_msg;
    }

    public void setDebug_msg(String debug_msg) {
        this.debug_msg = debug_msg;
    }
}
