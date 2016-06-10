package com.ifoer.mine.model;

import java.io.Serializable;

public class ExpertSkill implements Serializable {
    private static final long serialVersionUID = -4737819111403921570L;
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

    public String getEle_skill() {
        return this.ele_skill;
    }

    public void setEle_skill(String ele_skill) {
        this.ele_skill = ele_skill;
    }
}
