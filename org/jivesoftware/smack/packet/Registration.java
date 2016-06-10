package org.jivesoftware.smack.packet;

import com.tencent.mm.sdk.plugin.BaseProfile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registration extends IQ {
    private Map<String, String> attributes;
    private String instructions;
    private boolean registered;
    private boolean remove;
    private List<String> requiredFields;

    public Registration() {
        this.instructions = null;
        this.attributes = new HashMap();
        this.requiredFields = new ArrayList();
        this.registered = false;
        this.remove = false;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String str) {
        this.instructions = str;
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, String> map) {
        this.attributes = map;
    }

    public List<String> getRequiredFields() {
        return this.requiredFields;
    }

    public void addAttribute(String str, String str2) {
        this.attributes.put(str, str2);
    }

    public void setRegistered(boolean z) {
        this.registered = z;
    }

    public boolean isRegistered() {
        return this.registered;
    }

    public String getField(String str) {
        return (String) this.attributes.get(str);
    }

    public List<String> getFieldNames() {
        return new ArrayList(this.attributes.keySet());
    }

    public void setUsername(String str) {
        this.attributes.put(BaseProfile.COL_USERNAME, str);
    }

    public void setPassword(String str) {
        this.attributes.put("password", str);
    }

    public void setRemove(boolean z) {
        this.remove = z;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<query xmlns=\"jabber:iq:register\">");
        if (!(this.instructions == null || this.remove)) {
            stringBuilder.append("<instructions>").append(this.instructions).append("</instructions>");
        }
        if (this.attributes != null && this.attributes.size() > 0 && !this.remove) {
            for (String str : this.attributes.keySet()) {
                String str2 = (String) this.attributes.get(str);
                stringBuilder.append("<").append(str).append(">");
                stringBuilder.append(str2);
                stringBuilder.append("</").append(str).append(">");
            }
        } else if (this.remove) {
            stringBuilder.append("</remove>");
        }
        stringBuilder.append(getExtensionsXML());
        stringBuilder.append("</query>");
        return stringBuilder.toString();
    }
}
