package com.iflytek.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.p005a.C0245a;
import com.iflytek.p005a.C0247c;
import com.iflytek.p005a.C0249f;
import com.iflytek.p005a.C0250g;
import com.iflytek.p006b.C0252a;
import com.iflytek.p007c.C0255a;
import com.iflytek.record.C0287a;
import com.iflytek.record.PcmPlayer;
import com.iflytek.record.PcmPlayer.C0284a;
import com.iflytek.speech.C0294a;
import com.iflytek.speech.C1076b;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SpeechError.C0291a;
import com.iflytek.speech.p014a.C1277a;
import com.iflytek.ui.p015a.C0304l;
import com.iflytek.ui.p015a.C0305m;
import com.iflytek.ui.p015a.C0307n;
import com.iflytek.ui.p016b.C0310a;
import com.ifoer.util.MyHttpException;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jivesoftware.smackx.Form;

/* renamed from: com.iflytek.ui.a */
public final class C1087a extends C0304l implements OnClickListener {
    public static int f2058a;
    public boolean f2059b;
    private View f2060f;
    private Button f2061g;
    private Button f2062h;
    private View f2063i;
    private Button f2064j;
    private Button f2065k;
    private Button f2066l;
    private C1076b f2067m;
    private String f2068n;
    private String f2069o;
    private String f2070p;
    private RecognizerDialogListener f2071q;
    private boolean f2072r;
    private long f2073s;
    private C0294a f2074t;
    private volatile int f2075u;
    private PcmPlayer f2076v;
    private C0284a f2077w;

    static {
        f2058a = 14;
    }

    public C1087a(Context context, String str) {
        super(context);
        this.f2068n = null;
        this.f2069o = null;
        this.f2070p = null;
        this.f2072r = false;
        this.f2059b = true;
        this.f2073s = 0;
        this.f2074t = new C1088b(this);
        this.f2076v = null;
        this.f2077w = new C1089e(this);
        this.f2067m = C1076b.m2129a(getContext().getApplicationContext(), str);
        if (VERSION.SDK_INT < f2058a) {
            this.f2072r = false;
        } else {
            this.f2072r = true;
        }
        this.f2072r = new C0252a(str, (String[][]) null).m1125a("cancel_align_left", this.f2072r);
        m2219a();
        this.f2076v = new PcmPlayer(context);
    }

    private void m2197a(SpeechError speechError) {
        this.c.f1150a.setVisibility(8);
        this.c.f1151b.setVisibility(0);
        C0249f a = C0250g.m1098a().m1104a(getContext(), speechError, this.e);
        this.c.f1151b.m1363a(a);
        m2200a(a.m1091b(), C0255a.m1148b(8), true);
        C0291a operation = speechError.getOperation();
        if (!this.f2059b && operation == C0291a.MORE) {
            operation = C0291a.RETRY;
        }
        if (operation == C0291a.MORE) {
            m2217l();
        } else if (operation == C0291a.NETSET) {
            m2216k();
        } else if (operation == C0291a.RETRY) {
            m2218m();
        }
        this.f2075u = 4;
    }

