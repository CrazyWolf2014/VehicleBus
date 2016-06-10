package com.ifoer.entity;

import android.graphics.drawable.Drawable;

public class CircleFriend {
    private String groupName;
    private Drawable icon;
    private String name;

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
