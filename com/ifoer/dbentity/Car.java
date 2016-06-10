package com.ifoer.dbentity;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private int areaId;
    private List<CarLogo> carLogos;
    private String name;
    private List<Version> versions;

    public Car() {
        this.versions = new ArrayList();
        this.carLogos = new ArrayList();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Version> getVersions() {
        return this.versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public List<CarLogo> getCarLogos() {
        return this.carLogos;
    }

    public void setCarLogos(List<CarLogo> carLogos) {
        this.carLogos = carLogos;
    }

    public int getAreaId() {
        return this.areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }
}
