package com.autonavi.gelocator.api;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.autonavi.gelocator.api.a */
final class C0098a implements Comparable {
    public int f788a;
    public int f789b;
    public int f790c;
    public int f791d;
    public int f792e;
    public String f793f;
    public String f794g;
    public int f795h;
    public long f796i;
    public String f797j;
    public String f798k;

    C0098a() {
        this.f795h = 10;
        this.f797j = XmlPullParser.NO_NAMESPACE;
        this.f798k = XmlPullParser.NO_NAMESPACE;
        this.f788a = Integer.MAX_VALUE;
        this.f789b = Integer.MAX_VALUE;
        this.f790c = -1;
        this.f791d = -1;
        this.f792e = -1;
        this.f793f = XmlPullParser.NO_NAMESPACE;
        this.f794g = XmlPullParser.NO_NAMESPACE;
        this.f795h = 10;
        this.f796i = 0;
        this.f797j = XmlPullParser.NO_NAMESPACE;
        this.f798k = XmlPullParser.NO_NAMESPACE;
    }

    public final int compareTo(Object obj) {
        C0098a c0098a = (C0098a) obj;
        return this.f796i == c0098a.f796i ? 0 : this.f796i >= c0098a.f796i ? -1 : 1;
    }

    public final String toString() {
        return "lat=" + this.f788a + "; " + "lon=" + this.f789b + "; " + "stationId=" + this.f790c + "; " + "networkId=" + this.f791d + "; " + "systemId=" + this.f792e + " mnc=" + this.f793f + "; " + "mcc=" + this.f794g + "; " + "signal=" + this.f795h + SpecilApiUtil.LINE_SEP;
    }
}
