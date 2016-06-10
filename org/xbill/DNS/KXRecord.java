package org.xbill.DNS;

public class KXRecord extends U16NameBase {
    private static final long serialVersionUID = 7448568832769757809L;

    KXRecord() {
    }

    Record getObject() {
        return new KXRecord();
    }

    public KXRecord(Name name, int i, long j, int i2, Name name2) {
        super(name, 36, i, j, i2, "preference", name2, "target");
    }

    public Name getTarget() {
        return getNameField();
    }

    public int getPreference() {
        return getU16Field();
    }

    public Name getAdditionalName() {
        return getNameField();
    }
}
