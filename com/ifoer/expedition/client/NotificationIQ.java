package com.ifoer.expedition.client;

import org.jivesoftware.smack.packet.IQ;

public class NotificationIQ extends IQ {
    private String apiKey;
    private String id;
    private String message;
    private String serialno;
    private String title;
    private String type;
    private String uri;
    private String username;

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append("notification").append(" xmlns=\"").append("androidpn:iq:notification").append("\">");
        if (this.id != null) {
            buf.append("<id>").append(this.id).append("</id>");
        }
        buf.append("</").append("notification").append("> ");
        return buf.toString();
    }

    public String getTypes() {
        return this.type;
    }

    public void setTypes(String type) {
        this.type = type;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSerialno() {
        return this.serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String url) {
        this.uri = url;
    }
}
