package cn.sharesdk.incentive;

import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import cn.sharesdk.framework.C0049j;
import cn.sharesdk.framework.utils.C0051R;

/* renamed from: cn.sharesdk.incentive.k */
public class C1031k extends C0049j {
    private C1029e f1788a;

    public C1031k(C1029e c1029e) {
        this.f1788a = c1029e;
    }

    private void m1869a(String str) {
        this.f1788a.m1868a(Uri.parse(str).getAuthority(), C0051R.urlToBundle(str));
    }

    public String m1870a() {
        return "IncentiveClient";
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        if (str == null || !str.startsWith("sharesdk://")) {
            super.onPageStarted(webView, str, bitmap);
            return;
        }
        webView.stopLoading();
        m1869a(str);
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        webView.stopLoading();
        this.f1788a.finish();
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        sslErrorHandler.proceed();
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (str == null || !str.startsWith("sharesdk://")) {
            return super.shouldOverrideUrlLoading(webView, str);
        }
        webView.stopLoading();
        m1869a(str);
        return true;
    }
}
