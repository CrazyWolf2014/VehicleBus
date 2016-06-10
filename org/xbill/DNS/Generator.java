package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class Generator {
    private long current;
    public final int dclass;
    public long end;
    public final String namePattern;
    public final Name origin;
    public final String rdataPattern;
    public long start;
    public long step;
    public final long ttl;
    public final int type;

    public static boolean supportedType(int i) {
        Type.check(i);
        if (i == 12 || i == 5 || i == 39 || i == 1 || i == 28 || i == 2) {
            return true;
        }
        return false;
    }

    public Generator(long j, long j2, long j3, String str, int i, int i2, long j4, String str2, Name name) {
        if (j < 0 || j2 < 0 || j > j2 || j3 <= 0) {
            throw new IllegalArgumentException("invalid range specification");
        } else if (supportedType(i)) {
            DClass.check(i2);
            this.start = j;
            this.end = j2;
            this.step = j3;
            this.namePattern = str;
            this.type = i;
            this.dclass = i2;
            this.ttl = j4;
            this.rdataPattern = str2;
            this.origin = name;
            this.current = j;
        } else {
            throw new IllegalArgumentException("unsupported type");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String substitute(java.lang.String r18, long r19) throws java.io.IOException {
        /*
        r17 = this;
        r1 = 0;
        r14 = r18.getBytes();
        r15 = new java.lang.StringBuffer;
        r15.<init>();
        r0 = 0;
    L_0x000b:
        r2 = r14.length;
        if (r0 >= r2) goto L_0x0192;
    L_0x000e:
        r2 = r14[r0];
        r2 = r2 & 255;
        r5 = (char) r2;
        if (r1 == 0) goto L_0x001c;
    L_0x0015:
        r15.append(r5);
        r1 = 0;
    L_0x0019:
        r0 = r0 + 1;
        goto L_0x000b;
    L_0x001c:
        r2 = 92;
        if (r5 != r2) goto L_0x002f;
    L_0x0020:
        r1 = r0 + 1;
        r2 = r14.length;
        if (r1 != r2) goto L_0x002d;
    L_0x0025:
        r0 = new org.xbill.DNS.TextParseException;
        r1 = "invalid escape character";
        r0.<init>(r1);
        throw r0;
    L_0x002d:
        r1 = 1;
        goto L_0x0019;
    L_0x002f:
        r2 = 36;
        if (r5 != r2) goto L_0x018d;
    L_0x0033:
        r4 = 0;
        r2 = 0;
        r7 = 0;
        r10 = 10;
        r9 = 0;
        r6 = r0 + 1;
        r12 = r14.length;
        if (r6 >= r12) goto L_0x0053;
    L_0x0040:
        r6 = r0 + 1;
        r6 = r14[r6];
        r12 = 36;
        if (r6 != r12) goto L_0x0053;
    L_0x0048:
        r0 = r0 + 1;
        r2 = r14[r0];
        r2 = r2 & 255;
        r2 = (char) r2;
        r15.append(r2);
        goto L_0x0019;
    L_0x0053:
        r6 = r0 + 1;
        r12 = r14.length;
        if (r6 >= r12) goto L_0x0137;
    L_0x0058:
        r6 = r0 + 1;
        r6 = r14[r6];
        r12 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        if (r6 != r12) goto L_0x0137;
    L_0x0060:
        r6 = r0 + 1;
        r0 = r6 + 1;
        r12 = r14.length;
        if (r0 >= r12) goto L_0x01b0;
    L_0x0067:
        r0 = r6 + 1;
        r0 = r14[r0];
        r12 = 45;
        if (r0 != r12) goto L_0x01b0;
    L_0x006f:
        r0 = 1;
        r4 = r6 + 1;
        r16 = r5;
        r5 = r4;
        r4 = r16;
    L_0x0077:
        r6 = r5 + 1;
        r12 = r14.length;
        if (r6 >= r12) goto L_0x008b;
    L_0x007c:
        r5 = r5 + 1;
        r4 = r14[r5];
        r4 = r4 & 255;
        r4 = (char) r4;
        r6 = 44;
        if (r4 == r6) goto L_0x008b;
    L_0x0087:
        r6 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r4 != r6) goto L_0x00c0;
    L_0x008b:
        if (r0 == 0) goto L_0x01ad;
    L_0x008d:
        r12 = -r2;
    L_0x008e:
        r0 = 44;
        if (r4 != r0) goto L_0x01aa;
    L_0x0092:
        r2 = r7;
        r0 = r4;
        r4 = r5;
    L_0x0095:
        r5 = r4 + 1;
        r6 = r14.length;
        if (r5 >= r6) goto L_0x01a2;
    L_0x009a:
        r4 = r4 + 1;
        r0 = r14[r4];
        r0 = r0 & 255;
        r0 = (char) r0;
        r5 = 44;
        if (r0 == r5) goto L_0x01a2;
    L_0x00a5:
        r5 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r0 != r5) goto L_0x00d9;
    L_0x00a9:
        r7 = r2;
        r16 = r0;
        r0 = r4;
        r4 = r16;
    L_0x00af:
        r2 = 44;
        if (r4 != r2) goto L_0x019d;
    L_0x00b3:
        r2 = r0 + 1;
        r3 = r14.length;
        if (r2 != r3) goto L_0x00f2;
    L_0x00b8:
        r0 = new org.xbill.DNS.TextParseException;
        r1 = "invalid base";
        r0.<init>(r1);
        throw r0;
    L_0x00c0:
        r6 = 48;
        if (r4 < r6) goto L_0x00c8;
    L_0x00c4:
        r6 = 57;
        if (r4 <= r6) goto L_0x00d0;
    L_0x00c8:
        r0 = new org.xbill.DNS.TextParseException;
        r1 = "invalid offset";
        r0.<init>(r1);
        throw r0;
    L_0x00d0:
        r4 = r4 + -48;
        r4 = (char) r4;
        r12 = 10;
        r2 = r2 * r12;
        r12 = (long) r4;
        r2 = r2 + r12;
        goto L_0x0077;
    L_0x00d9:
        r5 = 48;
        if (r0 < r5) goto L_0x00e1;
    L_0x00dd:
        r5 = 57;
        if (r0 <= r5) goto L_0x00e9;
    L_0x00e1:
        r0 = new org.xbill.DNS.TextParseException;
        r1 = "invalid width";
        r0.<init>(r1);
        throw r0;
    L_0x00e9:
        r0 = r0 + -48;
        r0 = (char) r0;
        r5 = 10;
        r2 = r2 * r5;
        r5 = (long) r0;
        r2 = r2 + r5;
        goto L_0x0095;
    L_0x00f2:
        r4 = r0 + 1;
        r0 = r14[r4];
        r0 = r0 & 255;
        r0 = (char) r0;
        r2 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        if (r0 != r2) goto L_0x0115;
    L_0x00fd:
        r2 = 8;
        r0 = r9;
    L_0x0100:
        r5 = r4 + 1;
        r6 = r14.length;
        if (r5 == r6) goto L_0x010d;
    L_0x0105:
        r5 = r4 + 1;
        r5 = r14[r5];
        r6 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r5 == r6) goto L_0x0131;
    L_0x010d:
        r0 = new org.xbill.DNS.TextParseException;
        r1 = "invalid modifiers";
        r0.<init>(r1);
        throw r0;
    L_0x0115:
        r2 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        if (r0 != r2) goto L_0x011d;
    L_0x0119:
        r2 = 16;
        r0 = r9;
        goto L_0x0100;
    L_0x011d:
        r2 = 88;
        if (r0 != r2) goto L_0x0125;
    L_0x0121:
        r2 = 16;
        r0 = 1;
        goto L_0x0100;
    L_0x0125:
        r2 = 100;
        if (r0 == r2) goto L_0x0199;
    L_0x0129:
        r0 = new org.xbill.DNS.TextParseException;
        r1 = "invalid base";
        r0.<init>(r1);
        throw r0;
    L_0x0131:
        r4 = r4 + 1;
        r9 = r0;
        r10 = r2;
        r2 = r12;
        r0 = r4;
    L_0x0137:
        r2 = r2 + r19;
        r4 = 0;
        r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r4 >= 0) goto L_0x0147;
    L_0x013f:
        r0 = new org.xbill.DNS.TextParseException;
        r1 = "invalid offset expansion";
        r0.<init>(r1);
        throw r0;
    L_0x0147:
        r4 = 8;
        r4 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1));
        if (r4 != 0) goto L_0x0178;
    L_0x014d:
        r2 = java.lang.Long.toOctalString(r2);
    L_0x0151:
        if (r9 == 0) goto L_0x0197;
    L_0x0153:
        r2 = r2.toUpperCase();
        r4 = r2;
    L_0x0158:
        r2 = 0;
        r2 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x0188;
    L_0x015e:
        r2 = r4.length();
        r2 = (long) r2;
        r2 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x0188;
    L_0x0167:
        r2 = (int) r7;
        r3 = r4.length();
        r2 = r2 - r3;
    L_0x016d:
        r3 = r2 + -1;
        if (r2 <= 0) goto L_0x0188;
    L_0x0171:
        r2 = 48;
        r15.append(r2);
        r2 = r3;
        goto L_0x016d;
    L_0x0178:
        r4 = 16;
        r4 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1));
        if (r4 != 0) goto L_0x0183;
    L_0x017e:
        r2 = java.lang.Long.toHexString(r2);
        goto L_0x0151;
    L_0x0183:
        r2 = java.lang.Long.toString(r2);
        goto L_0x0151;
    L_0x0188:
        r15.append(r4);
        goto L_0x0019;
    L_0x018d:
        r15.append(r5);
        goto L_0x0019;
    L_0x0192:
        r0 = r15.toString();
        return r0;
    L_0x0197:
        r4 = r2;
        goto L_0x0158;
    L_0x0199:
        r0 = r9;
        r2 = r10;
        goto L_0x0100;
    L_0x019d:
        r2 = r10;
        r4 = r0;
        r0 = r9;
        goto L_0x0100;
    L_0x01a2:
        r7 = r2;
        r16 = r0;
        r0 = r4;
        r4 = r16;
        goto L_0x00af;
    L_0x01aa:
        r0 = r5;
        goto L_0x00af;
    L_0x01ad:
        r12 = r2;
        goto L_0x008e;
    L_0x01b0:
        r0 = r4;
        r4 = r5;
        r5 = r6;
        goto L_0x0077;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.Generator.substitute(java.lang.String, long):java.lang.String");
    }

    public Record nextRecord() throws IOException {
        if (this.current > this.end) {
            return null;
        }
        Name fromString = Name.fromString(substitute(this.namePattern, this.current), this.origin);
        String substitute = substitute(this.rdataPattern, this.current);
        this.current += this.step;
        return Record.fromString(fromString, this.type, this.dclass, this.ttl, substitute, this.origin);
    }

    public Record[] expand() throws IOException {
        List arrayList = new ArrayList();
        long j = this.start;
        while (j < this.end) {
            arrayList.add(Record.fromString(Name.fromString(substitute(this.namePattern, this.current), this.origin), this.type, this.dclass, this.ttl, substitute(this.rdataPattern, this.current), this.origin));
            j = this.step + j;
        }
        return (Record[]) arrayList.toArray(new Record[arrayList.size()]);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("$GENERATE ");
        stringBuffer.append(this.start + "-" + this.end);
        if (this.step > 1) {
            stringBuffer.append(FilePathGenerator.ANDROID_DIR_SEP + this.step);
        }
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.namePattern + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.ttl + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        if (!(this.dclass == 1 && Options.check("noPrintIN"))) {
            stringBuffer.append(DClass.string(this.dclass) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        }
        stringBuffer.append(Type.string(this.type) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.rdataPattern + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        return stringBuffer.toString();
    }
}
