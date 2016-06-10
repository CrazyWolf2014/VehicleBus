package cn.sharesdk.framework;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import cn.sharesdk.framework.p000a.C0025g;
import cn.sharesdk.framework.p001b.C0044b;
import cn.sharesdk.framework.p001b.p003b.C0042c;
import cn.sharesdk.framework.p001b.p003b.C1016a;
import cn.sharesdk.framework.p001b.p003b.C1018d;
import cn.sharesdk.framework.utils.C0058e;
import cn.sharesdk.framework.utils.UIHandler;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import dalvik.system.DexFile;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/* renamed from: cn.sharesdk.framework.i */
public class C0048i {
    private static boolean f88a;
    private ArrayList<Platform> f89b;
    private HashMap<String, HashMap<String, String>> f90c;
    private HashMap<Integer, HashMap<String, Object>> f91d;
    private ArrayList<Class<?>> f92e;
    private Context f93f;
    private HashMap<Integer, Service> f94g;
    private boolean f95h;
    private String f96i;

    static {
        f88a = true;
    }

    private ArrayList<Platform> m137a(PackageInfo packageInfo) {
        if (f88a) {
            return m143h();
        }
        ArrayList<Platform> arrayList = new ArrayList();
        try {
            DexFile dexFile = new DexFile(packageInfo.applicationInfo.sourceDir);
            Enumeration entries = dexFile.entries();
            dexFile.close();
        } catch (Throwable th) {
            C0058e.m220b(th);
            return null;
        }
        while (entries != null && entries.hasMoreElements()) {
            String str = (String) entries.nextElement();
            if (str.startsWith("cn.sharesdk") && !str.contains("$")) {
                try {
                    Class cls = Class.forName(str);
                    if (cls != null && Platform.class.isAssignableFrom(cls)) {
                        this.f92e.add(cls);
                        Constructor constructor = cls.getConstructor(new Class[]{Context.class});
                        constructor.setAccessible(true);
                        arrayList.add((Platform) constructor.newInstance(new Object[]{this.f93f}));
                    }
                } catch (Throwable th2) {
                    C0058e.m220b(th2);
                }
            }
        }
        return arrayList;
    }

    private ArrayList<Platform> m138a(ArrayList<Platform> arrayList) {
        if (arrayList == null) {
            return null;
        }
        ArrayList<Platform> arrayList2 = new ArrayList();
        while (arrayList.size() > 0) {
            int i = Integer.MAX_VALUE;
            int i2 = Integer.MIN_VALUE;
            int i3 = 0;
            int size = arrayList.size();
            while (i3 < size) {
                int sortId = ((Platform) arrayList.get(i3)).getSortId();
                if (sortId < i) {
                    i2 = sortId;
                    sortId = i3;
                } else {
                    sortId = i2;
                    i2 = i;
                }
                i3++;
                i = i2;
                i2 = sortId;
            }
            if (i2 >= 0) {
                arrayList2.add(arrayList.remove(i2));
            }
        }
        return arrayList2;
    }

    private void m139a(HashMap<String, String> hashMap, String str) {
        this.f90c.put(str, hashMap);
    }

    private ArrayList<Platform> m140e() {
        if (this.f89b != null) {
            Iterator it = this.f89b.iterator();
            while (it.hasNext()) {
                ((Platform) it.next()).m10b();
            }
            return this.f89b;
        }
        ArrayList a;
        if (this.f92e == null || this.f92e.size() <= 0) {
            PackageInfo f = m141f();
            if (f == null) {
                return null;
            }
            this.f92e = new ArrayList();
            a = m137a(f);
        } else {
            a = m142g();
        }
        this.f89b = m138a(a);
        return this.f89b;
    }

