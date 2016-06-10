package org.jivesoftware.smackx.workgroup.settings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.xmlpull.v1.XmlPullParser;

public class OfflineSettings extends IQ {
    public static final String ELEMENT_NAME = "offline-settings";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String emailAddress;
    private String offlineText;
    private String redirectURL;
    private String subject;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            String str = null;
            if (xmlPullParser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            IQ offlineSettings = new OfflineSettings();
            Object obj = null;
            String str2 = null;
            String str3 = null;
            String str4 = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && "redirectPage".equals(xmlPullParser.getName())) {
                    str4 = xmlPullParser.nextText();
                } else if (next == 2 && "subject".equals(xmlPullParser.getName())) {
                    str3 = xmlPullParser.nextText();
                } else if (next == 2 && "offlineText".equals(xmlPullParser.getName())) {
                    str2 = xmlPullParser.nextText();
                } else if (next == 2 && "emailAddress".equals(xmlPullParser.getName())) {
                    str = xmlPullParser.nextText();
                } else if (next == 3 && OfflineSettings.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            offlineSettings.setEmailAddress(str);
            offlineSettings.setRedirectURL(str4);
            offlineSettings.setSubject(str3);
            offlineSettings.setOfflineText(str2);
            return offlineSettings;
        }
    }

    public String getRedirectURL() {
        if (ModelUtil.hasLength(this.redirectURL)) {
            return this.redirectURL;
        }
        return XmlPullParser.NO_NAMESPACE;
    }

    public void setRedirectURL(String str) {
        this.redirectURL = str;
    }

    public String getOfflineText() {
        if (ModelUtil.hasLength(this.offlineText)) {
            return this.offlineText;
        }
        return XmlPullParser.NO_NAMESPACE;
    }

    public void setOfflineText(String str) {
        this.offlineText = str;
    }

    public String getEmailAddress() {
        if (ModelUtil.hasLength(this.emailAddress)) {
            return this.emailAddress;
        }
        return XmlPullParser.NO_NAMESPACE;
    }

    public void setEmailAddress(String str) {
        this.emailAddress = str;
    }

    public String getSubject() {
        if (ModelUtil.hasLength(this.subject)) {
            return this.subject;
        }
        return XmlPullParser.NO_NAMESPACE;
    }

    public void setSubject(String str) {
        this.subject = str;
    }

    public boolean redirects() {
        return ModelUtil.hasLength(getRedirectURL());
    }

    public boolean isConfigured() {
        return ModelUtil.hasLength(getEmailAddress()) && ModelUtil.hasLength(getSubject()) && ModelUtil.hasLength(getOfflineText());
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
