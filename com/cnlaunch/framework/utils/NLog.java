package com.cnlaunch.framework.utils;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;

public class NLog {
    private static final String LOG_FORMAT = "%1$s\n%2$s";
    public static boolean isDebug;

    static {
        isDebug = false;
    }

    public static void m916d(String tag, Object... args) {
        log(3, null, tag, args);
    }

    public static void m920i(String tag, Object... args) {
        log(4, null, tag, args);
    }

    public static void m921w(String tag, Object... args) {
        log(5, null, tag, args);
    }

    public static void m918e(Throwable ex) {
        log(6, ex, null, new Object[0]);
    }

    public static void m917e(String tag, Object... args) {
        log(6, null, tag, args);
    }

    public static void m919e(Throwable ex, String tag, Object... args) {
        log(6, ex, tag, args);
    }

    private static void log(int priority, Throwable ex, String tag, Object... args) {
        int i = 0;
        if (isDebug) {
            String log = XmlPullParser.NO_NAMESPACE;
            if (ex != null) {
                String logMessage = ex.getMessage();
                String logBody = Log.getStackTraceString(ex);
                log = String.format(LOG_FORMAT, new Object[]{logMessage, logBody});
            } else if (args != null && args.length > 0) {
                int length = args.length;
                while (i < length) {
                    log = new StringBuilder(String.valueOf(log)).append(String.valueOf(args[i])).toString();
                    i++;
                }
            }
            Log.println(priority, tag, log);
        }
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean isDebug) {
        isDebug = isDebug;
    }
}
