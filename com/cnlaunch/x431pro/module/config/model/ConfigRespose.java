package com.cnlaunch.x431pro.module.config.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;

public class ConfigRespose extends BaseResponse {
    private static final long serialVersionUID = 1661977668398668824L;
    private ConfigData data;

    public ConfigData getData() {
        return this.data;
    }

    public void setData(ConfigData data) {
        this.data = data;
    }
}
