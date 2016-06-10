package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class SharedGroupsInfo extends IQ {
    private List<String> groups;

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            IQ sharedGroupsInfo = new SharedGroupsInfo();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && xmlPullParser.getName().equals("group")) {
                    sharedGroupsInfo.getGroups().add(xmlPullParser.nextText());
                } else if (next == 3 && xmlPullParser.getName().equals("sharedgroup")) {
                    obj = 1;
                }
            }
            return sharedGroupsInfo;
        }
    }

    public SharedGroupsInfo() {
        this.groups = new ArrayList();
    }

    public List<String> getGroups() {
        return this.groups;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<sharedgroup xmlns=\"http://www.jivesoftware.org/protocol/sharedgroup\">");
        for (String append : this.groups) {
            stringBuilder.append("<group>").append(append).append("</group>");
        }
        stringBuilder.append("</sharedgroup>");
        return stringBuilder.toString();
    }
}
