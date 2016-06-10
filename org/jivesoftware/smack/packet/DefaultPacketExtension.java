package org.jivesoftware.smack.packet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultPacketExtension implements PacketExtension {
    private String elementName;
    private Map<String, String> map;
    private String namespace;

    public DefaultPacketExtension(String str, String str2) {
        this.elementName = str;
        this.namespace = str2;
    }

    public String getElementName() {
        return this.elementName;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(this.elementName).append(" xmlns=\"").append(this.namespace).append("\">");
        for (String str : getNames()) {
            String value = getValue(str);
            stringBuilder.append("<").append(str).append(">");
            stringBuilder.append(value);
            stringBuilder.append("</").append(str).append(">");
        }
        stringBuilder.append("</").append(this.elementName).append(">");
        return stringBuilder.toString();
    }

    public synchronized Collection<String> getNames() {
        Collection<String> emptySet;
        if (this.map == null) {
            emptySet = Collections.emptySet();
        } else {
            emptySet = Collections.unmodifiableSet(new HashMap(this.map).keySet());
        }
        return emptySet;
    }

    public synchronized String getValue(String str) {
        String str2;
        if (this.map == null) {
            str2 = null;
        } else {
            str2 = (String) this.map.get(str);
        }
        return str2;
    }

    public synchronized void setValue(String str, String str2) {
        if (this.map == null) {
            this.map = new HashMap();
        }
        this.map.put(str, str2);
    }
}
