package org.xbill.DNS;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public final class Options {
    private static Map table;

    static {
        try {
            refresh();
        } catch (SecurityException e) {
        }
    }

    private Options() {
    }

    public static void refresh() {
        String property = System.getProperty("dnsjava.options");
        if (property != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(property, ",");
            while (stringTokenizer.hasMoreTokens()) {
                property = stringTokenizer.nextToken();
                int indexOf = property.indexOf(61);
                if (indexOf == -1) {
                    set(property);
                } else {
                    set(property.substring(0, indexOf), property.substring(indexOf + 1));
                }
            }
        }
    }

    public static void clear() {
        table = null;
    }

    public static void set(String str) {
        if (table == null) {
            table = new HashMap();
        }
        table.put(str.toLowerCase(), "true");
    }

    public static void set(String str, String str2) {
        if (table == null) {
            table = new HashMap();
        }
        table.put(str.toLowerCase(), str2.toLowerCase());
    }

    public static void unset(String str) {
        if (table != null) {
            table.remove(str.toLowerCase());
        }
    }

    public static boolean check(String str) {
        if (table == null || table.get(str.toLowerCase()) == null) {
            return false;
        }
        return true;
    }

    public static String value(String str) {
        if (table == null) {
            return null;
        }
        return (String) table.get(str.toLowerCase());
    }

    public static int intValue(String str) {
        String value = value(str);
        if (value != null) {
            try {
                int parseInt = Integer.parseInt(value);
                if (parseInt > 0) {
                    return parseInt;
                }
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }
}
