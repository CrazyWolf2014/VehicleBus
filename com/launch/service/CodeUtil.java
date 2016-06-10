package com.launch.service;

import android.util.Log;

public class CodeUtil {
    public static final int RunningType = 0;
    private static boolean isShow;
    private static String tagStr;

    static {
        isShow = true;
        tagStr = "Sanda";
    }

    public static void m1471i(String logStr) {
        if (isShow) {
            Log.i(tagStr, logStr);
        }
    }

    public static void m1469d(String logStr) {
        if (isShow) {
            Log.d(tagStr, logStr);
        }
    }

    public static void m1470e(String logStr) {
        if (isShow) {
            Log.e(tagStr, logStr);
        }
    }

    public static void m1473w(String logStr) {
        if (isShow) {
            Log.w(tagStr, logStr);
        }
    }

    public static void m1472v(String logStr) {
        if (isShow) {
            Log.v(tagStr, logStr);
        }
    }
}
