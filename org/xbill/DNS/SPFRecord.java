package org.xbill.DNS;

import java.util.List;

public class SPFRecord extends TXTBase {
    private static final long serialVersionUID = -2100754352801658722L;

    public /* bridge */ /* synthetic */ List getStrings() {
        return super.getStrings();
    }

    public /* bridge */ /* synthetic */ List getStringsAsByteArrays() {
        return super.getStringsAsByteArrays();
    }

    SPFRecord() {
    }

    Record getObject() {
        return new SPFRecord();
    }

    public SPFRecord(Name name, int i, long j, List list) {
        super(name, 99, i, j, list);
    }

    public SPFRecord(Name name, int i, long j, String str) {
        super(name, 99, i, j, str);
    }
}
