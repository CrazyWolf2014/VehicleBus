package org.xbill.DNS;

abstract class SingleCompressedNameBase extends SingleNameBase {
    private static final long serialVersionUID = -236435396815460677L;

    protected SingleCompressedNameBase() {
    }

    protected SingleCompressedNameBase(Name name, int i, int i2, long j, Name name2, String str) {
        super(name, i, i2, j, name2, str);
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        this.singleName.toWire(dNSOutput, compression, z);
    }
}
