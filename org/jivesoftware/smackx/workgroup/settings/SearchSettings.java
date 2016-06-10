package org.jivesoftware.smackx.workgroup.settings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.xmlpull.v1.XmlPullParser;

public class SearchSettings extends IQ {
    public static final String ELEMENT_NAME = "search-settings";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String forumsLocation;
    private String kbLocation;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            String str = null;
            if (xmlPullParser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            IQ searchSettings = new SearchSettings();
            Object obj = null;
            String str2 = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && "forums".equals(xmlPullParser.getName())) {
                    str = xmlPullParser.nextText();
                } else if (next == 2 && "kb".equals(xmlPullParser.getName())) {
                    str2 = xmlPullParser.nextText();
                } else if (next == 3 && SearchSettings.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            searchSettings.setForumsLocation(str);
            searchSettings.setKbLocation(str2);
            return searchSettings;
        }
    }

    public boolean isSearchEnabled() {
        return ModelUtil.hasLength(getForumsLocation()) && ModelUtil.hasLength(getKbLocation());
    }

    public String getForumsLocation() {
        return this.forumsLocation;
    }

    public void setForumsLocation(String str) {
        this.forumsLocation = str;
    }

    public String getKbLocation() {
        return this.kbLocation;
    }

    public void setKbLocation(String str) {
        this.kbLocation = str;
    }

    public boolean hasKB() {
        return ModelUtil.hasLength(getKbLocation());
    }

    public boolean hasForums() {
        return ModelUtil.hasLength(getForumsLocation());
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=");
        stringBuilder.append('\"');
        stringBuilder.append(NAMESPACE);
        stringBuilder.append('\"');
        stringBuilder.append("></").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }
}
