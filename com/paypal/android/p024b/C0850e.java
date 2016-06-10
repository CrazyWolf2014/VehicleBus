package com.paypal.android.p024b;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.paypal.android.p022a.C0836e;

/* renamed from: com.paypal.android.b.e */
public final class C0850e extends LinearLayout {
    private AnimationDrawable f1586a;

    public C0850e(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(-2, -2));
        setPadding(10, 10, 10, 10);
        View linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LayoutParams(52, 29));
        View imageView = new ImageView(context);
        imageView.setLayoutParams(new LayoutParams(-1, -1));
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.addFrame(C0836e.m1558a(38862, 5362), 50);
        animationDrawable.addFrame(C0836e.m1558a(171068, 5447), 50);
        animationDrawable.addFrame(C0836e.m1558a(106633, 5446), 50);
        animationDrawable.addFrame(C0836e.m1558a(70013, 5475), 50);
        animationDrawable.addFrame(C0836e.m1558a(20096, 5400), 50);
        animationDrawable.addFrame(C0836e.m1558a(149433, 5270), 50);
        animationDrawable.addFrame(C0836e.m1558a(92990, 5423), 50);
        animationDrawable.addFrame(C0836e.m1558a(55698, 5485), 50);
        animationDrawable.addFrame(C0836e.m1558a(7623, 5470), 50);
        animationDrawable.addFrame(C0836e.m1558a(127767, 5428), 50);
        animationDrawable.addFrame(C0836e.m1558a(119431, 5415), 50);
        animationDrawable.addFrame(C0836e.m1558a(80891, 5367), 50);
        animationDrawable.addFrame(C0836e.m1558a(33070, 5362), 50);
        animationDrawable.addFrame(C0836e.m1558a(163774, 5399), 50);
        animationDrawable.addFrame(C0836e.m1558a(100757, 5412), 50);
        animationDrawable.addFrame(C0836e.m1558a(64110, 5470), 50);
        animationDrawable.addFrame(C0836e.m1558a(14681, 5415), 50);
        animationDrawable.addFrame(C0836e.m1558a(134029, 5357), 50);
        animationDrawable.addFrame(C0836e.m1558a(86845, 5303), 50);
        animationDrawable.addFrame(C0836e.m1558a(50173, 5525), 50);
        animationDrawable.addFrame(C0836e.m1558a(44224, 5524), 50);
        animationDrawable.addFrame(C0836e.m1558a(0, 5487), 50);
        animationDrawable.addFrame(C0836e.m1558a(113975, 5456), 50);
        animationDrawable.addFrame(C0836e.m1558a(75488, 5403), 50);
        animationDrawable.addFrame(C0836e.m1558a(27818, 5252), 50);
        animationDrawable.setOneShot(false);
        animationDrawable.setVisible(true, true);
        this.f1586a = animationDrawable;
        imageView.setBackgroundDrawable(this.f1586a);
        linearLayout.addView(imageView);
        addView(linearLayout);
    }

    public final void m1624a() {
        this.f1586a.stop();
        this.f1586a.start();
    }

    public final void m1625b() {
        this.f1586a.stop();
    }
}
