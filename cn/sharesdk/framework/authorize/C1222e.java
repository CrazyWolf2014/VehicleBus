package cn.sharesdk.framework.authorize;

import android.content.Intent;

/* renamed from: cn.sharesdk.framework.authorize.e */
public class C1222e extends C1014a {
    protected SSOListener f2344b;
    private C0031f f2345c;

    public void m2494a(SSOListener sSOListener) {
        this.f2344b = sSOListener;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        this.f2345c.m44a(i, i2, intent);
    }

    public void onCreate() {
        this.f2345c = this.a.getSSOProcessor(this);
        if (this.f2345c == null) {
            finish();
            AuthorizeListener authorizeListener = this.a.getAuthorizeListener();
            if (authorizeListener != null) {
                authorizeListener.onError(new Throwable("Failed to start SSO for " + this.a.getPlatform().getName()));
                return;
            }
            return;
        }
        this.f2345c.m43a(32973);
        this.f2345c.m42a();
    }

    protected void onNewIntent(Intent intent) {
        this.f2345c.m45a(intent);
    }
}
