package com.paypal.android.MEP;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.StateListDrawable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.paypal.android.MEP.p020a.C1102d;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C1107b;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public final class CheckoutButton extends LinearLayout implements OnClickListener {
    public static final int TEXT_DONATE = 1;
    public static final int TEXT_PAY = 0;
    private static boolean f1345p;
    private int f1346a;
    private int f1347b;
    private int f1348c;
    private int f1349d;
    private boolean f1350e;
    private LinearLayout f1351f;
    private GradientDrawable f1352g;
    private TextView f1353h;
    private TextView f1354i;
    private LinearLayout f1355j;
    private Context f1356k;
    private StateListDrawable f1357l;
    private TextView f1358m;
    private TextView f1359n;
    private ImageView f1360o;

    static {
        f1345p = false;
    }

    public CheckoutButton(Context context) {
        super(context);
        this.f1356k = context;
    }

    protected final void m1480a(int i, int i2) {
        int i3;
        float f;
        int i4;
        this.f1346a = i;
        switch (this.f1346a) {
            case KEYRecord.OWNER_USER /*0*/:
                this.f1347b = Opcodes.DCMPG;
                this.f1348c = 33;
                this.f1349d = 18;
                i3 = 22;
                f = 6.0f;
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                this.f1347b = 278;
                this.f1348c = 43;
                this.f1349d = 22;
                i3 = 30;
                f = 10.0f;
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                this.f1347b = 294;
                this.f1348c = 45;
                this.f1349d = 28;
                i3 = 40;
                f = 10.0f;
                break;
            default:
                this.f1347b = Wbxml.EXT_2;
                this.f1348c = 37;
                this.f1349d = 20;
                i3 = 22;
                f = 6.0f;
                break;
        }
        Typeface create = Typeface.create("Helvetica", 0);
        float f2 = (2.5f * ((float) this.f1346a)) + 10.0f;
        float f3 = ((float) this.f1349d) - 8.0f;
        float density = PayPal.getInstance().getDensity();
        this.f1347b = (int) (((float) this.f1347b) * density);
        this.f1348c = (int) (((float) this.f1348c) * density);
        this.f1349d = (int) (density * ((float) this.f1349d));
        setOrientation(TEXT_DONATE);
        setPadding(0, 0, 0, 0);
        this.f1352g = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-1, -1});
        this.f1352g.setSize(this.f1347b, this.f1348c + this.f1349d);
        this.f1352g.setCornerRadius(f);
        this.f1352g.setStroke(TEXT_DONATE, -5789785);
        this.f1351f = new LinearLayout(this.f1356k);
        this.f1351f.setLayoutParams(new LayoutParams(-1, this.f1349d));
        this.f1351f.setOrientation(0);
        this.f1351f.setPadding(0, 2, 0, 0);
        this.f1353h = new TextView(this.f1356k);
        this.f1353h.setLayoutParams(new LayoutParams(-1, -1, 1.0f));
        this.f1353h.setGravity(3);
        this.f1353h.setPadding(5, 0, 0, 0);
        this.f1353h.setTypeface(create, TEXT_DONATE);
        this.f1353h.setTextSize(f3);
        this.f1353h.setTextColor(-15066598);
        this.f1353h.setSingleLine(true);
        this.f1353h.setText(XmlPullParser.NO_NAMESPACE);
        this.f1353h.setOnClickListener(this);
        this.f1354i = new TextView(this.f1356k);
        this.f1354i.setLayoutParams(new LayoutParams(-2, -1));
        this.f1354i.setGravity(5);
        this.f1354i.setPadding(0, 0, 5, 0);
        this.f1354i.setTypeface(create);
        this.f1354i.setTextSize(f3);
        this.f1354i.setTextColor(-14993820);
        this.f1354i.setFocusable(true);
        CharSequence spannableString = new SpannableString(C0839h.m1568a("ANDROID_not_you"));
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        this.f1354i.setText(spannableString);
        this.f1354i.setOnClickListener(this);
        this.f1351f.addView(this.f1353h);
        this.f1351f.addView(this.f1354i);
        addView(this.f1351f);
        this.f1355j = new LinearLayout(this.f1356k);
        this.f1355j.setLayoutParams(new LayoutParams(-1, -1));
        this.f1355j.setGravity(17);
        this.f1355j.setOrientation(0);
        this.f1355j.setPadding(2, 2, 2, 2);
        this.f1355j.setOnClickListener(this);
        Drawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-4922, -22016});
        gradientDrawable.setSize(this.f1347b, this.f1348c);
        gradientDrawable.setCornerRadius(f);
        gradientDrawable.setStroke(TEXT_DONATE, -3637191);
        Drawable gradientDrawable2 = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-12951, -1937101});
        gradientDrawable2.setSize(this.f1347b, this.f1348c);
        gradientDrawable2.setCornerRadius(f);
        gradientDrawable2.setStroke(TEXT_DONATE, -3637191);
        this.f1357l = new StateListDrawable();
        StateListDrawable stateListDrawable = this.f1357l;
        int[] iArr = new int[TEXT_DONATE];
        iArr[0] = -16842919;
        stateListDrawable.addState(iArr, gradientDrawable);
        stateListDrawable = this.f1357l;
        int[] iArr2 = new int[TEXT_DONATE];
        iArr2[0] = 16842919;
        stateListDrawable.addState(iArr2, gradientDrawable2);
        this.f1355j.setBackgroundDrawable(this.f1357l);
        String str = i2 == TEXT_DONATE ? "donate" : "pay";
        String toLowerCase = PayPal.getInstance().getLanguage().toLowerCase();
        String substring = toLowerCase.substring(0, 2);
        String[] split = C0839h.m1568a("ANDROID_" + str + "_button").split("%PP", -1);
        for (i4 = 0; i4 < split.length; i4 += TEXT_DONATE) {
            int indexOf = split[0].indexOf("\\n");
            if (indexOf != -1) {
                split[i4] = split[i4].substring(0, indexOf) + '\n' + split[i4].substring(indexOf + 2);
            }
        }
        if (substring.equals(LocaleUtil.POLISH) || (substring.equals("fr") && str.equals("donate"))) {
            f = f2 - (2.0f + (0.5f * ((float) this.f1346a)));
            i4 = 3;
        } else if (substring.equals("zh") || substring.equals("jp")) {
            f = (2.0f + (0.5f * ((float) this.f1346a))) + f2;
            i4 = TEXT_DONATE;
        } else {
            f = f2;
            i4 = 3;
        }
        this.f1358m = new TextView(this.f1356k);
        if (split[0].equals(XmlPullParser.NO_NAMESPACE)) {
            this.f1358m.setVisibility(8);
        } else {
            this.f1358m.setText(split[0]);
            this.f1358m.setTypeface(create, i4);
            this.f1358m.setTextSize(f);
            this.f1358m.setTextColor(-14993820);
            this.f1358m.setGravity(17);
            this.f1358m.setVisibility(0);
        }
        this.f1359n = new TextView(this.f1356k);
        if (split.length <= TEXT_DONATE || split[TEXT_DONATE].equals(XmlPullParser.NO_NAMESPACE)) {
            this.f1359n.setVisibility(8);
        } else {
            this.f1359n.setText(split[TEXT_DONATE]);
            this.f1359n.setTypeface(create, i4);
            this.f1359n.setTextSize(f);
            this.f1359n.setTextColor(-14993820);
            if (split[0].equals(XmlPullParser.NO_NAMESPACE)) {
                this.f1359n.setGravity(17);
            }
            this.f1359n.setVisibility(0);
        }
        str = "paypal_";
        if (toLowerCase.equals("zh_hk")) {
            str = str + "cn_";
        }
        this.f1360o = C0836e.m1560a(this.f1356k, str + "logo_" + i3 + ".png");
        this.f1360o.setVisibility(0);
        this.f1355j.addView(this.f1358m);
        this.f1355j.addView(this.f1360o);
        this.f1355j.addView(this.f1359n);
        addView(this.f1355j);
        updateButton();
        setSelected(false);
    }

    public final void onClick(View view) {
        if (!f1345p && view != this.f1353h) {
            if (view == this.f1354i) {
                PayPal.logd("CheckoutButton", "reset the account");
                PayPal.getInstance().resetAccount();
                C1107b.m2383e().m2417a(12);
                C1102d.f2162a = true;
            } else if (view == this.f1355j) {
                C1102d.f2162a = false;
            }
            f1345p = true;
            performClick();
            setActive(false);
        }
    }

    public final void setActive(boolean z) {
        setClickable(z);
        setFocusable(z);
    }

    public final void updateButton() {
        boolean z = true;
        PayPal instance = PayPal.getInstance();
        f1345p = false;
        updateName();
        if (!(instance.getIsRememberMe() && instance.getAuthSetting() == TEXT_DONATE)) {
            z = false;
        }
        this.f1350e = z;
        if (!this.f1350e || this.f1353h.getText().toString().length() <= 0) {
            this.f1351f.setVisibility(8);
            setMinimumWidth(this.f1347b);
            setMinimumHeight(this.f1348c);
            setBackgroundColor(0);
        } else {
            this.f1351f.setVisibility(0);
            setMinimumWidth(this.f1347b);
            setMinimumHeight(this.f1348c + this.f1349d);
            setBackgroundDrawable(this.f1352g);
        }
        invalidate();
    }

    public final void updateName() {
        CharSequence accountName = PayPal.getInstance().getAccountName();
        if (accountName.length() > 21) {
            accountName = accountName.substring(0, 21) + "...";
        }
        this.f1353h.setText(accountName);
    }
}
