package com.cnlaunch.x431pro.module.config.db;

public class ConfigInfo {
    private Long id;
    private String key;
    private String value;

    public ConfigInfo(Long id) {
        this.id = id;
    }

    public ConfigInfo(Long id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
