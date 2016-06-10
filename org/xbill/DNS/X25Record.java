package org.xbill.DNS;

import java.io.IOException;

public class X25Record extends Record {
    private static final long serialVersionUID = 4267576252335579764L;
    private byte[] address;

    X25Record() {
    }

    Record getObject() {
        return new X25Record();
    }

    private static final byte[] checkAndConvertAddress(String str) {
        int length = str.length();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (!Character.isDigit(charAt)) {
                return null;
            }
            bArr[i] = (byte) charAt;
        }
        return bArr;
    }

    public X25Record(Name name, int i, long j, String str) {
        super(name, 19, i, j);
        this.address = checkAndConvertAddress(str);
        if (this.address == null) {
            throw new IllegalArgumentException("invalid PSDN address " + str);
        }
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.address = dNSInput.readCountedString();
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        String string = tokenizer.getString();
        this.address = checkAndConvertAddress(string);
        if (this.address == null) {
            throw tokenizer.exception("invalid PSDN address " + string);
        }
    }

    public String getAddress() {
        return Record.byteArrayToString(this.address, false);
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeCountedString(this.address);
    }

    String rrToString() {
        return Record.byteArrayToString(this.address, true);
    }
}
