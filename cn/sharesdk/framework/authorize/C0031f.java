package cn.sharesdk.framework.authorize;

import android.content.Intent;

/* renamed from: cn.sharesdk.framework.authorize.f */
public abstract class C0031f {
    protected C1222e f23a;
    protected int f24b;
    protected SSOListener f25c;

    public C0031f(C1222e c1222e) {
        this.f23a = c1222e;
        this.f25c = c1222e.m1789a().getSSOListener();
    }

    public abstract void m42a();

    public void m43a(int i) {
        this.f24b = i;
    }

    public void m44a(int i, int i2, Intent intent) {
    }

    protected void m45a(Intent intent) {
    }
}
