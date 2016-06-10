package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.packet.IQ.Type;

public class Bind extends IQ {
    private String jid;
    private String resource;

    public Bind() {
        this.resource = null;
        this.jid = null;
        setType(Type.SET);
    }

    public String getResource() {
        return this.resource;
    }

    public void setResource(String str) {
        this.resource = str;
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String str) {
        this.jid = str;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\">");
        if (this.resource != null) {
            stringBuilder.append("<resource>").append(this.resource).append("</resource>");
        }
        if (this.jid != null) {
            stringBuilder.append("<jid>").append(this.jid).append("</jid>");
        }
        stringBuilder.append("</bind>");
        return stringBuilder.toString();
    }
}
