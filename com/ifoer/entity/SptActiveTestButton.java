package com.ifoer.entity;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class SptActiveTestButton implements Serializable {
    private static final long serialVersionUID = 1;
    private String activeButtonContent;
    private String activeButtonId;
    private int activeButtonSite;

    public SptActiveTestButton() {
        this.activeButtonContent = XmlPullParser.NO_NAMESPACE;
        this.activeButtonSite = 0;
    }

    public String getActiveButtonId() {
        return this.activeButtonId;
    }

    public void setActiveButtonId(String activeButtonId) {
        this.activeButtonId = activeButtonId;
    }

    public String getActiveButtonContent() {
        return this.activeButtonContent;
    }

    public void setActiveButtonContent(String activeButtonContent) {
        this.activeButtonContent = activeButtonContent;
    }

    public int getActiveButtonSite() {
        return this.activeButtonSite;
    }

    public void setActiveButtonSite(int activeButtonSite) {
        this.activeButtonSite = activeButtonSite;
    }
}
