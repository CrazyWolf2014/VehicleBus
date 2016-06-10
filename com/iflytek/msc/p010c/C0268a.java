package com.iflytek.msc.p010c;

import com.iflytek.msc.MSC;

/* renamed from: com.iflytek.msc.c.a */
public class C0268a {
    private static volatile boolean f1019a;
    private static Object f1020b;

    static {
        f1019a = false;
        f1020b = new Object();
    }

    public static boolean m1188a() {
        boolean z = false;
        synchronized (f1020b) {
            if (f1019a) {
                if (MSC.QISVFini() == 0) {
                    f1019a = false;
                }
            }
            z = true;
        }
        return z;
    }
}
