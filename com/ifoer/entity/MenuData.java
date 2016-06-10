package com.ifoer.entity;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class MenuData implements Serializable {
    private static final long serialVersionUID = 1;
    private String MenuContent;
    private String MenuId;
    private String helpMenuContent;
    private String helpMenuId;
    private int site;
    private String titleMenuContent;
    private String titleMenuId;
    private int type;

    public MenuData() {
        this.MenuId = XmlPullParser.NO_NAMESPACE;
        this.MenuContent = XmlPullParser.NO_NAMESPACE;
        this.titleMenuId = XmlPullParser.NO_NAMESPACE;
        this.titleMenuContent = XmlPullParser.NO_NAMESPACE;
        this.helpMenuId = XmlPullParser.NO_NAMESPACE;
        this.helpMenuContent = XmlPullParser.NO_NAMESPACE;
        this.site = 0;
        this.type = 0;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMenuId() {
        return this.MenuId;
    }

    public void setMenuId(String menuId) {
        this.MenuId = menuId;
    }

    public String getMenuTitleId() {
        return this.titleMenuId;
    }

    public void setMenuTitleId(String menuId) {
        this.titleMenuId = menuId;
    }

    public String getMenuHelpId() {
        return this.helpMenuId;
    }

    public void setMenuHelpId(String menuId) {
        this.helpMenuId = menuId;
    }

    public String getMenuContent() {
        return this.MenuContent;
    }

    public void setMenuContent(String menuContent) {
        this.MenuContent = menuContent;
    }

    public void setMenuTitleContent(String menuContent) {
        this.titleMenuContent = menuContent;
    }

    public String getMenuTitleContent() {
        return this.titleMenuContent;
    }

    public void setMenuHelpContent(String menuContent) {
        this.helpMenuContent = menuContent;
    }

    public String getMenuHelpContent() {
        return this.helpMenuContent;
    }

    public int getSite() {
        return this.site;
    }

    public void setSite(int site) {
        this.site = site;
    }
}
