package cn.sharesdk.framework;

import android.content.Context;
import java.util.HashMap;

public class ShareSDK {
    private static C0048i f6a;

    static String m20a(int i, String str) {
        m25c();
        return f6a.m147a(i, str);
    }

    static String m21a(String str, String str2) {
        m25c();
        return f6a.m148a(str, str2);
    }

    static boolean m22a() {
        m25c();
        return f6a.m154a();
    }

    static boolean m23a(HashMap<String, Object> hashMap) {
        m25c();
        return f6a.m155a((HashMap) hashMap);
    }

    static boolean m24b() {
        m25c();
        return f6a.m163d();
    }

    private static void m25c() {
        if (f6a == null) {
            throw new NullPointerException("Please call ShareSDK.initSDK(Context) before any action.");
        }
    }

    public static Platform getPlatform(Context context, String str) {
        return f6a.m145a(str);
    }

    public static synchronized Platform[] getPlatformList(Context context) {
        Platform[] c;
        synchronized (ShareSDK.class) {
            m25c();
            c = f6a.m162c();
        }
        return c;
    }

    public static int getSDKVersionCode() {
        return 27;
    }

    public static String getSDKVersionName() {
        return "2.3.2";
    }

    public static <T extends Service> T getService(Class<T> cls) {
        m25c();
        return f6a.m160c((Class) cls);
    }

    public static void initSDK(Context context) {
        initSDK(context, null, true);
    }

    public static void initSDK(Context context, String str) {
        initSDK(context, str, true);
    }

    public static void initSDK(Context context, String str, boolean z) {
        if (f6a == null) {
            f6a = new C0048i();
            f6a.m150a(context, str, z);
        }
    }

    public static void initSDK(Context context, boolean z) {
        initSDK(context, null, z);
    }

    public static void logApiEvent(String str, int i) {
        m25c();
        f6a.m152a(str, i);
    }

    public static void logDemoEvent(int i, Platform platform) {
        m25c();
        f6a.m149a(i, platform);
    }

    public static String platformIdToName(int i) {
        m25c();
        return f6a.m146a(i);
    }

    public static int platformNameToId(String str) {
        m25c();
        return f6a.m156b(str);
    }

    public static void registerService(Class<? extends Service> cls) {
        m25c();
        f6a.m151a((Class) cls);
    }

    public static void setConnTimeout(int i) {
        m25c();
        f6a.m158b(i);
    }

    public static void setPlatformDevInfo(String str, HashMap<String, Object> hashMap) {
        m25c();
        f6a.m153a(str, (HashMap) hashMap);
    }

    public static void setReadTimeout(int i) {
        m25c();
        f6a.m161c(i);
    }

    public static void stopSDK(Context context) {
        if (f6a != null) {
            f6a.m157b();
        }
        f6a = null;
    }

    public static void unregisterService(Class<? extends Service> cls) {
        m25c();
        f6a.m159b((Class) cls);
    }
}
