package com.cnlaunch.x431pro.module.mine.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class ExpertParam extends BaseModel {
    private static final long serialVersionUID = -5822492934938652045L;
    private String auto_area;
    private String auto_code;
    private String auto_name;
    private String ele_skill;

    public String getAuto_code() {
        return this.auto_code;
    }

    public void setAuto_code(String auto_code) {
        this.auto_code = auto_code;
    }

    public String getAuto_name() {
        return this.auto_name;
    }

    public void setAuto_name(String auto_name) {
        this.auto_name = auto_name;
    }

    public String getAuto_area() {
        return this.auto_area;
    }

    public void setAuto_area(String auto_area) {
        this.auto_area = auto_area;
    }

    public String getEle_skill() {
        return this.ele_skill;
    }

    public void setEle_skill(String ele_skill) {
        this.ele_skill = ele_skill;
    }

    public String toString() {
        return "ExpertParam [auto_code=" + this.auto_code + ", auto_name=" + this.auto_name + ", auto_area=" + this.auto_area + ", ele_skill=" + this.ele_skill + "]";
    }
}
