package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseModel;

public class ConfData extends BaseModel {
    private static final long serialVersionUID = -1491813937688959896L;
    private String background;
    private String fontsize;
    private String gsound;
    private String is_accept;
    private String is_shock;
    private String sound;

    public String getFontsize() {
        return this.fontsize;
    }

    public void setFontsize(String fontsize) {
        this.fontsize = fontsize;
    }

    public String getBackground() {
        return this.background;
    }

    public void setBackground(String background) {
        this.background = background;
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

    public String getGsound() {
        return this.gsound;
    }

    public void setGsound(String gsound) {
        this.gsound = gsound;
    }

    public String getIs_accept() {
        return this.is_accept;
    }

    public void setIs_accept(String is_accept) {
        this.is_accept = is_accept;
    }

    public String toString() {
        return "ConfData [fontsize=" + this.fontsize + ", background=" + this.background + ", is_shock=" + this.is_shock + ", sound=" + this.sound + ", gsound=" + this.gsound + ", is_accept=" + this.is_accept + "]";
    }
}
