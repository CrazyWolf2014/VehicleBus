package org.jivesoftware.smackx.workgroup.packet;

import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentStatusRequest extends IQ {
    public static final String ELEMENT_NAME = "agent-status-request";
    public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
    private Set<Item> agents;

    public static class Item {
        private String jid;
        private String name;
        private String type;

        public Item(String str, String str2, String str3) {
            this.jid = str;
            this.type = str2;
            this.name = str3;
        }

        public String getJID() {
            return this.jid;
        }

        public String getType() {
            return this.type;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            IQ agentStatusRequest = new AgentStatusRequest();
            if (xmlPullParser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && "agent".equals(xmlPullParser.getName())) {
                    agentStatusRequest.agents.add(parseAgent(xmlPullParser));
                } else if (next == 3 && AgentStatusRequest.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            return agentStatusRequest;
        }

        private Item parseAgent(XmlPullParser xmlPullParser) throws Exception {
            Object obj = null;
            String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid");
            String attributeValue2 = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, SharedPref.TYPE);
            String str = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && "name".equals(xmlPullParser.getName())) {
                    str = xmlPullParser.nextText();
                } else if (next == 3 && "agent".equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            return new Item(attributeValue, attributeValue2, str);
        }
    }

    public AgentStatusRequest() {
        this.agents = new HashSet();
    }

    public int getAgentCount() {
        return this.agents.size();
    }

    public Set<Item> getAgents() {
        return Collections.unmodifiableSet(this.agents);
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE).append("\">");
        synchronized (this.agents) {
            for (Item item : this.agents) {
                stringBuilder.append("<agent jid=\"").append(item.getJID()).append("\">");
                if (item.getName() != null) {
                    stringBuilder.append("<name xmlns=\"http://jivesoftware.com/protocol/workgroup\">");
                    stringBuilder.append(item.getName());
                    stringBuilder.append("</name>");
                }
                stringBuilder.append("</agent>");
            }
        }
        stringBuilder.append("</").append(getElementName()).append("> ");
        return stringBuilder.toString();
    }
}
