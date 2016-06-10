package com.kenai.jbosh;

public final class BodyQName {
    static final String BOSH_NS_URI = "http://jabber.org/protocol/httpbind";
    private final QName qname;

    private BodyQName(QName qName) {
        this.qname = qName;
    }

    public static BodyQName create(String str, String str2) {
        return createWithPrefix(str, str2, null);
    }

    public static BodyQName createWithPrefix(String str, String str2, String str3) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("URI is required and may not be null/empty");
        } else if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("Local arg is required and may not be null/empty");
        } else if (str3 == null || str3.length() == 0) {
            return new BodyQName(new QName(str, str2));
        } else {
            return new BodyQName(new QName(str, str2, str3));
        }
    }

    public String getNamespaceURI() {
        return this.qname.getNamespaceURI();
    }

    public String getLocalPart() {
        return this.qname.getLocalPart();
    }

    public String getPrefix() {
        return this.qname.getPrefix();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BodyQName)) {
            return false;
        }
        return this.qname.equals(((BodyQName) obj).qname);
    }

    public int hashCode() {
        return this.qname.hashCode();
    }

    static BodyQName createBOSH(String str) {
        return createWithPrefix(BOSH_NS_URI, str, null);
    }

    boolean equalsQName(QName qName) {
        return this.qname.equals(qName);
    }
}