    private void m2200a(String str, String str2, boolean z) {
        this.c.m1391a((CharSequence) str);
        this.c.m1392a(false);
        this.c.m1396d();
        this.c.setLayoutParams(new LayoutParams(-1, 0, 1.0f));
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -2, 0.0f);
        layoutParams.setMargins(5, 5, 5, 5);
        this.f2060f.setLayoutParams(layoutParams);
        this.f2061g.setText(str2);
        this.f2061g.setEnabled(z);
        this.f2061g.setCompoundDrawables(null, null, null, null);
        this.f2061g.setOnClickListener(this);
        this.f2062h.setText(C0255a.m1148b(4));
        this.f2063i.setVisibility(8);
    }

    private void m2204d(boolean z) {
        Context context = getContext();
        try {
            Drawable a = C0305m.m1380a(context, "more_blank");
            this.c.m1392a(z);
            ViewGroup.LayoutParams layoutParams;
            Drawable a2;
            if (z) {
                this.c.setLayoutParams(new LayoutParams(-1, -2, 0.0f));
                layoutParams = new LayoutParams(-1, 0, 1.0f);
                layoutParams.setMargins(5, 5, 5, 5);
                this.f2060f.setLayoutParams(layoutParams);
                a2 = C0305m.m1380a(context, "more_expanded");
                this.f2063i.setVisibility(0);
                this.f2065k.setEnabled(true);
                this.f2065k.setLines(1);
                this.f2061g.setCompoundDrawablesWithIntrinsicBounds(a, null, a2, null);
                return;
            }
            this.c.setLayoutParams(new LayoutParams(-1, 0, 1.0f));
            layoutParams = new LayoutParams(-1, -2, 0.0f);
            layoutParams.setMargins(5, 5, 5, 5);
            this.f2060f.setLayoutParams(layoutParams);
            this.f2060f.setBackgroundDrawable(null);
            a2 = C0305m.m1380a(context, "more_collapsed");
            this.f2063i.setVisibility(8);
            if (this.f2076v != null) {
                this.f2076v.m1289d();
            }
            this.f2061g.setCompoundDrawablesWithIntrinsicBounds(a, null, a2, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void m2206e() {
        long j = this.f2073s;
        this.f2073s = SystemClock.elapsedRealtime();
        if (this.f2073s - j >= 300) {
            this.f2067m.m2134a(this.f2074t, this.f2068n, this.f2069o, this.f2070p);
            m2209g();
        }
    }

    private void m2207f() {
        this.c.m1391a(C0255a.m1143a(0));
        this.c.setLayoutParams(new LayoutParams(-1, 0, 1.0f));
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -2, 0.0f);
        layoutParams.setMargins(5, 5, 5, 5);
        this.f2060f.setLayoutParams(layoutParams);
        if (TextUtils.isEmpty(this.f2068n) || !this.f2068n.contains("sms")) {
            this.c.m1394b(C0255a.m1148b(13));
        } else {
            this.c.m1394b(C0255a.m1148b(12));
        }
        this.f2061g.setText(C0255a.m1148b(2));
        this.f2062h.setText(C0255a.m1148b(3));
        this.f2075u = 5;
    }

    private void m2209g() {
        this.c.f1151b.setVisibility(8);
        this.c.f1150a.m1401b();
        m2200a(C0255a.m1143a(2), C0255a.m1148b(5), true);
        this.f2075u = 2;
    }

    private void m2212h() {
        this.c.f1151b.setVisibility(8);
        this.c.f1150a.m1402c();
        m2200a(C0255a.m1143a(3), C0255a.m1148b(5), false);
        this.f2075u = 3;
    }

    private void m2216k() {
        this.f2061g.setText(C0255a.m1148b(6));
        this.f2061g.setOnClickListener(new C0314c(this));
        this.f2063i.setVisibility(8);
    }

    private void m2217l() {
        this.f2061g.setText(C0255a.m1148b(7));
        m2204d(false);
        this.f2061g.setOnClickListener(new C0315d(this));
        this.f2063i.setVisibility(8);
    }

    private void m2218m() {
        this.f2061g.setText(C0255a.m1148b(8));
        this.f2061g.setOnClickListener(this);
        this.f2063i.setVisibility(8);
    }

    public void m2219a() {
        try {
            Context context = getContext();
            View a = !this.f2072r ? C0305m.m1383a(context, "recognizer", (ViewGroup) this) : C0305m.m1383a(context, "recognizer_exchange", (ViewGroup) this);
            View view = (LinearLayout) findViewWithTag("container");
            this.c = new C0307n(context);
            view.addView(this.c, 0, new LayoutParams(-1, 0, 1.0f));
            this.f2060f = a.findViewWithTag("control");
            this.f2061g = (Button) a.findViewWithTag("recognize");
            this.f2061g.setOnClickListener(this);
            this.f2062h = (Button) a.findViewWithTag(Form.TYPE_CANCEL);
            this.f2062h.setOnClickListener(this);
            this.f2062h.setText(C0255a.m1148b(4));
            this.f2061g.setPadding(0, 0, 0, 0);
            this.f2062h.setPadding(0, 0, 0, 0);
            this.f2063i = a.findViewWithTag("more");
            Drawable b = C0305m.m1385b(context, "more_item_bg.xml");
            this.f2066l = (Button) a.findViewWithTag("retrieve");
            this.f2066l.setOnClickListener(this);
            this.f2066l.setText(C0255a.m1148b(11));
            this.f2066l.setBackgroundDrawable(b);
            this.f2066l.setPadding(0, 0, 0, 0);
            b = C0305m.m1385b(context, "more_item_bg.xml");
            this.f2065k = (Button) a.findViewWithTag("playback");
            this.f2065k.setOnClickListener(this);
            this.f2065k.setText(C0255a.m1148b(10));
            this.f2065k.setBackgroundDrawable(b);
            this.f2065k.setPadding(0, 0, 0, 0);
            Drawable b2 = C0305m.m1385b(context, "more_item_bg.xml");
            this.f2064j = (Button) a.findViewWithTag("retry");
            this.f2064j.setOnClickListener(this);
            this.f2064j.setText(C0255a.m1148b(8));
            this.f2064j.setBackgroundDrawable(b2);
            this.f2064j.setPadding(0, 0, 0, 0);
            C0310a.m1403a().m1406a("more").m1409a(this.f2063i);
            if (this.f2072r) {
                C0310a.m1403a().m1406a("leftbutton").m1409a(this.f2062h);
                C0310a.m1403a().m1406a("rightbutton").m1409a(this.f2061g);
            } else {
                C0310a.m1403a().m1406a("leftbutton").m1409a(this.f2061g);
                C0310a.m1403a().m1406a("rightbutton").m1409a(this.f2062h);
            }
            C0310a.m1403a().m1406a("morebutton").m1409a(this.f2066l);
            C0310a.m1403a().m1406a("morebutton").m1409a(this.f2065k);
            C0310a.m1403a().m1406a("morebutton").m1409a(this.f2064j);
            C0310a.m1403a().m1406a("container").m1409a(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void m2220a(int i) {
        this.f2067m.m2132a(i);
    }

    public void m2221a(RATE rate) {
        this.f2067m.m2133a(rate);
    }

    public void m2222a(RecognizerDialogListener recognizerDialogListener) {
        this.f2071q = recognizerDialogListener;
    }

    public void m2223a(String str, String str2, String str3) {
        this.f2068n = str;
        this.f2069o = str2;
        this.f2070p = str3;
    }

    public void m2224a(boolean z) {
        this.f2059b = z;
    }

    public int m2225b(boolean z) {
        return this.f2067m.m2136b(z);
    }

    public void m2226b() {
        super.m1375b();
        this.c.m1393b();
        if (this.f2072r) {
            C0310a.m1403a().m1406a("leftbutton").m1421d(this.f2062h);
            C0310a.m1403a().m1406a("rightbutton").m1421d(this.f2061g);
        } else {
            C0310a.m1403a().m1406a("leftbutton").m1421d(this.f2061g);
            C0310a.m1403a().m1406a("rightbutton").m1421d(this.f2062h);
        }
        if (C0245a.m1052a(getContext()).m1060b("first_start", true)) {
            m2207f();
        } else {
            m2206e();
        }
    }

    public int m2227c(boolean z) {
        return this.f2067m.m2131a(z);
    }

    public void m2228c() {
        if (this.f2067m.isAvaible() && 4 == this.f2075u) {
            C0247c.m1070a("asr").m1069b("asr.cancel");
            C0247c.m1071a().m1081a(null, true);
        }
        this.f2067m.cancel();
        if (this.f2076v != null) {
            this.f2076v.m1289d();
        }
        if (5 == this.f2075u) {
            this.c.m1396d();
            this.f2062h.setText(C0255a.m1148b(4));
        }
        super.m1376c();
    }

    protected boolean m2229d() {
        return super.m1377d() ? this.f2067m.destory(MyHttpException.ERROR_SERVER) : false;
    }

    public void onClick(View view) {
        if (view == this.f2061g) {
            C0276e.m1220a("Recognizer Button Click,State = " + this.f2075u);
            switch (this.f2075u) {
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    this.f2067m.m2137e();
                    m2212h();
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    C0247c.m1070a("asr").m1069b("asr.rerec");
                    m2206e();
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    C0245a.m1052a(getContext()).m1058a("first_start", false);
                    m2206e();
                default:
            }
        } else if (view == this.f2062h) {
            if (5 == this.f2075u) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse(C0255a.m1148b(1)));
                    intent.addFlags(268435456);
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getContext(), C0255a.m1150c(26), 0).show();
                }
                C0247c.m1070a("asr").m1069b("asr.detail");
                return;
            }
            m1379j();
        } else if (view == this.f2064j) {
            C0247c.m1070a("asr").m1069b("asr.rerec");
            if (this.f2076v != null) {
                this.f2076v.m1289d();
            }
            m2206e();
        } else if (view == this.f2065k) {
            C0247c.m1070a("asr").m1069b("asr.replay");
            if (this.f2076v != null) {
                this.f2076v.m1289d();
            }
            this.f2065k.setEnabled(false);
            ConcurrentLinkedQueue h = ((C1277a) this.f2067m).m2581h();
            C0287a c0287a = new C0287a(getContext(), this.f2067m.m2138f(), true, null);
            try {
                c0287a.m1296a(h);
                this.f2076v.m1286a(c0287a, this.f2077w);
            } catch (IOException e2) {
                e2.printStackTrace();
                Toast.makeText(getContext(), "\u64ad\u653e\u51fa\u9519", 0).show();
            }
        } else if (view == this.f2066l) {
            C0247c.m1070a("asr").m1069b("asr.retry");
            ConcurrentLinkedQueue h2 = ((C1277a) this.f2067m).m2581h();
            if (this.f2076v != null) {
                this.f2076v.m1289d();
            }
            this.f2067m.m2135a(this.f2074t, h2, this.f2068n, this.f2069o, this.f2070p);
            m2212h();
        }
    }
}
