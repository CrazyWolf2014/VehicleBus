package cn.sharesdk.framework.authorize;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import cn.sharesdk.framework.C0049j;

/* renamed from: cn.sharesdk.framework.authorize.b */
public abstract class C1015b extends C0049j {
    protected C1223g f1739a;
    protected String f1740b;
    protected AuthorizeListener f1741c;

    public C1015b(C1223g c1223g) {
        this.f1739a = c1223g;
        AuthorizeHelper a = c1223g.m1789a();
        this.f1740b = a.getRedirectUri();
        this.f1741c = a.getAuthorizeListener();
    }

    public String m1791a() {
        return "AuthClient";
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        webView.stopLoading();
        AuthorizeListener authorizeListener = this.f1739a.m1789a().getAuthorizeListener();
        this.f1739a.finish();
        if (authorizeListener != null) {
            authorizeListener.onError(new Throwable(str + " (" + i + "): " + str2));
        }
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        sslErrorHandler.proceed();
    }
}
