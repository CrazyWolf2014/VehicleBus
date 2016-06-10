package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.xbill.DNS.Tokenizer.Token;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.utils.base16;
import org.xmlpull.v1.XmlPullParser;

public abstract class Record implements Cloneable, Comparable, Serializable {
    private static final DecimalFormat byteFormat;
    private static final long serialVersionUID = 2694906050116005466L;
    protected int dclass;
    protected Name name;
    protected long ttl;
    protected int type;

    abstract Record getObject();

    abstract void rdataFromString(Tokenizer tokenizer, Name name) throws IOException;

    abstract void rrFromWire(DNSInput dNSInput) throws IOException;

    abstract String rrToString();

    abstract void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z);

    static {
        byteFormat = new DecimalFormat();
        byteFormat.setMinimumIntegerDigits(3);
    }

    protected Record() {
    }

    Record(Name name, int i, int i2, long j) {
        if (name.isAbsolute()) {
            Type.check(i);
            DClass.check(i2);
            TTL.check(j);
            this.name = name;
            this.type = i;
            this.dclass = i2;
            this.ttl = j;
            return;
        }
        throw new RelativeNameException(name);
    }

    private static final Record getEmptyRecord(Name name, int i, int i2, long j, boolean z) {
        Record proto;
        if (z) {
            proto = Type.getProto(i);
            if (proto != null) {
                proto = proto.getObject();
            } else {
                proto = new UNKRecord();
            }
        } else {
            proto = new EmptyRecord();
        }
        proto.name = name;
        proto.type = i;
        proto.dclass = i2;
        proto.ttl = j;
        return proto;
    }

    private static Record newRecord(Name name, int i, int i2, long j, int i3, DNSInput dNSInput) throws IOException {
        Record emptyRecord = getEmptyRecord(name, i, i2, j, dNSInput != null);
        if (dNSInput != null) {
            if (dNSInput.remaining() < i3) {
                throw new WireParseException("truncated record");
            }
            dNSInput.setActive(i3);
            emptyRecord.rrFromWire(dNSInput);
            if (dNSInput.remaining() > 0) {
                throw new WireParseException("invalid record length");
            }
            dNSInput.clearActive();
        }
        return emptyRecord;
    }

    public static Record newRecord(Name name, int i, int i2, long j, int i3, byte[] bArr) {
        if (name.isAbsolute()) {
            DNSInput dNSInput;
            Type.check(i);
            DClass.check(i2);
            TTL.check(j);
            if (bArr != null) {
                dNSInput = new DNSInput(bArr);
            } else {
                dNSInput = null;
            }
            try {
                return newRecord(name, i, i2, j, i3, dNSInput);
            } catch (IOException e) {
                return null;
            }
        }
        throw new RelativeNameException(name);
    }

    public static Record newRecord(Name name, int i, int i2, long j, byte[] bArr) {
        return newRecord(name, i, i2, j, bArr.length, bArr);
    }

    public static Record newRecord(Name name, int i, int i2, long j) {
        if (name.isAbsolute()) {
            Type.check(i);
            DClass.check(i2);
            TTL.check(j);
            return getEmptyRecord(name, i, i2, j, false);
        }
        throw new RelativeNameException(name);
    }

    public static Record newRecord(Name name, int i, int i2) {
        return newRecord(name, i, i2, 0);
    }

    static Record fromWire(DNSInput dNSInput, int i, boolean z) throws IOException {
        Name name = new Name(dNSInput);
        int readU16 = dNSInput.readU16();
        int readU162 = dNSInput.readU16();
        if (i == 0) {
            return newRecord(name, readU16, readU162);
        }
        long readU32 = dNSInput.readU32();
        int readU163 = dNSInput.readU16();
        if (readU163 == 0 && z && (i == 1 || i == 2)) {
            return newRecord(name, readU16, readU162, readU32);
        }
        return newRecord(name, readU16, readU162, readU32, readU163, dNSInput);
    }

    static Record fromWire(DNSInput dNSInput, int i) throws IOException {
        return fromWire(dNSInput, i, false);
    }

    public static Record fromWire(byte[] bArr, int i) throws IOException {
        return fromWire(new DNSInput(bArr), i, false);
    }

    void toWire(DNSOutput dNSOutput, int i, Compression compression) {
        this.name.toWire(dNSOutput, compression);
        dNSOutput.writeU16(this.type);
        dNSOutput.writeU16(this.dclass);
        if (i != 0) {
            dNSOutput.writeU32(this.ttl);
            int current = dNSOutput.current();
            dNSOutput.writeU16(0);
            rrToWire(dNSOutput, compression, false);
            dNSOutput.writeU16At((dNSOutput.current() - current) - 2, current);
        }
    }

    public byte[] toWire(int i) {
        DNSOutput dNSOutput = new DNSOutput();
        toWire(dNSOutput, i, null);
        return dNSOutput.toByteArray();
    }

    private void toWireCanonical(DNSOutput dNSOutput, boolean z) {
        this.name.toWireCanonical(dNSOutput);
        dNSOutput.writeU16(this.type);
        dNSOutput.writeU16(this.dclass);
        if (z) {
            dNSOutput.writeU32(0);
        } else {
            dNSOutput.writeU32(this.ttl);
        }
        int current = dNSOutput.current();
        dNSOutput.writeU16(0);
        rrToWire(dNSOutput, null, true);
        dNSOutput.writeU16At((dNSOutput.current() - current) - 2, current);
    }

    private byte[] toWireCanonical(boolean z) {
        DNSOutput dNSOutput = new DNSOutput();
        toWireCanonical(dNSOutput, z);
        return dNSOutput.toByteArray();
    }

    public byte[] toWireCanonical() {
        return toWireCanonical(false);
    }

    public byte[] rdataToWireCanonical() {
        DNSOutput dNSOutput = new DNSOutput();
        rrToWire(dNSOutput, null, true);
        return dNSOutput.toByteArray();
    }

    public String rdataToString() {
        return rrToString();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.name);
        if (stringBuffer.length() < 8) {
            stringBuffer.append("\t");
        }
        if (stringBuffer.length() < 16) {
            stringBuffer.append("\t");
        }
        stringBuffer.append("\t");
        if (Options.check("BINDTTL")) {
            stringBuffer.append(TTL.format(this.ttl));
        } else {
            stringBuffer.append(this.ttl);
        }
        stringBuffer.append("\t");
        if (!(this.dclass == 1 && Options.check("noPrintIN"))) {
            stringBuffer.append(DClass.string(this.dclass));
            stringBuffer.append("\t");
        }
        stringBuffer.append(Type.string(this.type));
        String rrToString = rrToString();
        if (!rrToString.equals(XmlPullParser.NO_NAMESPACE)) {
            stringBuffer.append("\t");
            stringBuffer.append(rrToString);
        }
        return stringBuffer.toString();
    }

    protected static byte[] byteArrayFromString(String str) throws TextParseException {
        int i;
        Object obj;
        byte[] bytes = str.getBytes();
        for (byte b : bytes) {
            byte b2;
            if (b2 == (byte) 92) {
                obj = 1;
                break;
            }
        }
        obj = null;
        if (obj != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i2 = 0;
            int i3 = 0;
            Object obj2 = null;
            for (i = 0; i < bytes.length; i++) {
                byte b3 = bytes[i];
                int i4;
                if (obj2 != null) {
                    if (b3 >= 48 && b3 <= 57 && i3 < 3) {
                        int i5 = i3 + 1;
                        i3 = (b3 - 48) + (i2 * 10);
                        if (i3 > KEYRecord.PROTOCOL_ANY) {
                            throw new TextParseException("bad escape");
                        } else if (i5 < 3) {
                            i2 = i3;
                            i3 = i5;
                        } else {
                            i2 = (byte) i3;
                            i4 = i5;
                        }
                    } else if (i3 <= 0 || i3 >= 3) {
                        i4 = i3;
                        i3 = i2;
                        b2 = b3;
                    } else {
                        throw new TextParseException("bad escape");
                    }
                    byteArrayOutputStream.write(i2);
                    i2 = i3;
                    i3 = i4;
                    obj2 = null;
                } else if (bytes[i] == (byte) 92) {
                    i2 = 0;
                    i3 = 0;
                    i4 = 1;
                } else {
                    byteArrayOutputStream.write(bytes[i]);
                }
            }
            if (i3 > 0 && i3 < 3) {
                throw new TextParseException("bad escape");
            } else if (byteArrayOutputStream.toByteArray().length <= KEYRecord.PROTOCOL_ANY) {
                return byteArrayOutputStream.toByteArray();
            } else {
                throw new TextParseException("text string too long");
            }
        } else if (bytes.length <= KEYRecord.PROTOCOL_ANY) {
            return bytes;
        } else {
            throw new TextParseException("text string too long");
        }
    }

    protected static String byteArrayToString(byte[] bArr, boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        if (z) {
            stringBuffer.append('\"');
        }
        for (byte b : bArr) {
            int i = b & KEYRecord.PROTOCOL_ANY;
            if (i < 32 || i >= Service.LOCUS_CON) {
                stringBuffer.append('\\');
                stringBuffer.append(byteFormat.format((long) i));
            } else if (i == 34 || i == 92) {
                stringBuffer.append('\\');
                stringBuffer.append((char) i);
            } else {
                stringBuffer.append((char) i);
            }
        }
        if (z) {
            stringBuffer.append('\"');
        }
        return stringBuffer.toString();
    }

    protected static String unknownToString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\\# ");
        stringBuffer.append(bArr.length);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(base16.toString(bArr));
        return stringBuffer.toString();
    }

    public static Record fromString(Name name, int i, int i2, long j, Tokenizer tokenizer, Name name2) throws IOException {
        if (name.isAbsolute()) {
            Type.check(i);
            DClass.check(i2);
            TTL.check(j);
            Token token = tokenizer.get();
            if (token.type == 3 && token.value.equals("\\#")) {
                int uInt16 = tokenizer.getUInt16();
                byte[] hex = tokenizer.getHex();
                if (hex == null) {
                    hex = new byte[0];
                }
                if (uInt16 != hex.length) {
                    throw tokenizer.exception("invalid unknown RR encoding: length mismatch");
                }
                return newRecord(name, i, i2, j, uInt16, new DNSInput(hex));
            }
            tokenizer.unget();
            Record emptyRecord = getEmptyRecord(name, i, i2, j, true);
            emptyRecord.rdataFromString(tokenizer, name2);
            Token token2 = tokenizer.get();
            if (token2.type == 1 || token2.type == 0) {
                return emptyRecord;
            }
            throw tokenizer.exception("unexpected tokens at end of record");
        }
        throw new RelativeNameException(name);
    }

    public static Record fromString(Name name, int i, int i2, long j, String str, Name name2) throws IOException {
        return fromString(name, i, i2, j, new Tokenizer(str), name2);
    }

    public Name getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public int getRRsetType() {
        if (this.type == 46) {
            return ((RRSIGRecord) this).getTypeCovered();
        }
        return this.type;
    }

    public int getDClass() {
        return this.dclass;
    }

    public long getTTL() {
        return this.ttl;
    }

    public boolean sameRRset(Record record) {
        return getRRsetType() == record.getRRsetType() && this.dclass == record.dclass && this.name.equals(record.name);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Record)) {
            return false;
        }
        Record record = (Record) obj;
        if (this.type == record.type && this.dclass == record.dclass && this.name.equals(record.name)) {
            return Arrays.equals(rdataToWireCanonical(), record.rdataToWireCanonical());
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        byte[] toWireCanonical = toWireCanonical(true);
        int i2 = 0;
        while (i < toWireCanonical.length) {
            i2 += (i2 << 3) + (toWireCanonical[i] & KEYRecord.PROTOCOL_ANY);
            i++;
        }
        return i2;
    }

    Record cloneRecord() {
        try {
            return (Record) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }

    public Record withName(Name name) {
        if (name.isAbsolute()) {
            Record cloneRecord = cloneRecord();
            cloneRecord.name = name;
            return cloneRecord;
        }
        throw new RelativeNameException(name);
    }

    Record withDClass(int i, long j) {
        Record cloneRecord = cloneRecord();
        cloneRecord.dclass = i;
        cloneRecord.ttl = j;
        return cloneRecord;
    }

    void setTTL(long j) {
        this.ttl = j;
    }

    public int compareTo(Object obj) {
        int i = 0;
        Record record = (Record) obj;
        if (this == record) {
            return 0;
        }
        int compareTo = this.name.compareTo(record.name);
        if (compareTo != 0) {
            return compareTo;
        }
        compareTo = this.dclass - record.dclass;
        if (compareTo != 0) {
            return compareTo;
        }
        compareTo = this.type - record.type;
        if (compareTo != 0) {
            return compareTo;
        }
        byte[] rdataToWireCanonical = rdataToWireCanonical();
        byte[] rdataToWireCanonical2 = record.rdataToWireCanonical();
        while (i < rdataToWireCanonical.length && i < rdataToWireCanonical2.length) {
            compareTo = (rdataToWireCanonical[i] & KEYRecord.PROTOCOL_ANY) - (rdataToWireCanonical2[i] & KEYRecord.PROTOCOL_ANY);
            if (compareTo != 0) {
                return compareTo;
            }
            i++;
        }
        return rdataToWireCanonical.length - rdataToWireCanonical2.length;
    }

    public Name getAdditionalName() {
        return null;
    }

    static int checkU8(String str, int i) {
        if (i >= 0 && i <= KEYRecord.PROTOCOL_ANY) {
            return i;
        }
        throw new IllegalArgumentException("\"" + str + "\" " + i + " must be an unsigned 8 " + "bit value");
    }

    static int checkU16(String str, int i) {
        if (i >= 0 && i <= InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) {
            return i;
        }
        throw new IllegalArgumentException("\"" + str + "\" " + i + " must be an unsigned 16 " + "bit value");
    }

    static long checkU32(String str, long j) {
        if (j >= 0 && j <= Util.MAX_32BIT_VALUE) {
            return j;
        }
        throw new IllegalArgumentException("\"" + str + "\" " + j + " must be an unsigned 32 " + "bit value");
    }

    static Name checkName(String str, Name name) {
        if (name.isAbsolute()) {
            return name;
        }
        throw new RelativeNameException(name);
    }

    static byte[] checkByteArrayLength(String str, byte[] bArr, int i) {
        if (bArr.length > InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) {
            throw new IllegalArgumentException("\"" + str + "\" array " + "must have no more than " + i + " elements");
        }
        Object obj = new byte[bArr.length];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        return obj;
    }
}
