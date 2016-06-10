package cn.sharesdk.framework.authorize;

import android.content.Context;
import android.content.Intent;
import cn.sharesdk.framework.FakeActivity;

/* renamed from: cn.sharesdk.framework.authorize.a */
public class C1014a extends FakeActivity {
    protected AuthorizeHelper f1738a;

    public AuthorizeHelper m1789a() {
        return this.f1738a;
    }

    public void m1790a(AuthorizeHelper authorizeHelper) {
        this.f1738a = authorizeHelper;
        super.show(authorizeHelper.getPlatform().getContext(), null);
    }

    public void show(Context context, Intent intent) {
        throw new RuntimeException("This method is deprecated, use show(AuthorizeHelper, Intent) instead");
    }
}
