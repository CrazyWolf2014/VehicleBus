package org.jivesoftware.smackx.workgroup.settings;

public class ChatSetting {
    private String key;
    private int type;
    private String value;

    public ChatSetting(String str, String str2, int i) {
        setKey(str);
        setValue(str2);
        setType(i);
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }
}
