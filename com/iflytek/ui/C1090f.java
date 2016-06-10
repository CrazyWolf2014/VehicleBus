package com.iflytek.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.iflytek.p005a.C0249f;
import com.iflytek.p005a.C0250g;
import com.iflytek.p007c.C0255a;
import com.iflytek.record.PcmPlayer.PLAY_STATE;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SpeechError.C0291a;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.iflytek.ui.p015a.C0304l;
import com.iflytek.ui.p015a.C0305m;
import com.iflytek.ui.p015a.C0307n;
import com.iflytek.ui.p016b.C0310a;
import com.ifoer.util.MyHttpException;
import org.jivesoftware.smackx.Form;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.ui.f */
public class C1090f extends C0304l implements OnClickListener {
    private int f2080a;
    private String f2081b;
    private String f2082f;
    private SynthesizerPlayer f2083g;
    private SynthesizerDialogListener f2084h;
    private View f2085i;
    private Button f2086j;
    private Button f2087k;
    private ProgressBar f2088l;
    private ImageButton f2089m;
    private Drawable f2090n;
    private Drawable f2091o;
    private View f2092p;
    private SynthesizerPlayerListener f2093q;

    /* renamed from: com.iflytek.ui.f.1 */
    static /* synthetic */ class C03161 {
        static final /* synthetic */ int[] f1179a;

        static {
            f1179a = new int[C0291a.values().length];
            try {
                f1179a[C0291a.RETRY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1179a[C0291a.MORE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1179a[C0291a.NETSET.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f1179a[C0291a.CANCEL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public C1090f(Context context, String str) {
        super(context);
        this.f2080a = 3;
        this.f2081b = XmlPullParser.NO_NAMESPACE;
        this.f2082f = XmlPullParser.NO_NAMESPACE;
        this.f2083g = null;
        this.f2084h = null;
        this.f2093q = new C1091h(this);
        this.f2083g = SynthesizerPlayer.createSynthesizerPlayer(context.getApplicationContext(), str);
        m2276g();
    }

    private void m2241a(SpeechError speechError) {
        this.f2085i.setVisibility(8);
        this.c.f1151b.setVisibility(0);
        this.c.f1150a.setVisibility(8);
        C0249f a = C0250g.m1098a().m1104a(getContext(), speechError, this.e);
        this.c.f1151b.m1363a(a);
        this.c.m1391a(a.m1091b());
        this.f2086j.setVisibility(0);
        m2265a(true);
        switch (C03161.f1179a[speechError.getOperation().ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                m2258q();
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                m2256o();
                break;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                m2257p();
                break;
        }
        this.f2080a = 4;
    }

    private void m2252k() {
        this.f2085i.setVisibility(8);
        this.f2088l.setProgress(0);
        this.f2088l.setSecondaryProgress(0);
        this.c.f1150a.setVisibility(0);
        this.c.f1150a.m1399a();
        this.c.f1151b.setVisibility(8);
        this.c.m1391a(C0255a.m1143a(4));
        this.f2086j.setVisibility(8);
        m2265a(false);
        this.f2083g.playText(this.f2081b, this.f2082f, this.f2093q);
        this.f2080a = 3;
    }

    private void m2253l() {
        this.f2085i.setVisibility(0);
        this.f2089m.setImageDrawable(this.f2091o);
        this.f2089m.setEnabled(true);
        this.c.f1150a.setVisibility(8);
        this.c.f1151b.setVisibility(8);
        this.c.m1391a(C0255a.m1143a(6));
        this.f2086j.setVisibility(8);
        m2265a(false);
        this.f2080a = 1;
    }

    private void m2254m() {
        this.f2085i.setVisibility(0);
        this.f2089m.setImageDrawable(this.f2090n);
        this.c.f1150a.setVisibility(8);
        this.c.f1151b.setVisibility(8);
        this.f2086j.setVisibility(8);
        m2265a(false);
        this.f2080a = 2;
    }

    private void m2255n() {
        this.f2085i.setVisibility(0);
        this.f2088l.setProgress(0);
        this.f2089m.setImageDrawable(this.f2090n);
        this.c.f1150a.setVisibility(8);
        this.c.f1151b.setVisibility(8);
        this.f2086j.setVisibility(8);
        m2265a(false);
        this.f2080a = 0;
    }

    private void m2256o() {
        this.f2086j.setText(C0255a.m1148b(6));
        this.f2086j.setOnClickListener(new C0317g(this));
    }

    private void m2257p() {
        this.f2086j.setVisibility(8);
        m2265a(false);
    }

    private void m2258q() {
        this.f2086j.setText(C0255a.m1148b(9));
        this.f2086j.setOnClickListener(this);
    }

    public PLAY_STATE m2259a() {
        return this.f2083g.getState();
    }

    public void m2260a(int i) {
        this.f2083g.setPitch(i);
    }

    public void m2261a(RATE rate) {
        this.f2083g.setSampleRate(rate);
    }

    public void m2262a(SynthesizerDialogListener synthesizerDialogListener) {
        this.f2084h = synthesizerDialogListener;
    }

    public void m2263a(String str) {
        this.f2083g.setVoiceName(str);
    }

    public void m2264a(String str, String str2) {
        this.f2081b = str;
        this.f2082f = str2;
    }

    public void m2265a(boolean z) {
        if (z) {
            C0310a.m1403a().m1406a("rightbutton").m1409a(this.f2087k);
        } else {
            C0310a.m1403a().m1406a("entirebutton").m1409a(this.f2087k);
        }
    }

    public int m2266b(boolean z) {
        return this.f2083g.getDownflowBytes(z);
    }

    protected void m2267b() {
        super.m1375b();
        this.c.m1393b();
        C0310a.m1403a().m1406a("leftbutton").m1421d(this.f2086j);
        m2252k();
    }

    public void m2268b(int i) {
        this.f2083g.setSpeed(i);
    }

    public void m2269b(String str) {
        this.f2083g.setBackgroundSound(str);
    }

    public int m2270c(boolean z) {
        return this.f2083g.getUpflowBytes(z);
    }

    public void m2271c() {
        this.f2083g.cancel();
        super.m1376c();
    }

    public void m2272c(int i) {
        this.f2083g.setVolume(i);
    }

    protected boolean m2273d() {
        if (super.m1377d()) {
            this.f2083g.destory(MyHttpException.ERROR_SERVER);
        }
        return false;
    }

    public void m2274e() {
        this.f2083g.pause();
    }

    public void m2275f() {
        this.f2083g.resume();
    }

    protected void finalize() throws Throwable {
        if (this.f2083g != null) {
            this.f2083g.cancel();
        }
        this.f2083g = null;
        super.finalize();
    }

    public void m2276g() {
        try {
            Context context = getContext();
            LinearLayout linearLayout = (LinearLayout) C0305m.m1383a(context, "synthesizer", (ViewGroup) this);
            View view = (LinearLayout) findViewWithTag("container");
            this.c = new C0307n(context);
            view.addView(this.c, 0, new LayoutParams(-1, 0, 1.0f));
            this.f2085i = this.c.m1395c();
            this.f2088l = (ProgressBar) findViewWithTag("progressbar");
            this.f2088l.setMax(100);
            this.f2088l.setProgress(0);
            this.f2088l.setSecondaryProgress(0);
            this.f2092p = linearLayout.findViewWithTag("control");
            ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -2, 0.0f);
            layoutParams.setMargins(5, 5, 5, 5);
            this.f2092p.setLayoutParams(layoutParams);
            this.f2086j = (Button) linearLayout.findViewWithTag("retry");
            this.f2087k = (Button) linearLayout.findViewWithTag(Form.TYPE_CANCEL);
            this.f2087k.setOnClickListener(this);
            this.f2087k.setText(C0255a.m1148b(4));
            m2265a(true);
            this.f2086j.setPadding(0, 0, 0, 0);
            this.f2087k.setPadding(0, 0, 0, 0);
            Drawable[] e = C0310a.m1403a().m1406a("playbutton").m1423e(getContext());
            if (e != null) {
                this.f2090n = e[0];
                this.f2091o = e[1];
            }
            this.f2089m = (ImageButton) findViewWithTag("play");
            this.f2089m.setImageDrawable(this.f2090n);
            this.f2089m.setPadding(0, 0, 0, 0);
            this.f2089m.setOnClickListener(this);
            C0310a.m1403a().m1406a("leftbutton").m1409a(this.f2086j);
            C0310a.m1403a().m1406a("container").m1409a(view);
            C0310a.m1403a().m1406a("playbutton").m1409a(this.f2089m);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void m2277h() {
        switch (this.f2080a) {
            case KEYRecord.OWNER_USER /*0*/:
                this.f2083g.replay();
                this.f2088l.setProgress(0);
                m2253l();
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.f2083g.pause();
                m2254m();
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                this.f2083g.resume();
                m2253l();
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                m2252k();
            default:
        }
    }

    public void onClick(View view) {
        if (view == this.f2087k) {
            m1379j();
        } else {
            m2277h();
        }
    }
}
