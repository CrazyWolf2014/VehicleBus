package com.iflytek.msc.p013f;

import android.content.Context;
import android.media.AudioManager;

/* renamed from: com.iflytek.msc.f.c */
public class C0274c {
    private static int f1037a;

    static {
        f1037a = 0;
    }

    public static boolean m1213a(Context context) {
        try {
            ((AudioManager) context.getSystemService("audio")).requestAudioFocus(null, 3, 1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean m1214b(Context context) {
        try {
            ((AudioManager) context.getSystemService("audio")).abandonAudioFocus(null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
