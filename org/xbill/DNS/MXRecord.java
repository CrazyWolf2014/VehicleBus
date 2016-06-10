package org.xbill.DNS;

public class MXRecord extends U16NameBase {
    private static final long serialVersionUID = 2914841027584208546L;

    MXRecord() {
    }

    Record getObject() {
        return new MXRecord();
    }

    public MXRecord(Name name, int i, long j, int i2, Name name2) {
        super(name, 15, i, j, i2, "priority", name2, "target");
    }

    public Name getTarget() {
        return getNameField();
    }

    public int getPriority() {
        return getU16Field();
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU16(this.u16Field);
        this.nameField.toWire(dNSOutput, compression, z);
    }

    public Name getAdditionalName() {
        return getNameField();
    }
}
