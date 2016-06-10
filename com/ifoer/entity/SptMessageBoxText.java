package com.ifoer.entity;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class SptMessageBoxText implements Serializable {
    private static final long serialVersionUID = 1;
    private String dialogContent;
    private String dialogTitle;
    private int dialogType;

    public SptMessageBoxText() {
        this.dialogTitle = XmlPullParser.NO_NAMESPACE;
        this.dialogContent = XmlPullParser.NO_NAMESPACE;
    }

    public int getDialogType() {
        return this.dialogType;
    }

    public void setDialogType(int dialogType) {
        this.dialogType = dialogType;
    }

    public String getDialogTitle() {
        return this.dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public String getDialogContent() {
        return this.dialogContent;
    }

    public void setDialogContent(String dialogContent) {
        this.dialogContent = dialogContent;
    }
}
