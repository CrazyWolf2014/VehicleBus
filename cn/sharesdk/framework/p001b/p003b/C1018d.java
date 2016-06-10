package cn.sharesdk.framework.p001b.p003b;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.b.b.d */
public class C1018d extends C0042c {
    private static int f1752d;
    private static long f1753n;
    public String f1754a;
    public int f1755b;
    public String f1756c;

    public C1018d() {
        this.f1756c = XmlPullParser.NO_NAMESPACE;
    }

    protected String m1806a() {
        return "[EVT]";
    }

    protected void m1807a(long j) {
        f1753n = j;
    }

    protected int m1808b() {
        return BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    }

    protected int m1809c() {
        return 30;
    }

    protected long m1810d() {
        return (long) f1752d;
    }

    protected long m1811e() {
        return f1753n;
    }

    protected void m1812f() {
        f1752d++;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.append('|').append(this.f1754a);
        stringBuilder.append('|').append(this.f1755b);
        stringBuilder.append('|').append(this.f1756c);
        return stringBuilder.toString();
    }
}
