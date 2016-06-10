package com.ifoer.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SpecialFunction implements Serializable {
    private static final long serialVersionUID = 1;
    private int buttonCount;
    private ArrayList<String> buttonList;
    private ArrayList<String> columsContentList;
    private int columsCount;
    private ArrayList<String> columsTitleList;
    private int columsWidth;
    private String customTitle;

    public SpecialFunction() {
        this.columsCount = 0;
        this.columsWidth = 0;
        this.buttonCount = 0;
        this.columsTitleList = null;
        this.columsContentList = null;
        this.buttonList = null;
    }

    public ArrayList<String> getButtonList() {
        return this.buttonList;
    }

    public void setButtonList(ArrayList<String> buttonList) {
        this.buttonList = buttonList;
    }

    public String getCustomTitle() {
        return this.customTitle;
    }

    public void setCustomTitle(String customTitle) {
        this.customTitle = customTitle;
    }

    public int getButtonCount() {
        return this.buttonCount;
    }

    public void setButtonCount(int buttonCount) {
        this.buttonCount = buttonCount;
    }

    public int getColumsWidth() {
        return this.columsWidth;
    }

    public void setColumsWidth(int columsWidth) {
        this.columsWidth = columsWidth;
    }

    public int getColumsCount() {
        return this.columsCount;
    }

    public void setColumsCount(int columsCount) {
        this.columsCount = columsCount;
    }

    public ArrayList<String> getColumsTitleList() {
        return this.columsTitleList;
    }

    public void setColumsTitleList(ArrayList<String> columsTitle) {
        this.columsTitleList = columsTitle;
    }

    public ArrayList<String> getColumsContentList() {
        return this.columsContentList;
    }

    public void setColumsContentList(ArrayList<String> columsContent) {
        this.columsContentList = columsContent;
    }
}
