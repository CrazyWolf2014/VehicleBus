package org.xbill.DNS;

public class AFSDBRecord extends U16NameBase {
    private static final long serialVersionUID = 3034379930729102437L;

    AFSDBRecord() {
    }

    Record getObject() {
        return new AFSDBRecord();
    }

    public AFSDBRecord(Name name, int i, long j, int i2, Name name2) {
        super(name, 18, i, j, i2, "subtype", name2, "host");
    }

    public int getSubtype() {
        return getU16Field();
    }

    public Name getHost() {
        return getNameField();
    }
}
