package cn.sharesdk.incentive;

import cn.sharesdk.framework.utils.C0058e;

/* renamed from: cn.sharesdk.incentive.d */
class C0070d extends Thread {
    final /* synthetic */ C1032m f127a;
    final /* synthetic */ Incentive f128b;

    C0070d(Incentive incentive, C1032m c1032m) {
        this.f128b = incentive;
        this.f127a = c1032m;
    }

    public void run() {
        try {
            this.f128b.m1860b(this.f128b.f1784b.m248a(this.f127a));
        } catch (Throwable th) {
            C0058e.m220b(th);
        }
    }
}
