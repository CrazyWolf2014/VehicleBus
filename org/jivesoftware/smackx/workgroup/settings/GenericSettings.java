package org.jivesoftware.smackx.workgroup.settings;

import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.xmlpull.v1.XmlPullParser;

public class GenericSettings extends IQ {
    public static final String ELEMENT_NAME = "generic-metadata";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private Map<String, String> map;
    private String query;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            if (xmlPullParser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            IQ genericSettings = new GenericSettings();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && "entry".equals(xmlPullParser.getName())) {
                    xmlPullParser.next();
                    String nextText = xmlPullParser.nextText();
                    xmlPullParser.next();
                    genericSettings.getMap().put(nextText, xmlPullParser.nextText());
                } else if (next == 3 && GenericSettings.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            return genericSettings;
        }
    }

    public GenericSettings() {
        this.map = new HashMap();
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String str) {
        this.query = str;
    }

    public Map<String, String> getMap() {
        return this.map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=");
        stringBuilder.append('\"');
        stringBuilder.append(NAMESPACE);
        stringBuilder.append('\"');
        stringBuilder.append(">");
        if (ModelUtil.hasLength(getQuery())) {
            stringBuilder.append("<query>" + getQuery() + "</query>");
        }
        stringBuilder.append("</").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }
}
