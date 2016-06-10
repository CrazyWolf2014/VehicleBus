package com.cnlaunch.x431pro.module.config.model;

import com.cnlaunch.x431pro.module.base.BaseModel;
import java.util.List;

public class ConfigData extends BaseModel {
    private static final long serialVersionUID = 8484011277225440517L;
    private String area;
    private List<ConfigUrl> urls;
    private String version;

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<ConfigUrl> getUrls() {
        return this.urls;
    }

    public void setUrls(List<ConfigUrl> urls) {
        this.urls = urls;
    }

    public String toString() {
        return "ConfigData [version=" + this.version + ", area=" + this.area + ", urls=" + this.urls + "]";
    }
}
