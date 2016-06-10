package org.jivesoftware.smackx.workgroup.packet;

import com.tencent.mm.sdk.platformtools.LocaleUtil;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class OfferRevokeProvider implements IQProvider {

    public class OfferRevokePacket extends IQ {
        private String reason;
        private String sessionID;
        private String userID;
        private String userJID;

        public OfferRevokePacket(String str, String str2, String str3, String str4) {
            this.userJID = str;
            this.userID = str2;
            this.reason = str3;
            this.sessionID = str4;
        }

        public String getUserJID() {
            return this.userJID;
        }

        public String getUserID() {
            return this.userID;
        }

        public String getReason() {
            return this.reason;
        }

        public String getSessionID() {
            return this.sessionID;
        }

        public String getChildElementXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<offer-revoke xmlns=\"http://jabber.org/protocol/workgroup\" jid=\"").append(this.userID).append("\">");
            if (this.reason != null) {
                stringBuilder.append("<reason>").append(this.reason).append("</reason>");
            }
            if (this.sessionID != null) {
                stringBuilder.append(new SessionID(this.sessionID).toXML());
            }
            if (this.userID != null) {
                stringBuilder.append(new UserID(this.userID).toXML());
            }
            stringBuilder.append("</offer-revoke>");
            return stringBuilder.toString();
        }
    }

    public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
        String str = null;
        String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid");
        Object obj = null;
        String str2 = null;
        String str3 = attributeValue;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2 && xmlPullParser.getName().equals("reason")) {
                str2 = xmlPullParser.nextText();
            } else if (next == 2 && xmlPullParser.getName().equals(SessionID.ELEMENT_NAME)) {
                str = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, LocaleUtil.INDONESIAN);
            } else if (next == 2 && xmlPullParser.getName().equals(UserID.ELEMENT_NAME)) {
                str3 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, LocaleUtil.INDONESIAN);
            } else if (next == 3 && xmlPullParser.getName().equals("offer-revoke")) {
                obj = 1;
            }
        }
        return new OfferRevokePacket(attributeValue, str3, str2, str);
    }
}
