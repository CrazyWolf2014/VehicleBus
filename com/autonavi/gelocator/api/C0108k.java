package com.autonavi.gelocator.api;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;

/* renamed from: com.autonavi.gelocator.api.k */
final class C0108k {
    public String f821a;
    public String f822b;
    public String f823c;
    public String f824d;
    public String f825e;
    public String f826f;

    C0108k() {
        this.f821a = null;
        this.f822b = null;
        this.f823c = null;
        this.f824d = null;
        this.f825e = null;
        this.f826f = null;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.f821a != null) {
            stringBuffer.append("deviceManufacturer=" + this.f821a + "; ");
        }
        if (this.f822b != null) {
            stringBuffer.append("deviceBrand=" + this.f822b + "; ");
        }
        if (this.f823c != null) {
            stringBuffer.append("osVersion=" + this.f823c + "; ");
        }
        if (this.f824d != null) {
            stringBuffer.append("deviceId=" + this.f824d + "; ");
        }
        if (this.f825e != null) {
            stringBuffer.append("screenSize=" + this.f825e + "; ");
        }
        if (this.f826f != null) {
            stringBuffer.append("nowNetworkType=" + this.f826f + "; ");
        }
        stringBuffer.append(SpecilApiUtil.LINE_SEP);
        return stringBuffer.toString();
    }
}
