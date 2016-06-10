package org.xbill.DNS;

import java.util.Date;

public class SIGRecord extends SIGBase {
    private static final long serialVersionUID = 4963556060953589058L;

    public /* bridge */ /* synthetic */ int getAlgorithm() {
        return super.getAlgorithm();
    }

    public /* bridge */ /* synthetic */ Date getExpire() {
        return super.getExpire();
    }

    public /* bridge */ /* synthetic */ int getFootprint() {
        return super.getFootprint();
    }

    public /* bridge */ /* synthetic */ int getLabels() {
        return super.getLabels();
    }

    public /* bridge */ /* synthetic */ long getOrigTTL() {
        return super.getOrigTTL();
    }

    public /* bridge */ /* synthetic */ byte[] getSignature() {
        return super.getSignature();
    }

    public /* bridge */ /* synthetic */ Name getSigner() {
        return super.getSigner();
    }

    public /* bridge */ /* synthetic */ Date getTimeSigned() {
        return super.getTimeSigned();
    }

    public /* bridge */ /* synthetic */ int getTypeCovered() {
        return super.getTypeCovered();
    }

    SIGRecord() {
    }

    Record getObject() {
        return new SIGRecord();
    }

    public SIGRecord(Name name, int i, long j, int i2, int i3, long j2, Date date, Date date2, int i4, Name name2, byte[] bArr) {
        super(name, 24, i, j, i2, i3, j2, date, date2, i4, name2, bArr);
    }
}
