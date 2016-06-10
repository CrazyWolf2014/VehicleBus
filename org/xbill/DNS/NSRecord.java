package org.xbill.DNS;

public class NSRecord extends SingleCompressedNameBase {
    private static final long serialVersionUID = 487170758138268838L;

    NSRecord() {
    }

    Record getObject() {
        return new NSRecord();
    }

    public NSRecord(Name name, int i, long j, Name name2) {
        super(name, 2, i, j, name2, "target");
    }

    public Name getTarget() {
        return getSingleName();
    }

    public Name getAdditionalName() {
        return getSingleName();
    }
}
