package com.paypal.android.p024b;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import com.ifoer.entity.Constant;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.p022a.C0836e;

/* renamed from: com.paypal.android.b.j */
public abstract class C0858j extends RelativeLayout {
    private ScrollView f1603a;
    private LinearLayout f1604b;

    public C0858j(Context context) {
        int i = 640;
        int i2 = Constant.TIME_OUT;
        super(context);
        int width = PayPalActivity.getInstance().getWindowManager().getDefaultDisplay().getWidth();
        int height = PayPalActivity.getInstance().getWindowManager().getDefaultDisplay().getHeight();
        if ((width <= height || height < Constant.TIME_OUT) && (height <= width || width < Constant.TIME_OUT)) {
            i2 = 640;
            i = 480;
        }
        if (width > height && height > i) {
            setPadding((width - i2) / 2, (height - i) / 2, (width - i2) / 2, (height - i) / 2);
        } else if (height <= width || width <= i) {
            setPadding(10, 10, 10, 10);
        } else {
            setPadding((width - i) / 2, (height - i2) / 2, (width - i) / 2, (height - i2) / 2);
        }
        setBackgroundColor(2130706432);
        m1637a(context);
    }

    public abstract void m1636a();

    protected void m1637a(Context context) {
        this.f1603a = new ScrollView(context);
        this.f1603a.setLayoutParams(new LayoutParams(-1, -1));
        Drawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-1, -987685, -987685, -987685});
        gradientDrawable.setCornerRadius(10.0f);
        gradientDrawable.setGradientRadius(10.0f);
        this.f1603a.setBackgroundDrawable(gradientDrawable);
        this.f1603a.setFillViewport(true);
        super.addView(this.f1603a);
        this.f1604b = new LinearLayout(context);
        this.f1604b.setOrientation(1);
        this.f1604b.setLayoutParams(new LayoutParams(-1, -1));
        this.f1604b.setBackgroundColor(0);
        this.f1603a.addView(this.f1604b);
        View linearLayout;
        ViewGroup.LayoutParams layoutParams;
        if (PayPal.getInstance().getServer() == 0) {
            linearLayout = new LinearLayout(PayPalActivity.getInstance());
            layoutParams = new LayoutParams(-2, -2);
            layoutParams.addRule(12);
            layoutParams.addRule(11);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.addView(C0836e.m1560a(context, "banner-sandbox.png"));
            super.addView(linearLayout);
        } else if (PayPal.getInstance().getServer() == 2) {
            linearLayout = new LinearLayout(PayPalActivity.getInstance());
            layoutParams = new LayoutParams(-2, -2);
            layoutParams.addRule(12);
            layoutParams.addRule(11);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.addView(C0836e.m1560a(context, "banner-demo.png"));
            super.addView(linearLayout);
        }
    }

    public void addView(View view) {
        this.f1604b.addView(view);
    }

    public abstract void m1638b();

    protected void onMeasure(int i, int i2) {
        int i3 = 640;
        int i4 = Constant.TIME_OUT;
        int i5 = 10;
        int size = MeasureSpec.getSize(i);
        int height = PayPalActivity.getInstance().getWindowManager().getDefaultDisplay().getHeight();
        Object obj = height - MeasureSpec.getSize(i2) > 100 ? 1 : null;
        if ((size <= height || height < Constant.TIME_OUT) && (height <= size || size < Constant.TIME_OUT)) {
            i4 = 640;
            i3 = 480;
        }
        int i6;
        int i7;
        if (size > height && height > i3) {
            i6 = (size - i4) / 2;
            i7 = (height - i3) / 2;
            i4 = (size - i4) / 2;
            if (obj == null) {
                i5 = (height - i3) / 2;
            }
            setPadding(i6, i7, i4, i5);
        } else if (height <= size || size <= i3) {
            setPadding(10, 10, 10, 10);
        } else {
            i6 = (size - i3) / 2;
            i7 = (height - i4) / 2;
            i3 = (size - i3) / 2;
            if (obj == null) {
                i5 = (height - i4) / 2;
            }
            setPadding(i6, i7, i3, i5);
        }
        super.onMeasure(i, i2);
    }
}
