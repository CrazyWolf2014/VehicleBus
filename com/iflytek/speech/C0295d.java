package com.iflytek.speech;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.iflytek.speech.C1078c.C1077a;
import java.util.ArrayList;

/* renamed from: com.iflytek.speech.d */
final class C0295d extends Handler {
    final /* synthetic */ C1077a f1123a;

    C0295d(C1077a c1077a, Looper looper) {
        this.f1123a = c1077a;
        super(looper);
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                if (this.f1123a.f2020b != null) {
                    this.f1123a.f2020b.m1351a((SpeechError) message.obj);
                }
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (this.f1123a.f2020b != null) {
                    this.f1123a.f2020b.m1350a();
                }
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                ArrayList arrayList = (ArrayList) message.obj;
                if (this.f1123a.f2020b != null) {
                    this.f1123a.f2020b.m1352a(arrayList, this.f1123a.f2021c, message.arg1, message.arg2);
                }
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                if (this.f1123a.f2020b != null) {
                    this.f1123a.f2020b.m1353b();
                }
            default:
        }
    }
}
