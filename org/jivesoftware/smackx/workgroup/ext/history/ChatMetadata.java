package org.jivesoftware.smackx.workgroup.ext.history;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.util.MetaDataUtils;
import org.xmlpull.v1.XmlPullParser;

public class ChatMetadata extends IQ {
    public static final String ELEMENT_NAME = "chat-metadata";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private Map<String, List<String>> map;
    private String sessionID;

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            IQ chatMetadata = new ChatMetadata();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    if (xmlPullParser.getName().equals("sessionID")) {
                        chatMetadata.setSessionID(xmlPullParser.nextText());
                    } else if (xmlPullParser.getName().equals(MetaData.ELEMENT_NAME)) {
                        chatMetadata.setMetadata(MetaDataUtils.parseMetaData(xmlPullParser));
                    }
                } else if (next == 3 && xmlPullParser.getName().equals(ChatMetadata.ELEMENT_NAME)) {
                    obj = 1;
                }
            }
            return chatMetadata;
        }
    }

    public ChatMetadata() {
        this.map = new HashMap();
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String str) {
        this.sessionID = str;
    }

    public void setMetadata(Map<String, List<String>> map) {
        this.map = map;
    }

    public Map<String, List<String>> getMetadata() {
        return this.map;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE).append("\">");
        stringBuilder.append("<sessionID>").append(getSessionID()).append("</sessionID>");
        stringBuilder.append("</").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }
}
