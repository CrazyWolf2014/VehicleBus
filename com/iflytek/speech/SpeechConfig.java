package com.iflytek.speech;

import org.xmlpull.v1.XmlPullParser;

public class SpeechConfig {
    public static final int Rate11K = 11000;
    public static final int Rate16K = 16000;
    public static final int Rate22K = 22000;
    public static final int Rate8K = 8000;
    private static RATE f1097a;
    private static int f1098b;
    private static RATE f1099c;
    private static String f1100d;
    private static int f1101e;
    private static int f1102f;
    private static int f1103g;
    private static String f1104h;

    public enum RATE {
        rate8k,
        rate11k,
        rate16k,
        rate22k
    }

    static {
        f1097a = RATE.rate16k;
        f1098b = 40;
        f1099c = RATE.rate16k;
        f1100d = "xiaoyan";
        f1101e = 50;
        f1102f = 50;
        f1103g = 50;
        f1104h = null;
    }

    public static int m1312a() {
        return m1317b(f1097a);
    }

    public static void m1313a(int i) {
        f1098b = i;
    }

    public static void m1314a(RATE rate) {
        f1097a = rate;
    }

    public static void m1315a(String str) {
        f1100d = str;
    }

    public static int m1316b() {
        return f1098b;
    }

    public static int m1317b(RATE rate) {
        return rate == RATE.rate8k ? Rate8K : rate != RATE.rate16k ? rate == RATE.rate11k ? Rate11K : rate == RATE.rate22k ? Rate22K : Rate16K : Rate16K;
    }

    public static void m1318b(int i) {
        f1101e = i;
    }

    public static void m1319b(String str) {
        f1104h = str;
    }

    public static int m1320c() {
        return m1317b(f1099c);
    }

    public static void m1321c(int i) {
        f1102f = i;
    }

    public static void m1322c(RATE rate) {
        f1099c = rate;
    }

    public static String m1323d() {
        return f1100d;
    }

    public static void m1324d(int i) {
        f1103g = i;
    }

    public static int m1325e() {
        return f1102f;
    }

    public static String m1326f() {
        String str = XmlPullParser.NO_NAMESPACE;
        return f1101e <= 20 ? "x-slow" : f1101e <= 40 ? "slow" : f1101e <= 60 ? "medium" : f1101e <= 80 ? "fast" : "x-fast";
    }

    public static String m1327g() {
        String str = XmlPullParser.NO_NAMESPACE;
        return f1103g <= 0 ? "silent" : f1103g <= 20 ? "x-soft" : f1103g <= 40 ? "soft" : f1103g <= 60 ? "medium" : f1103g <= 80 ? "loud" : "x-loud";
    }

    public static String m1328h() {
        return f1104h;
    }
}
