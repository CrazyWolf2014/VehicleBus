package com.cnlaunch.x431pro.module.base;

public class CommonResponse extends BaseResponse {
    private static final long serialVersionUID = -8237333478736743128L;
    private Object data;

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
