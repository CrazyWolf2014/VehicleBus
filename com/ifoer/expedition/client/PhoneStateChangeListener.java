package com.ifoer.expedition.client;

import android.telephony.PhoneStateListener;
import android.util.Log;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import org.xbill.DNS.KEYRecord;

public class PhoneStateChangeListener extends PhoneStateListener {
    private static final String LOGTAG;
    private final NotificationService notificationService;

    static {
        LOGTAG = LogUtil.makeLogTag(PhoneStateChangeListener.class);
    }

    public PhoneStateChangeListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void onDataConnectionStateChanged(int state) {
        super.onDataConnectionStateChanged(state);
        Log.d(LOGTAG, "onDataConnectionStateChanged()...");
        Log.d(LOGTAG, "Data Connection State = " + getState(state));
        if (state == 2) {
            this.notificationService.connect();
        }
    }

    private String getState(int state) {
        switch (state) {
            case KEYRecord.OWNER_USER /*0*/:
                return "DATA_DISCONNECTED";
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return "DATA_CONNECTING";
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return "DATA_CONNECTED";
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return "DATA_SUSPENDED";
            default:
                return "DATA_<UNKNOWN>";
        }
    }
}
