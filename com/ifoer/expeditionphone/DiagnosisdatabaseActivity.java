package com.ifoer.expeditionphone;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.util.AndroidToLan;
import java.util.Locale;

public class DiagnosisdatabaseActivity extends RelativeLayout {
    private View circleView;
    private ImageButton closeSli;
    private Context context;
    private LayoutInflater inflater;
    private int lanId;
    private WebView webView;

    class MyClient extends WebViewClient {
        MyClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url.replaceAll("www.dbscar.com", "50.112.172.51"));
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }
    }

    public DiagnosisdatabaseActivity(Context context) {
        super(context);
        this.inflater = null;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        if (MainActivity.country == null || MainActivity.country.length() <= 0) {
            MainActivity.country = Locale.getDefault().getCountry();
        }
        this.lanId = AndroidToLan.getLanId(MainActivity.country);
        this.circleView = this.inflater.inflate(C0136R.layout.diagnosis_database, null);
        addView(this.circleView, new LayoutParams(-1, -1));
        this.webView = (WebView) this.circleView.findViewById(C0136R.id.wv);
        this.webView.setWebViewClient(new MyClient());
        this.webView.setBackgroundColor(0);
        this.webView.getSettings().setSupportZoom(true);
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setScrollBarStyle(0);
        this.webView.setHorizontalScrollBarEnabled(false);
        this.webView.getSettings().setSupportZoom(true);
        this.webView.getSettings().setUseWideViewPort(true);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.setInitialScale(80);
        this.webView.setHorizontalScrollbarOverlay(true);
        this.webView.clearCache(true);
        CookieManager.getInstance().removeSessionCookie();
        this.webView.loadUrl("http://50.112.172.51/data/diagnosisAndRepair.action");
    }
}
