package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class PriconfData extends BaseModel {
    private static final long serialVersionUID = 3557306581620301179L;
    private String allow_vmt_find;
    private String find_by_mail;
    private String find_by_tel;
    private String is_rg;
    private String is_verify;
    private String open_space;
    private String recommend;
    private String visible;

    public String getIs_verify() {
        return this.is_verify;
    }

    public void setIs_verify(String is_verify) {
        this.is_verify = is_verify;
    }

    public String getFind_by_tel() {
        return this.find_by_tel;
    }

    public void setFind_by_tel(String find_by_tel) {
        this.find_by_tel = find_by_tel;
    }

    public String getRecommend() {
        return this.recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getOpen_space() {
        return this.open_space;
    }

    public void setOpen_space(String open_space) {
        this.open_space = open_space;
    }

    public String getVisible() {
        return this.visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getIs_rg() {
        return this.is_rg;
    }

    public void setIs_rg(String is_rg) {
        this.is_rg = is_rg;
    }

    public String getFind_by_mail() {
        return this.find_by_mail;
    }

    public void setFind_by_mail(String find_by_mail) {
        this.find_by_mail = find_by_mail;
    }

    public String getAllow_vmt_find() {
        return this.allow_vmt_find;
    }

    public void setAllow_vmt_find(String allow_vmt_find) {
        this.allow_vmt_find = allow_vmt_find;
    }
}
