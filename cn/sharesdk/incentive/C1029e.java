package cn.sharesdk.incentive;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import cn.sharesdk.framework.FakeActivity;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.authorize.RegisterView;
import cn.sharesdk.framework.utils.C0051R;
import cn.sharesdk.framework.utils.C0058e;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.io.File;
import java.net.URLDecoder;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;

/* renamed from: cn.sharesdk.incentive.e */
public class C1029e extends FakeActivity implements Callback {
    private IncentivePageAdapter f1785a;
    private RegisterView f1786b;
    private WebView f1787c;

    private void m1864b(Bundle bundle) {
        String string = bundle.getString("durl");
        if (!TextUtils.isEmpty(string)) {
            string = URLDecoder.decode(string);
        }
        String string2 = bundle.getString("package");
        if (!TextUtils.isEmpty(string2)) {
            try {
                PackageManager packageManager = this.activity.getPackageManager();
                if (packageManager.getPackageInfo(string2, 1) != null) {
                    startActivity(packageManager.getLaunchIntentForPackage(string2));
                    return;
                }
                return;
            } catch (Throwable th) {
                C0058e.m220b(th);
            }
        }
        new C0074i(this, string2, string).start();
    }

    protected RegisterView m1865a() {
        RegisterView registerView = new RegisterView(this.activity);
        registerView.m1784a().setOnClickListener(new C0071f(this));
        this.f1787c = registerView.m1786b();
        WebSettings settings = this.f1787c.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(1);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(this.activity.getDir("database", 0).getPath());
        this.f1787c.setVerticalScrollBarEnabled(false);
        this.f1787c.setHorizontalScrollBarEnabled(false);
        WebViewClient c1031k = new C1031k(this);
        this.f1787c.addJavascriptInterface(c1031k, c1031k.m1870a());
        this.f1787c.setWebViewClient(c1031k);
        new C0073h(this).start();
        return registerView;
    }

    public void m1866a(Bundle bundle) {
        boolean z = true;
        int i = bundle.getInt("res", 0);
        String string = bundle.getString("errmsg");
        String string2 = bundle.getString(LocaleUtil.INDONESIAN);
        String string3 = bundle.getString(DataPacketExtension.ELEMENT_NAME);
        if (!TextUtils.isEmpty(string3)) {
            string3 = URLDecoder.decode(string3);
        }
        if (this.f1785a != null) {
            IncentivePageAdapter incentivePageAdapter = this.f1785a;
            if (i != 1) {
                z = false;
            }
            incentivePageAdapter.onBuy(z, string, string2, string3);
        }
    }

    public void m1867a(IncentivePageAdapter incentivePageAdapter) {
        this.f1785a = incentivePageAdapter;
    }

    void m1868a(String str, Bundle bundle) {
        if ("buy".equals(str)) {
            m1866a(bundle);
        } else if ("download".equals(str)) {
            m1864b(bundle);
        }
        if (this.f1785a != null) {
            this.f1785a.onShareSDKAction(str, bundle);
        }
    }

    public boolean handleMessage(Message message) {
        int stringRes;
        switch (message.what) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                File file = (File) message.obj;
                if (file != null && file.exists()) {
                    try {
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        startActivity(intent);
                        break;
                    } catch (Throwable th) {
                        C0058e.m220b(th);
                    }
                }
                stringRes = C0051R.getStringRes(this.activity, "download_faield");
                if (stringRes > 0) {
                    Toast.makeText(this.activity, stringRes, 0).show();
                    break;
                }
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                String str = (String) message.obj;
                if (!TextUtils.isEmpty(str)) {
                    this.f1787c.loadUrl(str);
                    break;
                }
                stringRes = C0051R.getStringRes(this.activity, "failed_to_start_incentive_page");
                if (stringRes <= 0) {
                    finish();
                    break;
                }
                str = this.activity.getString(stringRes);
                this.f1787c.loadDataWithBaseURL(null, String.format("<!DOCTYPE html><html><body><h2>%s</h2></body></html>", new Object[]{str}), "text/html", AsyncHttpResponseHandler.DEFAULT_CHARSET, null);
                break;
        }
        return false;
    }

    public void onCreate() {
        TitleLayout titleLayout = null;
        if (this.f1786b == null) {
            this.f1786b = m1865a();
            titleLayout = this.f1786b.m1787c();
            int stringRes = C0051R.getStringRes(getContext(), "incentive_title");
            if (stringRes > 0) {
                titleLayout.getTvTitle().setText(stringRes);
            }
            titleLayout.setVisibility(8);
        }
        if (this.f1785a != null) {
            this.f1785a.setWebView(this.f1786b.m1786b());
            this.f1785a.setTitleView(titleLayout);
            this.f1785a.onCreate();
        }
        this.activity.setContentView(this.f1786b);
    }

    public void onDestroy() {
        if (this.f1785a != null) {
            this.f1785a.onDestroy();
        }
    }

    public void onPause() {
        if (this.f1785a != null) {
            this.f1785a.onPause();
        }
    }

    public void onRestart() {
        if (this.f1785a != null) {
            this.f1785a.onRestart();
        }
    }

    public void onResume() {
        if (this.f1785a != null) {
            this.f1785a.onResume();
        }
    }

    public void onStart() {
        if (this.f1785a != null) {
            this.f1785a.onStart();
        }
    }

    public void onStop() {
        if (this.f1785a != null) {
            this.f1785a.onStop();
        }
    }

    public void setActivity(Activity activity) {
        super.setActivity(activity);
        if (this.f1785a != null) {
            this.f1785a.setActivity(activity);
        }
    }
}
