package cn.sharesdk.incentive;

import android.os.Message;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.C0053b;
import cn.sharesdk.framework.utils.UIHandler;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;

/* renamed from: cn.sharesdk.incentive.h */
class C0073h extends Thread {
    final /* synthetic */ C1029e f131a;

    C0073h(C1029e c1029e) {
        this.f131a = c1029e;
    }

    public void run() {
        if (PrivacyRule.SUBSCRIPTION_NONE.equals(new C0053b(this.f131a.activity).m192l())) {
            UIHandler.sendEmptyMessage(2, this.f131a);
            return;
        }
        Message message = new Message();
        message.what = 2;
        message.obj = ((Incentive) ShareSDK.getService(Incentive.class)).m1861a();
        UIHandler.sendMessage(message, this.f131a);
    }
}
