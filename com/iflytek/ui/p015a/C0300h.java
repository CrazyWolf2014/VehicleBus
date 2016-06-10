package com.iflytek.ui.p015a;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iflytek.msc.p013f.C0275d;
import com.iflytek.p005a.C0247c;
import com.iflytek.p005a.C0249f;

/* renamed from: com.iflytek.ui.a.h */
public class C0300h extends LinearLayout {
    private TextView f1133a;
    private ImageView f1134b;
    private C0249f f1135c;

    /* renamed from: com.iflytek.ui.a.h.a */
    public class C0299a extends ClickableSpan {
        final /* synthetic */ C0300h f1131a;
        private String f1132b;

        public C0299a(C0300h c0300h, String str) {
            this.f1131a = c0300h;
            this.f1132b = str;
        }

        public void onClick(View view) {
            Uri parse = Uri.parse(this.f1132b);
            Context context = view.getContext();
            Intent intent = new Intent("android.intent.action.VIEW", parse);
            intent.putExtra("com.android.browser.application_id", context.getPackageName());
            context.startActivity(intent);
            C0247c.m1070a(null).m1065a(this.f1131a.f1135c);
        }
    }

    public C0300h(Context context, Drawable drawable) {
        super(context);
        this.f1133a = null;
        this.f1134b = null;
        this.f1135c = null;
        m1360a(drawable);
    }

    private void m1360a(Drawable drawable) {
        LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, 0, 1.0f);
        setPadding(C0275d.m1216a(getContext(), 10.0f), 0, C0275d.m1216a(getContext(), 10.0f), 0);
        setLayoutParams(layoutParams);
        setGravity(16);
        setVisibility(8);
        this.f1134b = new ImageView(getContext());
        layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(0, 0, C0275d.m1216a(getContext(), 10.0f), 0);
        this.f1134b.setLayoutParams(layoutParams);
        this.f1134b.setBackgroundDrawable(drawable);
        addView(this.f1134b);
        this.f1133a = new TextView(getContext());
        this.f1133a.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        this.f1133a.setMovementMethod(LinkMovementMethod.getInstance());
        addView(this.f1133a);
    }

    public void m1361a() {
        int i = 0;
        CharSequence text = this.f1133a.getText();
        if (text instanceof Spannable) {
            Spannable spannable = (Spannable) this.f1133a.getText();
            URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, text.length(), URLSpan.class);
            CharSequence spannableStringBuilder = new SpannableStringBuilder(text);
            spannableStringBuilder.clearSpans();
            int length = uRLSpanArr.length;
            while (i < length) {
                URLSpan uRLSpan = uRLSpanArr[i];
                spannableStringBuilder.setSpan(new C0299a(this, uRLSpan.getURL()), spannable.getSpanStart(uRLSpan), spannable.getSpanEnd(uRLSpan), 34);
                i++;
            }
            this.f1133a.setText(spannableStringBuilder);
        }
    }

    public void m1362a(int i) {
        this.f1133a.setTextColor(i);
    }

    public void m1363a(C0249f c0249f) {
        this.f1135c = c0249f;
        this.f1133a.setText(Html.fromHtml(c0249f.m1092c()));
        m1361a();
    }

    public void m1364b(int i) {
        if (i != -1) {
            this.f1133a.setLinkTextColor(i);
        }
    }

    public void m1365c(int i) {
        this.f1133a.setTextSize((float) i);
    }
}
