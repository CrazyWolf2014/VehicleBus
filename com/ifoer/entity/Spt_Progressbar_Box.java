package com.ifoer.entity;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class Spt_Progressbar_Box implements Serializable {
    private static final long serialVersionUID = 1;
    private String content;
    private int progressbarLen;
    private String title;

    public Spt_Progressbar_Box() {
        this.title = XmlPullParser.NO_NAMESPACE;
        this.progressbarLen = 0;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getProgressbarLen() {
        return this.progressbarLen;
    }

    public void setProgressbarLen(int progressbarLen) {
        this.progressbarLen = progressbarLen;
    }
}
