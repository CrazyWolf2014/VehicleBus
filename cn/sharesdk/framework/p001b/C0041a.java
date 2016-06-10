package cn.sharesdk.framework.p001b;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;
import android.util.Base64;
import cn.sharesdk.framework.p000a.C0023d;
import cn.sharesdk.framework.p000a.C0025g;
import cn.sharesdk.framework.p001b.p002a.C0038c;
import cn.sharesdk.framework.p001b.p002a.C0039d;
import cn.sharesdk.framework.p001b.p002a.C0040e;
import cn.sharesdk.framework.p001b.p003b.C0042c;
import cn.sharesdk.framework.p001b.p003b.C1017b;
import cn.sharesdk.framework.p001b.p003b.C1020f;
import cn.sharesdk.framework.p001b.p003b.C1020f.C0043a;
import cn.sharesdk.framework.p001b.p003b.C1021g;
import cn.sharesdk.framework.utils.C0052a;
import cn.sharesdk.framework.utils.C0053b;
import cn.sharesdk.framework.utils.C0055d;
import cn.sharesdk.framework.utils.C0058e;
import com.amap.mapapi.location.LocationManagerProxy;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnmobi.im.dto.MessageVo;
import com.ifoer.mine.Contact;
import com.ifoer.util.MySharedPreferences;
import com.launch.rcu.socket.SocketCode;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.json.JSONObject;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.b.a */
public class C0041a {
    public static String f41b;
    static String f42c;
    static long f43d;
    private static C0041a f44e;
    private static String f45f;
    private static String f46g;
    private static String f47h;
    Context f48a;
    private C0025g f49i;

    static {
        f43d = 0;
        f41b = "http://api2.sharesdk.cn:5566";
        f45f = "http://api2.sharesdk.cn:5566/conf3";
        f46g = "http://s.sharesdk.cn/api/convert3.do";
        f47h = "http://up.sharesdk.cn/upload/image";
    }

    private C0041a() {
        this.f49i = new C0025g();
    }

    public static C0041a m69a(Context context) {
        if (f44e == null) {
            f44e = new C0041a();
            f44e.f48a = context.getApplicationContext();
        }
        return f44e;
    }

    private String m70a(String str, Bitmap bitmap) {
        try {
            File createTempFile = File.createTempFile("bm_tmp", ".png");
            OutputStream fileOutputStream = new FileOutputStream(createTempFile);
            bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return m74b(str, createTempFile.getAbsolutePath());
        } catch (Throwable th) {
            C0058e.m220b(th);
            return null;
        }
    }

