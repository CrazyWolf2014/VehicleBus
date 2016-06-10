package com.iflytek.speech.p014a;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.iflytek.speech.C0294a;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.p014a.C1277a.C1072a;
import java.util.ArrayList;
import org.xbill.DNS.KEYRecord;

/* renamed from: com.iflytek.speech.a.b */
final class C0292b extends Handler {
    final /* synthetic */ C1072a f1114a;

    C0292b(C1072a c1072a, Looper looper) {
        this.f1114a = c1072a;
        super(looper);
    }

    public void handleMessage(Message message) {
        boolean z = true;
        if (this.f1114a.f2011b != null) {
            switch (message.what) {
                case KEYRecord.OWNER_USER /*0*/:
                    this.f1114a.f2011b.m1346a((SpeechError) message.obj);
                    break;
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    this.f1114a.f2011b.m1345a(message.arg1);
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    this.f1114a.f2011b.m1344a();
                    break;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    this.f1114a.f2011b.m1348b();
                    break;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    C0294a a = this.f1114a.f2011b;
                    ArrayList arrayList = (ArrayList) message.obj;
                    if (message.arg1 != 1) {
                        z = false;
                    }
                    a.m1347a(arrayList, z);
                    break;
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    this.f1114a.f2011b.m1349c();
                    break;
            }
            super.handleMessage(message);
        }
    }
}
