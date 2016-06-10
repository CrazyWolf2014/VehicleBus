package org.xbill.DNS;

public class MGRecord extends SingleNameBase {
    private static final long serialVersionUID = -3980055550863644582L;

    MGRecord() {
    }

    Record getObject() {
        return new MGRecord();
    }

    public MGRecord(Name name, int i, long j, Name name2) {
        super(name, 8, i, j, name2, "mailbox");
    }

    public Name getMailbox() {
        return getSingleName();
    }
}
