package com.cnlaunch.x431pro.module.config.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class IpAreaData extends BaseModel {
    private static final long serialVersionUID = -6402381633337830771L;
    private String area;
    private String area_id;
    private String city;
    private String city_id;
    private String country;
    private String country_id;
    private String county;
    private String county_id;
    private String ip;
    private String isp;
    private String isp_id;
    private String region;
    private String region_id;

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_id() {
        return this.country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea_id() {
        return this.area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion_id() {
        return this.region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity_id() {
        return this.city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCounty() {
        return this.county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCounty_id() {
        return this.county_id;
    }

    public void setCounty_id(String county_id) {
        this.county_id = county_id;
    }

    public String getIsp() {
        return this.isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getIsp_id() {
        return this.isp_id;
    }

    public void setIsp_id(String isp_id) {
        this.isp_id = isp_id;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
