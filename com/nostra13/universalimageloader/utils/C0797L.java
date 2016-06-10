package com.nostra13.universalimageloader.utils;

import android.util.Log;
import com.nostra13.universalimageloader.core.ImageLoader;

/* renamed from: com.nostra13.universalimageloader.utils.L */
public final class C0797L {
    private static volatile boolean DISABLED = false;
    private static final String LOG_FORMAT = "%1$s\n%2$s";

    static {
        DISABLED = false;
    }

    private C0797L() {
    }

    public static void enableLogging() {
        DISABLED = false;
    }

    public static void disableLogging() {
        DISABLED = true;
    }

    public static void m1474d(String message, Object... args) {
        C0797L.log(3, null, message, args);
    }

    public static void m1478i(String message, Object... args) {
        C0797L.log(4, null, message, args);
    }

    public static void m1479w(String message, Object... args) {
        C0797L.log(5, null, message, args);
    }

    public static void m1476e(Throwable ex) {
        C0797L.log(6, ex, null, new Object[0]);
    }

    public static void m1475e(String message, Object... args) {
        C0797L.log(6, null, message, args);
    }

    public static void m1477e(Throwable ex, String message, Object... args) {
        C0797L.log(6, ex, message, args);
    }

    private static void log(int priority, Throwable ex, String message, Object... args) {
        if (!DISABLED) {
            String log;
            if (args.length > 0) {
                message = String.format(message, args);
            }
            if (ex == null) {
                log = message;
            } else {
                String logMessage;
                if (message == null) {
                    logMessage = ex.getMessage();
                } else {
                    logMessage = message;
                }
                String logBody = Log.getStackTraceString(ex);
                log = String.format(LOG_FORMAT, new Object[]{logMessage, logBody});
            }
            Log.println(priority, ImageLoader.TAG, log);
        }
    }
}
