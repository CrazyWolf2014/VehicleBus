package cn.sharesdk.incentive;

import cn.sharesdk.framework.p000a.C0023d;
import cn.sharesdk.framework.p000a.C0025g;
import cn.sharesdk.framework.utils.C0052a;
import cn.sharesdk.framework.utils.C0058e;
import java.util.ArrayList;
import org.achartengine.chart.TimeChart;

/* renamed from: cn.sharesdk.incentive.l */
public class C0075l {
    private Incentive f135a;
    private String f136b;
    private C0025g f137c;

    public C0075l(Incentive incentive) {
        this.f135a = incentive;
        this.f137c = new C0025g();
    }

    private String m241b() {
        return this.f136b + "appactive";
    }

    private String m242c() {
        return this.f136b + "appdailyactive";
    }

    private String m243d() {
        return this.f136b + "shareaward";
    }

    private String m244e() {
        return this.f136b + "homepage";
    }

    public String m245a() {
        C1030j c1030j = new C1030j(this.f135a);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new C0023d("m", C0052a.m171b(c1030j.toString(), "sdk.sharesdk.sdk")));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new C0023d("User-Agent", "ShareSDK Incentive"));
        String a = this.f137c.m37a(m244e(), arrayList, null, arrayList2, null);
        C0058e.m219b("> shareaward  resp: " + a, new Object[0]);
        return a;
    }

    public String m246a(C1027a c1027a) {
        C0076n a = C0076n.m249a(this.f135a.getContext());
        int b = a.m253b("app_active_count");
        a.m251a("app_active_count", b + 1);
        if (b > 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new C0023d("m", C0052a.m171b(c1027a.toString(), "sdk.sharesdk.sdk")));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new C0023d("User-Agent", "ShareSDK Incentive"));
        String a2 = this.f137c.m37a(m241b(), arrayList, null, arrayList2, null);
        C0058e.m219b("> appactive  resp: " + a2, new Object[0]);
        return a2;
    }

    public String m247a(C1028b c1028b) {
        C0076n a = C0076n.m249a(this.f135a.getContext());
        long a2 = a.m250a("last_app_active_today");
        long currentTimeMillis = System.currentTimeMillis();
        if (a2 > 0 && currentTimeMillis - a2 <= TimeChart.DAY) {
            return null;
        }
        a.m252a("last_app_active_today", Long.valueOf(currentTimeMillis));
        ArrayList arrayList = new ArrayList();
        arrayList.add(new C0023d("m", C0052a.m171b(c1028b.toString(), "sdk.sharesdk.sdk")));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new C0023d("User-Agent", "ShareSDK Incentive"));
        String a3 = this.f137c.m37a(m242c(), arrayList, null, arrayList2, null);
        C0058e.m219b("> appdailyactive  resp: " + a3, new Object[0]);
        return a3;
    }

    public String m248a(C1032m c1032m) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new C0023d("m", C0052a.m171b(c1032m.toString(), "sdk.sharesdk.sdk")));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new C0023d("User-Agent", "ShareSDK Incentive"));
        String a = this.f137c.m37a(m243d(), arrayList, null, arrayList2, null);
        C0058e.m219b("> shareaward  resp: " + a, new Object[0]);
        return a;
    }
}
