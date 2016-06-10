package cn.sharesdk.framework;

import cn.sharesdk.framework.authorize.AuthorizeHelper;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.C0031f;
import cn.sharesdk.framework.authorize.C1222e;
import cn.sharesdk.framework.authorize.C1223g;
import cn.sharesdk.framework.authorize.SSOListener;

/* renamed from: cn.sharesdk.framework.e */
public abstract class C1025e implements AuthorizeHelper {
    protected Platform f1779a;
    private AuthorizeListener f1780b;
    private SSOListener f1781c;

    public C1025e(Platform platform) {
        this.f1779a = platform;
    }

    protected void m1850a(SSOListener sSOListener) {
        this.f1781c = sSOListener;
        C1222e c1222e = new C1222e();
        c1222e.m2494a(sSOListener);
        c1222e.m1790a(this);
    }

    protected void m1851b(AuthorizeListener authorizeListener) {
        this.f1780b = authorizeListener;
        C1223g c1223g = new C1223g();
        c1223g.m2499a(this.f1780b);
        c1223g.m1790a(this);
    }

    public int m1852c() {
        return this.f1779a.getPlatformId();
    }

    public AuthorizeListener getAuthorizeListener() {
        return this.f1780b;
    }

    public Platform getPlatform() {
        return this.f1779a;
    }

    public SSOListener getSSOListener() {
        return this.f1781c;
    }

    public C0031f getSSOProcessor(C1222e c1222e) {
        return null;
    }
}
