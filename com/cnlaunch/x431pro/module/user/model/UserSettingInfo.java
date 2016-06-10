package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class UserSettingInfo extends BaseModel {
    private static final long serialVersionUID = 7344229553261862931L;
    private String nickname;
    private int sex;
    private String signature;
    private String tag;

    public UserSettingInfo() {
        this.tag = null;
        this.signature = null;
        this.sex = -1;
        this.nickname = null;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
