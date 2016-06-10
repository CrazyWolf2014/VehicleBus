package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;

public class ConfResponse extends BaseResponse {
    private static final long serialVersionUID = -2421739764178639076L;
    private ConfData data;

    public ConfData getData() {
        return this.data;
    }

    public void setData(ConfData data) {
        this.data = data;
    }
}
