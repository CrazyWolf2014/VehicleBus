package com.paypal.android.p024b;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0845o;
import com.paypal.android.p022a.C0845o.C0844a;

/* renamed from: com.paypal.android.b.i */
public final class C0857i extends LinearLayout {
    private Context f1599a;
    private GradientDrawable f1600b;
    private ImageView f1601c;
    private TextView f1602d;

    /* renamed from: com.paypal.android.b.i.1 */
    static /* synthetic */ class C08551 {
        static final /* synthetic */ int[] f1593a;

        static {
            f1593a = new int[C0856a.values().length];
            try {
                f1593a[C0856a.RED_ALERT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1593a[C0856a.YELLOW_ALERT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1593a[C0856a.GREEN_ALERT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f1593a[C0856a.BLUE_ALERT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    /* renamed from: com.paypal.android.b.i.a */
    public enum C0856a {
        RED_ALERT,
        YELLOW_ALERT,
        GREEN_ALERT,
        BLUE_ALERT
    }

    public C0857i(Context context, C0856a c0856a) {
        super(context);
        this.f1599a = context;
        setOrientation(0);
        setLayoutParams(new LayoutParams(-1, -2));
        setGravity(16);
        View linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(17);
        linearLayout.setPadding(5, 10, 5, 10);
        switch (C08551.f1593a[c0856a.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.f1600b = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-3172, -52});
                this.f1600b.setStroke(1, -12529);
                this.f1600b.setCornerRadius(3.0f);
                setBackgroundDrawable(this.f1600b);
                this.f1601c = C0836e.m1560a(this.f1599a, "system-icon-error.png");
                this.f1602d = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                this.f1602d.setTextColor(-65536);
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                this.f1600b = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-3172, -52});
                this.f1600b.setStroke(1, -12529);
                this.f1600b.setCornerRadius(3.0f);
                setBackgroundDrawable(this.f1600b);
                this.f1601c = C0836e.m1560a(this.f1599a, "system-icon-alert.png");
                this.f1602d = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                this.f1602d.setTextColor(-65536);
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                this.f1600b = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-3219987, -1510918});
                this.f1600b.setStroke(1, -8280890);
                this.f1600b.setCornerRadius(3.0f);
                setBackgroundDrawable(this.f1600b);
                this.f1601c = C0836e.m1560a(this.f1599a, "system-icon-confirmation.png");
                this.f1602d = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                this.f1602d.setTextColor(-13408768);
                break;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                this.f1600b = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-3219987, -1510918});
                this.f1600b.setStroke(1, -8280890);
                this.f1600b.setCornerRadius(3.0f);
                setBackgroundDrawable(this.f1600b);
                this.f1601c = C0836e.m1560a(this.f1599a, "system-icon-notification.png");
                this.f1602d = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                this.f1602d.setTextColor(-13408615);
                break;
        }
        linearLayout.addView(this.f1601c);
        addView(linearLayout);
        addView(this.f1602d);
    }

    public final void m1635a(String str) {
        this.f1602d.setText(str);
    }
}
