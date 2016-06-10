package com.ifoer.mine;

import java.util.List;

public class TechDetail {
    private String code;
    private Contact contact;
    private String msg;
    private List<ProfessionalSkill> proSkillList;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<ProfessionalSkill> getProSkillList() {
        return this.proSkillList;
    }

    public void setProSkillList(List<ProfessionalSkill> proSkillList) {
        this.proSkillList = proSkillList;
    }
}
