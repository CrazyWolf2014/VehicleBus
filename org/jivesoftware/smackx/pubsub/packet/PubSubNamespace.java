package org.jivesoftware.smackx.pubsub.packet;

public enum PubSubNamespace {
    BASIC(null),
    ERROR("errors"),
    EVENT("event"),
    OWNER("owner");
    
    private String fragment;

    private PubSubNamespace(String str) {
        this.fragment = str;
    }

    public String getXmlns() {
        String str = "http://jabber.org/protocol/pubsub";
        if (this.fragment != null) {
            return str + '#' + this.fragment;
        }
        return str;
    }

    public String getFragment() {
        return this.fragment;
    }

    public static PubSubNamespace valueOfFromXmlns(String str) {
        if (str.lastIndexOf(35) != -1) {
            return valueOf(str.substring(str.lastIndexOf(35) + 1).toUpperCase());
        }
        return BASIC;
    }
}
