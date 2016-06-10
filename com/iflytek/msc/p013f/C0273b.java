package com.iflytek.msc.p013f;

import android.content.Context;
import android.os.Build.VERSION;

/* renamed from: com.iflytek.msc.f.b */
public class C0273b {
    public static int f1036a;

    static {
        f1036a = 9;
    }

    public static boolean m1211a(Context context, Boolean bool) {
        if (bool.booleanValue() && VERSION.SDK_INT >= f1036a) {
            C0274c.m1213a(context);
        }
        return false;
    }

    public static boolean m1212b(Context context, Boolean bool) {
        return (!bool.booleanValue() || VERSION.SDK_INT < f1036a) ? false : C0274c.m1214b(context);
    }
}
