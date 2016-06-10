package com.ifoer.entity;

import java.util.List;

public class Car {
    private List<language> language;
    private String name;
    private String path;
    private String vosion;

    public List<language> getLanguage() {
        return this.language;
    }

    public void setLanguage(List<language> language) {
        this.language = language;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
