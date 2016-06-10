package com.iflytek.msc.p013f;

import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.msc.f.k */
public class C0283k {
    public static HashMap<String, String> f1050a;
    private static boolean f1051b;
    private static long f1052c;
    private static String f1053d;
    private static String f1054e;
    private static String f1055f;
    private static String f1056g;
    private static String f1057h;

    static {
        f1051b = false;
        f1050a = new HashMap();
        f1052c = 0;
        f1053d = "=";
        f1054e = ",";
        f1055f = ":";
        f1056g = ";";
        f1057h = "=========================================================\n";
    }

    private static String m1265a() {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(System.currentTimeMillis()));
    }

    public static void m1266a(String str, String str2) {
        if (f1051b) {
            C0276e.m1224c("appendInfo:" + str + "," + str2);
            if (f1052c == 0) {
                f1050a.put(str, C0283k.m1265a());
                f1052c = System.currentTimeMillis();
                return;
            }
            long currentTimeMillis = System.currentTimeMillis() - f1052c;
            String str3 = !TextUtils.isEmpty(str2) ? str2 + f1055f + currentTimeMillis : XmlPullParser.NO_NAMESPACE + currentTimeMillis;
            if (!f1050a.containsKey(str) || TextUtils.isEmpty((CharSequence) f1050a.get(str))) {
                f1050a.put(str, str3);
            } else {
                f1050a.put(str, ((String) f1050a.get(str)) + f1056g + str3);
            }
        }
    }
}
