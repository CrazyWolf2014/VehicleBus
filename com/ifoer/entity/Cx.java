package com.ifoer.entity;

import java.util.ArrayList;
import java.util.List;

public class Cx {
    String chexing;
    String imagename;
    String imagepath;
    private List<Versis> version;

    public Cx() {
        this.version = new ArrayList();
    }

    public List<Versis> getVersion() {
        return this.version;
    }

    public void setVersion(List<Versis> version) {
        this.version = version;
    }

    public String getChexing() {
        return this.chexing;
    }

    public void setChexing(String chexing) {
        this.chexing = chexing;
    }

    public String getImagename() {
        return this.imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getImagepath() {
        return this.imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
