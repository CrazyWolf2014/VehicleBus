package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

public class DepartQueuePacket extends IQ {
    private String user;

    public DepartQueuePacket(String str) {
        this(str, null);
    }

    public DepartQueuePacket(String str, String str2) {
        this.user = str2;
        setTo(str);
        setType(Type.SET);
        setFrom(str2);
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder("<depart-queue xmlns=\"http://jabber.org/protocol/workgroup\"");
        if (this.user != null) {
            stringBuilder.append("><jid>").append(this.user).append("</jid></depart-queue>");
        } else {
            stringBuilder.append("/>");
        }
        return stringBuilder.toString();
    }
}
