package cn.sharesdk.framework.authorize;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import cn.sharesdk.framework.utils.C0051R;
import cn.sharesdk.framework.utils.C0058e;

/* renamed from: cn.sharesdk.framework.authorize.c */
class C0029c implements OnClickListener {
    final /* synthetic */ RegisterView f19a;

    C0029c(RegisterView registerView) {
        this.f19a = registerView;
    }

    public void onClick(View view) {
        try {
            String str = "android.intent.action.VIEW";
            view.getContext().startActivity(new Intent(str, Uri.parse(view.getResources().getString(C0051R.getStringRes(view.getContext(), "website")))));
        } catch (Throwable th) {
            C0058e.m220b(th);
        }
    }
}
