package cn.sharesdk.framework.authorize;

import android.os.Message;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import cn.sharesdk.framework.utils.C0053b;
import cn.sharesdk.framework.utils.UIHandler;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;

/* renamed from: cn.sharesdk.framework.authorize.j */
class C0035j extends Thread {
    final /* synthetic */ C1223g f29a;

    C0035j(C1223g c1223g) {
        this.f29a = c1223g;
    }

    public void run() {
        if (PrivacyRule.SUBSCRIPTION_NONE.equals(new C0053b(this.f29a.activity).m192l())) {
            UIHandler.sendEmptyMessage(2, this.f29a);
            return;
        }
        CookieSyncManager.createInstance(this.f29a.activity);
        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.createInstance(this.f29a.activity);
        Message message = new Message();
        message.what = 2;
        message.obj = this.f29a.a.getAuthorizeUrl();
        UIHandler.sendMessage(message, this.f29a);
    }
}
