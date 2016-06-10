package com.tencent.mm.sdk.platformtools;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.util.LinkedList;
import java.util.List;
import org.xbill.DNS.KEYRecord;

public class PhoneStatusWatcher {
    private static boolean aD;
    private TelephonyManager aE;
    private PhoneStateListener aF;
    private List<PhoneCallListener> aG;

    /* renamed from: com.tencent.mm.sdk.platformtools.PhoneStatusWatcher.1 */
    class C08691 extends PhoneStateListener {
        final /* synthetic */ PhoneStatusWatcher aH;

        C08691(PhoneStatusWatcher phoneStatusWatcher) {
            this.aH = phoneStatusWatcher;
        }

        public void onCallStateChanged(int i, String str) {
            for (PhoneCallListener onPhoneCall : this.aH.aG) {
                onPhoneCall.onPhoneCall(i);
            }
            super.onCallStateChanged(i, str);
            switch (i) {
                case KEYRecord.OWNER_USER /*0*/:
                    PhoneStatusWatcher.aD = false;
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    PhoneStatusWatcher.aD = true;
                default:
            }
        }
    }

    public interface PhoneCallListener {
        void onPhoneCall(int i);
    }

    public PhoneStatusWatcher() {
        this.aG = new LinkedList();
    }

    public static boolean isCalling() {
        return aD;
    }

    public void addPhoneCallListener(PhoneCallListener phoneCallListener) {
        this.aG.add(phoneCallListener);
    }

    public void begin(Context context) {
        if (this.aE == null) {
            this.aE = (TelephonyManager) context.getSystemService("phone");
        }
        if (this.aF == null) {
            this.aF = new C08691(this);
        }
        this.aE.listen(this.aF, 32);
    }

    public void clearPhoneCallListener() {
        this.aG.clear();
    }

    public void end() {
        if (this.aE != null) {
            this.aE.listen(this.aF, 0);
            this.aF = null;
        }
    }

    public void removePhoneCallListener(PhoneCallListener phoneCallListener) {
        this.aG.remove(phoneCallListener);
    }
}
