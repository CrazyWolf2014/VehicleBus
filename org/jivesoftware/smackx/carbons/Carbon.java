package org.jivesoftware.smackx.carbons;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.forward.Forwarded;
import org.xmlpull.v1.XmlPullParser;

public class Carbon implements PacketExtension {
    public static final String NAMESPACE = "urn:xmpp:carbons:2";
    private Direction dir;
    private Forwarded fwd;

    public enum Direction {
        received,
        sent
    }

    public static class Private implements PacketExtension {
        public static final String ELEMENT = "private";

        public String getElementName() {
            return ELEMENT;
        }

        public String getNamespace() {
            return Carbon.NAMESPACE;
        }

        public String toXML() {
            return "<private xmlns=\"urn:xmpp:carbons:2\"/>";
        }
    }

    public static class Provider implements PacketExtensionProvider {
        public PacketExtension parseExtension(XmlPullParser xmlPullParser) throws Exception {
            Direction valueOf = Direction.valueOf(xmlPullParser.getName());
            Forwarded forwarded = null;
            Object obj = null;
            while (obj == null) {
                Forwarded forwarded2;
                Object obj2;
                int next = xmlPullParser.next();
                if (next == 2 && xmlPullParser.getName().equals(Forwarded.ELEMENT_NAME)) {
                    Object obj3 = obj;
                    forwarded2 = (Forwarded) new org.jivesoftware.smackx.forward.Forwarded.Provider().parseExtension(xmlPullParser);
                    obj2 = obj3;
                } else if (next == 3 && valueOf == Direction.valueOf(xmlPullParser.getName())) {
                    obj2 = 1;
                    forwarded2 = forwarded;
                } else {
                    obj2 = obj;
                    forwarded2 = forwarded;
                }
                forwarded = forwarded2;
                obj = obj2;
            }
            if (forwarded != null) {
                return new Carbon(valueOf, forwarded);
            }
            throw new Exception("sent/received must contain exactly one <forwarded> tag");
        }
    }

    public Carbon(Direction direction, Forwarded forwarded) {
        this.dir = direction;
        this.fwd = forwarded;
    }

    public Direction getDirection() {
        return this.dir;
    }

    public Forwarded getForwarded() {
        return this.fwd;
    }

    public String getElementName() {
        return this.dir.toString();
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
        stringBuilder.append(this.fwd.toXML());
        stringBuilder.append("</").append(getElementName()).append(">");
        return stringBuilder.toString();
    }
}
