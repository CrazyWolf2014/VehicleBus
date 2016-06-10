package com.thoughtworks.xstream.persistence;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;

public class FilePersistenceStrategy extends AbstractFilePersistenceStrategy {
    private final String illegalChars;

    public FilePersistenceStrategy(File baseDirectory) {
        this(baseDirectory, new XStream(new DomDriver()));
    }

    public FilePersistenceStrategy(File baseDirectory, XStream xstream) {
        this(baseDirectory, xstream, "utf-8", "<>?:/\\\"|*%");
    }

    public FilePersistenceStrategy(File baseDirectory, XStream xstream, String encoding, String illegalChars) {
        super(baseDirectory, xstream, encoding);
        this.illegalChars = illegalChars;
    }

    protected boolean isValid(File dir, String name) {
        return super.isValid(dir, name) && name.indexOf(64) > 0;
    }

    protected Object extractKey(String name) {
        String key = unescape(name.substring(0, name.length() - 4));
        if ("null@null".equals(key)) {
            return null;
        }
        int idx = key.indexOf(64);
        if (idx < 0) {
            throw new StreamException("Not a valid key: " + key);
        }
        Class type = getMapper().realClass(key.substring(0, idx));
        Converter converter = getConverterLookup().lookupConverterForType(type);
        if (converter instanceof SingleValueConverter) {
            return ((SingleValueConverter) converter).fromString(key.substring(idx + 1));
        }
        throw new StreamException("No SingleValueConverter for type " + type.getName() + " available");
    }

    protected String unescape(String name) {
        StringBuffer buffer = new StringBuffer();
        int idx = name.indexOf(37);
        while (idx >= 0) {
            buffer.append(name.substring(0, idx));
            buffer.append((char) Integer.parseInt(name.substring(idx + 1, idx + 3), 16));
            name = name.substring(idx + 3);
            idx = name.indexOf(37);
        }
        buffer.append(name);
        return buffer.toString();
    }

    protected String getName(Object key) {
        if (key == null) {
            return "null@null.xml";
        }
        Class type = key.getClass();
        Converter converter = getConverterLookup().lookupConverterForType(type);
        if (converter instanceof SingleValueConverter) {
            return getMapper().serializedClass(type) + '@' + escape(((SingleValueConverter) converter).toString(key)) + ".xml";
        }
        throw new StreamException("No SingleValueConverter for type " + type.getName() + " available");
    }

    protected String escape(String key) {
        StringBuffer buffer = new StringBuffer();
        char[] array = key.toCharArray();
        for (char c : array) {
            if (c < ' ' || this.illegalChars.indexOf(c) >= 0) {
                buffer.append("%" + Integer.toHexString(c).toUpperCase());
            } else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }
}
