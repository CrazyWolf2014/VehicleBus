package com.autonavi.gelocator.api;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.autonavi.gelocator.api.b */
final class C0099b implements Comparable {
    public int f799a;
    public int f800b;
    public String f801c;
    public String f802d;
    public int f803e;
    public long f804f;

    C0099b() {
        this.f803e = 10;
        this.f799a = -1;
        this.f800b = -1;
        this.f801c = XmlPullParser.NO_NAMESPACE;
        this.f802d = XmlPullParser.NO_NAMESPACE;
        this.f803e = 10;
        this.f804f = 0;
    }

    public final int compareTo(Object obj) {
        C0099b c0099b = (C0099b) obj;
        return this.f804f == c0099b.f804f ? 0 : this.f804f >= c0099b.f804f ? -1 : 1;
    }

    public final String toString() {
        return "cid=" + this.f799a + "; " + "lac=" + this.f800b + "; " + "mnc=" + this.f801c + "; " + "mcc=" + this.f802d + "; " + "signal=" + this.f803e + SpecilApiUtil.LINE_SEP;
    }
}
