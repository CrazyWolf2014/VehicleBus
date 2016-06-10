package com.kenai.jbosh;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class QName implements Serializable {
    private static final String emptyString;
    private String localPart;
    private String namespaceURI;
    private String prefix;

    static {
        emptyString = XmlPullParser.NO_NAMESPACE.intern();
    }

    public QName(String str) {
        this(emptyString, str, emptyString);
    }

    public QName(String str, String str2) {
        this(str, str2, emptyString);
    }

    public QName(String str, String str2, String str3) {
        this.namespaceURI = str == null ? emptyString : str.intern();
        if (str2 == null) {
            throw new IllegalArgumentException("invalid QName local part");
        }
        this.localPart = str2.intern();
        if (str3 == null) {
            throw new IllegalArgumentException("invalid QName prefix");
        }
        this.prefix = str3.intern();
    }

    public String getNamespaceURI() {
        return this.namespaceURI;
    }

    public String getLocalPart() {
        return this.localPart;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String toString() {
        return this.namespaceURI == emptyString ? this.localPart : '{' + this.namespaceURI + '}' + this.localPart;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof QName)) {
            return false;
        }
        if (this.namespaceURI == ((QName) obj).namespaceURI && this.localPart == ((QName) obj).localPart) {
            return true;
        }
        return false;
    }

    public static QName valueOf(String str) {
        if (str == null || str.equals(XmlPullParser.NO_NAMESPACE)) {
            throw new IllegalArgumentException("invalid QName literal");
        } else if (str.charAt(0) != '{') {
            return new QName(str);
        } else {
            int indexOf = str.indexOf(Service.LOCUS_MAP);
            if (indexOf == -1) {
                throw new IllegalArgumentException("invalid QName literal");
            } else if (indexOf != str.length() - 1) {
                return new QName(str.substring(1, indexOf), str.substring(indexOf + 1));
            } else {
                throw new IllegalArgumentException("invalid QName literal");
            }
        }
    }

    public final int hashCode() {
        return this.namespaceURI.hashCode() ^ this.localPart.hashCode();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.namespaceURI = this.namespaceURI.intern();
        this.localPart = this.localPart.intern();
        this.prefix = this.prefix.intern();
    }
}
