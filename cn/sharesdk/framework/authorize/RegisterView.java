package cn.sharesdk.framework.authorize;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.utils.C0051R;
import cn.sharesdk.framework.utils.C0058e;

public class RegisterView extends ResizeLayout {
    private TitleLayout f1735a;
    private RelativeLayout f1736b;
    private WebView f1737c;

    public RegisterView(Context context) {
        super(context);
        m1782a(context);
    }

    public RegisterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        m1782a(context);
    }

    private void m1782a(Context context) {
        setBackgroundColor(-1);
        setOrientation(1);
        int b = m1783b(context);
        this.f1735a = new TitleLayout(context);
        try {
            this.f1735a.setBackgroundResource(C0051R.getBitmapRes(context, "ssdk_auth_title_back"));
        } catch (Throwable th) {
            C0058e.m220b(th);
        }
        this.f1735a.getBtnRight().setVisibility(8);
        this.f1735a.getTvTitle().setText(C0051R.getStringRes(getContext(), "weibo_oauth_regiseter"));
        addView(this.f1735a);
        View imageView = new ImageView(context);
        imageView.setImageResource(C0051R.getBitmapRes(context, "ssdk_logo"));
        imageView.setScaleType(ScaleType.CENTER_INSIDE);
        imageView.setPadding(0, 0, C0051R.dipToPx(context, 10), 0);
        imageView.setLayoutParams(new LayoutParams(-2, -1));
        imageView.setOnClickListener(new C0029c(this));
        this.f1735a.addView(imageView);
        this.f1736b = new RelativeLayout(context);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, 0);
        layoutParams.weight = 1.0f;
        this.f1736b.setLayoutParams(layoutParams);
        addView(this.f1736b);
        imageView = new LinearLayout(context);
        imageView.setOrientation(1);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        this.f1736b.addView(imageView);
        View textView = new TextView(context);
        textView.setLayoutParams(new LayoutParams(-1, 5));
        textView.setBackgroundColor(-12929302);
        imageView.addView(textView);
        textView.setVisibility(8);
        this.f1737c = new WebView(context);
        ViewGroup.LayoutParams layoutParams2 = new LayoutParams(-1, -2);
        layoutParams2.weight = 1.0f;
        this.f1737c.setLayoutParams(layoutParams2);
        this.f1737c.setWebChromeClient(new C0030d(this, textView, b));
        imageView.addView(this.f1737c);
        this.f1737c.requestFocus();
    }

    private int m1783b(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (!(context instanceof Activity)) {
            return 0;
        }
        WindowManager windowManager = ((Activity) context).getWindowManager();
        if (windowManager == null) {
            return 0;
        }
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public View m1784a() {
        return this.f1735a.getBtnBack();
    }

    public void m1785a(boolean z) {
        this.f1735a.setVisibility(z ? 8 : 0);
    }

    public WebView m1786b() {
        return this.f1737c;
    }

    public TitleLayout m1787c() {
        return this.f1735a;
    }

    public RelativeLayout m1788d() {
        return this.f1736b;
    }
}
