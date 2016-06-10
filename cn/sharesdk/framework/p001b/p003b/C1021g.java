package cn.sharesdk.framework.p001b.p003b;

import android.content.Context;
import android.text.TextUtils;
import cn.sharesdk.framework.p001b.p002a.C0038c;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/* renamed from: cn.sharesdk.framework.b.b.g */
public class C1021g extends C0042c {
    private static int f1768a;
    private static long f1769b;

    protected String m1829a() {
        return "[RUN]";
    }

    protected void m1830a(long j) {
        f1769b = j;
    }

    public boolean m1831a(Context context) {
        C0038c a = C0038c.m51a(context);
        f1768a = a.m57b("insertRunEventCount");
        f1769b = a.m53a("lastInsertRunEventTime");
        return super.m92a(context);
    }

    protected int m1832b() {
        return BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    }

    public void m1833b(Context context) {
        super.m94b(context);
        C0038c a = C0038c.m51a(context);
        a.m55a("lastInsertRunEventTime", Long.valueOf(f1769b));
        a.m54a("insertRunEventCount", f1768a);
    }

    protected int m1834c() {
        return 5;
    }

    protected long m1835d() {
        return (long) f1768a;
    }

    protected long m1836e() {
        return f1769b;
    }

    protected void m1837f() {
        f1768a++;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.append('|');
        if (!TextUtils.isEmpty(this.m)) {
            stringBuilder.append(this.m);
        }
        return stringBuilder.toString();
    }
}
