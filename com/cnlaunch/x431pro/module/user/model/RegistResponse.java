package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;

public class RegistResponse extends BaseResponse {
    private static final long serialVersionUID = -8777253969430078568L;
    private RegistData data;

    public RegistData getData() {
        return this.data;
    }

    public void setData(RegistData data) {
        this.data = data;
    }
}
