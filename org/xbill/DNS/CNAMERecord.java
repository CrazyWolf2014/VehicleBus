package org.xbill.DNS;

import com.tencent.mm.sdk.plugin.BaseProfile;

public class CNAMERecord extends SingleCompressedNameBase {
    private static final long serialVersionUID = -4020373886892538580L;

    CNAMERecord() {
    }

    Record getObject() {
        return new CNAMERecord();
    }

    public CNAMERecord(Name name, int i, long j, Name name2) {
        super(name, 5, i, j, name2, BaseProfile.COL_ALIAS);
    }

    public Name getTarget() {
        return getSingleName();
    }

    public Name getAlias() {
        return getSingleName();
    }
}
