package com.paypal.android.MEP.p020a;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.p020a.C1102d.C08051;
import com.paypal.android.p022a.C0835d;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C0845o;
import com.paypal.android.p022a.C0845o.C0844a;
import com.paypal.android.p022a.C1107b;
import com.paypal.android.p024b.C0854h;
import com.paypal.android.p024b.C0858j;
import org.achartengine.renderer.DefaultRenderer;
import org.xbill.DNS.KEYRecord;

/* renamed from: com.paypal.android.MEP.a.f */
public final class C1104f extends C0858j implements OnClickListener {
    private Button f2197a;

    public C1104f(Context context) {
        super(context);
    }

    public final void m2342a() {
    }

    protected final void m2343a(Context context) {
        String str;
        super.m1637a(context);
        setId(9006);
        View linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-1, -1));
        linearLayout.setBackgroundDrawable(C0835d.m1553a());
        linearLayout.setPadding(10, 5, 10, 5);
        linearLayout.setGravity(1);
        linearLayout.addView(new C0854h(C0839h.m1568a("ANDROID_help"), context));
        View linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new LayoutParams(-1, -2));
        linearLayout2.setPadding(0, 0, 0, 15);
        Drawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-1, -1510918, -1510918, -1510918, -1510918, -1510918});
        gradientDrawable.setCornerRadius(10.0f);
        gradientDrawable.setStroke(2, -8280890);
        linearLayout2.setBackgroundDrawable(gradientDrawable);
        View a = C0845o.m1618a(C0844a.HELVETICA_16_BOLD, context);
        a.setText(C0839h.m1568a("ANDROID_about_paypal"));
        linearLayout2.addView(a);
        a = C0845o.m1618a(C0844a.HELVETICA_16_NORMAL, context);
        a.setText(C0839h.m1568a("ANDROID_help_string"));
        Linkify.addLinks(a, 1);
        linearLayout2.addView(a);
        a = C0845o.m1618a(C0844a.HELVETICA_16_BOLD, context);
        a.setText(C0839h.m1568a("ANDROID_sign_up"));
        linearLayout2.addView(a);
        a = C0845o.m1618a(C0844a.HELVETICA_16_NORMAL, context);
        a.setText(C0839h.m1568a("ANDROID_no_account"));
        Linkify.addLinks(a, 1);
        linearLayout2.addView(a);
        a = C0845o.m1618a(C0844a.HELVETICA_16_BOLD, context);
        a.setText(C0839h.m1568a("ANDROID_forgot_password"));
        linearLayout2.addView(a);
        a = C0845o.m1618a(C0844a.HELVETICA_16_NORMAL, context);
        a.setText(C0839h.m1568a("ANDROID_forgot_password_body"));
        Linkify.addLinks(a, 1);
        linearLayout2.addView(a);
        if (PayPal.getInstance().shouldShowFees()) {
            a = C0845o.m1618a(C0844a.HELVETICA_16_BOLD, context);
            a.setText(C0839h.m1568a("ANDROID_help_fees_header"));
            linearLayout2.addView(a);
            a = C0845o.m1618a(C0844a.HELVETICA_16_NORMAL, context);
            a.setText(C0839h.m1568a("ANDROID_help_fees_body"));
            Linkify.addLinks(a, 1);
            linearLayout2.addView(a);
        }
        TelephonyManager telephonyManager = (TelephonyManager) PayPal.getInstance().getParentContext().getSystemService("phone");
        String str2 = telephonyManager.getPhoneType() == 1 ? "IMEI" : "MEID";
        View a2 = C0845o.m1618a(C0844a.HELVETICA_16_BOLD, context);
        a2.setPadding(5, 1, 5, 2);
        a2.setText(C0839h.m1568a("ANDROID_debug_support"));
        linearLayout2.addView(a2);
        a2 = C0845o.m1618a(C0844a.HELVETICA_14_NORMAL, context);
        a2.setPadding(5, 1, 5, 2);
        a2.setText(C0839h.m1568a("ANDROID_debug_version") + ": " + PayPal.getVersionWithoutBuild());
        linearLayout2.addView(a2);
        a2 = C0845o.m1618a(C0844a.HELVETICA_14_NORMAL, context);
        a2.setPadding(5, 1, 5, 2);
        a2.setText(C0839h.m1568a("ANDROID_debug_build") + ": " + PayPal.getBuild());
        linearLayout2.addView(a2);
        a2 = C0845o.m1618a(C0844a.HELVETICA_14_NORMAL, context);
        a2.setPadding(5, 1, 5, 2);
        a2.setText(C0839h.m1568a("ANDROID_debug_platform") + ": " + "Android");
        linearLayout2.addView(a2);
        a2 = C0845o.m1618a(C0844a.HELVETICA_14_NORMAL, context);
        a2.setPadding(5, 1, 5, 2);
        a2.setText(C0839h.m1568a("ANDROID_debug_model") + ": " + Build.MODEL);
        linearLayout2.addView(a2);
        a2 = C0845o.m1618a(C0844a.HELVETICA_14_NORMAL, context);
        a2.setPadding(5, 1, 5, 2);
        a2.setText(C0839h.m1568a("ANDROID_debug_os") + ": " + VERSION.RELEASE);
        linearLayout2.addView(a2);
        switch (PayPal.getInstance().getServer()) {
            case KEYRecord.OWNER_USER /*0*/:
                str = "Sandbox";
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                str = "Demo";
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                str = "Stage (" + C1107b.m2376b() + ")";
                break;
            default:
                str = "Live";
                break;
        }
        View a3 = C0845o.m1618a(C0844a.HELVETICA_14_NORMAL, context);
        a3.setPadding(5, 1, 5, 2);
        a3.setText(C0839h.m1568a("ANDROID_debug_server") + ": " + str);
        linearLayout2.addView(a3);
        a2 = C0845o.m1618a(C0844a.HELVETICA_14_NORMAL, context);
        a2.setPadding(5, 1, 5, 2);
        a2.setText(str2 + ": " + telephonyManager.getDeviceId());
        linearLayout2.addView(a2);
        linearLayout.addView(linearLayout2);
        this.f2197a = new Button(context);
        this.f2197a.setText(C0839h.m1568a("ANDROID_ok"));
        this.f2197a.setLayoutParams(new LayoutParams(-1, C0835d.m1557b()));
        this.f2197a.setGravity(17);
        this.f2197a.setBackgroundDrawable(C0836e.m1559a());
        this.f2197a.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f2197a.setOnClickListener(this);
        a = new LinearLayout(context);
        a.setOrientation(1);
        a.setLayoutParams(new LayoutParams(-1, -2));
        a.setPadding(0, 15, 0, 0);
        a.addView(this.f2197a);
        linearLayout.addView(a);
        addView(linearLayout);
    }

    public final void m2344b() {
    }

    public final void onClick(View view) {
        if (view == this.f2197a) {
            C08051.m1496a();
        }
    }
}
