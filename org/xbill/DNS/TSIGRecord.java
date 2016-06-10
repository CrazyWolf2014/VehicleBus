package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.IOException;
import java.util.Date;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.utils.base64;

public class TSIGRecord extends Record {
    private static final long serialVersionUID = -88820909016649306L;
    private Name alg;
    private int error;
    private int fudge;
    private int originalID;
    private byte[] other;
    private byte[] signature;
    private Date timeSigned;

    TSIGRecord() {
    }

    Record getObject() {
        return new TSIGRecord();
    }

    public TSIGRecord(Name name, int i, long j, Name name2, Date date, int i2, byte[] bArr, int i3, int i4, byte[] bArr2) {
        super(name, Type.TSIG, i, j);
        this.alg = Record.checkName("alg", name2);
        this.timeSigned = date;
        this.fudge = Record.checkU16("fudge", i2);
        this.signature = bArr;
        this.originalID = Record.checkU16("originalID", i3);
        this.error = Record.checkU16("error", i4);
        this.other = bArr2;
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.alg = new Name(dNSInput);
        this.timeSigned = new Date(((((long) dNSInput.readU16()) << 32) + dNSInput.readU32()) * 1000);
        this.fudge = dNSInput.readU16();
        this.signature = dNSInput.readByteArray(dNSInput.readU16());
        this.originalID = dNSInput.readU16();
        this.error = dNSInput.readU16();
        int readU16 = dNSInput.readU16();
        if (readU16 > 0) {
            this.other = dNSInput.readByteArray(readU16);
        } else {
            this.other = null;
        }
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        throw tokenizer.exception("no text format defined for TSIG");
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.alg);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        if (Options.check("multiline")) {
            stringBuffer.append("(\n\t");
        }
        stringBuffer.append(this.timeSigned.getTime() / 1000);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.fudge);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.signature.length);
        if (Options.check("multiline")) {
            stringBuffer.append(SpecilApiUtil.LINE_SEP);
            stringBuffer.append(base64.formatString(this.signature, 64, "\t", false));
        } else {
            stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            stringBuffer.append(base64.toString(this.signature));
        }
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(Rcode.TSIGstring(this.error));
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        if (this.other == null) {
            stringBuffer.append(0);
        } else {
            stringBuffer.append(this.other.length);
            if (Options.check("multiline")) {
                stringBuffer.append("\n\n\n\t");
            } else {
                stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            }
            if (this.error != 18) {
                stringBuffer.append("<");
                stringBuffer.append(base64.toString(this.other));
                stringBuffer.append(">");
            } else if (this.other.length != 6) {
                stringBuffer.append("<invalid BADTIME other data>");
            } else {
                long j = (((((((long) (this.other[0] & KEYRecord.PROTOCOL_ANY)) << 40) + (((long) (this.other[1] & KEYRecord.PROTOCOL_ANY)) << 32)) + ((long) ((this.other[2] & KEYRecord.PROTOCOL_ANY) << 24))) + ((long) ((this.other[3] & KEYRecord.PROTOCOL_ANY) << 16))) + ((long) ((this.other[4] & KEYRecord.PROTOCOL_ANY) << 8))) + ((long) (this.other[5] & KEYRecord.PROTOCOL_ANY));
                stringBuffer.append("<server time: ");
                stringBuffer.append(new Date(j * 1000));
                stringBuffer.append(">");
            }
        }
        if (Options.check("multiline")) {
            stringBuffer.append(" )");
        }
        return stringBuffer.toString();
    }

    public Name getAlgorithm() {
        return this.alg;
    }

    public Date getTimeSigned() {
        return this.timeSigned;
    }

    public int getFudge() {
        return this.fudge;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public int getOriginalID() {
        return this.originalID;
    }

    public int getError() {
        return this.error;
    }

    public byte[] getOther() {
        return this.other;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        this.alg.toWire(dNSOutput, null, z);
        long time = this.timeSigned.getTime() / 1000;
        time &= Util.MAX_32BIT_VALUE;
        dNSOutput.writeU16((int) (time >> 32));
        dNSOutput.writeU32(time);
        dNSOutput.writeU16(this.fudge);
        dNSOutput.writeU16(this.signature.length);
        dNSOutput.writeByteArray(this.signature);
        dNSOutput.writeU16(this.originalID);
        dNSOutput.writeU16(this.error);
        if (this.other != null) {
            dNSOutput.writeU16(this.other.length);
            dNSOutput.writeByteArray(this.other);
            return;
        }
        dNSOutput.writeU16(0);
    }
}
