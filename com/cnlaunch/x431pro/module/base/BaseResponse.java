package com.cnlaunch.x431pro.module.base;

public class BaseResponse extends BaseModel {
    public static final int ERROR_INVALID_TOKEN = -1;
    private static final long serialVersionUID = -8547901708373607611L;
    private int code;
    private String msg;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return "BaseResponse [code=" + this.code + ", msg=" + this.msg + "]";
    }
}
