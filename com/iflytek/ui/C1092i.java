package com.iflytek.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.iflytek.p005a.C0249f;
import com.iflytek.p005a.C0250g;
import com.iflytek.p007c.C0255a;
import com.iflytek.speech.DataUploader;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SpeechError.C0291a;
import com.iflytek.speech.SpeechListener;
import com.iflytek.ui.p015a.C0304l;
import com.iflytek.ui.p015a.C0305m;
import com.iflytek.ui.p015a.C0307n;
import com.iflytek.ui.p016b.C0310a;
import org.jivesoftware.smackx.Form;

/* renamed from: com.iflytek.ui.i */
public class C1092i extends C0304l implements OnClickListener, SpeechListener {
    private Button f2095a;
    private Button f2096b;
    private View f2097f;
    private byte[] f2098g;
    private String f2099h;
    private String f2100i;
    private DataUploader f2101j;
    private SpeechListener f2102k;

    /* renamed from: com.iflytek.ui.i.1 */
    static /* synthetic */ class C03181 {
        static final /* synthetic */ int[] f1181a;

        static {
            f1181a = new int[C0291a.values().length];
            try {
                f1181a[C0291a.RETRY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1181a[C0291a.MORE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1181a[C0291a.NETSET.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f1181a[C0291a.CANCEL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public C1092i(Context context) {
        super(context);
        this.f2101j = null;
        this.f2102k = null;
        this.f2101j = new DataUploader();
        m2278a();
    }

    private void m2278a() {
        try {
            Context context = getContext();
            View a = C0305m.m1383a(context, "synthesizer", (ViewGroup) this);
            View view = (LinearLayout) findViewWithTag("container");
            this.c = new C0307n(context);
            view.addView(this.c, 0, new LayoutParams(-1, 0, 1.0f));
            this.f2097f = a.findViewWithTag("control");
            ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -2, 0.0f);
            layoutParams.setMargins(5, 5, 5, 5);
            this.f2097f.setLayoutParams(layoutParams);
            this.f2095a = (Button) a.findViewWithTag("retry");
            this.f2095a.setVisibility(0);
            this.f2095a.setOnClickListener(this);
            this.f2096b = (Button) a.findViewWithTag(Form.TYPE_CANCEL);
            this.f2096b.setOnClickListener(this);
            this.f2096b.setText(C0255a.m1148b(4));
            m2287a(false);
            this.f2095a.setPadding(0, 0, 0, 0);
            this.f2096b.setPadding(0, 0, 0, 0);
            C0310a.m1403a().m1406a("leftbutton").m1409a(this.f2095a);
            C0310a.m1403a().m1406a("container").m1409a(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void m2282e() {
        this.f2095a.setText(C0255a.m1148b(6));
        this.f2095a.setOnClickListener(new C0319j(this));
    }

    private void m2283f() {
        this.f2095a.setText(C0255a.m1148b(9));
        this.f2095a.setOnClickListener(this);
    }

    private void m2284g() {
        this.f2095a.setVisibility(8);
        m2287a(false);
    }

    public void m2285a(SpeechListener speechListener) {
        this.f2102k = speechListener;
    }

    public void m2286a(String str, byte[] bArr, String str2) {
        this.f2100i = str;
        this.f2098g = bArr;
        this.f2099h = str2;
    }

    public void m2287a(boolean z) {
        if (z) {
            C0310a.m1403a().m1406a("rightbutton").m1409a(this.f2096b);
        } else {
            C0310a.m1403a().m1406a("entirebutton").m1409a(this.f2096b);
        }
    }

    protected void m2288b() {
        super.m1375b();
        this.c.m1393b();
        C0310a.m1403a().m1406a("leftbutton").m1421d(this.f2095a);
        this.f2101j.uploadData(getContext(), this, this.f2100i, this.f2099h, this.f2098g);
        this.c.m1391a(C0255a.m1143a(7));
        this.c.f1151b.setVisibility(8);
        this.c.f1150a.m1399a();
        this.f2095a.setVisibility(8);
        m2287a(false);
    }

    public void m2289c() {
        this.f2101j.cancel();
    }

    protected boolean m2290d() {
        return super.m1377d() ? this.f2101j.destory(1000) : false;
    }

    public void onClick(View view) {
        if (view == this.f2095a) {
            m2288b();
        } else if (view == this.f2096b) {
            m2289c();
            m1379j();
        }
    }

    public void onData(byte[] bArr) {
        this.f2102k.onData(bArr);
    }

    public void onEnd(SpeechError speechError) {
        if (speechError != null && this.d) {
            this.c.f1150a.setVisibility(8);
            this.c.f1151b.setVisibility(0);
            C0249f a = C0250g.m1098a().m1104a(getContext(), speechError, this.e);
            this.c.f1151b.m1363a(a);
            this.c.m1391a(a.m1091b());
            this.f2095a.setVisibility(0);
            m2287a(true);
            switch (C03181.f1181a[speechError.getOperation().ordinal()]) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    m2283f();
                    break;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    m2282e();
                    break;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    m2284g();
                    break;
                default:
                    break;
            }
        }
        m1379j();
        if (this.f2102k != null) {
            this.f2102k.onEnd(speechError);
        }
    }

    public void onEvent(int i, Bundle bundle) {
    }
}
