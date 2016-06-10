package com.thoughtworks.xstream.mapper;

import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

public class AbstractXmlFriendlyMapper extends MapperWrapper {
    private char dollarReplacementInClass;
    private String dollarReplacementInField;
    private String noPackagePrefix;
    private String underscoreReplacementInField;

    protected AbstractXmlFriendlyMapper(Mapper wrapped) {
        super(wrapped);
        this.dollarReplacementInClass = SignatureVisitor.SUPER;
        this.dollarReplacementInField = "_DOLLAR_";
        this.underscoreReplacementInField = "__";
        this.noPackagePrefix = "default";
    }

    protected String escapeClassName(String className) {
        className = className.replace('$', this.dollarReplacementInClass);
        if (className.charAt(0) == this.dollarReplacementInClass) {
            return this.noPackagePrefix + className;
        }
        return className;
    }

    protected String unescapeClassName(String className) {
        if (className.startsWith(this.noPackagePrefix + this.dollarReplacementInClass)) {
            className = className.substring(this.noPackagePrefix.length());
        }
        return className.replace(this.dollarReplacementInClass, '$');
    }

    protected String escapeFieldName(String fieldName) {
        StringBuffer result = new StringBuffer();
        int length = fieldName.length();
        for (int i = 0; i < length; i++) {
            char c = fieldName.charAt(i);
            if (c == '$') {
                result.append(this.dollarReplacementInField);
            } else if (c == '_') {
                result.append(this.underscoreReplacementInField);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    protected String unescapeFieldName(String xmlName) {
        StringBuffer result = new StringBuffer();
        int length = xmlName.length();
        int i = 0;
        while (i < length) {
            char c = xmlName.charAt(i);
            if (stringFoundAt(xmlName, i, this.underscoreReplacementInField)) {
                i += this.underscoreReplacementInField.length() - 1;
                result.append('_');
            } else if (stringFoundAt(xmlName, i, this.dollarReplacementInField)) {
                i += this.dollarReplacementInField.length() - 1;
                result.append('$');
            } else {
                result.append(c);
            }
            i++;
        }
        return result.toString();
    }

    private boolean stringFoundAt(String name, int i, String replacement) {
        if (name.length() < replacement.length() + i || !name.substring(i, replacement.length() + i).equals(replacement)) {
            return false;
        }
        return true;
    }
}
