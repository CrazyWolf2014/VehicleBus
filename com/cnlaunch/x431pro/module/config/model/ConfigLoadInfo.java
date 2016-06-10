package com.cnlaunch.x431pro.module.config.model;

import com.cnlaunch.x431pro.module.base.BaseModel;
import com.ifoer.mine.Contact;
import org.xmlpull.v1.XmlPullParser;

public class ConfigLoadInfo extends BaseModel {
    private static final long serialVersionUID = -5887454448791475583L;
    private String configlan;
    private String configurl;
    private String country_id;

    public ConfigLoadInfo() {
        this.configurl = XmlPullParser.NO_NAMESPACE;
        this.configlan = Contact.RELATION_FRIEND;
    }

    public String getConfiglan() {
        return this.configlan;
    }

    public void setConfiglan(String configlan) {
        this.configlan = configlan;
    }

    public String getConfigurl() {
        return this.configurl;
    }

    public void setConfigurl(String configurl) {
        this.configurl = configurl;
    }

    public String getCountry_id() {
        return this.country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
}
