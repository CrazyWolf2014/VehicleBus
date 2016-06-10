package org.xbill.DNS;

import com.tencent.mm.sdk.plugin.BaseProfile;

public class DNAMERecord extends SingleNameBase {
    private static final long serialVersionUID = 2670767677200844154L;

    DNAMERecord() {
    }

    Record getObject() {
        return new DNAMERecord();
    }

    public DNAMERecord(Name name, int i, long j, Name name2) {
        super(name, 39, i, j, name2, BaseProfile.COL_ALIAS);
    }

    public Name getTarget() {
        return getSingleName();
    }

    public Name getAlias() {
        return getSingleName();
    }
}
