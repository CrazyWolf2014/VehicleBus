package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.PacketExtension;

public abstract class PEPItem implements PacketExtension {
    String id;

    abstract String getItemDetailsXML();

    abstract String getNode();

    public PEPItem(String str) {
        this.id = str;
    }

    public String getElementName() {
        return "item";
    }

    public String getNamespace() {
        return "http://jabber.org/protocol/pubsub";
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(getElementName()).append(" id=\"").append(this.id).append("\">");
        stringBuilder.append(getItemDetailsXML());
        stringBuilder.append("</").append(getElementName()).append(">");
        return stringBuilder.toString();
    }
}
