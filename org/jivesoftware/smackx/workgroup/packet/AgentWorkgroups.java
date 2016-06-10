package org.jivesoftware.smackx.workgroup.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentWorkgroups extends IQ {
    private String agentJID;
    private List<String> workgroups;

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            String attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid");
            List arrayList = new ArrayList();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    if (xmlPullParser.getName().equals(WorkgroupInformation.ELEMENT_NAME)) {
                        arrayList.add(xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "jid"));
                    }
                } else if (next == 3 && xmlPullParser.getName().equals("workgroups")) {
                    obj = 1;
                }
            }
            return new AgentWorkgroups(attributeValue, arrayList);
        }
    }

    public AgentWorkgroups(String str) {
        this.agentJID = str;
        this.workgroups = new ArrayList();
    }

    public AgentWorkgroups(String str, List<String> list) {
        this.agentJID = str;
        this.workgroups = list;
    }

    public String getAgentJID() {
        return this.agentJID;
    }

    public List<String> getWorkgroups() {
        return Collections.unmodifiableList(this.workgroups);
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<workgroups xmlns=\"http://jabber.org/protocol/workgroup\" jid=\"").append(this.agentJID).append("\">");
        for (String str : this.workgroups) {
            stringBuilder.append("<workgroup jid=\"" + str + "\"/>");
        }
        stringBuilder.append("</workgroups>");
        return stringBuilder.toString();
    }
}
