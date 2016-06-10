package cn.sharesdk.framework.p001b.p003b;

import android.content.Context;
import android.text.TextUtils;
import cn.sharesdk.framework.p001b.p002a.C0038c;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/* renamed from: cn.sharesdk.framework.b.b.e */
public class C1019e extends C0042c {
    private static int f1757b;
    private static long f1758c;
    public long f1759a;

    protected String m1813a() {
        return "[EXT]";
    }

    protected void m1814a(long j) {
        f1758c = j;
    }

    public boolean m1815a(Context context) {
        C0038c a = C0038c.m51a(context);
        f1757b = a.m57b("insertExitEventCount");
        f1758c = a.m53a("lastInsertExitEventTime");
        return super.m92a(context);
    }

    protected int m1816b() {
        return BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    }

    public void m1817b(Context context) {
        super.m94b(context);
        C0038c a = C0038c.m51a(context);
        a.m55a("lastInsertExitEventTime", Long.valueOf(f1758c));
        a.m54a("insertExitEventCount", f1757b);
    }

    protected int m1818c() {
        return 5;
    }

    protected long m1819d() {
        return (long) f1757b;
    }

    protected long m1820e() {
        return f1758c;
    }

    protected void m1821f() {
        f1757b++;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.append('|');
        if (!TextUtils.isEmpty(this.m)) {
            stringBuilder.append(this.m);
        }
        stringBuilder.append('|').append(this.f1759a / 1000);
        return stringBuilder.toString();
    }
}
