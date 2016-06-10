package org.jivesoftware.smackx.workgroup.settings;

import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class ChatSettings extends IQ {
    public static final int BOT_SETTINGS = 2;
    public static final String ELEMENT_NAME = "chat-settings";
    public static final int IMAGE_SETTINGS = 0;
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    public static final int TEXT_SETTINGS = 1;
    private String key;
    private List<ChatSetting> settings;
    private int type;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            if (xmlPullParser.getEventType() != ChatSettings.BOT_SETTINGS) {
                throw new IllegalStateException("Parser not in proper position, or bad XML.");
            }
            IQ chatSettings = new ChatSettings();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == ChatSettings.BOT_SETTINGS && "chat-setting".equals(xmlPullParser.getName())) {
                    chatSettings.addSetting(parseChatSetting(xmlPullParser));
                } else if (next == 3 && ChatSettings.ELEMENT_NAME.equals(xmlPullParser.getName())) {
                    obj = ChatSettings.TEXT_SETTINGS;
                }
            }
            return chatSettings;
        }

        private ChatSetting parseChatSetting(XmlPullParser xmlPullParser) throws Exception {
            String str = null;
            int i = ChatSettings.IMAGE_SETTINGS;
            String str2 = null;
            int i2 = ChatSettings.IMAGE_SETTINGS;
            while (i2 == 0) {
                int next = xmlPullParser.next();
                if (next == ChatSettings.BOT_SETTINGS && SharedPref.KEY.equals(xmlPullParser.getName())) {
                    str2 = xmlPullParser.nextText();
                } else if (next == ChatSettings.BOT_SETTINGS && SharedPref.VALUE.equals(xmlPullParser.getName())) {
                    str = xmlPullParser.nextText();
                } else if (next == ChatSettings.BOT_SETTINGS && SharedPref.TYPE.equals(xmlPullParser.getName())) {
                    i = Integer.parseInt(xmlPullParser.nextText());
                } else if (next == 3 && "chat-setting".equals(xmlPullParser.getName())) {
                    i2 = ChatSettings.TEXT_SETTINGS;
                }
            }
            return new ChatSetting(str2, str, i);
        }
    }

    public ChatSettings() {
        this.type = -1;
        this.settings = new ArrayList();
    }

    public ChatSettings(String str) {
        this.type = -1;
        setKey(str);
    }

    public void setKey(String str) {
        this.key = str;
    }

    public void setType(int i) {
        this.type = i;
    }

    public void addSetting(ChatSetting chatSetting) {
        this.settings.add(chatSetting);
    }

    public Collection<ChatSetting> getSettings() {
        return this.settings;
    }

    public ChatSetting getChatSetting(String str) {
        Collection<ChatSetting> settings = getSettings();
        if (settings != null) {
            for (ChatSetting chatSetting : settings) {
                if (chatSetting.getKey().equals(str)) {
                    return chatSetting;
                }
            }
        }
        return null;
    }

    public ChatSetting getFirstEntry() {
        if (this.settings.size() > 0) {
            return (ChatSetting) this.settings.get(IMAGE_SETTINGS);
        }
        return null;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=");
        stringBuilder.append('\"');
        stringBuilder.append(NAMESPACE);
        stringBuilder.append('\"');
        if (this.key != null) {
            stringBuilder.append(" key=\"" + this.key + "\"");
        }
        if (this.type != -1) {
            stringBuilder.append(" type=\"" + this.type + "\"");
        }
        stringBuilder.append("></").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }
}
