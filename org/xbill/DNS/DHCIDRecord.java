package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base64;

public class DHCIDRecord extends Record {
    private static final long serialVersionUID = -8214820200808997707L;
    private byte[] data;

    DHCIDRecord() {
    }

    Record getObject() {
        return new DHCIDRecord();
    }

    public DHCIDRecord(Name name, int i, long j, byte[] bArr) {
        super(name, 49, i, j);
        this.data = bArr;
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.data = dNSInput.readByteArray();
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.data = tokenizer.getBase64();
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeByteArray(this.data);
    }

    String rrToString() {
        return base64.toString(this.data);
    }

    public byte[] getData() {
        return this.data;
    }
}
