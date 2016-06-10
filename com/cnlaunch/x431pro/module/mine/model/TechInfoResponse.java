package com.cnlaunch.x431pro.module.mine.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;

public class TechInfoResponse extends BaseResponse {
    private static final long serialVersionUID = 2230884517309528885L;
    private PersonImformationModel data;

    public PersonImformationModel getData() {
        return this.data;
    }

    public void setData(PersonImformationModel data) {
        this.data = data;
    }
}
