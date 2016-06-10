package com.cnlaunch.x431pro.module.config.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;

public class GetIpAreaResponse extends BaseResponse {
    private static final long serialVersionUID = 1661977668398668824L;
    private IpAreaData data;

    public IpAreaData getData() {
        return this.data;
    }

    public void setData(IpAreaData data) {
        this.data = data;
    }
}