    private PackageInfo m141f() {
        try {
            PackageManager packageManager = this.f93f.getPackageManager();
            String packageName = this.f93f.getPackageName();
            for (PackageInfo packageInfo : packageManager.getInstalledPackages(Flags.FLAG2)) {
                if (packageName.equals(packageInfo.packageName)) {
                    return packageInfo;
                }
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    private ArrayList<Platform> m142g() {
        ArrayList<Platform> arrayList = new ArrayList();
        Iterator it = this.f92e.iterator();
        while (it.hasNext()) {
            try {
                Constructor constructor = ((Class) it.next()).getConstructor(new Class[]{Context.class});
                constructor.setAccessible(true);
                arrayList.add((Platform) constructor.newInstance(new Object[]{this.f93f}));
            } catch (Throwable th) {
            }
        }
        return arrayList;
    }

    private ArrayList<Platform> m143h() {
        String[] strArr = new String[]{"cn.sharesdk.douban.Douban", "cn.sharesdk.evernote.Evernote", "cn.sharesdk.facebook.Facebook", "cn.sharesdk.netease.microblog.NetEaseMicroBlog", "cn.sharesdk.renren.Renren", "cn.sharesdk.sina.weibo.SinaWeibo", "cn.sharesdk.sohu.microblog.SohuMicroBlog", "cn.sharesdk.sohu.suishenkan.SohuSuishenkan", "cn.sharesdk.kaixin.KaiXin", "cn.sharesdk.linkedin.LinkedIn", "cn.sharesdk.system.email.Email", "cn.sharesdk.system.text.ShortMessage", "cn.sharesdk.tencent.qq.QQ", "cn.sharesdk.tencent.qzone.QZone", "cn.sharesdk.tencent.weibo.TencentWeibo", "cn.sharesdk.twitter.Twitter", "cn.sharesdk.wechat.friends.Wechat", "cn.sharesdk.wechat.moments.WechatMoments", "cn.sharesdk.wechat.favorite.WechatFavorite", "cn.sharesdk.youdao.YouDao", "cn.sharesdk.google.GooglePlus", "cn.sharesdk.foursquare.FourSquare", "cn.sharesdk.pinterest.Pinterest", "cn.sharesdk.flickr.Flickr", "cn.sharesdk.tumblr.Tumblr", "cn.sharesdk.dropbox.Dropbox", "cn.sharesdk.vkontakte.VKontakte", "cn.sharesdk.instagram.Instagram", "cn.sharesdk.yixin.friends.Yixin", "cn.sharesdk.yixin.moments.YixinMoments"};
        ArrayList<Platform> arrayList = new ArrayList();
        for (String cls : strArr) {
            try {
                Class cls2 = Class.forName(cls);
                this.f92e.add(cls2);
                Constructor constructor = cls2.getConstructor(new Class[]{Context.class});
                constructor.setAccessible(true);
                arrayList.add((Platform) constructor.newInstance(new Object[]{this.f93f}));
            } catch (Throwable th) {
                C0058e.m220b(th);
            }
        }
        return arrayList;
    }

    private void m144i() {
        XmlPullParser newPullParser;
        InputStream open;
        try {
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            newInstance.setNamespaceAware(true);
            newPullParser = newInstance.newPullParser();
            open = this.f93f.getAssets().open("ShareSDK.xml");
        } catch (Throwable th) {
            C0058e.m220b(th);
            return;
        }
        newPullParser.setInput(open, "utf-8");
        for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
            if (eventType == 2) {
                String name = newPullParser.getName();
                HashMap hashMap = new HashMap();
                int attributeCount = newPullParser.getAttributeCount();
                for (eventType = 0; eventType < attributeCount; eventType++) {
                    hashMap.put(newPullParser.getAttributeName(eventType), newPullParser.getAttributeValue(eventType));
                }
                this.f90c.put(name, hashMap);
            }
        }
        open.close();
    }

    public Platform m145a(String str) {
        if (str == null) {
            return null;
        }
        Platform[] c = m162c();
        if (c == null) {
            return null;
        }
        for (Platform platform : c) {
            if (str.equals(platform.getName())) {
                return platform;
            }
        }
        return null;
    }

    public String m146a(int i) {
        Platform[] c = m162c();
        if (c == null) {
            return null;
        }
        for (Platform platform : c) {
            if (platform != null && platform.getPlatformId() == i) {
                return platform.getName();
            }
        }
        return null;
    }

    public String m147a(int i, String str) {
        if (this.f91d == null) {
            this.f91d = new HashMap();
        }
        HashMap hashMap = (HashMap) this.f91d.get(Integer.valueOf(i));
        if (hashMap == null) {
            return null;
        }
        Object obj = hashMap.get(str);
        return obj == null ? null : String.valueOf(obj);
    }

    public String m148a(String str, String str2) {
        if (this.f90c == null) {
            this.f90c = new HashMap();
        }
        if (((HashMap) this.f90c.get(str)) == null) {
            m144i();
        }
        HashMap hashMap = (HashMap) this.f90c.get(str);
        return hashMap == null ? null : (String) hashMap.get(str2);
    }

    public void m149a(int i, Platform platform) {
        C0042c c1018d = new C1018d();
        switch (i) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                c1018d.f1754a = "SHARESDK_ENTER_SHAREMENU";
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                c1018d.f1754a = "SHARESDK_CANCEL_SHAREMENU";
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                c1018d.f1754a = "SHARESDK_EDIT_SHARE";
                break;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                c1018d.f1754a = "SHARESDK_FAILED_SHARE";
                break;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                c1018d.f1754a = "SHARESDK_CANCEL_SHARE";
                break;
        }
        if (platform != null) {
            c1018d.f1755b = platform.getPlatformId();
        }
        C0044b a = C0044b.m100a(null);
        if (a != null) {
            a.m106a(c1018d);
        }
    }

    public void m150a(Context context, String str, boolean z) {
        UIHandler.prepare();
        this.f93f = context.getApplicationContext();
        this.f94g = new HashMap();
        this.f96i = str;
        if (this.f96i == null) {
            this.f96i = m148a("ShareSDK", "AppKey");
        }
        this.f95h = z;
        if (z) {
            C0044b a = C0044b.m100a(context);
            a.m107a(this.f96i);
            a.start();
        }
    }

    public void m151a(Class<? extends Service> cls) {
        synchronized (this.f94g) {
            if (this.f94g.containsKey(Integer.valueOf(cls.hashCode()))) {
                return;
            }
            try {
                Service service = (Service) cls.newInstance();
                service.m18a(this.f93f);
                service.m19a(this.f96i);
                this.f94g.put(Integer.valueOf(cls.hashCode()), service);
                service.onBind();
            } catch (Throwable th) {
                C0058e.m220b(th);
            }
        }
    }

    public void m152a(String str, int i) {
        C0044b a = C0044b.m100a(null);
        if (a != null) {
            C0042c c1016a = new C1016a();
            c1016a.f1745b = str;
            c1016a.f1744a = i;
            a.m106a(c1016a);
        }
    }

    public void m153a(String str, HashMap<String, Object> hashMap) {
        HashMap hashMap2 = new HashMap();
        for (Entry entry : hashMap.entrySet()) {
            String str2 = (String) entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                hashMap2.put(str2, String.valueOf(value));
            }
        }
        m139a(hashMap2, str);
    }

