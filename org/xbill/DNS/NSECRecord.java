package org.xbill.DNS;

import java.io.IOException;

public class NSECRecord extends Record {
    private static final long serialVersionUID = -5165065768816265385L;
    private Name next;
    private TypeBitmap types;

    NSECRecord() {
    }

    Record getObject() {
        return new NSECRecord();
    }

    public NSECRecord(Name name, int i, long j, Name name2, int[] iArr) {
        super(name, 47, i, j);
        this.next = Record.checkName("next", name2);
        for (int check : iArr) {
            Type.check(check);
        }
        this.types = new TypeBitmap(iArr);
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.next = new Name(dNSInput);
        this.types = new TypeBitmap(dNSInput);
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        this.next.toWire(dNSOutput, null, false);
        this.types.toWire(dNSOutput);
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.next = tokenizer.getName(name);
        this.types = new TypeBitmap(tokenizer);
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.next);
        if (!this.types.empty()) {
            stringBuffer.append(' ');
            stringBuffer.append(this.types.toString());
        }
        return stringBuffer.toString();
    }

    public Name getNext() {
        return this.next;
    }

    public int[] getTypes() {
        return this.types.toArray();
    }

    public boolean hasType(int i) {
        return this.types.contains(i);
    }
}
