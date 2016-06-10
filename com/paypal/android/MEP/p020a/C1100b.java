package com.paypal.android.MEP.p020a;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.paypal.android.MEP.p020a.C1102d.C08051;
import com.paypal.android.p022a.C0835d;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C0845o;
import com.paypal.android.p022a.C0845o.C0844a;
import com.paypal.android.p024b.C0854h;
import com.paypal.android.p024b.C0858j;
import org.achartengine.renderer.DefaultRenderer;

/* renamed from: com.paypal.android.MEP.a.b */
public final class C1100b extends C0858j implements OnClickListener {
    private Button f2156a;

    public C1100b(Context context) {
        super(context);
        setId(9005);
        View linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-1, -1));
        linearLayout.setBackgroundDrawable(C0835d.m1553a());
        linearLayout.setPadding(10, 5, 10, 5);
        linearLayout.setGravity(1);
        linearLayout.addView(new C0854h(C0839h.m1568a("ANDROID_about_quickpay"), context));
        View linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new LayoutParams(-1, -2));
        linearLayout2.setPadding(0, 0, 0, 15);
        Drawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-1, -1510918, -1510918, -1510918, -1510918, -1510918});
        gradientDrawable.setCornerRadius(10.0f);
        gradientDrawable.setStroke(2, -8280890);
        linearLayout2.setBackgroundDrawable(gradientDrawable);
        View a = C0845o.m1618a(C0844a.HELVETICA_16_BOLD, context);
        a.setText(C0839h.m1568a("ANDROID_for_checkout"));
        linearLayout2.addView(a);
        a = C0845o.m1618a(C0844a.HELVETICA_16_NORMAL, context);
        a.setText(C0839h.m1568a("ANDROID_quickpay_help"));
        linearLayout2.addView(a);
        linearLayout.addView(linearLayout2);
        this.f2156a = new Button(context);
        this.f2156a.setText(C0839h.m1568a("ANDROID_ok"));
        this.f2156a.setLayoutParams(new LayoutParams(-1, C0835d.m1557b()));
        this.f2156a.setGravity(17);
        this.f2156a.setBackgroundDrawable(C0836e.m1559a());
        this.f2156a.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f2156a.setOnClickListener(this);
        linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout2.setLayoutParams(new LayoutParams(-1, -2));
        linearLayout2.setPadding(0, 15, 0, 0);
        linearLayout2.addView(this.f2156a);
        linearLayout.addView(linearLayout2);
        addView(linearLayout);
    }

    public final void m2310a() {
    }

    public final void m2311b() {
    }

    public final void onClick(View view) {
        if (view == this.f2156a) {
            C08051.m1496a();
        }
    }
}