    private String m71a(String str, String str2, String str3, int i, String str4) {
        if (str == null) {
            return str;
        }
        ArrayList arrayList = new ArrayList();
        Pattern compile = Pattern.compile(str3);
        Matcher matcher = compile.matcher(str);
        while (matcher.find()) {
            String group = matcher.group();
            if (group != null && group.length() > 0) {
                arrayList.add(group);
            }
        }
        if (arrayList.size() == 0) {
            return str;
        }
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new C0023d(SharedPref.KEY, str2));
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(new C0023d("urls", ((String) arrayList.get(i2)).toString()));
        }
        arrayList2.add(new C0023d("deviceid", C0053b.m175a(this.f48a).m195o()));
        arrayList2.add(new C0023d("snsplat", String.valueOf(i)));
        C0058e.m221c("> deviceid  devicekey: %s", C0053b.m175a(this.f48a).m195o());
        CharSequence d = m78d(str2, str4);
        if (TextUtils.isEmpty(d)) {
            return str;
        }
        String a;
        arrayList2.add(new C0023d("m", d));
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add(new C0023d("User-Agent", f42c));
        try {
            a = this.f49i.m37a(f46g, arrayList2, null, arrayList3, null);
        } catch (Throwable th) {
            C0058e.m220b(th);
            a = null;
        }
        if (a == null) {
            return str;
        }
        int intValue;
        C0058e.m221c("> SERVER_SHORT_LINK_URL  resp: %s", a);
        HashMap a2 = new C0055d().m212a(a);
        try {
            intValue = ((Integer) a2.get(LocationManagerProxy.KEY_STATUS_CHANGED)).intValue();
        } catch (Throwable th2) {
            C0058e.m220b(th2);
            intValue = -1;
        }
        if (intValue != AsyncTaskManager.REQUEST_SUCCESS_CODE) {
            return str;
        }
        ArrayList arrayList4 = (ArrayList) a2.get(DataPacketExtension.ELEMENT_NAME);
        a2 = new HashMap();
        Iterator it = arrayList4.iterator();
        while (it.hasNext()) {
            HashMap hashMap = (HashMap) it.next();
            a2.put(String.valueOf(hashMap.get("source")), String.valueOf(hashMap.get("surl")));
        }
        Matcher matcher2 = compile.matcher(str);
        StringBuilder stringBuilder = new StringBuilder();
        intValue = 0;
        while (matcher2.find()) {
            stringBuilder.append(str.substring(intValue, matcher2.start()));
            stringBuilder.append((String) a2.get(matcher2.group()));
            intValue = matcher2.end();
        }
        stringBuilder.append(str.substring(intValue, str.length()));
        C0058e.m221c("> SERVER_SHORT_LINK_URL content after replace link ===  %s", stringBuilder.toString());
        return stringBuilder.toString();
    }

    private void m72a(C0039d c0039d) {
        if (c0039d.f37b.size() == 1 ? m76c(c0039d.f36a, Contact.RELATION_ASK) : m76c(m88b(c0039d.f36a), Contact.RELATION_FRIEND)) {
            C0040e.m65a(this.f48a, c0039d.f37b);
        }
    }

    private String m73b() {
        return f41b + "/date";
    }

    private String m74b(String str, String str2) {
        C0058e.m219b(" upload file , server url = %s, file path = %s", str, str2);
        try {
            C0023d c0023d = new C0023d(MessageVo.TYPE_FILE, str2);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new C0023d("User-Agent", f42c));
            String a = this.f49i.m37a(str, null, c0023d, arrayList, null);
            C0058e.m219b("upload file response == %s", a);
            if (a == null || a.length() <= 0) {
                return null;
            }
            HashMap a2 = new C0055d().m212a(a);
            if ((a2.containsKey(LocationManagerProxy.KEY_STATUS_CHANGED) ? Integer.parseInt(a2.get(LocationManagerProxy.KEY_STATUS_CHANGED).toString()) : -1) != AsyncTaskManager.REQUEST_SUCCESS_CODE) {
                return null;
            }
            return a2.containsKey("url") ? a2.get("url").toString() : null;
        } catch (Throwable th) {
            C0058e.m220b(th);
            return null;
        }
    }

    private String m75c() {
        return f41b + "/log4";
    }

    private boolean m76c(String str, String str2) {
        try {
            boolean z;
            ArrayList arrayList = new ArrayList();
            arrayList.add(new C0023d("m", str));
            arrayList.add(new C0023d("t", str2));
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(new C0023d("User-Agent", f42c));
            String a = this.f49i.m37a(m75c(), arrayList, null, arrayList2, null);
            C0058e.m219b("> %s  resp: %s", m75c(), a);
            if (a == null) {
                z = false;
            } else if (a.length() <= 0) {
                return true;
            } else {
                z = true;
            }
            return z;
        } catch (Throwable th) {
            C0058e.m220b(th);
            return false;
        }
    }

    private String m77d() {
        return f41b + "/data";
    }

    private String m78d(String str, String str2) {
        C0053b a = C0053b.m175a(this.f48a);
        C0038c a2 = C0038c.m51a(this.f48a);
        boolean b = a2.m58b();
        boolean c = a2.m60c();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(C0052a.m172c(a.m196p(), "utf-8")).append("|");
            stringBuilder.append(C0052a.m172c(a.m199s(), "utf-8")).append("|");
            stringBuilder.append(C0052a.m172c(String.valueOf(20027), "utf-8")).append("|");
            stringBuilder.append(C0052a.m172c(String.valueOf(a.m193m()), "utf-8")).append("|");
            stringBuilder.append(C0052a.m172c(a.m192l(), "utf-8")).append("|");
            if (b) {
                stringBuilder.append(C0052a.m172c(a.m187g(), "utf-8")).append("|");
                stringBuilder.append(C0052a.m172c(a.m189i(), "utf-8")).append("|");
                stringBuilder.append(C0052a.m172c(a.m183c(), "utf-8")).append("|");
                stringBuilder.append(C0052a.m172c(a.m182b(), "utf-8")).append("|");
                stringBuilder.append(C0052a.m172c(a.m190j(), "utf-8")).append("|");
            } else {
                stringBuilder.append("|||||");
            }
            if (c) {
                stringBuilder.append(str2);
            } else {
                stringBuilder.append(str2.split("\\|")[0]);
                stringBuilder.append("|||||");
            }
            C0058e.m221c("shorLinkMsg ===>>>>", stringBuilder.toString());
            C0058e.m221c("Base64AES key ===>>>>", a.m195o() + ":" + str);
            return m86a(stringBuilder.toString(), C0052a.m173c(String.format("%s:%s", new Object[]{a.m195o(), str})));
        } catch (Throwable th) {
            C0058e.m220b(th);
            return XmlPullParser.NO_NAMESPACE;
        }
    }

    private String m79e() {
        return f41b + "/snsconf";
    }

    private String m80f() {
        JSONObject jSONObject = new JSONObject();
        C0053b a = C0053b.m175a(this.f48a);
        try {
            jSONObject.put(SharedPref.TYPE, (Object) "DEVICE");
            jSONObject.put(SharedPref.KEY, a.m195o());
            jSONObject.put("mac", a.m179a());
            jSONObject.put("udid", a.m184d());
            jSONObject.put(MySharedPreferences.model, a.m182b());
            jSONObject.put("factory", a.m183c());
            jSONObject.put("plat", a.m193m());
            jSONObject.put("sysver", a.m187g());
            jSONObject.put("breaked", false);
            jSONObject.put("screensize", a.m189i());
            jSONObject.put("carrier", a.m190j());
        } catch (Throwable e) {
            C0058e.m220b(e);
        }
        return jSONObject.toString();
    }

    private String m81g() {
        JSONObject jSONObject = new JSONObject();
        C0053b a = C0053b.m175a(this.f48a);
        try {
            jSONObject.put(SharedPref.TYPE, (Object) "PROCESS");
            jSONObject.put("plat", a.m193m());
            jSONObject.put(AlixDefine.DEVICE, a.m195o());
            jSONObject.put("list", a.m194n());
        } catch (Throwable e) {
            C0058e.m220b(e);
        }
        return jSONObject.toString();
    }

    public long m82a() {
        String str = "{}";
        try {
            str = this.f49i.m38a(m73b(), null, null, null);
        } catch (Throwable th) {
            C0058e.m220b(th);
        }
        C0038c a = C0038c.m51a(this.f48a);
        HashMap a2 = new C0055d().m212a(str);
        if (!a2.containsKey("timestamp")) {
            return a.m52a();
        }
        long currentTimeMillis = System.currentTimeMillis() - Long.parseLong(a2.get("timestamp").toString());
        a.m55a("service_time", Long.valueOf(currentTimeMillis));
        return currentTimeMillis;
    }

    public long m83a(String str) {
        String a;
        C0038c a2 = C0038c.m51a(this.f48a);
        C0053b a3 = C0053b.m175a(this.f48a);
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(new C0023d("appkey", str));
            arrayList.add(new C0023d(AlixDefine.DEVICE, a3.m195o()));
            arrayList.add(new C0023d("plat", String.valueOf(a3.m193m())));
            arrayList.add(new C0023d("apppkg", a3.m196p()));
            arrayList.add(new C0023d("appver", String.valueOf(a3.m198r())));
            arrayList.add(new C0023d("sdkver", String.valueOf(20027)));
            arrayList.add(new C0023d("networktype", a3.m192l()));
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(new C0023d("User-Agent", f42c));
            a = this.f49i.m37a(f45f, arrayList, null, arrayList2, null);
            if (a == null || a.length() <= 0) {
                a = "{}";
            }
            C0058e.m219b(" get server config response == %s", a);
        } catch (Throwable th) {
            C0058e.m220b(th);
            a = "{}";
        }
        HashMap a4 = new C0055d().m212a(a);
        if (a4.containsKey(LocationManagerProxy.KEY_STATUS_CHANGED) && Integer.parseInt(String.valueOf(a4.get(LocationManagerProxy.KEY_STATUS_CHANGED))) == AsyncTaskManager.HTTP_ERROR_CODE) {
            System.err.print(String.valueOf(a4.get("error")));
            return 0;
        }
        long j;
        if (a4.containsKey("timestamp")) {
            long currentTimeMillis = System.currentTimeMillis() - Long.parseLong(a4.get("timestamp").toString());
            a2.m55a("service_time", Long.valueOf(currentTimeMillis));
            j = currentTimeMillis;
        } else {
            j = 0;
        }
        if (a4.containsKey("switchs")) {
            HashMap hashMap = new HashMap();
            hashMap = (HashMap) a4.get("switchs");
            String obj = hashMap.get(AlixDefine.DEVICE).toString();
            String obj2 = hashMap.get(SocketCode.REMOTE_SHARE).toString();
            a = hashMap.get("auth").toString();
            a2.m59c(obj);
            a2.m63e(obj2);
            a2.m61d(a);
        }
        if (a4.containsKey("requesthost") && a4.containsKey("requestport")) {
            Object valueOf = String.valueOf(a4.get("requesthost"));
            Object valueOf2 = String.valueOf(a4.get("requestport"));
            if (!(TextUtils.isEmpty(valueOf) || TextUtils.isEmpty(valueOf2))) {
                f41b = "http://" + valueOf + ":" + valueOf2;
            }
        }
        HashMap hashMap2 = (HashMap) a4.get("unfinish");
        if (hashMap2 == null || hashMap2.size() <= 0) {
            return j;
        }
        if ("true".equals(String.valueOf(hashMap2.get(AlixDefine.DEVICE)))) {
            try {
                C0058e.m216a(" upload device info == %s", m80f());
                arrayList = new ArrayList();
                arrayList.add(new C0023d("m", C0052a.m171b(a, "sdk.sharesdk.sdk")));
                arrayList2 = new ArrayList();
                arrayList2.add(new C0023d("User-Agent", f42c));
                a = this.f49i.m37a(m77d(), arrayList, null, arrayList2, null);
                C0058e.m219b("> DEVICE_UNFINISHED  resp: %s", a);
            } catch (Throwable th2) {
                C0058e.m220b(th2);
            }
        }
        if ("true".equals(String.valueOf(hashMap2.get("apps")))) {
            try {
                hashMap = new HashMap();
                hashMap.put(SharedPref.TYPE, "APPS");
                hashMap.put("plat", Integer.valueOf(a3.m193m()));
                hashMap.put(AlixDefine.DEVICE, a3.m195o());
                hashMap.put("list", a3.m181a(false));
                C0058e.m216a(" upload apps info == %s", new C0055d().m211a(hashMap));
                arrayList = new ArrayList();
                arrayList.add(new C0023d("m", C0052a.m171b(a, "sdk.sharesdk.sdk")));
                arrayList2 = new ArrayList();
                arrayList2.add(new C0023d("User-Agent", f42c));
                a = this.f49i.m37a(m77d(), arrayList, null, arrayList2, null);
                C0058e.m219b("> APPS_UNFINISHED  resp: %s", a);
            } catch (Throwable th22) {
                C0058e.m220b(th22);
            }
        }
        if (!"true".equals(String.valueOf(hashMap2.get("process")))) {
            return j;
        }
        try {
            C0058e.m216a(" upload device info == %s", m81g());
            arrayList = new ArrayList();
            arrayList.add(new C0023d("m", C0052a.m171b(a, "sdk.sharesdk.sdk")));
            arrayList2 = new ArrayList();
            arrayList2.add(new C0023d("User-Agent", f42c));
            a = this.f49i.m37a(m77d(), arrayList, null, arrayList2, null);
            C0058e.m219b("> PROCESS_UNFINISHED  resp: %s", a);
            return j;
        } catch (Throwable th222) {
            C0058e.m220b(th222);
            return j;
        }
    }

    public String m84a(String str, String str2) {
        return new String(C0052a.m169a(C0052a.m173c(str + ":" + C0053b.m175a(this.f48a).m195o()), Base64.decode(str2, 0)), AsyncHttpResponseHandler.DEFAULT_CHARSET);
    }

    public String m85a(String str, String str2, int i, boolean z, String str3) {
        String a;
        C0058e.m221c("> SERVER_SHORT_LINK_URL content before replace link ===  %s", str);
        if (z) {
            a = m71a(str, str2, "<a[^>]*?href[\\s]*=[\\s]*[\"']?([^'\">]+?)['\"]?>", i, str3);
            if (!(a == null || a.equals(str))) {
                return a;
            }
        }
        a = m71a(str, str2, "(http://|https://){1}[\\w\\.\\-/:\\?&%=,;\\[\\]\\{\\}`~!@#\\$\\^\\*\\(\\)_\\+\\\\\\|]+", i, str3);
        return (a == null || a.equals(str)) ? str : a;
    }

    public String m86a(String str, byte[] bArr) {
        String encodeToString;
        Throwable th;
        try {
            encodeToString = Base64.encodeToString(C0052a.m168a(bArr, str), 0);
            try {
                if (encodeToString.contains(SpecilApiUtil.LINE_SEP)) {
                    encodeToString = encodeToString.replace(SpecilApiUtil.LINE_SEP, XmlPullParser.NO_NAMESPACE);
                }
            } catch (Throwable th2) {
                th = th2;
                C0058e.m220b(th);
                return encodeToString;
            }
        } catch (Throwable th3) {
            Throwable th4 = th3;
            encodeToString = null;
            th = th4;
            C0058e.m220b(th);
            return encodeToString;
        }
        return encodeToString;
    }

    public void m87a(C0042c c0042c) {
        int i = 0;
        C0053b a = C0053b.m175a(this.f48a);
        String l = a.m192l();
        C0058e.m219b(" event string == %s", c0042c.toString());
        C0038c a2 = C0038c.m51a(this.f48a);
        if ((c0042c instanceof C1021g) && !l.endsWith(PrivacyRule.SUBSCRIPTION_NONE)) {
            f42c = (a.m196p() + FilePathGenerator.ANDROID_DIR_SEP + a.m199s()) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "ShareSDK/2.3.2" + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + ("Android/" + a.m187g());
            m83a(c0042c.m99g());
        } else if (c0042c instanceof C1017b) {
            r1 = a2.m60c();
            Object obj = ((C1017b) c0042c).f1750c;
            if (!r1 || TextUtils.isEmpty(obj)) {
                ((C1017b) c0042c).f1751d = null;
                ((C1017b) c0042c).f1750c = null;
            } else {
                ((C1017b) c0042c).f1750c = C0052a.m171b(obj, c0042c.f51f.substring(0, 16));
            }
        } else if (c0042c instanceof C1020f) {
            C1020f c1020f = (C1020f) c0042c;
            r1 = a2.m62d();
            boolean c = a2.m60c();
            C0043a c0043a = c1020f.f1766n;
            if (r1) {
                int i2;
                String b;
                for (i2 = 0; i2 < c0043a.f63e.size(); i2++) {
                    b = m74b(f47h, (String) c0043a.f63e.get(i2));
                    if (b != null) {
                        c0043a.f62d.add(b);
                    }
                }
                for (i2 = 0; i2 < c0043a.f64f.size(); i2++) {
                    b = m70a(f47h, (Bitmap) c0043a.f64f.get(i2));
                    if (b != null) {
                        c0043a.f62d.add(b);
                    }
                }
            } else {
                c1020f.f1766n = null;
            }
            if (!c) {
                c1020f.f1767o = null;
            }
        }
        if (!a2.m58b()) {
            c0042c.f58m = null;
        }
        long a3 = a2.m52a();
        if (a3 == 0) {
            a3 = m82a();
        }
        c0042c.f50e = System.currentTimeMillis() - a3;
        C0040e.m64a(this.f48a, c0042c.toString(), c0042c.f50e);
        if (!l.equals(PrivacyRule.SUBSCRIPTION_NONE)) {
            a3 = System.currentTimeMillis();
            if (a3 - f43d >= 10000) {
                f43d = a3;
                ArrayList b2 = l.toLowerCase().equals("wifi") ? C0040e.m68b(this.f48a) : C0040e.m66a(this.f48a);
                while (i < b2.size()) {
                    m72a((C0039d) b2.get(i));
                    i++;
                }
            }
        }
    }

    public String m88b(String str) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String str2 = null;
        try {
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            byte[] bArr = new byte[Flags.FLAG5];
            while (true) {
                int read = byteArrayInputStream.read(bArr, 0, Flags.FLAG5);
                if (read == -1) {
                    break;
                }
                gZIPOutputStream.write(bArr, 0, read);
            }
            gZIPOutputStream.finish();
            gZIPOutputStream.flush();
            gZIPOutputStream.close();
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            byteArrayInputStream.close();
            str2 = Base64.encodeToString(toByteArray, 0);
        } catch (Throwable e) {
            C0058e.m220b(e);
        }
        return str2.contains(SpecilApiUtil.LINE_SEP) ? str2.replace(SpecilApiUtil.LINE_SEP, XmlPullParser.NO_NAMESPACE) : str2;
    }

    public String m89c(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new C0023d("appkey", str));
        arrayList.add(new C0023d(AlixDefine.DEVICE, C0053b.m175a(this.f48a).m195o()));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new C0023d("User-Agent", f42c));
        return this.f49i.m37a(m79e(), arrayList, null, arrayList2, null);
    }
}
