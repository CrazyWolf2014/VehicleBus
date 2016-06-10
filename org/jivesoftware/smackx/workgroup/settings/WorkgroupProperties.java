package org.jivesoftware.smackx.workgroup.settings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.xmlpull.v1.XmlPullParser;

public class WorkgroupProperties extends IQ {
    public static final String ELEMENT_NAME = "workgroup-properties";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private boolean authRequired;
    private String email;
    private String fullName;
    private String jid;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            if (xmlPullParser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            IQ workgroupProperties = new WorkgroupProperties();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && "authRequired".equals(xmlPullParser.getName())) {
                    workgroupProperties.setAuthRequired(new Boolean(xmlPullParser.nextText()).booleanValue());
                } else if (next == 2 && "email".equals(xmlPullParser.getName())) {
                    workgroupProperties.setEmail(xmlPullParser.nextText());
                } else if (next == 2 && "name".equals(xmlPullParser.getName())) {
                    workgroupProperties.setFullName(xmlPullParser.nextText());
                } else if (next == 3 && WorkgroupProperties.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            return workgroupProperties;
        }
    }

    public boolean isAuthRequired() {
        return this.authRequired;
    }

    public void setAuthRequired(boolean z) {
        this.authRequired = z;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String str) {
        this.fullName = str;
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String str) {
        this.jid = str;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=");
        stringBuilder.append('\"');
        stringBuilder.append(NAMESPACE);
        stringBuilder.append('\"');
        if (ModelUtil.hasLength(getJid())) {
            stringBuilder.append("jid=\"" + getJid() + "\" ");
        }
        stringBuilder.append("></").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }
}
