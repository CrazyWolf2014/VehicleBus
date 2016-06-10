package org.jivesoftware.smackx.pubsub;

import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smack.packet.PacketExtension;

public class Affiliation implements PacketExtension {
    protected String jid;
    protected String node;
    protected Type type;

    public enum Type {
        member,
        none,
        outcast,
        owner,
        publisher
    }

    public Affiliation(String str, Type type) {
        this(str, null, type);
    }

    public Affiliation(String str, String str2, Type type) {
        this.jid = str;
        this.node = str2;
        this.type = type;
    }

    public String getJid() {
        return this.jid;
    }

    public String getNode() {
        return this.node;
    }

    public Type getType() {
        return this.type;
    }

    public String getElementName() {
        return "affiliation";
    }

    public String getNamespace() {
        return null;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder("<");
        stringBuilder.append(getElementName());
        if (this.node != null) {
            appendAttribute(stringBuilder, "node", this.node);
        }
        appendAttribute(stringBuilder, "jid", this.jid);
        appendAttribute(stringBuilder, "affiliation", this.type.toString());
        stringBuilder.append("/>");
        return stringBuilder.toString();
    }

    private void appendAttribute(StringBuilder stringBuilder, String str, String str2) {
        stringBuilder.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuilder.append(str);
        stringBuilder.append("='");
        stringBuilder.append(str2);
        stringBuilder.append("'");
    }
}
