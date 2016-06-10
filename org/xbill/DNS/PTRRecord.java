package org.xbill.DNS;

public class PTRRecord extends SingleCompressedNameBase {
    private static final long serialVersionUID = -8321636610425434192L;

    PTRRecord() {
    }

    Record getObject() {
        return new PTRRecord();
    }

    public PTRRecord(Name name, int i, long j, Name name2) {
        super(name, 12, i, j, name2, "target");
    }

    public Name getTarget() {
        return getSingleName();
    }
}
