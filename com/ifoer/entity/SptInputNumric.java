package com.ifoer.entity;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class SptInputNumric implements Serializable {
    private static final long serialVersionUID = 1;
    private String dialogContent;
    private String dialogTitle;
    private int digit;
    private String inputHint;

    public SptInputNumric() {
        this.dialogTitle = XmlPullParser.NO_NAMESPACE;
        this.dialogContent = XmlPullParser.NO_NAMESPACE;
        this.inputHint = XmlPullParser.NO_NAMESPACE;
        this.digit = 2;
    }

    public int getDigit() {
        return this.digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }

    public String getInputHint() {
        return this.inputHint;
    }

    public void setInputHint(String inputHint) {
        this.inputHint = inputHint;
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
