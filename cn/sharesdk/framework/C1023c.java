package cn.sharesdk.framework;

import cn.sharesdk.framework.p001b.C0044b;
import cn.sharesdk.framework.p001b.p003b.C0042c;
import cn.sharesdk.framework.p001b.p003b.C1017b;
import cn.sharesdk.framework.utils.C0055d;
import cn.sharesdk.framework.utils.C0058e;
import java.util.HashMap;

/* renamed from: cn.sharesdk.framework.c */
class C1023c implements PlatformActionListener {
    final /* synthetic */ PlatformActionListener f1771a;
    final /* synthetic */ int f1772b;
    final /* synthetic */ HashMap f1773c;
    final /* synthetic */ C1022b f1774d;

    C1023c(C1022b c1022b, PlatformActionListener platformActionListener, int i, HashMap hashMap) {
        this.f1774d = c1022b;
        this.f1771a = platformActionListener;
        this.f1772b = i;
        this.f1773c = hashMap;
    }

    public void onCancel(Platform platform, int i) {
        this.f1774d.f1770a = this.f1771a;
        if (this.f1774d.f1770a != null) {
            this.f1774d.f1770a.onComplete(platform, this.f1772b, this.f1773c);
        }
    }

    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        this.f1774d.f1770a = this.f1771a;
        if (this.f1774d.f1770a != null) {
            this.f1774d.f1770a.onComplete(platform, this.f1772b, this.f1773c);
        }
        C0042c c1017b = new C1017b();
        c1017b.f1748a = platform.getPlatformId();
        c1017b.f1749b = "TencentWeibo".equals(platform.getName()) ? platform.getDb().get("name") : platform.getDb().getUserId();
        c1017b.f1750c = new C0055d().m211a((HashMap) hashMap);
        c1017b.f1751d = this.f1774d.m1840a(platform);
        C0044b.m100a(platform.getContext()).m106a(c1017b);
    }

    public void onError(Platform platform, int i, Throwable th) {
        C0058e.m220b(th);
        this.f1774d.f1770a = this.f1771a;
        if (this.f1774d.f1770a != null) {
            this.f1774d.f1770a.onComplete(platform, this.f1772b, this.f1773c);
        }
    }
}
