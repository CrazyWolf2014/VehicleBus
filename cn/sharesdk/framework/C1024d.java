package cn.sharesdk.framework;

import java.util.HashMap;

/* renamed from: cn.sharesdk.framework.d */
class C1024d implements PlatformActionListener {
    final /* synthetic */ PlatformActionListener f1775a;
    final /* synthetic */ int f1776b;
    final /* synthetic */ Object f1777c;
    final /* synthetic */ C1022b f1778d;

    C1024d(C1022b c1022b, PlatformActionListener platformActionListener, int i, Object obj) {
        this.f1778d = c1022b;
        this.f1775a = platformActionListener;
        this.f1776b = i;
        this.f1777c = obj;
    }

    public void onCancel(Platform platform, int i) {
        this.f1778d.f1770a = this.f1775a;
        if (this.f1778d.f1770a != null) {
            this.f1778d.f1770a.onCancel(platform, this.f1776b);
        }
    }

    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        this.f1778d.f1770a = this.f1775a;
        platform.m14c(this.f1776b, this.f1777c);
    }

    public void onError(Platform platform, int i, Throwable th) {
        this.f1778d.f1770a = this.f1775a;
        if (this.f1778d.f1770a != null) {
            this.f1778d.f1770a.onError(platform, i, th);
        }
    }
}
