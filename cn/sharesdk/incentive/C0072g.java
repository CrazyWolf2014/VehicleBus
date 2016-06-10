package cn.sharesdk.incentive;

import android.app.Instrumentation;
import cn.sharesdk.framework.utils.C0058e;

/* renamed from: cn.sharesdk.incentive.g */
class C0072g extends Thread {
    final /* synthetic */ C0071f f130a;

    C0072g(C0071f c0071f) {
        this.f130a = c0071f;
    }

    public void run() {
        try {
            new Instrumentation().sendKeyDownUpSync(4);
        } catch (Throwable th) {
            C0058e.m220b(th);
            this.f130a.f129a.finish();
        }
    }
}
