package com.iflytek.msc.p013f;

import android.util.Log;
import com.iflytek.Setting;

/* renamed from: com.iflytek.msc.f.e */
public class C0276e {
    private static String f1039a;
    private static boolean f1040b;

    static {
        f1039a = "MscSpeechLog";
        f1040b = false;
    }

    public static void m1220a(String str) {
        if (Setting.f928a) {
            Log.d(f1039a, str);
        }
    }

    public static void m1221a(String str, String str2) {
        if (Setting.f928a) {
            Log.d(str, str2);
        }
    }

    public static void m1222b(String str) {
        if (Setting.f928a) {
            Log.e(f1039a, str);
        }
    }

    public static void m1223b(String str, String str2) {
        if (Setting.f928a) {
            Log.e(str, str2);
        }
    }

    public static void m1224c(String str) {
        if (Setting.f928a && f1040b) {
            Log.d(f1039a, str);
        }
    }
}
