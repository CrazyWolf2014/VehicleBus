package com.ifoer.expeditionphone;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.util.MyApplication;

public class BankRemittanceActivity extends Activity {
    private WebView webview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.bank_remittance);
        MyApplication.getInstance().addActivity(this);
        this.webview = (WebView) findViewById(C0136R.id.webview);
        this.webview.loadUrl("file:///android_res/drawable/bank_transfers.png");
        this.webview.getSettings().setSupportZoom(true);
        this.webview.getSettings().setBuiltInZoomControls(true);
        this.webview.getSettings().setJavaScriptEnabled(true);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
