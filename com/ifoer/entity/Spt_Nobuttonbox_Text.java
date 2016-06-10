package com.ifoer.entity;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class Spt_Nobuttonbox_Text implements Serializable {
    private static final long serialVersionUID = 1;
    private String content;
    private String title;

    public Spt_Nobuttonbox_Text() {
        this.title = XmlPullParser.NO_NAMESPACE;
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
}
