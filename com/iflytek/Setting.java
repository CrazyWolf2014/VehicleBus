package com.iflytek;

import com.iflytek.msc.MSC;
import com.iflytek.p007c.C0255a;
import java.util.Locale;

public class Setting {
    public static boolean f928a;
    public static boolean f929b;
    public static boolean f930c;
    public static LOG_LEVEL f931d;
    public static String f932e;

    public enum LOG_LEVEL {
        all,
        detail,
        normal,
        low,
        none
    }

    static {
        f928a = true;
        f929b = true;
        f930c = true;
        f931d = LOG_LEVEL.none;
        f932e = null;
    }

    private Setting() {
    }

    public static void checkNetwork(boolean z) {
        f929b = z;
    }

    public static void saveLogFile(LOG_LEVEL log_level, String str) {
        f931d = log_level;
        f932e = str;
    }

    public static void setLanguage(Locale locale) {
        C0255a.m1144a(locale);
    }

    public static void setLocationEnable(boolean z) {
        f930c = z;
    }

    public static void showLogcat(boolean z) {
        f928a = z;
        MSC.DebugLog(z);
    }
}
