package com.thoughtworks.xstream.converters.basic;

import com.ifoer.mine.Contact;

public class BooleanConverter extends AbstractSingleValueConverter {
    public static final BooleanConverter BINARY;
    public static final BooleanConverter TRUE_FALSE;
    public static final BooleanConverter YES_NO;
    private final boolean caseSensitive;
    private final String negative;
    private final String positive;

    static {
        TRUE_FALSE = new BooleanConverter("true", "false", false);
        YES_NO = new BooleanConverter("yes", "no", false);
        BINARY = new BooleanConverter(Contact.RELATION_FRIEND, Contact.RELATION_ASK, true);
    }

    public BooleanConverter(String positive, String negative, boolean caseSensitive) {
        this.positive = positive;
        this.negative = negative;
        this.caseSensitive = caseSensitive;
    }

    public BooleanConverter() {
        this("true", "false", false);
    }

    public boolean shouldConvert(Class type, Object value) {
        return true;
    }

    public boolean canConvert(Class type) {
        return type.equals(Boolean.TYPE) || type.equals(Boolean.class);
    }

    public Object fromString(String str) {
        return this.caseSensitive ? this.positive.equals(str) ? Boolean.TRUE : Boolean.FALSE : this.positive.equalsIgnoreCase(str) ? Boolean.TRUE : Boolean.FALSE;
    }

    public String toString(Object obj) {
        Boolean value = (Boolean) obj;
        if (obj == null) {
            return null;
        }
        return value.booleanValue() ? this.positive : this.negative;
    }
}
