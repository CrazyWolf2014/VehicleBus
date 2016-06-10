package org.xbill.DNS;

import java.io.IOException;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;

public class NULLRecord extends Record {
    private static final long serialVersionUID = -5796493183235216538L;
    private byte[] data;

    NULLRecord() {
    }

    Record getObject() {
        return new NULLRecord();
    }

    public NULLRecord(Name name, int i, long j, byte[] bArr) {
        super(name, 10, i, j);
        if (bArr.length > InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) {
            throw new IllegalArgumentException("data must be <65536 bytes");
        }
        this.data = bArr;
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.data = dNSInput.readByteArray();
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        throw tokenizer.exception("no defined text format for NULL records");
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
