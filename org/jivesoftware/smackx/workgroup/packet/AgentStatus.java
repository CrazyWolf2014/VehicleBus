package org.jivesoftware.smackx.workgroup.packet;

import com.tencent.mm.sdk.plugin.BaseProfile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentStatus implements PacketExtension {
    public static final String ELEMENT_NAME = "agent-status";
    public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
    private static final SimpleDateFormat UTC_FORMAT;
    private List<ChatInfo> currentChats;
    private int maxChats;
    private String workgroupJID;

    public static class ChatInfo {
        private Date date;
        private String email;
        private String question;
        private String sessionID;
        private String userID;
        private String username;

        public ChatInfo(String str, String str2, Date date, String str3, String str4, String str5) {
            this.sessionID = str;
            this.userID = str2;
            this.date = date;
            this.email = str3;
            this.username = str4;
            this.question = str5;
        }

        public String getSessionID() {
            return this.sessionID;
        }

        public String getUserID() {
            return this.userID;
        }

        public Date getDate() {
            return this.date;
        }

        public String getEmail() {
            return this.email;
        }

        public String getUsername() {
            return this.username;
        }

        public String getQuestion() {
            return this.question;
        }

        public String toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<chat ");
            if (this.sessionID != null) {
                stringBuilder.append(" sessionID=\"").append(this.sessionID).append("\"");
            }
            if (this.userID != null) {
                stringBuilder.append(" userID=\"").append(this.userID).append("\"");
            }
            if (this.date != null) {
                stringBuilder.append(" startTime=\"").append(AgentStatus.UTC_FORMAT.format(this.date)).append("\"");
            }
            if (this.email != null) {
                stringBuilder.append(" email=\"").append(this.email).append("\"");
            }
            if (this.username != null) {
                stringBuilder.append(" username=\"").append(this.username).append("\"");
            }
            if (this.question != null) {
                stringBuilder.append(" question=\"").append(this.question).append("\"");
            }
            stringBuilder.append("/>");
            return stringBuilder.toString();
        }
    }

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
            PacketExtension agentStatus = new AgentStatus();
            agentStatus.workgroupJID = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid");
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    if ("chat".equals(xmlPullParser.getName())) {
                        agentStatus.currentChats.add(parseChatInfo(xmlPullParser));
                    } else if ("max-chats".equals(xmlPullParser.getName())) {
                        agentStatus.maxChats = Integer.parseInt(xmlPullParser.nextText());
                    }
                } else if (next == 3 && AgentStatus.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            return agentStatus;
        }

        private ChatInfo parseChatInfo(XmlPullParser xmlPullParser) {
            String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "sessionID");
            String attributeValue2 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "userID");
            Date date = null;
            try {
                date = AgentStatus.UTC_FORMAT.parse(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "startTime"));
            } catch (ParseException e) {
            }
            return new ChatInfo(attributeValue, attributeValue2, date, xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "email"), xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, BaseProfile.COL_USERNAME), xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "question"));
        }
    }

    static {
        UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    AgentStatus() {
        this.currentChats = new ArrayList();
        this.maxChats = -1;
    }

    public String getWorkgroupJID() {
        return this.workgroupJID;
    }

    public List<ChatInfo> getCurrentChats() {
        return Collections.unmodifiableList(this.currentChats);
    }

    public int getMaxChats() {
        return this.maxChats;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE).append("\"");
        if (this.workgroupJID != null) {
            stringBuilder.append(" jid=\"").append(this.workgroupJID).append("\"");
        }
        stringBuilder.append(">");
        if (this.maxChats != -1) {
            stringBuilder.append("<max-chats>").append(this.maxChats).append("</max-chats>");
        }
        if (!this.currentChats.isEmpty()) {
            stringBuilder.append("<current-chats xmlns= \"http://jivesoftware.com/protocol/workgroup\">");
            for (ChatInfo toXML : this.currentChats) {
                stringBuilder.append(toXML.toXML());
            }
            stringBuilder.append("</current-chats>");
        }
        stringBuilder.append("</").append(getElementName()).append("> ");
        return stringBuilder.toString();
    }
}
