package org.xbill.DNS;

public class NSAP_PTRRecord extends SingleNameBase {
    private static final long serialVersionUID = 2386284746382064904L;

    NSAP_PTRRecord() {
    }

    Record getObject() {
        return new NSAP_PTRRecord();
    }

    public NSAP_PTRRecord(Name name, int i, long j, Name name2) {
        super(name, 23, i, j, name2, "target");
    }

    public Name getTarget() {
        return getSingleName();
    }
}
