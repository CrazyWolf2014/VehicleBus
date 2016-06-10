package com.iflytek.msc.p013f;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.text.SimpleDateFormat;
import java.util.Random;

/* renamed from: com.iflytek.msc.f.d */
public class C0275d {
    public static DisplayMetrics f1038a;

    static {
        f1038a = null;
    }

    public static int m1215a(int i, int i2) {
        return new Random().nextInt(i2 - i) + i;
    }

    public static int m1216a(Context context, float f) {
        if (f1038a == null) {
            f1038a = context.getResources().getDisplayMetrics();
        }
        return (int) TypedValue.applyDimension(1, f, f1038a);
    }

    public static int m1217a(String str) {
        if (str.substring(0, 2).equalsIgnoreCase("0x")) {
            str = str.substring(2);
        }
        return (int) Long.parseLong(str.toLowerCase(), 16);
    }

    public static String m1218a(long j) {
        return new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss SSS").format(Long.valueOf(j));
    }

    public static int m1219b(Context context, float f) {
        if (f1038a == null) {
            f1038a = context.getResources().getDisplayMetrics();
        }
        return (int) ((TypedValue.applyDimension(1, 1.0f, f1038a) * f) / TypedValue.applyDimension(2, 1.0f, f1038a));
    }
}
