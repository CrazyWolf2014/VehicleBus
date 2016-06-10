package cn.sharesdk.framework;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

/* renamed from: cn.sharesdk.framework.k */
class C0050k extends TextView {
    final /* synthetic */ ImageView f97a;
    final /* synthetic */ TitleLayout f98b;

    C0050k(TitleLayout titleLayout, Context context, ImageView imageView) {
        this.f98b = titleLayout;
        this.f97a = imageView;
        super(context);
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        this.f97a.setVisibility(i);
    }
}