    public boolean m154a() {
        return this.f95h;
    }

    public boolean m155a(HashMap<String, Object> hashMap) {
        if (this.f91d == null) {
            this.f91d = new HashMap();
        }
        if (hashMap == null || hashMap.size() <= 0) {
            return false;
        }
        Iterator it = ((ArrayList) hashMap.get("fakelist")).iterator();
        while (it.hasNext()) {
            int parseInt;
            HashMap hashMap2 = (HashMap) it.next();
            try {
                parseInt = Integer.parseInt(String.valueOf(hashMap2.get("snsplat")));
            } catch (Throwable th) {
                C0058e.m220b(th);
                parseInt = -1;
            }
            if (parseInt != -1) {
                this.f91d.put(Integer.valueOf(parseInt), hashMap2);
            }
        }
        return true;
    }

    public int m156b(String str) {
        Platform a = m145a(str);
        return a == null ? 0 : a.getPlatformId();
    }

    public void m157b() {
        synchronized (this.f94g) {
            for (Entry value : this.f94g.entrySet()) {
                ((Service) value.getValue()).onUnbind();
            }
            this.f94g.clear();
        }
        C0044b.m100a(this.f93f).m105a();
    }

    public void m158b(int i) {
        C0025g.f13a = i;
    }

    public void m159b(Class<? extends Service> cls) {
        synchronized (this.f94g) {
            int hashCode = cls.hashCode();
            if (this.f94g.containsKey(Integer.valueOf(hashCode))) {
                ((Service) this.f94g.get(Integer.valueOf(hashCode))).onUnbind();
                this.f94g.remove(Integer.valueOf(hashCode));
            }
        }
    }

    public <T extends Service> T m160c(Class<T> cls) {
        try {
            return (Service) cls.cast(this.f94g.get(Integer.valueOf(cls.hashCode())));
        } catch (Throwable th) {
            System.err.println(cls.getName() + " has not registered");
            C0058e.m220b(th);
            return null;
        }
    }

    public void m161c(int i) {
        C0025g.f14b = i;
    }

    public synchronized Platform[] m162c() {
        Platform[] platformArr;
        synchronized (this) {
            long currentTimeMillis = System.currentTimeMillis();
            ArrayList e = m140e();
            if (e == null) {
                platformArr = null;
            } else {
                C0058e.m219b("Platform list got, use time: %s", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                currentTimeMillis = System.currentTimeMillis();
                ArrayList arrayList = new ArrayList();
                Iterator it = e.iterator();
                while (it.hasNext()) {
                    Platform platform = (Platform) it.next();
                    if (platform != null && platform.m16c()) {
                        arrayList.add(platform);
                    }
                }
                if (arrayList.size() <= 0) {
                    platformArr = null;
                } else {
                    Platform[] platformArr2 = new Platform[arrayList.size()];
                    for (int i = 0; i < platformArr2.length; i++) {
                        platformArr2[i] = (Platform) arrayList.get(i);
                    }
                    C0058e.m219b("sort list use time: %s", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    platformArr = platformArr2;
                }
            }
        }
        return platformArr;
    }

    public boolean m163d() {
        return this.f91d != null && this.f91d.size() > 0;
    }
}
