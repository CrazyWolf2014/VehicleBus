package com.iflytek.util.p017a;

import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.util.a.c */
public class C0327c {
    public static final String[] f1217a;
    public static final String[] f1218b;
    public static final String[] f1219c;

    static {
        f1217a = new String[]{"134", "135", "136", "137", "138", "139", "147", "150", "151", "152", "157", "158", "159", "182", "187", "188"};
        f1218b = new String[]{"130", "131", "132", "155", "156", "185", "186"};
        f1219c = new String[]{"133", "153", "180", "189"};
    }

    public static String m1464a(String str) {
        String str2 = XmlPullParser.NO_NAMESPACE;
        if (str == null) {
            return str;
        }
        str = str.replaceAll(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE).replaceAll("-", XmlPullParser.NO_NAMESPACE);
        StringBuilder stringBuilder = new StringBuilder(str);
        return stringBuilder.length() > 5 ? (stringBuilder.substring(0, 3).equals("+86") || stringBuilder.substring(0, 3).equals("086")) ? stringBuilder.substring(3, stringBuilder.length()) : stringBuilder.substring(0, 2).equals("86") ? stringBuilder.substring(2, stringBuilder.length()) : (stringBuilder.substring(0, 5).equals("12530") || stringBuilder.substring(0, 5).equals("12520") || stringBuilder.substring(0, 5).equals("17951") || stringBuilder.substring(0, 5).equals("17911") || stringBuilder.subSequence(0, 5).equals("12593")) ? stringBuilder.substring(5, stringBuilder.length()) : str : str;
    }

    public static String m1465a(String[] strArr, char c) {
        StringBuilder stringBuilder = new StringBuilder();
        if (strArr != null) {
            for (String str : strArr) {
                if (str != null) {
                    stringBuilder.append(str);
                    stringBuilder.append(c);
                }
            }
        }
        return stringBuilder.toString();
    }
}
