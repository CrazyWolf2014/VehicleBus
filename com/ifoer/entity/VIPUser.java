package com.ifoer.entity;

import java.io.Serializable;

public class VIPUser implements Serializable {
    private String address;
    private int age;
    private String carType;
    private String carUrl;
    private String car_no;
    private String id;
    private String if_pass;
    private String name;
    private String serNo;
    private String sex;
    private String sortKey;
    private String user_id;
    private int warn_un_readNun;

    public String getIf_pass() {
        return this.if_pass;
    }

    public void setIf_pass(String if_pass) {
        this.if_pass = if_pass;
    }

    public int getWarn_un_readNun() {
        return this.warn_un_readNun;
    }

    public void setWarn_un_readNun(int warn_un_readNun) {
        this.warn_un_readNun = warn_un_readNun;
    }

    public String getCar_no() {
        return this.car_no;
    }

    public void setCar_no(String car_no) {
        this.car_no = car_no;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSortKey() {
        return this.sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getCarType() {
        return this.carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getName() {
        return this.name;
    }

    public String getSerNo() {
        return this.serNo;
    }

    public void setSerNo(String serNo) {
        this.serNo = serNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarUrl() {
        return this.carUrl;
    }

    public void setCarUrl(String carUrl) {
        this.carUrl = carUrl;
    }
}
