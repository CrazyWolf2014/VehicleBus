package org.jivesoftware.smackx.pubsub;

import org.jivesoftware.smack.packet.PacketExtension;
import org.xmlpull.v1.XmlPullParser;

public class NodeExtension implements PacketExtension {
    private PubSubElementType element;
    private String node;

    public NodeExtension(PubSubElementType pubSubElementType, String str) {
        this.element = pubSubElementType;
        this.node = str;
    }

    public NodeExtension(PubSubElementType pubSubElementType) {
        this(pubSubElementType, null);
    }

    public String getNode() {
        return this.node;
    }

    public String getElementName() {
        return this.element.getElementName();
    }

    public String getNamespace() {
        return this.element.getNamespace().getXmlns();
    }

    public String toXML() {
        return '<' + getElementName() + (this.node == null ? XmlPullParser.NO_NAMESPACE : " node='" + this.node + '\'') + "/>";
    }

    public String toString() {
        return getClass().getName() + " - content [" + toXML() + "]";
    }
}
