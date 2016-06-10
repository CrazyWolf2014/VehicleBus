package org.jivesoftware.smack.packet;

public class PrivacyItem {
    private boolean allow;
    private boolean filterIQ;
    private boolean filterMessage;
    private boolean filterPresence_in;
    private boolean filterPresence_out;
    private int order;
    private PrivacyRule rule;

    public static class PrivacyRule {
        public static final String SUBSCRIPTION_BOTH = "both";
        public static final String SUBSCRIPTION_FROM = "from";
        public static final String SUBSCRIPTION_NONE = "none";
        public static final String SUBSCRIPTION_TO = "to";
        private Type type;
        private String value;

        protected static PrivacyRule fromString(String str) {
            if (str == null) {
                return null;
            }
            PrivacyRule privacyRule = new PrivacyRule();
            privacyRule.setType(Type.valueOf(str.toLowerCase()));
            return privacyRule;
        }

        public Type getType() {
            return this.type;
        }

        private void setType(Type type) {
            this.type = type;
        }

        public String getValue() {
            return this.value;
        }

        protected void setValue(String str) {
            if (isSuscription()) {
                setSuscriptionValue(str);
            } else {
                this.value = str;
            }
        }

        private void setSuscriptionValue(String str) {
            String str2;
            if (str == null) {
            }
            if (SUBSCRIPTION_BOTH.equalsIgnoreCase(str)) {
                str2 = SUBSCRIPTION_BOTH;
            } else if (SUBSCRIPTION_TO.equalsIgnoreCase(str)) {
                str2 = SUBSCRIPTION_TO;
            } else if (SUBSCRIPTION_FROM.equalsIgnoreCase(str)) {
                str2 = SUBSCRIPTION_FROM;
            } else if (SUBSCRIPTION_NONE.equalsIgnoreCase(str)) {
                str2 = SUBSCRIPTION_NONE;
            } else {
                str2 = null;
            }
            this.value = str2;
        }

        public boolean isSuscription() {
            return getType() == Type.subscription;
        }
    }

    public enum Type {
        group,
        jid,
        subscription
    }

    public PrivacyItem(String str, boolean z, int i) {
        this.filterIQ = false;
        this.filterMessage = false;
        this.filterPresence_in = false;
        this.filterPresence_out = false;
        setRule(PrivacyRule.fromString(str));
        setAllow(z);
        setOrder(i);
    }

    public boolean isAllow() {
        return this.allow;
    }

    private void setAllow(boolean z) {
        this.allow = z;
    }

    public boolean isFilterIQ() {
        return this.filterIQ;
    }

    public void setFilterIQ(boolean z) {
        this.filterIQ = z;
    }

    public boolean isFilterMessage() {
        return this.filterMessage;
    }

    public void setFilterMessage(boolean z) {
        this.filterMessage = z;
    }

    public boolean isFilterPresence_in() {
        return this.filterPresence_in;
    }

    public void setFilterPresence_in(boolean z) {
        this.filterPresence_in = z;
    }

    public boolean isFilterPresence_out() {
        return this.filterPresence_out;
    }

    public void setFilterPresence_out(boolean z) {
        this.filterPresence_out = z;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int i) {
        this.order = i;
    }

    public void setValue(String str) {
        if (getRule() != null || str != null) {
            getRule().setValue(str);
        }
    }

    public Type getType() {
        if (getRule() == null) {
            return null;
        }
        return getRule().getType();
    }

    public String getValue() {
        if (getRule() == null) {
            return null;
        }
        return getRule().getValue();
    }

    public boolean isFilterEverything() {
        return (isFilterIQ() || isFilterMessage() || isFilterPresence_in() || isFilterPresence_out()) ? false : true;
    }

    private PrivacyRule getRule() {
        return this.rule;
    }

    private void setRule(PrivacyRule privacyRule) {
        this.rule = privacyRule;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<item");
        if (isAllow()) {
            stringBuilder.append(" action=\"allow\"");
        } else {
            stringBuilder.append(" action=\"deny\"");
        }
        stringBuilder.append(" order=\"").append(getOrder()).append("\"");
        if (getType() != null) {
            stringBuilder.append(" type=\"").append(getType()).append("\"");
        }
        if (getValue() != null) {
            stringBuilder.append(" value=\"").append(getValue()).append("\"");
        }
        if (isFilterEverything()) {
            stringBuilder.append("/>");
        } else {
            stringBuilder.append(">");
            if (isFilterIQ()) {
                stringBuilder.append("<iq/>");
            }
            if (isFilterMessage()) {
                stringBuilder.append("<message/>");
            }
            if (isFilterPresence_in()) {
                stringBuilder.append("<presence-in/>");
            }
            if (isFilterPresence_out()) {
                stringBuilder.append("<presence-out/>");
            }
            stringBuilder.append("</item>");
        }
        return stringBuilder.toString();
    }
}
