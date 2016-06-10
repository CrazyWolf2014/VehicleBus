package cn.sharesdk.incentive;

import android.os.Message;
import cn.sharesdk.framework.p000a.C0025g;
import cn.sharesdk.framework.utils.C0051R;
import cn.sharesdk.framework.utils.C0058e;
import cn.sharesdk.framework.utils.UIHandler;
import java.io.File;

/* renamed from: cn.sharesdk.incentive.i */
class C0074i extends Thread {
    final /* synthetic */ String f132a;
    final /* synthetic */ String f133b;
    final /* synthetic */ C1029e f134c;

    C0074i(C1029e c1029e, String str, String str2) {
        this.f134c = c1029e;
        this.f132a = str;
        this.f133b = str2;
    }

    public void run() {
        Object file = new File(C0051R.getCachePath(this.f134c.activity, "apks"), this.f132a);
        if (file.exists()) {
            file.delete();
        }
        try {
            new C0025g().m39a(this.f133b, (File) file);
        } catch (Throwable th) {
            C0058e.m220b(th);
            file = null;
        }
        Message message = new Message();
        message.what = 1;
        message.obj = file;
        UIHandler.sendMessage(message, this.f134c);
    }
}
