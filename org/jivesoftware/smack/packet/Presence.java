package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.StringUtils;
import org.xbill.DNS.KEYRecord.Flags;

public class Presence extends Packet {
    private String language;
    private Mode mode;
    private int priority;
    private String status;
    private Type type;

    public enum Mode {
        chat,
        available,
        away,
        xa,
        dnd
    }

    public enum Type {
        available,
        unavailable,
        subscribe,
        subscribed,
        unsubscribe,
        unsubscribed,
        error
    }

    public Presence(Type type) {
        this.type = Type.available;
        this.status = null;
        this.priority = Integer.MIN_VALUE;
        this.mode = null;
        setType(type);
    }

    public Presence(Type type, String str, int i, Mode mode) {
        this.type = Type.available;
        this.status = null;
        this.priority = Integer.MIN_VALUE;
        this.mode = null;
        setType(type);
        setStatus(str);
        setPriority(i);
        setMode(mode);
    }

    public boolean isAvailable() {
        return this.type == Type.available;
    }

    public boolean isAway() {
        return this.type == Type.available && (this.mode == Mode.away || this.mode == Mode.xa || this.mode == Mode.dnd);
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        if (type == null) {
            throw new NullPointerException("Type cannot be null");
        }
        this.type = type;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int i) {
        if (i < -128 || i > Flags.FLAG8) {
            throw new IllegalArgumentException("Priority value " + i + " is not valid. Valid range is -128 through 128.");
        }
        this.priority = i;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String str) {
        this.language = str;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<presence");
        if (getXmlns() != null) {
            stringBuilder.append(" xmlns=\"").append(getXmlns()).append("\"");
        }
        if (this.language != null) {
            stringBuilder.append(" xml:lang=\"").append(getLanguage()).append("\"");
        }
        if (getPacketID() != null) {
            stringBuilder.append(" id=\"").append(getPacketID()).append("\"");
        }
        if (getTo() != null) {
            stringBuilder.append(" to=\"").append(StringUtils.escapeForXML(getTo())).append("\"");
        }
        if (getFrom() != null) {
            stringBuilder.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
        }
        if (this.type != Type.available) {
            stringBuilder.append(" type=\"").append(this.type).append("\"");
        }
        stringBuilder.append(">");
        if (this.status != null) {
            stringBuilder.append("<status>").append(StringUtils.escapeForXML(this.status)).append("</status>");
        }
        if (this.priority != Integer.MIN_VALUE) {
            stringBuilder.append("<priority>").append(this.priority).append("</priority>");
        }
        if (!(this.mode == null || this.mode == Mode.available)) {
            stringBuilder.append("<show>").append(this.mode).append("</show>");
        }
        stringBuilder.append(getExtensionsXML());
        XMPPError error = getError();
        if (error != null) {
            stringBuilder.append(error.toXML());
        }
        stringBuilder.append("</presence>");
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.type);
        if (this.mode != null) {
            stringBuilder.append(": ").append(this.mode);
        }
        if (getStatus() != null) {
            stringBuilder.append(" (").append(getStatus()).append(")");
        }
        return stringBuilder.toString();
    }
}
