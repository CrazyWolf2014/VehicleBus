package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class MonitorPacket extends IQ {
    public static final String ELEMENT_NAME = "monitor";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private boolean isMonitor;
    private String sessionID;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            if (xmlPullParser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            IQ monitorPacket = new MonitorPacket();
            boolean z = false;
            while (!z) {
                int next = xmlPullParser.next();
                if (next == 2 && "isMonitor".equals(xmlPullParser.getName())) {
                    if ("false".equalsIgnoreCase(xmlPullParser.nextText())) {
                        monitorPacket.setMonitor(false);
                    } else {
                        monitorPacket.setMonitor(true);
                    }
                } else if (next == 3 && MonitorPacket.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    z = true;
                }
            }
            return monitorPacket;
        }
    }

    public boolean isMonitor() {
        return this.isMonitor;
    }

    public void setMonitor(boolean z) {
        this.isMonitor = z;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String str) {
        this.sessionID = str;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=");
        stringBuilder.append('\"');
        stringBuilder.append(NAMESPACE);
        stringBuilder.append('\"');
        stringBuilder.append(">");
        if (this.sessionID != null) {
            stringBuilder.append("<makeOwner sessionID=\"" + this.sessionID + "\"></makeOwner>");
        }
        stringBuilder.append("</").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }
}
