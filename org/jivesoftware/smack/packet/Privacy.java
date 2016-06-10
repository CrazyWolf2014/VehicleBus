package org.jivesoftware.smack.packet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Privacy extends IQ {
    private String activeName;
    private boolean declineActiveList;
    private boolean declineDefaultList;
    private String defaultName;
    private Map<String, List<PrivacyItem>> itemLists;

    public Privacy() {
        this.declineActiveList = false;
        this.declineDefaultList = false;
        this.itemLists = new HashMap();
    }

    public List<PrivacyItem> setPrivacyList(String str, List<PrivacyItem> list) {
        getItemLists().put(str, list);
        return list;
    }

    public List<PrivacyItem> setActivePrivacyList() {
        setActiveName(getDefaultName());
        return (List) getItemLists().get(getActiveName());
    }

    public void deletePrivacyList(String str) {
        getItemLists().remove(str);
        if (getDefaultName() != null && str.equals(getDefaultName())) {
            setDefaultName(null);
        }
    }

    public List<PrivacyItem> getActivePrivacyList() {
        if (getActiveName() == null) {
            return null;
        }
        return (List) getItemLists().get(getActiveName());
    }

    public List<PrivacyItem> getDefaultPrivacyList() {
        if (getDefaultName() == null) {
            return null;
        }
        return (List) getItemLists().get(getDefaultName());
    }

    public List<PrivacyItem> getPrivacyList(String str) {
        return (List) getItemLists().get(str);
    }

    public PrivacyItem getItem(String str, int i) {
        Iterator it = getPrivacyList(str).iterator();
        PrivacyItem privacyItem = null;
        while (privacyItem == null && it.hasNext()) {
            PrivacyItem privacyItem2 = (PrivacyItem) it.next();
            if (privacyItem2.getOrder() != i) {
                privacyItem2 = privacyItem;
            }
            privacyItem = privacyItem2;
        }
        return privacyItem;
    }

    public boolean changeDefaultList(String str) {
        if (!getItemLists().containsKey(str)) {
            return false;
        }
        setDefaultName(str);
        return true;
    }

    public void deleteList(String str) {
        getItemLists().remove(str);
    }

    public String getActiveName() {
        return this.activeName;
    }

    public void setActiveName(String str) {
        this.activeName = str;
    }

    public String getDefaultName() {
        return this.defaultName;
    }

    public void setDefaultName(String str) {
        this.defaultName = str;
    }

    public Map<String, List<PrivacyItem>> getItemLists() {
        return this.itemLists;
    }

    public boolean isDeclineActiveList() {
        return this.declineActiveList;
    }

    public void setDeclineActiveList(boolean z) {
        this.declineActiveList = z;
    }

    public boolean isDeclineDefaultList() {
        return this.declineDefaultList;
    }

    public void setDeclineDefaultList(boolean z) {
        this.declineDefaultList = z;
    }

    public Set<String> getPrivacyListNames() {
        return this.itemLists.keySet();
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<query xmlns=\"jabber:iq:privacy\">");
        if (isDeclineActiveList()) {
            stringBuilder.append("<active/>");
        } else if (getActiveName() != null) {
            stringBuilder.append("<active name=\"").append(getActiveName()).append("\"/>");
        }
        if (isDeclineDefaultList()) {
            stringBuilder.append("<default/>");
        } else if (getDefaultName() != null) {
            stringBuilder.append("<default name=\"").append(getDefaultName()).append("\"/>");
        }
        for (Entry entry : getItemLists().entrySet()) {
            String str = (String) entry.getKey();
            List<PrivacyItem> list = (List) entry.getValue();
            if (list.isEmpty()) {
                stringBuilder.append("<list name=\"").append(str).append("\"/>");
            } else {
                stringBuilder.append("<list name=\"").append(str).append("\">");
            }
            for (PrivacyItem toXML : list) {
                stringBuilder.append(toXML.toXML());
            }
            if (!list.isEmpty()) {
                stringBuilder.append("</list>");
            }
        }
        stringBuilder.append(getExtensionsXML());
        stringBuilder.append("</query>");
        return stringBuilder.toString();
    }
}
