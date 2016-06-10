package org.jivesoftware.smackx.workgroup;

import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.workgroup.util.MetaDataUtils;

public class MetaData implements PacketExtension {
    public static final String ELEMENT_NAME = "metadata";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private Map<String, List<String>> metaData;

    public MetaData(Map<String, List<String>> map) {
        this.metaData = map;
    }

    public Map<String, List<String>> getMetaData() {
        return this.metaData;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        return MetaDataUtils.serializeMetaData(getMetaData());
    }
}
