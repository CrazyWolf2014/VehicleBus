package org.jivesoftware.smackx.workgroup.packet;

import com.tencent.mm.sdk.platformtools.LocaleUtil;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class SessionID implements PacketExtension {
    public static final String ELEMENT_NAME = "session";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String sessionID;

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
            String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, LocaleUtil.INDONESIAN);
            xmlPullParser.next();
            return new SessionID(attributeValue);
        }
    }

    public SessionID(String str) {
        this.sessionID = str;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE).append("\" ");
        stringBuilder.append("id=\"").append(getSessionID());
        stringBuilder.append("\"/>");
        return stringBuilder.toString();
    }
}
