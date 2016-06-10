package com.cnlaunch.x431pro.module.config.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class ConfigUrl extends BaseModel {
    private static final long serialVersionUID = 7163129377620862929L;
    private String key;
    private String value;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "ConfigUrl [key=" + this.key + ", value=" + this.value + "]";
    }
}
