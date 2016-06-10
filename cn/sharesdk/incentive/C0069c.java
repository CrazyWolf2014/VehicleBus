package cn.sharesdk.incentive;

import cn.sharesdk.framework.utils.C0058e;

/* renamed from: cn.sharesdk.incentive.c */
class C0069c extends Thread {
    final /* synthetic */ Incentive f126a;

    C0069c(Incentive incentive) {
        this.f126a = incentive;
    }

    public void run() {
        try {
            this.f126a.m1860b(this.f126a.f1784b.m246a(new C1027a(this.f126a)));
        } catch (Throwable th) {
            C0058e.m220b(th);
        }
        try {
            this.f126a.m1860b(this.f126a.f1784b.m247a(new C1028b(this.f126a)));
        } catch (Throwable th2) {
            C0058e.m220b(th2);
        }
    }
}
