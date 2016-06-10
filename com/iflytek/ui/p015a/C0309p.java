package com.iflytek.ui.p015a;

import android.os.Handler;
import android.os.Message;
import com.google.protobuf.DescriptorProtos.MessageOptions;

/* renamed from: com.iflytek.ui.a.p */
final class C0309p extends Handler {
    final /* synthetic */ C0308o f1164a;

    C0309p(C0308o c0308o) {
        this.f1164a = c0308o;
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.f1164a.invalidate();
                if (this.f1164a.f1157a != null) {
                    this.f1164a.f1157a.m1368b();
                    int a = this.f1164a.f1157a.m1366a();
                    if (a > 0) {
                        this.f1164a.f1159c.removeMessages(1);
                        this.f1164a.f1159c.sendEmptyMessageDelayed(1, (long) a);
                    }
                }
            default:
        }
    }
}
