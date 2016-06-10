package com.ifoer.expedition.client;

public class LogUtil {
    public static String makeLogTag(Class cls) {
        return "Androidpn_" + cls.getSimpleName();
    }
}
