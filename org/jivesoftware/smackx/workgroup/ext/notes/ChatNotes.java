package org.jivesoftware.smackx.workgroup.ext.notes;

import com.cnmobi.im.dto.MessageVo;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class ChatNotes extends IQ {
    public static final String ELEMENT_NAME = "chat-notes";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String notes;
    private String sessionID;

    public static class Provider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            IQ chatNotes = new ChatNotes();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    if (xmlPullParser.getName().equals("sessionID")) {
                        chatNotes.setSessionID(xmlPullParser.nextText());
                    } else if (xmlPullParser.getName().equals(MessageVo.TYPE_TEXT)) {
                        chatNotes.setNotes(xmlPullParser.nextText().replaceAll("\\\\n", SpecilApiUtil.LINE_SEP));
                    }
                } else if (next == 3 && xmlPullParser.getName().equals(ChatNotes.ELEMENT_NAME)) {
                    obj = 1;
                }
            }
            return chatNotes;
        }
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String str) {
        this.sessionID = str;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String str) {
        this.notes = str;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE).append("\">");
        stringBuilder.append("<sessionID>").append(getSessionID()).append("</sessionID>");
        if (getNotes() != null) {
            stringBuilder.append("<notes>").append(getNotes()).append("</notes>");
        }
        stringBuilder.append("</").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }

    public static final String replace(String str, String str2, String str3) {
        if (str == null) {
            return null;
        }
        if (str3 == null) {
            return str;
        }
        int indexOf = str.indexOf(str2, 0);
        if (indexOf < 0) {
            return str;
        }
        char[] toCharArray = str.toCharArray();
        char[] toCharArray2 = str3.toCharArray();
        int length = str2.length();
        StringBuilder stringBuilder = new StringBuilder(toCharArray.length);
        stringBuilder.append(toCharArray, 0, indexOf).append(toCharArray2);
        indexOf += length;
        int i = indexOf;
        while (true) {
            i = str.indexOf(str2, i);
            if (i > 0) {
                stringBuilder.append(toCharArray, indexOf, i - indexOf).append(toCharArray2);
                indexOf = i + length;
                i = indexOf;
            } else {
                stringBuilder.append(toCharArray, indexOf, toCharArray.length - indexOf);
                return stringBuilder.toString();
            }
        }
    }
}
