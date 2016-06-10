package cn.sharesdk.framework.authorize;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/* renamed from: cn.sharesdk.framework.authorize.d */
class C0030d extends WebChromeClient {
    final /* synthetic */ TextView f20a;
    final /* synthetic */ int f21b;
    final /* synthetic */ RegisterView f22c;

    C0030d(RegisterView registerView, TextView textView, int i) {
        this.f22c = registerView;
        this.f20a = textView;
        this.f21b = i;
    }

    public void onProgressChanged(WebView webView, int i) {
        super.onProgressChanged(webView, i);
        LayoutParams layoutParams = (LayoutParams) this.f20a.getLayoutParams();
        layoutParams.width = (this.f21b * i) / 100;
        this.f20a.setLayoutParams(layoutParams);
        if (i <= 0 || i >= 100) {
            this.f20a.setVisibility(8);
        } else {
            this.f20a.setVisibility(0);
        }
    }
}
