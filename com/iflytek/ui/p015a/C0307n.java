package com.iflytek.ui.p015a;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.iflytek.p005a.C0247c;
import com.iflytek.p005a.C0249f;
import com.iflytek.p005a.C0250g;
import com.iflytek.ui.p016b.C0310a;
import org.achartengine.ChartFactory;

/* renamed from: com.iflytek.ui.a.n */
public class C0307n extends LinearLayout {
    public C0308o f1150a;
    public C0300h f1151b;
    public C0249f f1152c;
    private TextView f1153d;
    private LinearLayout f1154e;
    private TextView f1155f;
    private TextView f1156g;

    /* renamed from: com.iflytek.ui.a.n.a */
    public class C0306a extends ClickableSpan {
        final /* synthetic */ C0307n f1148a;
        private String f1149b;

        public C0306a(C0307n c0307n, String str) {
            this.f1148a = c0307n;
            this.f1149b = str;
        }

        public void onClick(View view) {
            Uri parse = Uri.parse(this.f1149b);
            Context context = view.getContext();
            Intent intent = new Intent("android.intent.action.VIEW", parse);
            intent.putExtra("com.android.browser.application_id", context.getPackageName());
            context.startActivity(intent);
            C0250g.m1098a().m1106a(this.f1148a.f1152c);
            C0247c.m1070a(null).m1065a(this.f1148a.f1152c);
        }
    }

    public C0307n(Context context) {
        super(context);
        m1390a();
    }

    public void m1390a() {
        try {
            setOrientation(1);
            setGravity(17);
            Context context = getContext();
            View a = C0305m.m1383a(context, "speechbox", (ViewGroup) this);
            LinearLayout linearLayout = (LinearLayout) findViewWithTag("box");
            this.f1150a = new C0308o(context);
            linearLayout.addView(this.f1150a, 0, new LayoutParams(-1, 0, 1.0f));
            this.f1151b = C0310a.m1403a().m1406a("error").m1420d(context);
            linearLayout.addView(this.f1151b, 0, new LayoutParams(-1, 0, 1.0f));
            this.f1153d = (TextView) a.findViewWithTag(ChartFactory.TITLE);
            this.f1154e = (LinearLayout) a.findViewWithTag("box");
            this.f1155f = (TextView) a.findViewWithTag("link");
            this.f1155f.setMovementMethod(LinkMovementMethod.getInstance());
            this.f1155f.setMaxLines(1);
            C0310a.m1403a().m1406a("state").m1409a(this.f1150a);
            C0310a.m1403a().m1406a(ChartFactory.TITLE).m1409a(this.f1153d);
            C0310a.m1403a().m1406a("speechbox").m1409a(this.f1154e);
            C0310a.m1403a().m1406a("link").m1409a(this.f1155f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void m1391a(CharSequence charSequence) {
        this.f1153d.setText(charSequence);
    }

    public void m1392a(boolean z) {
        if (z) {
            this.f1154e.setVisibility(8);
        } else {
            this.f1154e.setVisibility(0);
        }
    }

    public void m1393b() {
        int i = 0;
        this.f1152c = C0250g.m1098a().m1107b(getContext());
        this.f1155f.setText(Html.fromHtml(this.f1152c.m1092c()));
        CharSequence text = this.f1155f.getText();
        if (text instanceof Spannable) {
            Spannable spannable = (Spannable) this.f1155f.getText();
            URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, text.length(), URLSpan.class);
            CharSequence spannableStringBuilder = new SpannableStringBuilder(text);
            spannableStringBuilder.clearSpans();
            int length = uRLSpanArr.length;
            while (i < length) {
                URLSpan uRLSpan = uRLSpanArr[i];
                spannableStringBuilder.setSpan(new C0306a(this, uRLSpan.getURL()), spannable.getSpanStart(uRLSpan), spannable.getSpanEnd(uRLSpan), 34);
                i++;
            }
            this.f1155f.setText(spannableStringBuilder);
        }
    }

    public void m1394b(CharSequence charSequence) {
        if (this.f1156g == null) {
            this.f1156g = new TextView(getContext());
            this.f1156g.setPadding(15, 3, 5, 3);
            this.f1156g.setGravity(19);
            this.f1156g.setMovementMethod(ScrollingMovementMethod.getInstance());
            try {
                C0310a.m1403a().m1406a("help").m1409a(this.f1156g);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.f1154e.addView(this.f1156g, new LayoutParams(-1, 0, 1.0f));
            this.f1150a.setVisibility(8);
            this.f1155f.bringToFront();
        }
        this.f1156g.setText(charSequence);
    }

    public View m1395c() {
        Exception exception;
        View findViewWithTag = findViewWithTag("progress");
        if (findViewWithTag != null) {
            return findViewWithTag;
        }
        try {
            Context context = getContext();
            C0305m.m1383a(context, "progress", this.f1154e);
            View findViewWithTag2 = findViewWithTag("progress");
            try {
                ((ProgressBar) findViewWithTag("progressbar")).setProgressDrawable(C0305m.m1385b(context, "progress_horizontal.xml"));
                this.f1155f.bringToFront();
                return findViewWithTag2;
            } catch (Exception e) {
                Exception exception2 = e;
                findViewWithTag = findViewWithTag2;
                exception = exception2;
                exception.printStackTrace();
                return findViewWithTag;
            }
        } catch (Exception e2) {
            exception = e2;
            exception.printStackTrace();
            return findViewWithTag;
        }
    }

    public void m1396d() {
        if (this.f1156g != null) {
            this.f1154e.removeView(this.f1156g);
            this.f1156g = null;
            this.f1150a.setVisibility(0);
        }
    }
}
