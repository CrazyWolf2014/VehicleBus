package org.xbill.DNS;

public class MRRecord extends SingleNameBase {
    private static final long serialVersionUID = -5617939094209927533L;

    MRRecord() {
    }

    Record getObject() {
        return new MRRecord();
    }

    public MRRecord(Name name, int i, long j, Name name2) {
        super(name, 9, i, j, name2, "new name");
    }

    public Name getNewName() {
        return getSingleName();
    }
}
