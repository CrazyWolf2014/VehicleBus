package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class WorkgroupInformation implements PacketExtension {
    public static final String ELEMENT_NAME = "workgroup";
    public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
    private String workgroupJID;

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
            String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid");
            xmlPullParser.next();
            return new WorkgroupInformation(attributeValue);
        }
    }

    public WorkgroupInformation(String str) {
        this.workgroupJID = str;
    }

    public String getWorkgroupJID() {
        return this.workgroupJID;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('<').append(ELEMENT_NAME);
        stringBuilder.append(" jid=\"").append(getWorkgroupJID()).append("\"");
        stringBuilder.append(" xmlns=\"").append(NAMESPACE).append("\" />");
        return stringBuilder.toString();
    }
}
