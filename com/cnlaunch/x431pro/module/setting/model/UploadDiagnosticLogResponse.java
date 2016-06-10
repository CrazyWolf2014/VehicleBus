package com.cnlaunch.x431pro.module.setting.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;

public class UploadDiagnosticLogResponse extends BaseResponse {
    private static final long serialVersionUID = 249437280147693640L;
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

    public String toString() {
        return "UploadDiagnosticLogResponse [data=" + this.data + ", message=" + this.message + ", getData()=" + getData() + ", getMessage()=" + getMessage() + ", getCode()=" + getCode() + ", getMsg()=" + getMsg() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
    }
}
