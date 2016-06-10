package org.xbill.DNS;

import java.util.List;

public class TXTRecord extends TXTBase {
    private static final long serialVersionUID = -5780785764284221342L;

    public /* bridge */ /* synthetic */ List getStrings() {
        return super.getStrings();
    }

    public /* bridge */ /* synthetic */ List getStringsAsByteArrays() {
        return super.getStringsAsByteArrays();
    }

    TXTRecord() {
    }

    Record getObject() {
        return new TXTRecord();
    }

    public TXTRecord(Name name, int i, long j, List list) {
        super(name, 16, i, j, list);
    }

    public TXTRecord(Name name, int i, long j, String str) {
        super(name, 16, i, j, str);
    }
}
