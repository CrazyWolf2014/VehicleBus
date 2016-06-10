package org.xbill.DNS;

import java.io.IOException;
import java.util.BitSet;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class NXTRecord extends Record {
    private static final long serialVersionUID = -8851454400765507520L;
    private BitSet bitmap;
    private Name next;

    NXTRecord() {
    }

    Record getObject() {
        return new NXTRecord();
    }

    public NXTRecord(Name name, int i, long j, Name name2, BitSet bitSet) {
        super(name, 30, i, j);
        this.next = Record.checkName("next", name2);
        this.bitmap = bitSet;
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.next = new Name(dNSInput);
        this.bitmap = new BitSet();
        int remaining = dNSInput.remaining();
        for (int i = 0; i < remaining; i++) {
            int readU8 = dNSInput.readU8();
            for (int i2 = 0; i2 < 8; i2++) {
                if (((1 << (7 - i2)) & readU8) != 0) {
                    this.bitmap.set((i * 8) + i2);
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void rdataFromString(org.xbill.DNS.Tokenizer r4, org.xbill.DNS.Name r5) throws java.io.IOException {
        /*
        r3 = this;
        r0 = r4.getName(r5);
        r3.next = r0;
        r0 = new java.util.BitSet;
        r0.<init>();
        r3.bitmap = r0;
    L_0x000d:
        r0 = r4.get();
        r1 = r0.isString();
        if (r1 != 0) goto L_0x001b;
    L_0x0017:
        r4.unget();
        return;
    L_0x001b:
        r1 = r0.value;
        r2 = 1;
        r1 = org.xbill.DNS.Type.value(r1, r2);
        if (r1 <= 0) goto L_0x0028;
    L_0x0024:
        r2 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r1 <= r2) goto L_0x0042;
    L_0x0028:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Invalid type: ";
        r1 = r1.append(r2);
        r0 = r0.value;
        r0 = r1.append(r0);
        r0 = r0.toString();
        r0 = r4.exception(r0);
        throw r0;
    L_0x0042:
        r0 = r3.bitmap;
        r0.set(r1);
        goto L_0x000d;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.NXTRecord.rdataFromString(org.xbill.DNS.Tokenizer, org.xbill.DNS.Name):void");
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.next);
        int length = this.bitmap.length();
        for (int i = 0; i < length; i = (short) (i + 1)) {
            if (this.bitmap.get(i)) {
                stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                stringBuffer.append(Type.string(i));
            }
        }
        return stringBuffer.toString();
    }

    public Name getNext() {
        return this.next;
    }

    public BitSet getBitmap() {
        return this.bitmap;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        this.next.toWire(dNSOutput, null, z);
        int length = this.bitmap.length();
        int i = 0;
        int i2 = 0;
        while (i2 < length) {
            int i3;
            if (this.bitmap.get(i2)) {
                i3 = 1 << (7 - (i2 % 8));
            } else {
                i3 = 0;
            }
            i3 |= i;
            if (i2 % 8 == 7 || i2 == length - 1) {
                dNSOutput.writeU8(i3);
                i3 = 0;
            }
            i2++;
            i = i3;
        }
    }
}
