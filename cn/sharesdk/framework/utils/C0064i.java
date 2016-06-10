package cn.sharesdk.framework.utils;

import android.os.Handler.Callback;
import android.os.Message;

/* renamed from: cn.sharesdk.framework.utils.i */
final class C0064i implements Callback {
    C0064i() {
    }

    public boolean handleMessage(Message message) {
        UIHandler.handleMessage(message);
        return false;
    }
}
