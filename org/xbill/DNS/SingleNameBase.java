package org.xbill.DNS;

import java.io.IOException;

abstract class SingleNameBase extends Record {
    private static final long serialVersionUID = -18595042501413L;
    protected Name singleName;

    protected SingleNameBase() {
    }

    protected SingleNameBase(Name name, int i, int i2, long j) {
        super(name, i, i2, j);
    }

    protected SingleNameBase(Name name, int i, int i2, long j, Name name2, String str) {
        super(name, i, i2, j);
        this.singleName = Record.checkName(str, name2);
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.singleName = new Name(dNSInput);
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.singleName = tokenizer.getName(name);
    }

    String rrToString() {
        return this.singleName.toString();
    }

    protected Name getSingleName() {
        return this.singleName;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        this.singleName.toWire(dNSOutput, null, z);
    }
}
