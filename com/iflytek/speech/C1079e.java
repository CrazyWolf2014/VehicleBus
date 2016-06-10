package com.iflytek.speech;

import com.iflytek.msc.p008a.C0262c;
import com.iflytek.msc.p010c.C0268a;

/* renamed from: com.iflytek.speech.e */
public class C1079e extends C0262c {
    private static C1079e f2024e;

    static {
        f2024e = null;
    }

    public static C1079e m2158d() {
        return f2024e;
    }

    protected boolean m2159c() {
        boolean z = true;
        if (f2024e != null) {
            z = C0268a.m1188a();
            if (z) {
                f2024e = null;
            }
        }
        return z;
    }
}
