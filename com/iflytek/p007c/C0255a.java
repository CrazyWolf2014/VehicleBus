package com.iflytek.p007c;

import android.text.TextUtils;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.c.a */
public class C0255a {
    public static String f966a;
    private static boolean f967b;
    private static Locale f968c;

    static {
        f966a = "iflytek_skin_gray_default";
        f967b = false;
        f968c = Locale.CHINA;
    }

    public static String m1143a(int i) {
        String[] strArr = C0256b.f970b;
        if (f968c.equals(Locale.US)) {
            strArr = C0257c.f974b;
        } else if (f968c.equals(Locale.TRADITIONAL_CHINESE)) {
            strArr = C0258d.f978b;
        }
        return (i < 0 || i >= strArr.length) ? XmlPullParser.NO_NAMESPACE : strArr[i];
    }

    public static void m1144a(Locale locale) {
        if (locale != null) {
            if (locale.equals(Locale.US) || locale.equals(Locale.CHINA) || locale.equals(Locale.TRADITIONAL_CHINESE)) {
                f968c = locale;
            }
        }
    }

    public static boolean m1145a() {
        return f967b;
    }

    public static boolean m1146a(String str) {
        String str2 = XmlPullParser.NO_NAMESPACE;
        if (str != null) {
            str2 = str.trim();
        }
        return f968c.toString().equalsIgnoreCase(str2) ? true : C0255a.m1149b(str2) && C0255a.m1149b(f968c.toString());
    }

    public static String m1147b() {
        return f968c.toString();
    }

    public static String m1148b(int i) {
        String[] strArr = C0256b.f969a;
        if (f968c.equals(Locale.US)) {
            strArr = C0257c.f973a;
        } else if (f968c.equals(Locale.TRADITIONAL_CHINESE)) {
            strArr = C0258d.f977a;
        }
        return (i < 0 || i >= strArr.length) ? XmlPullParser.NO_NAMESPACE : strArr[i];
    }

    private static boolean m1149b(String str) {
        return TextUtils.isEmpty(str) || Locale.CHINA.toString().equalsIgnoreCase(str) || Locale.CHINESE.toString().equalsIgnoreCase(str);
    }

    public static String m1150c(int i) {
        String[] strArr = C0256b.f971c;
        if (f968c.equals(Locale.US)) {
            strArr = C0257c.f975c;
        } else if (f968c.equals(Locale.TRADITIONAL_CHINESE)) {
            strArr = C0258d.f979c;
        }
        return (i <= 0 || i >= strArr.length) ? C0255a.m1151d(1) : strArr[i];
    }

    public static String m1151d(int i) {
        String[] strArr = C0256b.f972d;
        if (f968c.equals(Locale.US)) {
            strArr = C0257c.f976d;
        } else if (f968c.equals(Locale.TRADITIONAL_CHINESE)) {
            strArr = C0258d.f980d;
        }
        return (i < 0 || i >= strArr.length) ? XmlPullParser.NO_NAMESPACE : strArr[i];
    }
}
