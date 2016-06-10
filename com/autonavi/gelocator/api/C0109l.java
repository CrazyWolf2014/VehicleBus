package com.autonavi.gelocator.api;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;

/* renamed from: com.autonavi.gelocator.api.l */
final class C0109l {
    public String f827a;
    public String f828b;
    public int f829c;

    C0109l() {
        this.f827a = null;
        this.f828b = null;
        this.f829c = Integer.MIN_VALUE;
    }

    public final String toString() {
        return "mac=" + this.f827a + "; " + "ssid=" + this.f828b + "; " + "rssi=" + this.f829c + "; " + SpecilApiUtil.LINE_SEP;
    }
}
