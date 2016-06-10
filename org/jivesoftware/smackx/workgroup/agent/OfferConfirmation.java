package org.jivesoftware.smackx.workgroup.agent;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class OfferConfirmation extends IQ {
    private long sessionID;
    private String userJID;

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            IQ offerConfirmation = new OfferConfirmation();
            Object obj = null;
            while (obj == null) {
                xmlPullParser.next();
                String name = xmlPullParser.getName();
                if (xmlPullParser.getEventType() == 2 && "user-jid".equals(name)) {
                    try {
                        offerConfirmation.setUserJID(xmlPullParser.nextText());
                    } catch (NumberFormatException e) {
                    }
                } else if (xmlPullParser.getEventType() == 2 && "session-id".equals(name)) {
                    try {
                        offerConfirmation.setSessionID(Long.valueOf(xmlPullParser.nextText()).longValue());
                    } catch (NumberFormatException e2) {
                    }
                } else if (xmlPullParser.getEventType() == 3 && "offer-confirmation".equals(name)) {
                    obj = 1;
                }
            }
            return offerConfirmation;
        }
    }

    private class NotifyServicePacket extends IQ {
        String roomName;

        NotifyServicePacket(String str, String str2) {
            setTo(str);
            setType(Type.RESULT);
            this.roomName = str2;
        }

        public String getChildElementXML() {
            return "<offer-confirmation  roomname=\"" + this.roomName + "\" xmlns=\"http://jabber.org/protocol/workgroup" + "\"/>";
        }
    }

    public String getUserJID() {
        return this.userJID;
    }

    public void setUserJID(String str) {
        this.userJID = str;
    }

    public long getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(long j) {
        this.sessionID = j;
    }

    public void notifyService(Connection connection, String str, String str2) {
        connection.sendPacket(new NotifyServicePacket(str, str2));
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<offer-confirmation xmlns=\"http://jabber.org/protocol/workgroup\">");
        stringBuilder.append("</offer-confirmation>");
        return stringBuilder.toString();
    }
}
