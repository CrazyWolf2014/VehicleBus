package com.google.zxing.client.result.optional;

import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;

final class NDEFRecord {
    public static final String ACTION_WELL_KNOWN_TYPE = "act";
    public static final String SMART_POSTER_WELL_KNOWN_TYPE = "Sp";
    private static final int SUPPORTED_HEADER = 17;
    private static final int SUPPORTED_HEADER_MASK = 63;
    public static final String TEXT_WELL_KNOWN_TYPE = "T";
    public static final String URI_WELL_KNOWN_TYPE = "U";
    private final int header;
    private final byte[] payload;
    private final int totalRecordLength;
    private final String type;

    private NDEFRecord(int i, String str, byte[] bArr, int i2) {
        this.header = i;
        this.type = str;
        this.payload = bArr;
        this.totalRecordLength = i2;
    }

    static NDEFRecord readRecord(byte[] bArr, int i) {
        int i2 = bArr[i] & KEYRecord.PROTOCOL_ANY;
        if (((i2 ^ SUPPORTED_HEADER) & SUPPORTED_HEADER_MASK) != 0) {
            return null;
        }
        int i3 = bArr[i + 1] & KEYRecord.PROTOCOL_ANY;
        int i4 = bArr[i + 2] & KEYRecord.PROTOCOL_ANY;
        String bytesToString = AbstractNDEFResultParser.bytesToString(bArr, i + 3, i3, StringEncodings.US_ASCII);
        Object obj = new byte[i4];
        System.arraycopy(bArr, (i + 3) + i3, obj, 0, i4);
        return new NDEFRecord(i2, bytesToString, obj, (i3 + 3) + i4);
    }

    byte[] getPayload() {
        return this.payload;
    }

    int getTotalRecordLength() {
        return this.totalRecordLength;
    }

    String getType() {
        return this.type;
    }

    boolean isMessageBegin() {
        return (this.header & Flags.FLAG8) != 0;
    }

    boolean isMessageEnd() {
        return (this.header & 64) != 0;
    }
}
