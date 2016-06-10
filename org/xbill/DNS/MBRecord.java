package org.xbill.DNS;

public class MBRecord extends SingleNameBase {
    private static final long serialVersionUID = 532349543479150419L;

    MBRecord() {
    }

    Record getObject() {
        return new MBRecord();
    }

    public MBRecord(Name name, int i, long j, Name name2) {
        super(name, 7, i, j, name2, "mailbox");
    }

    public Name getMailbox() {
        return getSingleName();
    }

    public Name getAdditionalName() {
        return getSingleName();
    }
}
