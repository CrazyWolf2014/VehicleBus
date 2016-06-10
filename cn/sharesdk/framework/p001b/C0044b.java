package cn.sharesdk.framework.p001b;

import android.content.Context;
import cn.sharesdk.framework.p001b.p003b.C0042c;
import cn.sharesdk.framework.p001b.p003b.C1019e;
import cn.sharesdk.framework.p001b.p003b.C1021g;
import cn.sharesdk.framework.utils.C0053b;
import cn.sharesdk.framework.utils.C0055d;
import cn.sharesdk.framework.utils.C0058e;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.b.b */
public class C0044b extends Thread {
    private static C0044b f66a;
    private Context f67b;
    private C0053b f68c;
    private ArrayList<C0042c> f69d;
    private String f70e;
    private boolean f71f;
    private long f72g;
    private boolean f73h;

    private C0044b(Context context) {
        this.f67b = context;
        this.f69d = new ArrayList();
        this.f68c = C0053b.m175a(context);
    }

    public static synchronized C0044b m100a(Context context) {
        C0044b c0044b;
        synchronized (C0044b.class) {
            if (f66a == null) {
                if (context == null) {
                    c0044b = null;
                } else {
                    f66a = new C0044b(context.getApplicationContext());
                }
            }
            c0044b = f66a;
        }
        return c0044b;
    }

    private void m101b(C0042c c0042c) {
        c0042c.f51f = this.f68c.m195o();
        c0042c.f52g = this.f70e;
        c0042c.f53h = this.f68c.m196p();
        c0042c.f54i = this.f68c.m198r();
        c0042c.f55j = String.valueOf(20027);
        c0042c.f56k = this.f68c.m193m();
        c0042c.f57l = this.f68c.m192l();
        if (!"cn.sharesdk.demo".equals(c0042c.f53h) && "api20".equals(this.f70e)) {
            System.err.println("Your application is using the appkey of ShareSDK Demo, this will cause its data won't be count!");
        }
        c0042c.f58m = this.f68c.m185e();
    }

    private boolean m102b() {
        C0053b a = C0053b.m175a(this.f67b);
        String u = a.m201u();
        String p = a.m196p();
        return p != null && p.equals(u);
    }

    private void m103c(C0042c c0042c) {
        C0041a.m69a(this.f67b).m87a(c0042c);
    }

    public String m104a(String str, boolean z, int i, String str2) {
        if (!this.f71f) {
            return str;
        }
        return C0041a.m69a(this.f67b).m85a(str, this.f70e, i, z, str2);
    }

    public synchronized void m105a() {
        if (this.f71f) {
            long currentTimeMillis = System.currentTimeMillis() - this.f72g;
            C0042c c1019e = new C1019e();
            c1019e.f1759a = currentTimeMillis;
            m106a(c1019e);
            this.f71f = false;
            f66a = null;
        }
    }

    public void m106a(C0042c c0042c) {
        synchronized (this.f69d) {
            if (this.f71f) {
                m101b(c0042c);
                if (c0042c.m92a(this.f67b)) {
                    this.f69d.add(c0042c);
                    c0042c.m94b(this.f67b);
                } else {
                    C0058e.m216a("Drop event: " + c0042c.toString(), new Object[0]);
                }
            }
        }
    }

    public void m107a(String str) {
        this.f70e = str;
    }

    public boolean m108a(HashMap<String, Object> hashMap) {
        if (this.f71f) {
            C0041a a = C0041a.m69a(this.f67b);
            try {
                String c = a.m89c(this.f70e);
                HashMap a2 = new C0055d().m212a(c);
                if (a2.containsKey("error")) {
                    System.err.println(c);
                    return false;
                } else if (a2.containsKey("res")) {
                    C0058e.m219b("snsconf returns ===> %s", a.m84a(this.f70e, String.valueOf(a2.get("res")).replace(SpecilApiUtil.LINE_SEP, XmlPullParser.NO_NAMESPACE)).trim());
                    hashMap.putAll(new C0055d().m212a(r2));
                    return true;
                } else {
                    System.err.println("SNS configuration is empty");
                    return false;
                }
            } catch (Throwable th) {
                th.printStackTrace();
                return false;
            }
        }
        System.err.println("Statistics module unopened");
        return false;
    }

    public void run() {
        while (true) {
            if (this.f71f || this.f69d.size() > 0) {
                boolean b = m102b();
                if (b) {
                    if (!this.f73h) {
                        this.f72g = System.currentTimeMillis();
                        m106a(new C1021g());
                    }
                } else if (this.f73h) {
                    long currentTimeMillis = System.currentTimeMillis() - this.f72g;
                    C0042c c1019e = new C1019e();
                    c1019e.f1759a = currentTimeMillis;
                    m106a(c1019e);
                }
                this.f73h = b;
                C0042c c0042c = null;
                try {
                    synchronized (this.f69d) {
                        if (this.f69d.size() > 0) {
                            c0042c = (C0042c) this.f69d.remove(0);
                        }
                    }
                    if (c0042c != null) {
                        m103c(c0042c);
                    }
                } catch (Throwable th) {
                    C0058e.m217a(th);
                }
                try {
                    Thread.sleep(80);
                } catch (Throwable th2) {
                    C0058e.m217a(th2);
                }
            } else {
                return;
            }
        }
    }

    public synchronized void start() {
        if (!this.f71f) {
            this.f71f = true;
            super.start();
        }
    }
}
