package com.paypal.android.MEP.p021b;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Build.VERSION;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.googlecode.leptonica.android.Skew;
import com.ifoer.mine.Contact;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0839h;
import org.achartengine.renderer.DefaultRenderer;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.paypal.android.MEP.b.e */
public final class C0816e extends RelativeLayout implements OnClickListener {
    private boolean f1489a;
    private int f1490b;
    private LinearLayout f1491c;
    private LinearLayout f1492d;
    private ImageView f1493e;
    private ImageView f1494f;
    private ImageView f1495g;
    private ImageView f1496h;
    private TextView f1497i;
    private EditText f1498j;
    private EditText f1499k;
    private GradientDrawable f1500l;

    public C0816e(Context context) {
        int i;
        super(context);
        PayPal instance = PayPal.getInstance();
        setLayoutParams(new LayoutParams(-1, -2));
        setPadding(5, 5, 5, 5);
        this.f1491c = new LinearLayout(context);
        this.f1491c.setId(5001);
        this.f1491c.setBackgroundColor(0);
        this.f1491c.setOrientation(0);
        this.f1491c.setGravity(3);
        this.f1491c.setPadding(5, 0, 5, 0);
        if (instance.isLightCountry()) {
            i = 0;
        } else {
            this.f1493e = C0836e.m1560a(context, "tab-selected-email.png");
            this.f1493e.setPadding(5, 0, 5, 0);
            this.f1493e.setFocusable(false);
            this.f1491c.addView(this.f1493e);
            this.f1494f = C0836e.m1560a(context, "tab-unselected-email.png");
            this.f1494f.setPadding(5, 0, 5, 0);
            this.f1494f.setOnClickListener(this);
            this.f1494f.setFocusable(false);
            this.f1491c.addView(this.f1494f);
            this.f1495g = C0836e.m1560a(context, "tab-selected-phone.png");
            this.f1495g.setPadding(5, 0, 5, 0);
            this.f1495g.setFocusable(false);
            this.f1491c.addView(this.f1495g);
            this.f1496h = C0836e.m1560a(context, "tab-unselected-phone.png");
            this.f1496h.setPadding(5, 0, 5, 0);
            this.f1496h.setOnClickListener(this);
            this.f1496h.setFocusable(false);
            this.f1491c.addView(this.f1496h);
            i = -1;
        }
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.addRule(10);
        layoutParams.addRule(9);
        this.f1491c.setLayoutParams(layoutParams);
        this.f1492d = new LinearLayout(context);
        this.f1492d.setId(5002);
        this.f1500l = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-789517, DefaultRenderer.TEXT_COLOR});
        this.f1500l.setCornerRadius(Skew.SWEEP_DELTA);
        this.f1500l.setStroke(1, -5197648);
        this.f1492d.setBackgroundDrawable(this.f1500l);
        this.f1492d.setOrientation(1);
        this.f1492d.setGravity(3);
        this.f1492d.setPadding(10, 5, 10, 5);
        layoutParams = new LayoutParams(-1, -2);
        layoutParams.addRule(5, this.f1491c.getId());
        layoutParams.addRule(3, this.f1491c.getId());
        layoutParams.setMargins(0, i, 0, 0);
        this.f1492d.setLayoutParams(layoutParams);
        this.f1497i = new TextView(context);
        this.f1497i.setId(5003);
        this.f1497i.setTypeface(Typeface.create("Helvetica", 1));
        this.f1497i.setTextSize(16.0f);
        this.f1497i.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        ViewGroup.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams2.setMargins(0, 2, 0, 2);
        this.f1497i.setLayoutParams(layoutParams2);
        this.f1492d.addView(this.f1497i);
        this.f1498j = new EditText(context);
        this.f1498j.setId(5004);
        this.f1498j.setLayoutParams(layoutParams2);
        this.f1498j.setSingleLine(true);
        this.f1492d.addView(this.f1498j);
        this.f1499k = new EditText(context);
        this.f1499k.setId(5005);
        this.f1499k.setLayoutParams(layoutParams2);
        this.f1499k.setSingleLine(true);
        this.f1492d.addView(this.f1499k);
        addView(this.f1491c);
        addView(this.f1492d);
        if (!VERSION.SDK.equals(Contact.RELATION_NODONE)) {
            bringChildToFront(this.f1491c);
        }
        PayPal.logd("LoginWidget", "Setup login tab, authMethod " + Integer.toString(instance.getAuthMethod()));
        if (instance.getAuthMethod() == 1 && instance.isHeavyCountry()) {
            this.f1489a = false;
            m1506b(instance);
            return;
        }
        m1505a(instance);
    }

    private void m1505a(PayPal payPal) {
        PayPal.logd("LoginWidget", "Setup login tab for email");
        if (!payPal.isLightCountry()) {
            this.f1493e.setVisibility(0);
            this.f1494f.setVisibility(8);
            this.f1495g.setVisibility(8);
            this.f1496h.setVisibility(0);
        }
        this.f1489a = true;
        this.f1497i.setText(C0839h.m1568a("ANDROID_login_with_email_and_password") + ":");
        this.f1498j.setVisibility(0);
        this.f1498j.setHint(C0839h.m1568a("ANDROID_email_field"));
        this.f1498j.setInputType(32);
        this.f1498j.setText(payPal.getAccountEmail());
        this.f1499k.setText(XmlPullParser.NO_NAMESPACE);
        this.f1499k.setHint(C0839h.m1568a("ANDROID_password_field"));
        this.f1499k.setInputType(Flags.FLAG8);
        this.f1499k.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.f1490b = 0;
    }

    private void m1506b(PayPal payPal) {
        int i = 0;
        PayPal.logd("LoginWidget", "Setup login tab for PIN");
        if (!payPal.isLightCountry()) {
            this.f1493e.setVisibility(8);
            this.f1494f.setVisibility(0);
            this.f1495g.setVisibility(0);
            this.f1496h.setVisibility(8);
        }
        if (this.f1489a) {
            this.f1497i.setText(C0839h.m1568a("ANDROID_login_with_phone_and_pin") + ":");
        } else {
            this.f1497i.setText(C0839h.m1568a("ANDROID_login_with_pin") + ":");
        }
        EditText editText = this.f1498j;
        if (!this.f1489a) {
            i = 8;
        }
        editText.setVisibility(i);
        this.f1498j.setHint(C0839h.m1568a("ANDROID_phone_field"));
        this.f1498j.setInputType(3);
        this.f1498j.setText(payPal.getAccountPhone());
        this.f1499k.setText(XmlPullParser.NO_NAMESPACE);
        this.f1499k.setHint(C0839h.m1568a("ANDROID_pin_field"));
        this.f1499k.setInputType(3);
        this.f1499k.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.f1490b = 1;
    }

    public final String m1507a() {
        return this.f1498j.getText().toString().replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE);
    }

    public final String m1508b() {
        return this.f1499k.getText().toString();
    }

    public final EditText m1509c() {
        return this.f1498j;
    }

    public final EditText m1510d() {
        return this.f1499k;
    }

    public final void m1511e() {
        try {
            ((InputMethodManager) PayPalActivity.getInstance().getSystemService("input_method")).hideSoftInputFromWindow(this.f1498j.getWindowToken(), 0);
        } catch (Exception e) {
        }
        try {
            ((InputMethodManager) PayPalActivity.getInstance().getSystemService("input_method")).hideSoftInputFromWindow(this.f1499k.getWindowToken(), 0);
        } catch (Exception e2) {
        }
    }

    public final void onClick(View view) {
        if (view == this.f1494f) {
            if (this.f1490b != 0) {
                m1505a(PayPal.getInstance());
            }
        } else if (view == this.f1496h && this.f1490b != 1) {
            m1506b(PayPal.getInstance());
        }
    }
}
