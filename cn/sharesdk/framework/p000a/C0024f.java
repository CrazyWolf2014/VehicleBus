package cn.sharesdk.framework.p000a;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: cn.sharesdk.framework.a.f */
public class C0024f extends InputStream {
    private ArrayList<InputStream> f11a;
    private int f12b;

    C0024f() {
        this.f11a = new ArrayList();
    }

    private boolean m30a() {
        return this.f11a == null || this.f11a.size() <= 0;
    }

    public void m31a(InputStream inputStream) {
        this.f11a.add(inputStream);
    }

    public int available() {
        return m30a() ? 0 : ((InputStream) this.f11a.get(this.f12b)).available();
    }

    public void close() {
        Iterator it = this.f11a.iterator();
        while (it.hasNext()) {
            ((InputStream) it.next()).close();
        }
    }

    public int read() {
        if (m30a()) {
            return -1;
        }
        int read = ((InputStream) this.f11a.get(this.f12b)).read();
        while (read < 0) {
            this.f12b++;
            if (this.f12b >= this.f11a.size()) {
                return read;
            }
            read = ((InputStream) this.f11a.get(this.f12b)).read();
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) {
        if (m30a()) {
            return -1;
        }
        int read = ((InputStream) this.f11a.get(this.f12b)).read(bArr, i, i2);
        while (read < 0) {
            this.f12b++;
            if (this.f12b >= this.f11a.size()) {
                return read;
            }
            read = ((InputStream) this.f11a.get(this.f12b)).read(bArr, i, i2);
        }
        return read;
    }

    public long skip(long j) {
        throw new IOException();
    }
}
