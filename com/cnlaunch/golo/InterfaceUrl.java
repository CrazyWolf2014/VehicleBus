package com.cnlaunch.golo;

import java.io.Serializable;

public class InterfaceUrl implements Serializable {
    private static final long serialVersionUID = 3101451267965085321L;
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
}
