package cn.sharesdk.framework.p000a;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/* renamed from: cn.sharesdk.framework.a.k */
public class C1013k extends C0022c {
    private StringBuilder f1734a;

    public C1013k() {
        this.f1734a = new StringBuilder();
    }

    public C1013k m1779a(String str) {
        this.f1734a.append(str);
        return this;
    }

    protected InputStream m1780a() {
        return new ByteArrayInputStream(this.f1734a.toString().getBytes("utf-8"));
    }

    protected long m1781b() {
        return (long) this.f1734a.toString().getBytes("utf-8").length;
    }

    public String toString() {
        return this.f1734a.toString();
    }
}
