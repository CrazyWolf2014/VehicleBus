package org.xbill.DNS;

import com.cnmobi.im.util.XmppConnection;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class Name implements Comparable, Serializable {
    private static final int LABEL_COMPRESSION = 192;
    private static final int LABEL_MASK = 192;
    private static final int LABEL_NORMAL = 0;
    private static final int MAXLABEL = 63;
    private static final int MAXLABELS = 128;
    private static final int MAXNAME = 255;
    private static final int MAXOFFSETS = 7;
    private static final DecimalFormat byteFormat;
    public static final Name empty;
    private static final byte[] emptyLabel;
    private static final byte[] lowercase;
    public static final Name root;
    private static final long serialVersionUID = -7257019940971525644L;
    private static final Name wild;
    private static final byte[] wildLabel;
    private int hashcode;
    private byte[] name;
    private long offsets;

    static {
        emptyLabel = new byte[]{(byte) 0};
        wildLabel = new byte[]{(byte) 1, (byte) 42};
        byteFormat = new DecimalFormat();
        lowercase = new byte[KEYRecord.OWNER_ZONE];
        byteFormat.setMinimumIntegerDigits(3);
        int i = LABEL_NORMAL;
        while (i < lowercase.length) {
            if (i < 65 || i > 90) {
                lowercase[i] = (byte) i;
            } else {
                lowercase[i] = (byte) ((i - 65) + 97);
            }
            i++;
        }
        root = new Name();
        root.appendSafe(emptyLabel, LABEL_NORMAL, 1);
        empty = new Name();
        empty.name = new byte[LABEL_NORMAL];
        wild = new Name();
        wild.appendSafe(wildLabel, LABEL_NORMAL, 1);
    }

    private Name() {
    }

    private final void setoffset(int i, int i2) {
        if (i < MAXOFFSETS) {
            int i3 = (7 - i) * 8;
            this.offsets &= (255 << i3) ^ -1;
            this.offsets |= ((long) i2) << i3;
        }
    }

    private final int offset(int i) {
        if (i == 0 && getlabels() == 0) {
            return LABEL_NORMAL;
        }
        if (i < 0 || i >= getlabels()) {
            throw new IllegalArgumentException("label out of range");
        } else if (i < MAXOFFSETS) {
            return ((int) (this.offsets >>> ((7 - i) * 8))) & MAXNAME;
        } else {
            int offset = offset(6);
            int i2 = 6;
            while (i2 < i) {
                i2++;
                offset = (this.name[offset] + 1) + offset;
            }
            return offset;
        }
    }

    private final void setlabels(int i) {
        this.offsets &= -256;
        this.offsets |= (long) i;
    }

    private final int getlabels() {
        return (int) (this.offsets & 255);
    }

    private static final void copy(Name name, Name name2) {
        int i = LABEL_NORMAL;
        if (name.offset(LABEL_NORMAL) == 0) {
            name2.name = name.name;
            name2.offsets = name.offsets;
            return;
        }
        int offset = name.offset(LABEL_NORMAL);
        int length = name.name.length - offset;
        int labels = name.labels();
        name2.name = new byte[length];
        System.arraycopy(name.name, offset, name2.name, LABEL_NORMAL, length);
        while (i < labels && i < MAXOFFSETS) {
            name2.setoffset(i, name.offset(i) - offset);
            i++;
        }
        name2.setlabels(labels);
    }

    private final void append(byte[] bArr, int i, int i2) throws NameTooLongException {
        int i3;
        int i4 = LABEL_NORMAL;
        int length = this.name == null ? LABEL_NORMAL : this.name.length - offset(LABEL_NORMAL);
        int i5 = i;
        int i6 = LABEL_NORMAL;
        for (i3 = LABEL_NORMAL; i3 < i2; i3++) {
            byte b = bArr[i5];
            if (b > MAXLABEL) {
                throw new IllegalStateException("invalid label");
            }
            int i7 = b + 1;
            i5 += i7;
            i6 += i7;
        }
        i5 = length + i6;
        if (i5 > MAXNAME) {
            throw new NameTooLongException();
        }
        i3 = getlabels();
        i7 = i3 + i2;
        if (i7 > MAXLABELS) {
            throw new IllegalStateException("too many labels");
        }
        Object obj = new byte[i5];
        if (length != 0) {
            System.arraycopy(this.name, offset(LABEL_NORMAL), obj, LABEL_NORMAL, length);
        }
        System.arraycopy(bArr, i, obj, length, i6);
        this.name = obj;
        while (i4 < i2) {
            setoffset(i3 + i4, length);
            length += obj[length] + 1;
            i4++;
        }
        setlabels(i7);
    }

    private static TextParseException parseException(String str, String str2) {
        return new TextParseException("'" + str + "': " + str2);
    }

    private final void appendFromString(String str, byte[] bArr, int i, int i2) throws TextParseException {
        try {
            append(bArr, i, i2);
        } catch (NameTooLongException e) {
            throw parseException(str, "Name too long");
        }
    }

    private final void appendSafe(byte[] bArr, int i, int i2) {
        try {
            append(bArr, i, i2);
        } catch (NameTooLongException e) {
        }
    }

    public Name(String str, Name name) throws TextParseException {
        if (str.equals(XmlPullParser.NO_NAMESPACE)) {
            throw parseException(str, "empty name");
        } else if (str.equals(XmppConnection.JID_SEPARATOR)) {
            if (name == null) {
                copy(empty, this);
            } else {
                copy(name, this);
            }
        } else if (str.equals(".")) {
            copy(root, this);
        } else {
            int i = -1;
            int i2 = 1;
            byte[] bArr = new byte[64];
            Object obj = null;
            int i3 = LABEL_NORMAL;
            int i4 = LABEL_NORMAL;
            for (int i5 = LABEL_NORMAL; i5 < str.length(); i5++) {
                byte charAt = (byte) str.charAt(i5);
                if (obj != null) {
                    byte b;
                    if (charAt >= 48 && charAt <= 57 && i3 < 3) {
                        i3++;
                        i4 = (i4 * 10) + (charAt - 48);
                        if (i4 > MAXNAME) {
                            throw parseException(str, "bad escape");
                        } else if (i3 < 3) {
                            continue;
                        } else {
                            b = (byte) i4;
                        }
                    } else if (i3 <= 0 || i3 >= 3) {
                        b = charAt;
                    } else {
                        throw parseException(str, "bad escape");
                    }
                    if (i2 > MAXLABEL) {
                        throw parseException(str, "label too long");
                    }
                    i = i2 + 1;
                    bArr[i2] = b;
                    obj = null;
                    int i6 = i;
                    i = i2;
                    i2 = i6;
                } else if (charAt == 92) {
                    obj = 1;
                    i3 = LABEL_NORMAL;
                    i4 = LABEL_NORMAL;
                } else if (charAt != 46) {
                    int i7;
                    if (i == -1) {
                        i7 = i5;
                    } else {
                        i7 = i;
                    }
                    if (i2 > MAXLABEL) {
                        throw parseException(str, "label too long");
                    }
                    i = i2 + 1;
                    bArr[i2] = charAt;
                    i2 = i;
                    i = i7;
                } else if (i == -1) {
                    throw parseException(str, "invalid empty label");
                } else {
                    bArr[LABEL_NORMAL] = (byte) (i2 - 1);
                    appendFromString(str, bArr, LABEL_NORMAL, 1);
                    i = -1;
                    i2 = 1;
                }
            }
            if (i3 > 0 && i3 < 3) {
                throw parseException(str, "bad escape");
            } else if (obj != null) {
                throw parseException(str, "bad escape");
            } else {
                Object obj2;
                if (i == -1) {
                    appendFromString(str, emptyLabel, LABEL_NORMAL, 1);
                    obj2 = 1;
                } else {
                    bArr[LABEL_NORMAL] = (byte) (i2 - 1);
                    appendFromString(str, bArr, LABEL_NORMAL, 1);
                    obj2 = LABEL_NORMAL;
                }
                if (name != null && r0 == null) {
                    appendFromString(str, name.name, LABEL_NORMAL, name.getlabels());
                }
            }
        }
    }

    public Name(String str) throws TextParseException {
        this(str, null);
    }

    public static Name fromString(String str, Name name) throws TextParseException {
        if (str.equals(XmppConnection.JID_SEPARATOR) && name != null) {
            return name;
        }
        if (str.equals(".")) {
            return root;
        }
        return new Name(str, name);
    }

    public static Name fromString(String str) throws TextParseException {
        return fromString(str, null);
    }

    public static Name fromConstantString(String str) {
        try {
            return fromString(str, null);
        } catch (TextParseException e) {
            throw new IllegalArgumentException("Invalid name '" + str + "'");
        }
    }

    public Name(DNSInput dNSInput) throws WireParseException {
        byte[] bArr = new byte[64];
        int i = LABEL_NORMAL;
        int i2 = LABEL_NORMAL;
        while (i2 == 0) {
            int readU8 = dNSInput.readU8();
            switch (readU8 & LABEL_MASK) {
                case LABEL_NORMAL /*0*/:
                    if (getlabels() < MAXLABELS) {
                        if (readU8 != 0) {
                            bArr[LABEL_NORMAL] = (byte) readU8;
                            dNSInput.readByteArray(bArr, 1, readU8);
                            append(bArr, LABEL_NORMAL, 1);
                            break;
                        }
                        append(emptyLabel, LABEL_NORMAL, 1);
                        i2 = 1;
                        break;
                    }
                    throw new WireParseException("too many labels");
                case LABEL_MASK /*192*/:
                    readU8 = ((readU8 & -193) << 8) + dNSInput.readU8();
                    if (Options.check("verbosecompression")) {
                        System.err.println("currently " + dNSInput.current() + ", pointer to " + readU8);
                    }
                    if (readU8 < dNSInput.current() - 2) {
                        if (i == 0) {
                            dNSInput.save();
                            i = 1;
                        }
                        dNSInput.jump(readU8);
                        if (!Options.check("verbosecompression")) {
                            break;
                        }
                        System.err.println("current name '" + this + "', seeking to " + readU8);
                        break;
                    }
                    throw new WireParseException("bad compression");
                default:
                    throw new WireParseException("bad label type");
            }
        }
        if (i != 0) {
            dNSInput.restore();
        }
    }

    public Name(byte[] bArr) throws IOException {
        this(new DNSInput(bArr));
    }

    public Name(Name name, int i) {
        int labels = name.labels();
        if (i > labels) {
            throw new IllegalArgumentException("attempted to remove too many labels");
        }
        this.name = name.name;
        setlabels(labels - i);
        int i2 = LABEL_NORMAL;
        while (i2 < MAXOFFSETS && i2 < labels - i) {
            setoffset(i2, name.offset(i2 + i));
            i2++;
        }
    }

    public static Name concatenate(Name name, Name name2) throws NameTooLongException {
        if (name.isAbsolute()) {
            return name;
        }
        Name name3 = new Name();
        copy(name, name3);
        name3.append(name2.name, name2.offset(LABEL_NORMAL), name2.getlabels());
        return name3;
    }

    public Name relativize(Name name) {
        if (name == null || !subdomain(name)) {
            return this;
        }
        Name name2 = new Name();
        copy(this, name2);
        int length = length() - name.length();
        name2.setlabels(name2.labels() - name.labels());
        name2.name = new byte[length];
        System.arraycopy(this.name, offset(LABEL_NORMAL), name2.name, LABEL_NORMAL, length);
        return name2;
    }

    public Name wild(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("must replace 1 or more labels");
        }
        try {
            Name name = new Name();
            copy(wild, name);
            name.append(this.name, offset(i), getlabels() - i);
            return name;
        } catch (NameTooLongException e) {
            throw new IllegalStateException("Name.wild: concatenate failed");
        }
    }

    public Name fromDNAME(DNAMERecord dNAMERecord) throws NameTooLongException {
        int i = LABEL_NORMAL;
        Name name = dNAMERecord.getName();
        Name target = dNAMERecord.getTarget();
        if (!subdomain(name)) {
            return null;
        }
        int labels = labels() - name.labels();
        int length = length() - name.length();
        int offset = offset(LABEL_NORMAL);
        int labels2 = target.labels();
        short length2 = target.length();
        if (length + length2 > MAXNAME) {
            throw new NameTooLongException();
        }
        name = new Name();
        name.setlabels(labels + labels2);
        name.name = new byte[(length + length2)];
        System.arraycopy(this.name, offset, name.name, LABEL_NORMAL, length);
        System.arraycopy(target.name, LABEL_NORMAL, name.name, length, length2);
        int i2 = LABEL_NORMAL;
        while (i2 < MAXOFFSETS && i2 < labels + labels2) {
            name.setoffset(i2, i);
            i += name.name[i] + 1;
            i2++;
        }
        return name;
    }

    public boolean isWild() {
        boolean z = true;
        if (labels() == 0) {
            return false;
        }
        if (!(this.name[LABEL_NORMAL] == (byte) 1 && this.name[1] == 42)) {
            z = LABEL_NORMAL;
        }
        return z;
    }

    public boolean isAbsolute() {
        if (labels() != 0 && this.name[this.name.length - 1] == null) {
            return true;
        }
        return false;
    }

    public short length() {
        if (getlabels() == 0) {
            return (short) 0;
        }
        return (short) (this.name.length - offset(LABEL_NORMAL));
    }

    public int labels() {
        return getlabels();
    }

    public boolean subdomain(Name name) {
        int labels = labels();
        int labels2 = name.labels();
        if (labels2 > labels) {
            return false;
        }
        if (labels2 == labels) {
            return equals(name);
        }
        return name.equals(this.name, offset(labels - labels2));
    }

    private String byteString(byte[] bArr, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        int i2 = i + 1;
        byte b = bArr[i];
        for (int i3 = i2; i3 < i2 + b; i3++) {
            int i4 = bArr[i3] & MAXNAME;
            if (i4 <= 32 || i4 >= Service.LOCUS_CON) {
                stringBuffer.append('\\');
                stringBuffer.append(byteFormat.format((long) i4));
            } else if (i4 == 34 || i4 == 40 || i4 == 41 || i4 == 46 || i4 == 59 || i4 == 92 || i4 == 64 || i4 == 36) {
                stringBuffer.append('\\');
                stringBuffer.append((char) i4);
            } else {
                stringBuffer.append((char) i4);
            }
        }
        return stringBuffer.toString();
    }

    public String toString() {
        int i = LABEL_NORMAL;
        int labels = labels();
        if (labels == 0) {
            return XmppConnection.JID_SEPARATOR;
        }
        if (labels == 1 && this.name[offset(LABEL_NORMAL)] == null) {
            return ".";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int offset = offset(LABEL_NORMAL);
        while (i < labels) {
            byte b = this.name[offset];
            if (b > MAXLABEL) {
                throw new IllegalStateException("invalid label");
            } else if (b == null) {
                break;
            } else {
                stringBuffer.append(byteString(this.name, offset));
                stringBuffer.append('.');
                offset += b + 1;
                i++;
            }
        }
        if (!isAbsolute()) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    public byte[] getLabel(int i) {
        int offset = offset(i);
        byte b = (byte) (this.name[offset] + 1);
        Object obj = new byte[b];
        System.arraycopy(this.name, offset, obj, LABEL_NORMAL, b);
        return obj;
    }

    public String getLabelString(int i) {
        return byteString(this.name, offset(i));
    }

    public void toWire(DNSOutput dNSOutput, Compression compression) {
        if (isAbsolute()) {
            int labels = labels();
            for (int i = LABEL_NORMAL; i < labels - 1; i++) {
                Name name;
                if (i == 0) {
                    name = this;
                } else {
                    name = new Name(this, i);
                }
                int i2 = -1;
                if (compression != null) {
                    i2 = compression.get(name);
                }
                if (i2 >= 0) {
                    dNSOutput.writeU16(i2 | KEYRecord.FLAG_NOKEY);
                    return;
                }
                if (compression != null) {
                    compression.add(dNSOutput.current(), name);
                }
                i2 = offset(i);
                dNSOutput.writeByteArray(this.name, i2, this.name[i2] + 1);
            }
            dNSOutput.writeU8(LABEL_NORMAL);
            return;
        }
        throw new IllegalArgumentException("toWire() called on non-absolute name");
    }

    public byte[] toWire() {
        DNSOutput dNSOutput = new DNSOutput();
        toWire(dNSOutput, null);
        return dNSOutput.toByteArray();
    }

    public void toWireCanonical(DNSOutput dNSOutput) {
        dNSOutput.writeByteArray(toWireCanonical());
    }

    public byte[] toWireCanonical() {
        int labels = labels();
        if (labels == 0) {
            return new byte[LABEL_NORMAL];
        }
        byte[] bArr = new byte[(this.name.length - offset(LABEL_NORMAL))];
        int offset = offset(LABEL_NORMAL);
        int i = LABEL_NORMAL;
        for (int i2 = LABEL_NORMAL; i2 < labels; i2++) {
            byte b = this.name[offset];
            if (b > MAXLABEL) {
                throw new IllegalStateException("invalid label");
            }
            int i3 = i + 1;
            int i4 = offset + 1;
            bArr[i] = this.name[offset];
            offset = i4;
            i4 = i3;
            byte b2 = LABEL_NORMAL;
            while (b2 < b) {
                i = i4 + 1;
                int i5 = offset + 1;
                bArr[i4] = lowercase[this.name[offset] & MAXNAME];
                b2++;
                i4 = i;
                offset = i5;
            }
            i = i4;
        }
        return bArr;
    }

    public void toWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        if (z) {
            toWireCanonical(dNSOutput);
        } else {
            toWire(dNSOutput, compression);
        }
    }

    private final boolean equals(byte[] bArr, int i) {
        int labels = labels();
        int offset = offset(LABEL_NORMAL);
        int i2 = LABEL_NORMAL;
        while (i2 < labels) {
            if (this.name[offset] != bArr[i]) {
                return false;
            }
            int i3 = offset + 1;
            byte b = this.name[offset];
            offset = i + 1;
            if (b > MAXLABEL) {
                throw new IllegalStateException("invalid label");
            }
            int i4 = offset;
            offset = i3;
            byte b2 = LABEL_NORMAL;
            while (b2 < b) {
                int i5 = offset + 1;
                byte b3 = lowercase[this.name[offset] & MAXNAME];
                offset = i4 + 1;
                if (b3 != lowercase[bArr[i4] & MAXNAME]) {
                    return false;
                }
                b2++;
                i4 = offset;
                offset = i5;
            }
            i2++;
            i = i4;
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof Name)) {
            return false;
        }
        Name name = (Name) obj;
        if (name.hashcode == 0) {
            name.hashCode();
        }
        if (this.hashcode == 0) {
            hashCode();
        }
        if (name.hashcode == this.hashcode && name.labels() == labels()) {
            return equals(name.name, name.offset(LABEL_NORMAL));
        }
        return false;
    }

    public int hashCode() {
        int i = LABEL_NORMAL;
        if (this.hashcode != 0) {
            return this.hashcode;
        }
        for (int offset = offset(LABEL_NORMAL); offset < this.name.length; offset++) {
            i += (i << 3) + lowercase[this.name[offset] & MAXNAME];
        }
        this.hashcode = i;
        return this.hashcode;
    }

    public int compareTo(Object obj) {
        Name name = (Name) obj;
        if (this == name) {
            return LABEL_NORMAL;
        }
        int labels = labels();
        int labels2 = name.labels();
        int i = labels > labels2 ? labels2 : labels;
        for (int i2 = 1; i2 <= i; i2++) {
            int offset = offset(labels - i2);
            int offset2 = name.offset(labels2 - i2);
            byte b = this.name[offset];
            byte b2 = name.name[offset2];
            byte b3 = LABEL_NORMAL;
            while (b3 < b && b3 < b2) {
                int i3 = lowercase[this.name[(b3 + offset) + 1] & MAXNAME] - lowercase[name.name[(b3 + offset2) + 1] & MAXNAME];
                if (i3 != 0) {
                    return i3;
                }
                b3++;
            }
            if (b != b2) {
                return b - b2;
            }
        }
        return labels - labels2;
    }
}
