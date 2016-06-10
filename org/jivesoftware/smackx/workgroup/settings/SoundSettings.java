package org.jivesoftware.smackx.workgroup.settings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;

public class SoundSettings extends IQ {
    public static final String ELEMENT_NAME = "sound-settings";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String incomingSound;
    private String outgoingSound;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            if (xmlPullParser.getEventType() != 2) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            IQ soundSettings = new SoundSettings();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2 && "outgoingSound".equals(xmlPullParser.getName())) {
                    soundSettings.setOutgoingSound(xmlPullParser.nextText());
                } else if (next == 2 && "incomingSound".equals(xmlPullParser.getName())) {
                    soundSettings.setIncomingSound(xmlPullParser.nextText());
                } else if (next == 3 && SoundSettings.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    obj = 1;
                }
            }
            return soundSettings;
        }
    }

    public void setOutgoingSound(String str) {
        this.outgoingSound = str;
    }

    public void setIncomingSound(String str) {
        this.incomingSound = str;
    }

    public byte[] getIncomingSoundBytes() {
        return StringUtils.decodeBase64(this.incomingSound);
    }

    public byte[] getOutgoingSoundBytes() {
        return StringUtils.decodeBase64(this.outgoingSound);
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
