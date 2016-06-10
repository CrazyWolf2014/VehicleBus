package cn.sharesdk.incentive;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import cn.sharesdk.framework.TitleLayout;

public class IncentivePageAdapter {
    private Activity activity;
    private TitleLayout title;
    private WebView webview;

    public Activity getActivity() {
        return this.activity;
    }

    public TitleLayout getTitleLayout() {
        return this.title;
    }

    public WebView getWebBody() {
        return this.webview;
    }

    public void onBuy(boolean z, String str, String str2, String str3) {
    }

    public void onCreate() {
    }

    public void onDestroy() {
    }

    public void onJS(String str) {
    }

    public void onPause() {
    }

    public void onRestart() {
    }

    public void onResume() {
    }

    public void onShareSDKAction(String str, Bundle bundle) {
    }

    public void onStart() {
    }

    public void onStop() {
    }

    void setActivity(Activity activity) {
        this.activity = activity;
    }

    void setTitleView(TitleLayout titleLayout) {
        this.title = titleLayout;
    }

    void setWebView(WebView webView) {
        this.webview = webView;
    }
}
