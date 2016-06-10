package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentInfo extends IQ {
    public static final String ELEMENT_NAME = "agent-info";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String jid;
    private String name;

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            IQ agentInfo = new AgentInfo();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    if (xmlPullParser.getName().equals("jid")) {
                        agentInfo.setJid(xmlPullParser.nextText());
                    } else if (xmlPullParser.getName().equals("name")) {
                        agentInfo.setName(xmlPullParser.nextText());
                    }
                } else if (next == 3 && xmlPullParser.getName().equals(AgentInfo.ELEMENT_NAME)) {
                    obj = 1;
                }
            }
            return agentInfo;
        }
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String str) {
        this.jid = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE).append("\">");
        if (this.jid != null) {
            stringBuilder.append("<jid>").append(getJid()).append("</jid>");
        }
        if (this.name != null) {
            stringBuilder.append("<name>").append(getName()).append("</name>");
        }
        stringBuilder.append("</").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }
}
