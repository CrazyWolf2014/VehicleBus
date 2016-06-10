package com.cnlaunch.x431pro.module.setting.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;

public class UploadServerCheckLogResponse extends BaseResponse {
    private static final long serialVersionUID = -6811013980069644970L;
    private Object data;
    private String message;

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String toString() {
        return "SendServerCheckLogResponse [data=" + this.data + ", message=" + this.message + "]";
    }
}
