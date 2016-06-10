package cn.sharesdk.framework.p001b.p003b;

import android.text.TextUtils;
import android.util.Base64;
import cn.sharesdk.framework.utils.C0052a;
import cn.sharesdk.framework.utils.C0058e;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.b.b.b */
public class C1017b extends C0042c {
    private static int f1746n;
    private static long f1747o;
    public int f1748a;
    public String f1749b;
    public String f1750c;
    public String f1751d;

    protected String m1799a() {
        return "[AUT]";
    }

    protected void m1800a(long j) {
        f1747o = j;
    }

    protected int m1801b() {
        return BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    }

    protected int m1802c() {
        return 5;
    }

    protected long m1803d() {
        return (long) f1746n;
    }

    protected long m1804e() {
        return f1747o;
    }

    protected void m1805f() {
        f1746n++;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.append('|').append(this.f1748a);
        stringBuilder.append('|').append(this.f1749b);
        stringBuilder.append('|');
        if (!TextUtils.isEmpty(this.f1751d)) {
            try {
                String encodeToString = Base64.encodeToString(C0052a.m167a(this.f.substring(0, 16), this.f1751d), 0);
                if (encodeToString.contains(SpecilApiUtil.LINE_SEP)) {
                    encodeToString = encodeToString.replace(SpecilApiUtil.LINE_SEP, XmlPullParser.NO_NAMESPACE);
                }
                stringBuilder.append(encodeToString);
            } catch (Throwable th) {
                C0058e.m220b(th);
            }
        }
        stringBuilder.append('|');
        if (!TextUtils.isEmpty(this.m)) {
            stringBuilder.append(this.m);
        }
        stringBuilder.append('|');
        if (!TextUtils.isEmpty(this.f1750c)) {
            stringBuilder.append(this.f1750c);
        }
        return stringBuilder.toString();
    }
}
