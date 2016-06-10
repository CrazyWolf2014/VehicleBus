package com.ifoer.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.AndroidToLan;
import java.util.Locale;

public class DiagnosisdatabaseActivity extends Activity {
    private View circleView;
    private ImageButton closeSli;
    private Context context;
    private LayoutInflater inflater;
    private int lanId;
    private ProgressDialog loading;
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
            DiagnosisdatabaseActivity.this.loading.dismiss();
        }

        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }
    }

    public DiagnosisdatabaseActivity() {
        this.inflater = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.diagnosis_database);
        this.context = this;
        initView();
    }

    private void initView() {
        if (MainActivity.country == null || MainActivity.country.length() <= 0) {
            MainActivity.country = Locale.getDefault().getCountry();
        }
        this.lanId = AndroidToLan.getLanId(MainActivity.country);
        this.loading = new ProgressDialog(this);
        this.loading.setMessage(getResources().getString(C0136R.string.find_wait));
        this.loading.show();
        this.webView = (WebView) findViewById(C0136R.id.wv);
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
