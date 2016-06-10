package com.thoughtworks.xstream.io.xml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.xmlpull.v1.XmlPullParser;

public class QNameMap {
    private String defaultNamespace;
    private String defaultPrefix;
    private Map javaToQName;
    private Map qnameToJava;

    public QNameMap() {
        this.defaultPrefix = XmlPullParser.NO_NAMESPACE;
        this.defaultNamespace = XmlPullParser.NO_NAMESPACE;
    }

    public String getJavaClassName(QName qname) {
        if (this.qnameToJava != null) {
            String answer = (String) this.qnameToJava.get(qname);
            if (answer != null) {
                return answer;
            }
        }
        return qname.getLocalPart();
    }

    public QName getQName(String javaClassName) {
        if (this.javaToQName != null) {
            QName answer = (QName) this.javaToQName.get(javaClassName);
            if (answer != null) {
                return answer;
            }
        }
        return new QName(this.defaultNamespace, javaClassName, this.defaultPrefix);
    }

    public synchronized void registerMapping(QName qname, String javaClassName) {
        if (this.javaToQName == null) {
            this.javaToQName = Collections.synchronizedMap(new HashMap());
        }
        if (this.qnameToJava == null) {
            this.qnameToJava = Collections.synchronizedMap(new HashMap());
        }
        this.javaToQName.put(javaClassName, qname);
        this.qnameToJava.put(qname, javaClassName);
    }

    public synchronized void registerMapping(QName qname, Class type) {
        registerMapping(qname, type.getName());
    }

    public String getDefaultNamespace() {
        return this.defaultNamespace;
    }

    public void setDefaultNamespace(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }

    public String getDefaultPrefix() {
        return this.defaultPrefix;
    }

    public void setDefaultPrefix(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
    }
}
