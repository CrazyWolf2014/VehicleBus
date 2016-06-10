package com.iflytek.record;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.iflytek.speech.SpeechError;
import org.xbill.DNS.KEYRecord;

/* renamed from: com.iflytek.record.b */
final class C0288b extends Handler {
    final /* synthetic */ PcmPlayer f1087a;

    C0288b(PcmPlayer pcmPlayer, Looper looper) {
        this.f1087a = pcmPlayer;
        super(looper);
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case KEYRecord.OWNER_USER /*0*/:
                if (this.f1087a.f1064e != null) {
                    this.f1087a.f1064e.m1269a((SpeechError) message.obj);
                    this.f1087a.f1064e = null;
                }
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                if (this.f1087a.f1064e != null) {
                    this.f1087a.f1064e.m1267a();
                }
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (this.f1087a.f1064e != null) {
                    this.f1087a.f1064e.m1270b();
                }
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                if (this.f1087a.f1064e != null) {
                    this.f1087a.f1064e.m1268a(message.arg1, message.arg2, this.f1087a.f1068i);
                }
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                if (this.f1087a.f1064e != null) {
                    this.f1087a.f1064e.m1271c();
                    this.f1087a.f1064e = null;
                }
            default:
        }
    }
}
