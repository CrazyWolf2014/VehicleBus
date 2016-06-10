package org.xbill.DNS;

import java.util.HashMap;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

class Mnemonic {
    static final int CASE_LOWER = 3;
    static final int CASE_SENSITIVE = 1;
    static final int CASE_UPPER = 2;
    private static Integer[] cachedInts;
    private String description;
    private int max;
    private boolean numericok;
    private String prefix;
    private HashMap strings;
    private HashMap values;
    private int wordcase;

    static {
        cachedInts = new Integer[64];
        for (int i = 0; i < cachedInts.length; i += CASE_SENSITIVE) {
            cachedInts[i] = new Integer(i);
        }
    }

    public Mnemonic(String str, int i) {
        this.description = str;
        this.wordcase = i;
        this.strings = new HashMap();
        this.values = new HashMap();
        this.max = Integer.MAX_VALUE;
    }

    public void setMaximum(int i) {
        this.max = i;
    }

    public void setPrefix(String str) {
        this.prefix = sanitize(str);
    }

    public void setNumericAllowed(boolean z) {
        this.numericok = z;
    }

    public static Integer toInteger(int i) {
        if (i < 0 || i >= cachedInts.length) {
            return new Integer(i);
        }
        return cachedInts[i];
    }

    public void check(int i) {
        if (i < 0 || i > this.max) {
            throw new IllegalArgumentException(this.description + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + i + "is out of range");
        }
    }

    private String sanitize(String str) {
        if (this.wordcase == CASE_UPPER) {
            return str.toUpperCase();
        }
        if (this.wordcase == CASE_LOWER) {
            return str.toLowerCase();
        }
        return str;
    }

    private int parseNumeric(String str) {
        try {
            int parseInt = Integer.parseInt(str);
            if (parseInt >= 0 && parseInt <= this.max) {
                return parseInt;
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    public void add(int i, String str) {
        check(i);
        Integer toInteger = toInteger(i);
        String sanitize = sanitize(str);
        this.strings.put(sanitize, toInteger);
        this.values.put(toInteger, sanitize);
    }

    public void addAlias(int i, String str) {
        check(i);
        Integer toInteger = toInteger(i);
        this.strings.put(sanitize(str), toInteger);
    }

    public void addAll(Mnemonic mnemonic) {
        if (this.wordcase != mnemonic.wordcase) {
            throw new IllegalArgumentException(mnemonic.description + ": wordcases do not match");
        }
        this.strings.putAll(mnemonic.strings);
        this.values.putAll(mnemonic.values);
    }

    public String getText(int i) {
        check(i);
        String str = (String) this.values.get(toInteger(i));
        if (str != null) {
            return str;
        }
        str = Integer.toString(i);
        if (this.prefix != null) {
            return this.prefix + str;
        }
        return str;
    }

    public int getValue(String str) {
        String sanitize = sanitize(str);
        Integer num = (Integer) this.strings.get(sanitize);
        if (num != null) {
            return num.intValue();
        }
        if (this.prefix != null && sanitize.startsWith(this.prefix)) {
            int parseNumeric = parseNumeric(sanitize.substring(this.prefix.length()));
            if (parseNumeric >= 0) {
                return parseNumeric;
            }
        }
        if (this.numericok) {
            return parseNumeric(sanitize);
        }
        return -1;
    }
}
