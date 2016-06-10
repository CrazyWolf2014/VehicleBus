package org.xbill.DNS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.xbill.DNS.utils.base16;

public class NSAPRecord extends Record {
    private static final long serialVersionUID = -1037209403185658593L;
    private byte[] address;

    NSAPRecord() {
    }

    Record getObject() {
        return new NSAPRecord();
    }

    private static final byte[] checkAndConvertAddress(String str) {
        int i = 2;
        if (!str.substring(0, 2).equalsIgnoreCase("0x")) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i2 = 0;
        int i3 = 0;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (charAt != '.') {
                int digit = Character.digit(charAt, 16);
                if (digit == -1) {
                    return null;
                }
                if (i3 != 0) {
                    i2 += digit;
                    byteArrayOutputStream.write(i2);
                    i3 = 0;
                } else {
                    i2 = digit << 4;
                    i3 = 1;
                }
            }
            i++;
        }
        if (i3 != 0) {
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    public NSAPRecord(Name name, int i, long j, String str) {
        super(name, 22, i, j);
        this.address = checkAndConvertAddress(str);
        if (this.address == null) {
            throw new IllegalArgumentException("invalid NSAP address " + str);
        }
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.address = dNSInput.readByteArray();
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        String string = tokenizer.getString();
        this.address = checkAndConvertAddress(string);
        if (this.address == null) {
            throw tokenizer.exception("invalid NSAP address " + string);
        }
    }

    public String getAddress() {
        return Record.byteArrayToString(this.address, false);
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeByteArray(this.address);
    }

    String rrToString() {
        return "0x" + base16.toString(this.address);
    }
}
