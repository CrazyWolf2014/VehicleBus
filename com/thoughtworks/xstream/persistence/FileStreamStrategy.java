package com.thoughtworks.xstream.persistence;

import com.thoughtworks.xstream.XStream;
import java.io.File;

public class FileStreamStrategy extends AbstractFilePersistenceStrategy implements StreamStrategy {
    public FileStreamStrategy(File baseDirectory) {
        this(baseDirectory, new XStream());
    }

    public FileStreamStrategy(File baseDirectory, XStream xstream) {
        super(baseDirectory, xstream, null);
    }

    protected Object extractKey(String name) {
        String key = unescape(name.substring(0, name.length() - 4));
        return key.equals("\u0000") ? null : key;
    }

    protected String unescape(String name) {
        StringBuffer buffer = new StringBuffer();
        char lastC = '\uffff';
        int currentValue = -1;
        char[] array = name.toCharArray();
        for (char c : array) {
            if (c == '_' && currentValue != -1) {
                if (lastC == '_') {
                    buffer.append('_');
                } else {
                    buffer.append((char) currentValue);
                }
                currentValue = -1;
            } else if (c == '_') {
                currentValue = 0;
            } else if (currentValue != -1) {
                currentValue = (currentValue * 16) + Integer.parseInt(String.valueOf(c), 16);
            } else {
                buffer.append(c);
            }
            lastC = c;
        }
        return buffer.toString();
    }

    protected String getName(Object key) {
        return escape(key == null ? "\u0000" : key.toString()) + ".xml";
    }

    protected String escape(String key) {
        StringBuffer buffer = new StringBuffer();
        char[] array = key.toCharArray();
        for (char c : array) {
            if (Character.isDigit(c) || ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
                buffer.append(c);
            } else if (c == '_') {
                buffer.append("__");
            } else {
                buffer.append("_" + Integer.toHexString(c) + "_");
            }
        }
        return buffer.toString();
    }
}
