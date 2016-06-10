package cn.sharesdk.framework.p001b.p003b;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/* renamed from: cn.sharesdk.framework.b.b.a */
public class C1016a extends C0042c {
    private static int f1742c;
    private static long f1743d;
    public int f1744a;
    public String f1745b;

    protected String m1792a() {
        return "[API]";
    }

    protected void m1793a(long j) {
        f1743d = j;
    }

    protected int m1794b() {
        return BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    }

    protected int m1795c() {
        return 50;
    }

    protected long m1796d() {
        return (long) f1742c;
    }

    protected long m1797e() {
        return f1743d;
    }

    protected void m1798f() {
        f1742c++;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.append('|').append(this.f1744a);
        stringBuilder.append('|').append(this.f1745b);
        return stringBuilder.toString();
    }
}
