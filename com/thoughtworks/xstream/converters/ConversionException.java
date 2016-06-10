package com.thoughtworks.xstream.converters;

import com.launch.service.BundleBuilder;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.core.util.OrderRetainingMap;
import java.util.Iterator;
import java.util.Map;

public class ConversionException extends XStreamException implements ErrorWriter {
    private static final String SEPARATOR = "\n-------------------------------";
    private Map stuff;

    public ConversionException(String msg, Throwable cause) {
        super(msg, cause);
        this.stuff = new OrderRetainingMap();
        if (msg != null) {
            add(BundleBuilder.AskFromMessage, msg);
        }
        if (cause != null) {
            add("cause-exception", cause.getClass().getName());
            add("cause-message", cause instanceof ConversionException ? ((ConversionException) cause).getShortMessage() : cause.getMessage());
        }
    }

    public ConversionException(String msg) {
        super(msg);
        this.stuff = new OrderRetainingMap();
    }

    public ConversionException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public String get(String errorKey) {
        return (String) this.stuff.get(errorKey);
    }

    public void add(String name, String information) {
        String key = name;
        int i = 0;
        while (this.stuff.containsKey(key)) {
            if (!information.equals((String) this.stuff.get(key))) {
                i++;
                key = name + "[" + i + "]";
            } else {
                return;
            }
        }
        this.stuff.put(key, information);
    }

    public void set(String name, String information) {
        String key = name;
        int i = 0;
        this.stuff.put(key, information);
        while (this.stuff.containsKey(key)) {
            if (i != 0) {
                this.stuff.remove(key);
            }
            i++;
            key = name + "[" + i + "]";
        }
    }

    public Iterator keys() {
        return this.stuff.keySet().iterator();
    }

    public String getMessage() {
        StringBuffer result = new StringBuffer();
        if (super.getMessage() != null) {
            result.append(super.getMessage());
        }
        if (!result.toString().endsWith(SEPARATOR)) {
            result.append("\n---- Debugging information ----");
        }
        Iterator iterator = keys();
        while (iterator.hasNext()) {
            String k = (String) iterator.next();
            String v = get(k);
            result.append('\n').append(k);
            result.append("                    ".substring(Math.min(20, k.length())));
            result.append(": ").append(v);
        }
        result.append(SEPARATOR);
        return result.toString();
    }

    public String getShortMessage() {
        return super.getMessage();
    }
}
