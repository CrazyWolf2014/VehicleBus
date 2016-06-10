package cn.sharesdk.framework.p000a;

import cn.sharesdk.framework.utils.C0052a;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/* renamed from: cn.sharesdk.framework.a.a */
public class C1009a extends C0022c {
    private byte[] f1730a;

    public C1009a m1758a(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (this.f1730a != null && this.f1730a.length > 0) {
            byteArrayOutputStream.write(this.f1730a);
        }
        byteArrayOutputStream.write(bArr);
        byteArrayOutputStream.flush();
        this.f1730a = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return this;
    }

    protected InputStream m1759a() {
        return (this.f1730a == null || this.f1730a.length <= 0) ? new ByteArrayInputStream(new byte[0]) : new ByteArrayInputStream(this.f1730a);
    }

    protected long m1760b() {
        return this.f1730a == null ? 0 : (long) this.f1730a.length;
    }

    public String toString() {
        return C0052a.m165a(this.f1730a);
    }
}
