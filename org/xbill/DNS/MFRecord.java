package org.xbill.DNS;

public class MFRecord extends SingleNameBase {
    private static final long serialVersionUID = -6670449036843028169L;

    MFRecord() {
    }

    Record getObject() {
        return new MFRecord();
    }

    public MFRecord(Name name, int i, long j, Name name2) {
        super(name, 4, i, j, name2, "mail agent");
    }

    public Name getMailAgent() {
        return getSingleName();
    }

    public Name getAdditionalName() {
        return getSingleName();
    }
}
