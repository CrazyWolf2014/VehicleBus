package com.iflytek.msc.p008a;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.iflytek.msc.p008a.C0262c.C1068a;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.speech.SpeechError;
import org.xbill.DNS.KEYRecord;

/* renamed from: com.iflytek.msc.a.e */
final class C0264e extends Handler {
    final /* synthetic */ C1068a f1007a;

    C0264e(C1068a c1068a, Looper looper) {
        this.f1007a = c1068a;
        super(looper);
    }

    public void handleMessage(Message message) {
        if (this.f1007a.f1972b != null) {
            C0276e.m1220a("SpeechListener onMsg = " + message.what);
            switch (message.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    this.f1007a.f1972b.onEvent(message.arg1, (Bundle) message.obj);
                    break;
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    this.f1007a.f1972b.onData((byte[]) message.obj);
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    this.f1007a.f1972b.onEnd((SpeechError) message.obj);
                    break;
            }
            super.handleMessage(message);
        }
    }
}
