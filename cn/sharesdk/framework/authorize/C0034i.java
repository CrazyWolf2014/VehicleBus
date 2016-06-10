package cn.sharesdk.framework.authorize;

import android.app.Instrumentation;
import cn.sharesdk.framework.utils.C0058e;

/* renamed from: cn.sharesdk.framework.authorize.i */
class C0034i extends Thread {
    final /* synthetic */ C0033h f28a;

    C0034i(C0033h c0033h) {
        this.f28a = c0033h;
    }

    public void run() {
        try {
            new Instrumentation().sendKeyDownUpSync(4);
        } catch (Throwable th) {
            C0058e.m220b(th);
            AuthorizeListener authorizeListener = this.f28a.f27a.a.getAuthorizeListener();
            if (authorizeListener != null) {
                authorizeListener.onCancel();
            }
            this.f28a.f27a.finish();
        }
    }
}
