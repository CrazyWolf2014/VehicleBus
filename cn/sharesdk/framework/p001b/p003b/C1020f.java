package cn.sharesdk.framework.p001b.p003b;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;
import cn.sharesdk.framework.utils.C0052a;
import cn.sharesdk.framework.utils.C0055d;
import cn.sharesdk.framework.utils.C0058e;
import com.cnmobi.im.dto.MessageVo;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.b.b.f */
public class C1020f extends C0042c {
    private static int f1760p;
    private static long f1761q;
    public int f1762a;
    public String f1763b;
    public String f1764c;
    public String f1765d;
    public C0043a f1766n;
    public String f1767o;

    /* renamed from: cn.sharesdk.framework.b.b.f.a */
    public static class C0043a {
        public String f59a;
        public String f60b;
        public ArrayList<String> f61c;
        public ArrayList<String> f62d;
        public ArrayList<String> f63e;
        public ArrayList<Bitmap> f64f;
        public HashMap<String, Object> f65g;

        public C0043a() {
            this.f59a = XmlPullParser.NO_NAMESPACE;
            this.f61c = new ArrayList();
            this.f62d = new ArrayList();
            this.f63e = new ArrayList();
            this.f64f = new ArrayList();
        }

        public String toString() {
            HashMap hashMap = new HashMap();
            hashMap.put(MessageVo.TYPE_TEXT, this.f60b);
            hashMap.put("url", this.f61c);
            if (this.f62d != null && this.f62d.size() > 0) {
                hashMap.put("imgs", this.f62d);
            }
            if (this.f65g != null) {
                hashMap.put("attch", new C0055d().m211a(this.f65g));
            }
            return new C0055d().m211a(hashMap);
        }
    }

    public C1020f() {
        this.f1765d = XmlPullParser.NO_NAMESPACE;
        this.f1766n = new C0043a();
    }

    protected String m1822a() {
        return "[SHR]";
    }

    protected void m1823a(long j) {
        f1761q = j;
    }

    protected int m1824b() {
        return BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    }

    protected int m1825c() {
        return 30;
    }

    protected long m1826d() {
        return (long) f1760p;
    }

    protected long m1827e() {
        return f1761q;
    }

    protected void m1828f() {
        f1760p++;
    }

    public String toString() {
        String encodeToString;
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.append('|').append(this.f1762a);
        stringBuilder.append('|').append(this.f1763b);
        stringBuilder.append('|').append(TextUtils.isEmpty(this.f1764c) ? XmlPullParser.NO_NAMESPACE : this.f1764c);
        stringBuilder.append('|').append(this.f1765d);
        stringBuilder.append('|');
        if (this.f1766n != null) {
            try {
                encodeToString = Base64.encodeToString(C0052a.m167a(this.f.substring(0, 16), this.f1766n.toString()), 0);
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
        if (!TextUtils.isEmpty(this.f1767o)) {
            try {
                encodeToString = Base64.encodeToString(C0052a.m167a(this.f.substring(0, 16), this.f1767o), 0);
                if (encodeToString.contains(SpecilApiUtil.LINE_SEP)) {
                    encodeToString = encodeToString.replace(SpecilApiUtil.LINE_SEP, XmlPullParser.NO_NAMESPACE);
                }
                stringBuilder.append(encodeToString);
            } catch (Throwable th2) {
                C0058e.m220b(th2);
            }
        }
        return stringBuilder.toString();
    }
}
