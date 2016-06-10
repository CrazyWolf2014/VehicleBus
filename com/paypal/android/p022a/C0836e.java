package com.paypal.android.p022a;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.StateListDrawable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import com.googlecode.leptonica.android.Skew;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.p022a.p023a.C0830j;
import java.io.ByteArrayInputStream;
import org.achartengine.renderer.DefaultRenderer;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.paypal.android.a.e */
public final class C0836e {
    public static Drawable m1558a(int i, int i2) {
        Throwable th;
        Drawable drawable = null;
        ByteArrayInputStream byteArrayInputStream;
        try {
            byteArrayInputStream = new ByteArrayInputStream(C0838g.m1563a(i, i2));
            try {
                drawable = Drawable.createFromStream(byteArrayInputStream, XmlPullParser.NO_NAMESPACE + i + "." + i2);
                try {
                    byteArrayInputStream.close();
                } catch (Throwable th2) {
                }
            } catch (Throwable th3) {
                th = th3;
                if (byteArrayInputStream != null) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable th4) {
                    }
                }
                throw th;
            }
        } catch (Throwable th5) {
            Throwable th6 = th5;
            byteArrayInputStream = drawable;
            th = th6;
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
            throw th;
        }
        return drawable;
    }

    public static StateListDrawable m1559a() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-6733, -22016});
        Drawable gradientDrawable2 = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-211356, -1937101});
        Drawable gradientDrawable3 = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-803493, -3845098});
        Drawable gradientDrawable4 = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{2147272292, 2145546547});
        gradientDrawable.setCornerRadius(Skew.SWEEP_DELTA);
        gradientDrawable.setStroke(1, -3637191);
        gradientDrawable2.setCornerRadius(Skew.SWEEP_DELTA);
        gradientDrawable2.setStroke(1, -1858224);
        gradientDrawable3.setCornerRadius(Skew.SWEEP_DELTA);
        gradientDrawable3.setStroke(1, -3042498);
        gradientDrawable4.setCornerRadius(Skew.SWEEP_DELTA);
        gradientDrawable4.setStroke(1, 2145625424);
        stateListDrawable.addState(new int[]{16842910, -16842919, -16842908}, gradientDrawable);
        stateListDrawable.addState(new int[]{16842910, -16842919, 16842908}, gradientDrawable2);
        stateListDrawable.addState(new int[]{16842910, 16842919, -16842908}, gradientDrawable3);
        stateListDrawable.addState(new int[]{16842910, 16842919, 16842908}, gradientDrawable3);
        stateListDrawable.addState(new int[]{-16842910, -16842919, -16842908}, gradientDrawable4);
        stateListDrawable.addState(new int[]{-16842910, -16842919, 16842908}, gradientDrawable4);
        stateListDrawable.addState(new int[]{-16842910, 16842919, -16842908}, gradientDrawable4);
        stateListDrawable.addState(new int[]{-16842910, 16842919, 16842908}, gradientDrawable4);
        return stateListDrawable;
    }

    public static ImageView m1560a(Context context, String str) {
        ImageView imageView = new ImageView(context);
        Drawable a = C0836e.m1558a(((Integer) C0830j.f1554a.get(str)).intValue(), ((Integer) C0830j.f1555b.get(str)).intValue());
        imageView.setLayoutParams(new LayoutParams((int) (((double) a.getIntrinsicWidth()) * Math.pow((double) PayPal.getInstance().getDensity(), 2.0d)), (int) (((double) a.getIntrinsicHeight()) * Math.pow((double) PayPal.getInstance().getDensity(), 2.0d))));
        imageView.setImageDrawable(a);
        imageView.setScaleType(ScaleType.FIT_XY);
        return imageView;
    }

    public static StateListDrawable m1561b() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-197380, DefaultRenderer.TEXT_COLOR});
        Drawable gradientDrawable2 = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-3487030, -6645094});
        Drawable gradientDrawable3 = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-7302507, -10395036});
        Drawable gradientDrawable4 = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{2143996618, 2140838554});
        gradientDrawable.setCornerRadius(Skew.SWEEP_DELTA);
        gradientDrawable.setStroke(1, -10066330);
        gradientDrawable2.setCornerRadius(Skew.SWEEP_DELTA);
        gradientDrawable2.setStroke(1, -12303292);
        gradientDrawable3.setCornerRadius(Skew.SWEEP_DELTA);
        gradientDrawable3.setStroke(1, -13421773);
        gradientDrawable4.setCornerRadius(Skew.SWEEP_DELTA);
        gradientDrawable4.setStroke(1, 2135180356);
        stateListDrawable.addState(new int[]{16842910, -16842919, -16842908}, gradientDrawable);
        stateListDrawable.addState(new int[]{16842910, -16842919, 16842908}, gradientDrawable2);
        stateListDrawable.addState(new int[]{16842910, 16842919, -16842908}, gradientDrawable3);
        stateListDrawable.addState(new int[]{16842910, 16842919, 16842908}, gradientDrawable3);
        stateListDrawable.addState(new int[]{-16842910, -16842919, -16842908}, gradientDrawable4);
        stateListDrawable.addState(new int[]{-16842910, -16842919, 16842908}, gradientDrawable4);
        stateListDrawable.addState(new int[]{-16842910, 16842919, -16842908}, gradientDrawable4);
        stateListDrawable.addState(new int[]{-16842910, 16842919, 16842908}, gradientDrawable4);
        return stateListDrawable;
    }
}
