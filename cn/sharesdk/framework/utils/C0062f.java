package cn.sharesdk.framework.utils;

import android.util.Base64;
import cn.sharesdk.framework.p000a.C0023d;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.ifoer.entity.Constant;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.entity.mime.MIME;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.utils.f */
public class C0062f {
    private C0061b f118a;
    private C1224g f119b;

    /* renamed from: cn.sharesdk.framework.utils.f.1 */
    static /* synthetic */ class C00591 {
        static final /* synthetic */ int[] f109a;

        static {
            f109a = new int[C0060a.values().length];
            try {
                f109a[C0060a.HMAC_SHA1.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f109a[C0060a.PLAINTEXT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* renamed from: cn.sharesdk.framework.utils.f.a */
    public enum C0060a {
        HMAC_SHA1,
        PLAINTEXT
    }

    /* renamed from: cn.sharesdk.framework.utils.f.b */
    public static class C0061b {
        public String f113a;
        public String f114b;
        public String f115c;
        public String f116d;
        public String f117e;
    }

    public C0062f() {
        this.f118a = new C0061b();
        this.f119b = new C1224g("-._~", false);
    }

    private ArrayList<C0023d<String>> m222a(long j, String str) {
        ArrayList<C0023d<String>> arrayList = new ArrayList();
        arrayList.add(new C0023d("oauth_consumer_key", this.f118a.f113a));
        arrayList.add(new C0023d("oauth_signature_method", str));
        arrayList.add(new C0023d("oauth_timestamp", String.valueOf(j / 1000)));
        arrayList.add(new C0023d("oauth_nonce", String.valueOf(j)));
        arrayList.add(new C0023d("oauth_version", Constant.APP_VERSION));
        String str2 = this.f118a.f115c;
        if (str2 != null && str2.length() > 0) {
            arrayList.add(new C0023d("oauth_token", str2));
        }
        return arrayList;
    }

    private ArrayList<C0023d<String>> m223a(long j, ArrayList<C0023d<String>> arrayList, String str) {
        Iterator it;
        int i = 0;
        HashMap hashMap = new HashMap();
        if (arrayList != null) {
            it = arrayList.iterator();
            while (it.hasNext()) {
                C0023d c0023d = (C0023d) it.next();
                hashMap.put(m227a(c0023d.f9a), m227a((String) c0023d.f10b));
            }
        }
        ArrayList a = m222a(j, str);
        if (a != null) {
            it = a.iterator();
            while (it.hasNext()) {
                c0023d = (C0023d) it.next();
                hashMap.put(m227a(c0023d.f9a), m227a((String) c0023d.f10b));
            }
        }
        String[] strArr = new String[hashMap.size()];
        int i2 = 0;
        for (Entry key : hashMap.entrySet()) {
            strArr[i2] = (String) key.getKey();
            i2++;
        }
        Arrays.sort(strArr);
        ArrayList<C0023d<String>> arrayList2 = new ArrayList();
        i2 = strArr.length;
        while (i < i2) {
            String str2 = strArr[i];
            arrayList2.add(new C0023d(str2, hashMap.get(str2)));
            i++;
        }
        return arrayList2;
    }

    private ArrayList<C0023d<String>> m224a(String str, String str2, ArrayList<C0023d<String>> arrayList, C0060a c0060a) {
        Object trim;
        String str3 = null;
        long currentTimeMillis = System.currentTimeMillis();
        switch (C00591.f109a[c0060a.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                str3 = "HMAC-SHA1";
                Key secretKeySpec = new SecretKeySpec((m227a(this.f118a.f114b) + '&' + m227a(this.f118a.f116d)).getBytes("utf-8"), "HMAC-SHA1");
                Mac instance = Mac.getInstance("HMAC-SHA1");
                instance.init(secretKeySpec);
                trim = new String(Base64.encode(instance.doFinal((str2 + '&' + m227a(str.toLowerCase()) + '&' + m227a(m225b(m223a(currentTimeMillis, (ArrayList) arrayList, str3)))).getBytes("utf-8")), 0)).trim();
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                str3 = "PLAINTEXT";
                trim = m227a(this.f118a.f114b) + '&' + m227a(this.f118a.f116d);
                break;
            default:
                trim = null;
                break;
        }
        ArrayList<C0023d<String>> a = m222a(currentTimeMillis, str3);
        a.add(new C0023d("oauth_signature", trim));
        return a;
    }

    private String m225b(ArrayList<C0023d<String>> arrayList) {
        if (arrayList == null || arrayList.size() <= 0) {
            return XmlPullParser.NO_NAMESPACE;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            C0023d c0023d = (C0023d) it.next();
            if (i > 0) {
                stringBuilder.append('&');
            }
            stringBuilder.append(c0023d.f9a).append(SignatureVisitor.INSTANCEOF).append((String) c0023d.f10b);
            i++;
        }
        return stringBuilder.toString();
    }

    public C0061b m226a() {
        return this.f118a;
    }

    public String m227a(String str) {
        return str == null ? XmlPullParser.NO_NAMESPACE : this.f119b.escape(str);
    }

    public ArrayList<C0023d<String>> m228a(String str, ArrayList<C0023d<String>> arrayList) {
        return m229a(str, (ArrayList) arrayList, C0060a.HMAC_SHA1);
    }

    public ArrayList<C0023d<String>> m229a(String str, ArrayList<C0023d<String>> arrayList, C0060a c0060a) {
        return m224a(str, "POST", arrayList, c0060a);
    }

    public ArrayList<C0023d<String>> m230a(ArrayList<C0023d<String>> arrayList) {
        StringBuilder stringBuilder = new StringBuilder("OAuth ");
        Iterator it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            C0023d c0023d = (C0023d) it.next();
            if (i > 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append(c0023d.f9a).append("=\"").append(m227a((String) c0023d.f10b)).append("\"");
            i++;
        }
        ArrayList<C0023d<String>> arrayList2 = new ArrayList();
        arrayList2.add(new C0023d("Authorization", stringBuilder.toString()));
        arrayList2.add(new C0023d(MIME.CONTENT_TYPE, "application/x-www-form-urlencoded"));
        return arrayList2;
    }

    public void m231a(String str, String str2) {
        this.f118a.f115c = str;
        this.f118a.f116d = str2;
    }

    public void m232a(String str, String str2, String str3) {
        this.f118a.f113a = str;
        this.f118a.f114b = str2;
        this.f118a.f117e = str3;
    }

    public ArrayList<C0023d<String>> m233b(String str, ArrayList<C0023d<String>> arrayList) {
        return m234b(str, arrayList, C0060a.HMAC_SHA1);
    }

    public ArrayList<C0023d<String>> m234b(String str, ArrayList<C0023d<String>> arrayList, C0060a c0060a) {
        return m224a(str, "GET", arrayList, c0060a);
    }

    public ArrayList<C0023d<String>> m235c(String str, ArrayList<C0023d<String>> arrayList, C0060a c0060a) {
        return m224a(str, "PUT", arrayList, c0060a);
    }
}
