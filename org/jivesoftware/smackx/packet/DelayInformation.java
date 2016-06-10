package org.jivesoftware.smackx.packet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.GroupChatInvitation;

public class DelayInformation implements PacketExtension {
    public static final DateFormat XEP_0091_UTC_FORMAT;
    private String from;
    private String reason;
    private Date stamp;

    static {
        XEP_0091_UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        XEP_0091_UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public DelayInformation(Date date) {
        this.stamp = date;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String str) {
        this.from = str;
    }

    public Date getStamp() {
        return this.stamp;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String str) {
        this.reason = str;
    }

    public String getElementName() {
        return GroupChatInvitation.ELEMENT_NAME;
    }

    public String getNamespace() {
        return "jabber:x:delay";
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\"");
        stringBuilder.append(" stamp=\"");
        synchronized (XEP_0091_UTC_FORMAT) {
            stringBuilder.append(XEP_0091_UTC_FORMAT.format(this.stamp));
        }
        stringBuilder.append("\"");
        if (this.from != null && this.from.length() > 0) {
            stringBuilder.append(" from=\"").append(this.from).append("\"");
        }
        stringBuilder.append(">");
        if (this.reason != null && this.reason.length() > 0) {
            stringBuilder.append(this.reason);
        }
        stringBuilder.append("</").append(getElementName()).append(">");
        return stringBuilder.toString();
    }
}
