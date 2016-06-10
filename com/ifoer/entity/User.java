package com.ifoer.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String address;
    private int age;
    private String name;
    private String six;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSix() {
        return this.six;
    }

    public void setSix(String six) {
        this.six = six;
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
}
