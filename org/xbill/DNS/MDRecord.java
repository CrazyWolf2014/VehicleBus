package org.xbill.DNS;

public class MDRecord extends SingleNameBase {
    private static final long serialVersionUID = 5268878603762942202L;

    MDRecord() {
    }

    Record getObject() {
        return new MDRecord();
    }

    public MDRecord(Name name, int i, long j, Name name2) {
        super(name, 3, i, j, name2, "mail agent");
    }

    public Name getMailAgent() {
        return getSingleName();
    }

    public Name getAdditionalName() {
        return getSingleName();
    }
}
