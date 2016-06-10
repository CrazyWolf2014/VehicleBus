package org.jivesoftware.smackx.forward;

import com.launch.service.BundleBuilder;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.packet.DelayInfo;
import org.jivesoftware.smackx.provider.DelayInfoProvider;
import org.xmlpull.v1.XmlPullParser;

public class Forwarded implements PacketExtension {
    public static final String ELEMENT_NAME = "forwarded";
    public static final String NAMESPACE = "urn:xmpp:forward:0";
    private DelayInfo delay;
    private Packet forwardedPacket;

    public static class Provider implements PacketExtensionProvider {
        DelayInfoProvider dip;

        public Provider() {
            this.dip = new DelayInfoProvider();
        }

        public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
            Packet packet = null;
            Object obj = null;
            DelayInfo delayInfo = null;
            while (obj == null) {
                Packet packet2;
                DelayInfo delayInfo2;
                Object obj2;
                int next = xmlPullParser.next();
                if (next == 2) {
                    if (xmlPullParser.getName().equals("delay")) {
                        Object obj3 = obj;
                        packet2 = packet;
                        delayInfo2 = (DelayInfo) this.dip.parseExtension(xmlPullParser);
                        obj2 = obj3;
                    } else if (xmlPullParser.getName().equals(BundleBuilder.AskFromMessage)) {
                        delayInfo2 = delayInfo;
                        obj2 = obj;
                        packet2 = PacketParserUtils.parseMessage(xmlPullParser);
                    } else {
                        throw new Exception("Unsupported forwarded packet type: " + xmlPullParser.getName());
                    }
                } else if (next == 3 && xmlPullParser.getName().equals(Forwarded.ELEMENT_NAME)) {
                    obj2 = 1;
                    packet2 = packet;
                    delayInfo2 = delayInfo;
                } else {
                    obj2 = obj;
                    packet2 = packet;
                    delayInfo2 = delayInfo;
                }
                delayInfo = delayInfo2;
                packet = packet2;
                obj = obj2;
            }
            if (packet != null) {
                return new Forwarded(delayInfo, packet);
            }
            throw new Exception("forwarded extension must contain a packet");
        }
    }

    public Forwarded(DelayInfo delayInfo, Packet packet) {
        this.delay = delayInfo;
        this.forwardedPacket = packet;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
        if (this.delay != null) {
            stringBuilder.append(this.delay.toXML());
        }
        stringBuilder.append(this.forwardedPacket.toXML());
        stringBuilder.append("</").append(getElementName()).append(">");
        return stringBuilder.toString();
    }

    public Packet getForwardedPacket() {
        return this.forwardedPacket;
    }

    public DelayInfo getDelayInfo() {
        return this.delay;
    }
}
