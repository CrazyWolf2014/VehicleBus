package org.xbill.DNS;

import java.io.IOException;

public class UNKRecord extends Record {
    private static final long serialVersionUID = -4193583311594626915L;
    private byte[] data;

    UNKRecord() {
    }

    Record getObject() {
        return new UNKRecord();
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.data = dNSInput.readByteArray();
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        throw tokenizer.exception("invalid unknown RR encoding");
    }

    String rrToString() {
        return Record.unknownToString(this.data);
    }

    public byte[] getData() {
        return this.data;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeByteArray(this.data);
    }
}
