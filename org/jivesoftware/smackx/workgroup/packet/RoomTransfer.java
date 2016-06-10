package org.jivesoftware.smackx.workgroup.packet;

import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class RoomTransfer implements PacketExtension {
    public static final String ELEMENT_NAME = "transfer";
    public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
    private String invitee;
    private String inviter;
    private String reason;
    private String room;
    private String sessionID;
    private Type type;

    public enum Type {
        user,
        queue,
        workgroup
    }

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
            PacketExtension roomTransfer = new RoomTransfer();
            roomTransfer.type = Type.valueOf(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, SharedPref.TYPE));
            Object obj = null;
            while (obj == null) {
                xmlPullParser.next();
                String name = xmlPullParser.getName();
                if (xmlPullParser.getEventType() == 2) {
                    if (SessionID.ELEMENT_NAME.equals(name)) {
                        roomTransfer.sessionID = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, LocaleUtil.INDONESIAN);
                    } else if ("invitee".equals(name)) {
                        roomTransfer.invitee = xmlPullParser.nextText();
                    } else if ("inviter".equals(name)) {
                        roomTransfer.inviter = xmlPullParser.nextText();
                    } else if ("reason".equals(name)) {
                        roomTransfer.reason = xmlPullParser.nextText();
                    } else if ("room".equals(name)) {
                        roomTransfer.room = xmlPullParser.nextText();
                    }
                } else if (xmlPullParser.getEventType() == 3 && RoomTransfer.ELEMENT_NAME.equals(name)) {
                    obj = 1;
                }
            }
            return roomTransfer;
        }
    }

    public RoomTransfer(Type type, String str, String str2, String str3) {
        this.type = type;
        this.invitee = str;
        this.sessionID = str2;
        this.reason = str3;
    }

    private RoomTransfer() {
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String getInviter() {
        return this.inviter;
    }

    public String getRoom() {
        return this.room;
    }

    public String getReason() {
        return this.reason;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE);
        stringBuilder.append("\" type=\"").append(this.type).append("\">");
        stringBuilder.append("<session xmlns=\"http://jivesoftware.com/protocol/workgroup\" id=\"").append(this.sessionID).append("\"></session>");
        if (this.invitee != null) {
            stringBuilder.append("<invitee>").append(this.invitee).append("</invitee>");
        }
        if (this.inviter != null) {
            stringBuilder.append("<inviter>").append(this.inviter).append("</inviter>");
        }
        if (this.reason != null) {
            stringBuilder.append("<reason>").append(this.reason).append("</reason>");
        }
        stringBuilder.append("</").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }
}
