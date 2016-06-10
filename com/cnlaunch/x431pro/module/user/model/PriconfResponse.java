package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;

public class PriconfResponse extends BaseResponse {
    private static final long serialVersionUID = -1923553172116320747L;
    private PriconfData data;

    public PriconfData getData() {
        return this.data;
    }

    public void setData(PriconfData data) {
        this.data = data;
    }
}
