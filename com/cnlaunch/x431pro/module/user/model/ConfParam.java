package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class ConfParam extends BaseModel {
    private static final long serialVersionUID = 8127910920431140019L;
    private String allow_vmt_find;
    private String find_by_tel;
    private String fontsize;
    private String is_shock;
    private String is_verify;
    private String open_space;
    private String recommend;
    private String sound;
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

    public String getIs_shock() {
        return this.is_shock;
    }

    public void setIs_shock(String is_shock) {
        this.is_shock = is_shock;
    }

    public String getSound() {
        return this.sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getFontsize() {
        return this.fontsize;
    }

    public void setFontsize(String fontsize) {
        this.fontsize = fontsize;
    }

    public String getAllow_vmt_find() {
        return this.allow_vmt_find;
    }

    public void setAllow_vmt_find(String allow_vmt_find) {
        this.allow_vmt_find = allow_vmt_find;
    }

    public String toString() {
        return "ConfInfo [is_verify=" + this.is_verify + ", find_by_tel=" + this.find_by_tel + ", recommend=" + this.recommend + ", open_space=" + this.open_space + ", visible=" + this.visible + ", is_shock=" + this.is_shock + ", sound=" + this.sound + ", fontsize=" + this.fontsize + ", allow_vmt_find=" + this.allow_vmt_find + "]";
    }
}
