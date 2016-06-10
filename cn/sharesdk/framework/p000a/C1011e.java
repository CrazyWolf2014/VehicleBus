package cn.sharesdk.framework.p000a;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: cn.sharesdk.framework.a.e */
public class C1011e extends C0022c {
    private ArrayList<C0022c> f1732a;

    public C1011e() {
        this.f1732a = new ArrayList();
    }

    public C1011e m1765a(C0022c c0022c) {
        this.f1732a.add(c0022c);
        return this;
    }

    protected InputStream m1766a() {
        InputStream c0024f = new C0024f();
        Iterator it = this.f1732a.iterator();
        while (it.hasNext()) {
            c0024f.m31a(((C0022c) it.next()).m27a());
        }
        return c0024f;
    }

    protected long m1767b() {
        Iterator it = this.f1732a.iterator();
        long j = 0;
        while (it.hasNext()) {
            j += ((C0022c) it.next()).m28b();
        }
        return j;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = this.f1732a.iterator();
        while (it.hasNext()) {
            stringBuilder.append(((C0022c) it.next()).toString());
        }
        return stringBuilder.toString();
    }
}
