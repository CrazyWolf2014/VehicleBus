package org.jivesoftware.smackx.workgroup.packet;

import com.tencent.mm.sdk.platformtools.LocaleUtil;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class UserID implements PacketExtension {
    public static final String ELEMENT_NAME = "user";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String userID;

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
            String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, LocaleUtil.INDONESIAN);
            xmlPullParser.next();
            return new UserID(attributeValue);
        }
    }

    public UserID(String str) {
        this.userID = str;
    }

    public String getUserID() {
        return this.userID;
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
        stringBuilder.append("id=\"").append(getUserID());
        stringBuilder.append("\"/>");
        return stringBuilder.toString();
    }
}
