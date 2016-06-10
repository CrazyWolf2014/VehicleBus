package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.IOException;
import java.util.Date;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.utils.base64;

public class TKEYRecord extends Record {
    public static final int DELETE = 5;
    public static final int DIFFIEHELLMAN = 2;
    public static final int GSSAPI = 3;
    public static final int RESOLVERASSIGNED = 4;
    public static final int SERVERASSIGNED = 1;
    private static final long serialVersionUID = 8828458121926391756L;
    private Name alg;
    private int error;
    private byte[] key;
    private int mode;
    private byte[] other;
    private Date timeExpire;
    private Date timeInception;

    TKEYRecord() {
    }

    Record getObject() {
        return new TKEYRecord();
    }

    public TKEYRecord(Name name, int i, long j, Name name2, Date date, Date date2, int i2, int i3, byte[] bArr, byte[] bArr2) {
        super(name, Type.TKEY, i, j);
        this.alg = Record.checkName("alg", name2);
        this.timeInception = date;
        this.timeExpire = date2;
        this.mode = Record.checkU16("mode", i2);
        this.error = Record.checkU16("error", i3);
        this.key = bArr;
        this.other = bArr2;
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.alg = new Name(dNSInput);
        this.timeInception = new Date(dNSInput.readU32() * 1000);
        this.timeExpire = new Date(dNSInput.readU32() * 1000);
        this.mode = dNSInput.readU16();
        this.error = dNSInput.readU16();
        int readU16 = dNSInput.readU16();
        if (readU16 > 0) {
            this.key = dNSInput.readByteArray(readU16);
        } else {
            this.key = null;
        }
        readU16 = dNSInput.readU16();
        if (readU16 > 0) {
            this.other = dNSInput.readByteArray(readU16);
        } else {
            this.other = null;
        }
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        throw tokenizer.exception("no text format defined for TKEY");
    }

    protected String modeString() {
        switch (this.mode) {
            case SERVERASSIGNED /*1*/:
                return "SERVERASSIGNED";
            case DIFFIEHELLMAN /*2*/:
                return "DIFFIEHELLMAN";
            case GSSAPI /*3*/:
                return "GSSAPI";
            case RESOLVERASSIGNED /*4*/:
                return "RESOLVERASSIGNED";
            case DELETE /*5*/:
                return "DELETE";
            default:
                return Integer.toString(this.mode);
        }
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.alg);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        if (Options.check("multiline")) {
            stringBuffer.append("(\n\t");
        }
        stringBuffer.append(FormattedTime.format(this.timeInception));
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(FormattedTime.format(this.timeExpire));
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(modeString());
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(Rcode.TSIGstring(this.error));
        if (Options.check("multiline")) {
            stringBuffer.append(SpecilApiUtil.LINE_SEP);
            if (this.key != null) {
                stringBuffer.append(base64.formatString(this.key, 64, "\t", false));
                stringBuffer.append(SpecilApiUtil.LINE_SEP);
            }
            if (this.other != null) {
                stringBuffer.append(base64.formatString(this.other, 64, "\t", false));
            }
            stringBuffer.append(" )");
        } else {
            stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            if (this.key != null) {
                stringBuffer.append(base64.toString(this.key));
                stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            }
            if (this.other != null) {
                stringBuffer.append(base64.toString(this.other));
            }
        }
        return stringBuffer.toString();
    }

    public Name getAlgorithm() {
        return this.alg;
    }

    public Date getTimeInception() {
        return this.timeInception;
    }

    public Date getTimeExpire() {
        return this.timeExpire;
    }

    public int getMode() {
        return this.mode;
    }

    public int getError() {
        return this.error;
    }

    public byte[] getKey() {
        return this.key;
    }

    public byte[] getOther() {
        return this.other;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        this.alg.toWire(dNSOutput, null, z);
        dNSOutput.writeU32(this.timeInception.getTime() / 1000);
        dNSOutput.writeU32(this.timeExpire.getTime() / 1000);
        dNSOutput.writeU16(this.mode);
        dNSOutput.writeU16(this.error);
        if (this.key != null) {
            dNSOutput.writeU16(this.key.length);
            dNSOutput.writeByteArray(this.key);
        } else {
            dNSOutput.writeU16(0);
        }
        if (this.other != null) {
            dNSOutput.writeU16(this.other.length);
            dNSOutput.writeByteArray(this.other);
            return;
        }
        dNSOutput.writeU16(0);
    }
}
