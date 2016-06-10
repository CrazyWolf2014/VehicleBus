package org.xbill.DNS;

public class RTRecord extends U16NameBase {
    private static final long serialVersionUID = -3206215651648278098L;

    RTRecord() {
    }

    Record getObject() {
        return new RTRecord();
    }

    public RTRecord(Name name, int i, long j, int i2, Name name2) {
        super(name, 21, i, j, i2, "preference", name2, "intermediateHost");
    }

    public int getPreference() {
        return getU16Field();
    }

    public Name getIntermediateHost() {
        return getNameField();
    }
}
