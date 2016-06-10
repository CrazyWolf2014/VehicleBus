package org.jivesoftware.smackx.workgroup.ext.history;

import com.cnmobi.im.dto.Msg;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentChatHistory extends IQ {
    public static final String ELEMENT_NAME = "chat-sessions";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private List<AgentChatSession> agentChatSessions;
    private String agentJID;
    private int maxSessions;
    private long startDate;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            if (xmlPullParser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            IQ agentChatHistory = new AgentChatHistory();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && "chat-session".equals(xmlPullParser.getName())) {
                    agentChatHistory.addChatSession(parseChatSetting(xmlPullParser));
                } else if (next == 3 && AgentChatHistory.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            return agentChatHistory;
        }

        private AgentChatSession parseChatSetting(XmlPullParser xmlPullParser) throws Exception {
            String str = null;
            Object obj = null;
            long j = 0;
            String str2 = null;
            String str3 = null;
            String str4 = null;
            Date date = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && Msg.DATE.equals(xmlPullParser.getName())) {
                    date = new Date(Long.valueOf(xmlPullParser.nextText()).longValue());
                } else if (next == 2 && "duration".equals(xmlPullParser.getName())) {
                    j = Long.valueOf(xmlPullParser.nextText()).longValue();
                } else if (next == 2 && "visitorsName".equals(xmlPullParser.getName())) {
                    str4 = xmlPullParser.nextText();
                } else if (next == 2 && "visitorsEmail".equals(xmlPullParser.getName())) {
                    str3 = xmlPullParser.nextText();
                } else if (next == 2 && "sessionID".equals(xmlPullParser.getName())) {
                    str2 = xmlPullParser.nextText();
                } else if (next == 2 && "question".equals(xmlPullParser.getName())) {
                    str = xmlPullParser.nextText();
                } else if (next == 3 && "chat-session".equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            return new AgentChatSession(date, j, str4, str3, str2, str);
        }
    }

    public AgentChatHistory(String str, int i, Date date) {
        this.agentChatSessions = new ArrayList();
        this.agentJID = str;
        this.maxSessions = i;
        this.startDate = date.getTime();
    }

    public AgentChatHistory(String str, int i) {
        this.agentChatSessions = new ArrayList();
        this.agentJID = str;
        this.maxSessions = i;
        this.startDate = 0;
    }

    public AgentChatHistory() {
        this.agentChatSessions = new ArrayList();
    }

    public void addChatSession(AgentChatSession agentChatSession) {
        this.agentChatSessions.add(agentChatSession);
    }

    public Collection<AgentChatSession> getAgentChatSessions() {
        return this.agentChatSessions;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=");
        stringBuilder.append('\"');
        stringBuilder.append(NAMESPACE);
        stringBuilder.append('\"');
        stringBuilder.append(" agentJID=\"" + this.agentJID + "\"");
        stringBuilder.append(" maxSessions=\"" + this.maxSessions + "\"");
        stringBuilder.append(" startDate=\"" + this.startDate + "\"");
        stringBuilder.append("></").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }
}
