package org.jivesoftware.smackx.workgroup.packet;

import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.agent.InvitationRequest;
import org.jivesoftware.smackx.workgroup.agent.OfferContent;
import org.jivesoftware.smackx.workgroup.agent.TransferRequest;
import org.jivesoftware.smackx.workgroup.agent.UserRequest;
import org.jivesoftware.smackx.workgroup.settings.WorkgroupProperties;
import org.jivesoftware.smackx.workgroup.util.MetaDataUtils;
import org.xmlpull.v1.XmlPullParser;

public class OfferRequestProvider implements IQProvider {

    public static class OfferRequestPacket extends IQ {
        private OfferContent content;
        private Map<String, List<String>> metaData;
        private String sessionID;
        private int timeout;
        private String userID;
        private String userJID;

        public OfferRequestPacket(String str, String str2, int i, Map<String, List<String>> map, String str3, OfferContent offerContent) {
            this.userJID = str;
            this.userID = str2;
            this.timeout = i;
            this.metaData = map;
            this.sessionID = str3;
            this.content = offerContent;
        }

        public String getUserID() {
            return this.userID;
        }

        public String getUserJID() {
            return this.userJID;
        }

        public String getSessionID() {
            return this.sessionID;
        }

        public int getTimeout() {
            return this.timeout;
        }

        public OfferContent getContent() {
            return this.content;
        }

        public Map<String, List<String>> getMetaData() {
            return this.metaData;
        }

        public String getChildElementXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<offer xmlns=\"http://jabber.org/protocol/workgroup\" jid=\"").append(this.userJID).append("\">");
            stringBuilder.append("<timeout>").append(this.timeout).append("</timeout>");
            if (this.sessionID != null) {
                stringBuilder.append('<').append(SessionID.ELEMENT_NAME);
                stringBuilder.append(" session=\"");
                stringBuilder.append(getSessionID()).append("\" xmlns=\"");
                stringBuilder.append(WorkgroupProperties.NAMESPACE).append("\"/>");
            }
            if (this.metaData != null) {
                stringBuilder.append(MetaDataUtils.serializeMetaData(this.metaData));
            }
            if (this.userID != null) {
                stringBuilder.append('<').append(UserID.ELEMENT_NAME);
                stringBuilder.append(" id=\"");
                stringBuilder.append(this.userID).append("\" xmlns=\"");
                stringBuilder.append(WorkgroupProperties.NAMESPACE).append("\"/>");
            }
            stringBuilder.append("</offer>");
            return stringBuilder.toString();
        }
    }

    public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
        String attributeValue;
        String str;
        Object obj;
        String str2;
        OfferContent offerContent = null;
        int eventType = xmlPullParser.getEventType();
        int i = -1;
        Map hashMap = new HashMap();
        if (eventType != 2) {
            attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid");
            str = attributeValue;
            obj = null;
            str2 = null;
        } else {
            attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid");
            str = attributeValue;
            obj = null;
            str2 = null;
        }
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                String name = xmlPullParser.getName();
                if ("timeout".equals(name)) {
                    i = Integer.parseInt(xmlPullParser.nextText());
                } else if (MetaData.ELEMENT_NAME.equals(name)) {
                    hashMap = MetaDataUtils.parseMetaData(xmlPullParser);
                } else if (SessionID.ELEMENT_NAME.equals(name)) {
                    str2 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, LocaleUtil.INDONESIAN);
                } else if (UserID.ELEMENT_NAME.equals(name)) {
                    str = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, LocaleUtil.INDONESIAN);
                } else if ("user-request".equals(name)) {
                    offerContent = UserRequest.getInstance();
                } else if (RoomInvitation.ELEMENT_NAME.equals(name)) {
                    RoomInvitation roomInvitation = (RoomInvitation) PacketParserUtils.parsePacketExtension(RoomInvitation.ELEMENT_NAME, AgentStatusRequest.NAMESPACE, xmlPullParser);
                    offerContent = new InvitationRequest(roomInvitation.getInviter(), roomInvitation.getRoom(), roomInvitation.getReason());
                } else if (RoomTransfer.ELEMENT_NAME.equals(name)) {
                    RoomTransfer roomTransfer = (RoomTransfer) PacketParserUtils.parsePacketExtension(RoomTransfer.ELEMENT_NAME, AgentStatusRequest.NAMESPACE, xmlPullParser);
                    offerContent = new TransferRequest(roomTransfer.getInviter(), roomTransfer.getRoom(), roomTransfer.getReason());
                }
            } else if (next == 3 && "offer".equals(xmlPullParser.getName())) {
                obj = 1;
            }
        }
        IQ offerRequestPacket = new OfferRequestPacket(attributeValue, str, i, hashMap, str2, offerContent);
        offerRequestPacket.setType(Type.SET);
        return offerRequestPacket;
    }
}
