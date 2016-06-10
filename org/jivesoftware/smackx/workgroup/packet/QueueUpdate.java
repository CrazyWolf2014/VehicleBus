package org.jivesoftware.smackx.workgroup.packet;

import com.cnmobi.im.dto.Msg;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class QueueUpdate implements PacketExtension {
    public static final String ELEMENT_NAME = "queue-status";
    public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
    private int position;
    private int remainingTime;

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
            int i = -1;
            Object obj = null;
            int i2 = -1;
            while (obj == null) {
                xmlPullParser.next();
                String name = xmlPullParser.getName();
                if (xmlPullParser.getEventType() == 2 && "position".equals(name)) {
                    try {
                        i2 = Integer.parseInt(xmlPullParser.nextText());
                    } catch (NumberFormatException e) {
                    }
                } else if (xmlPullParser.getEventType() == 2 && Msg.TIME_REDIO.equals(name)) {
                    try {
                        i = Integer.parseInt(xmlPullParser.nextText());
                    } catch (NumberFormatException e2) {
                    }
                } else if (xmlPullParser.getEventType() == 3 && QueueUpdate.ELEMENT_NAME.equals(name)) {
                    obj = 1;
                }
            }
            return new QueueUpdate(i2, i);
        }
    }

    public QueueUpdate(int i, int i2) {
        this.position = i;
        this.remainingTime = i2;
    }

    public int getPosition() {
        return this.position;
    }

    public int getRemaingTime() {
        return this.remainingTime;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<queue-status xmlns=\"http://jabber.org/protocol/workgroup\">");
        if (this.position != -1) {
            stringBuilder.append("<position>").append(this.position).append("</position>");
        }
        if (this.remainingTime != -1) {
            stringBuilder.append("<time>").append(this.remainingTime).append("</time>");
        }
        stringBuilder.append("</queue-status>");
        return stringBuilder.toString();
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }
}
