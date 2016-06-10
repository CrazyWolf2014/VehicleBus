package com.iflytek.msc.p013f;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import com.iflytek.p006b.C0252a;
import java.lang.reflect.Field;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.msc.f.a */
public class C0272a {
    public static String[][] f1032a;
    private static String[][] f1033b;
    private static C0252a f1034c;
    private static boolean f1035d;

    static {
        r0 = new String[11][];
        r0[0] = new String[]{"os.manufact", Build.MANUFACTURER};
        r0[1] = new String[]{"os.model", Build.MODEL};
        r0[2] = new String[]{"os.product", Build.PRODUCT};
        r0[3] = new String[]{"os.display", Build.DISPLAY};
        r0[4] = new String[]{"os.host", Build.HOST};
        r0[5] = new String[]{"os.id", Build.ID};
        r0[6] = new String[]{"os.device", Build.DEVICE};
        r0[7] = new String[]{"os.board", Build.BOARD};
        r0[8] = new String[]{"os.brand", Build.BRAND};
        r0[9] = new String[]{"os.user", Build.USER};
        r0[10] = new String[]{"os.type", Build.TYPE};
        f1032a = r0;
        r0 = new String[6][];
        r0[0] = new String[]{"os.cpu", "CPU_ABI"};
        r0[1] = new String[]{"os.cpu2", "CPU_ABI2"};
        r0[2] = new String[]{"os.serial", "SERIAL"};
        r0[3] = new String[]{"os.hardware", "HARDWARE"};
        r0[4] = new String[]{"os.bootloader", "BOOTLOADER"};
        r0[5] = new String[]{"os.radio", "RADIO"};
        f1033b = r0;
        f1034c = new C0252a();
        f1035d = false;
    }

    public static synchronized C0252a m1207a(Context context) {
        C0252a c0252a;
        synchronized (C0272a.class) {
            if (f1035d) {
                c0252a = f1034c;
            } else {
                C0272a.m1210b(context);
                c0252a = f1034c;
            }
        }
        return c0252a;
    }

    private static String m1208a(String str) {
        try {
            Field field = Build.class.getField(str);
            return field != null ? field.get(new Build()).toString() : "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }

    public static void m1209a(C0252a c0252a, Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            c0252a.m1121a("app.ver.name", packageInfo.versionName);
            c0252a.m1121a("app.ver.code", XmlPullParser.NO_NAMESPACE + packageInfo.versionCode);
            c0252a.m1121a("app.pkg", applicationInfo.packageName);
            c0252a.m1121a("app.path", applicationInfo.dataDir);
            c0252a.m1121a("app.name", applicationInfo.loadLabel(context.getPackageManager()).toString());
        } catch (Exception e) {
        }
    }

    private static void m1210b(Context context) {
        try {
            int i;
            f1034c.m1119a();
            f1034c.m1121a("os.system", "Android");
            C0272a.m1209a(f1034c, context);
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            f1034c.m1121a("os.resolution", displayMetrics.widthPixels + "*" + displayMetrics.heightPixels);
            f1034c.m1121a("os.density", XmlPullParser.NO_NAMESPACE + displayMetrics.density);
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            f1034c.m1121a("os.imei", telephonyManager.getDeviceId());
            f1034c.m1121a("os.imsi", telephonyManager.getSubscriberId());
            f1034c.m1121a("net.mac", ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress());
            f1034c.m1121a("os.version", VERSION.SDK);
            f1034c.m1121a("os.release", VERSION.RELEASE);
            f1034c.m1121a("os.version", VERSION.INCREMENTAL);
            for (i = 0; i < f1032a.length; i++) {
                f1034c.m1121a(f1032a[i][0], f1032a[i][1]);
            }
            for (i = 0; i < f1033b.length; i++) {
                f1034c.m1121a(f1033b[i][0], C0272a.m1208a(f1033b[i][1]));
            }
            f1035d = true;
        } catch (Exception e) {
            C0276e.m1220a("Failed to get prop Info");
            f1035d = false;
        }
    }
}
