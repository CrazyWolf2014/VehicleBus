package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.utils.base16;

public class APLRecord extends Record {
    private static final long serialVersionUID = -1348173791712935864L;
    private List elements;

    public static class Element {
        public final Object address;
        public final int family;
        public final boolean negative;
        public final int prefixLength;

        private Element(int i, boolean z, Object obj, int i2) {
            this.family = i;
            this.negative = z;
            this.address = obj;
            this.prefixLength = i2;
            if (!APLRecord.validatePrefixLength(i, i2)) {
                throw new IllegalArgumentException("invalid prefix length");
            }
        }

        public Element(boolean z, InetAddress inetAddress, int i) {
            this(Address.familyOf(inetAddress), z, inetAddress, i);
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            if (this.negative) {
                stringBuffer.append("!");
            }
            stringBuffer.append(this.family);
            stringBuffer.append(":");
            if (this.family == 1 || this.family == 2) {
                stringBuffer.append(((InetAddress) this.address).getHostAddress());
            } else {
                stringBuffer.append(base16.toString((byte[]) this.address));
            }
            stringBuffer.append(FilePathGenerator.ANDROID_DIR_SEP);
            stringBuffer.append(this.prefixLength);
            return stringBuffer.toString();
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Element)) {
                return false;
            }
            Element element = (Element) obj;
            if (this.family == element.family && this.negative == element.negative && this.prefixLength == element.prefixLength && this.address.equals(element.address)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.negative ? 1 : 0) + (this.prefixLength + this.address.hashCode());
        }
    }

    APLRecord() {
    }

    Record getObject() {
        return new APLRecord();
    }

    private static boolean validatePrefixLength(int i, int i2) {
        if (i2 < 0 || i2 >= KEYRecord.OWNER_ZONE) {
            return false;
        }
        if (i == 1 && i2 > 32) {
            return false;
        }
        if (i != 2 || i2 <= Flags.FLAG8) {
            return true;
        }
        return false;
    }

    public APLRecord(Name name, int i, long j, List list) {
        super(name, 42, i, j);
        this.elements = new ArrayList(list.size());
        for (Object next : list) {
            if (next instanceof Element) {
                Element element = (Element) next;
                if (element.family == 1 || element.family == 2) {
                    this.elements.add(element);
                } else {
                    throw new IllegalArgumentException("unknown family");
                }
            }
            throw new IllegalArgumentException("illegal element");
        }
    }

    private static byte[] parseAddress(byte[] bArr, int i) throws WireParseException {
        if (bArr.length > i) {
            throw new WireParseException("invalid address length");
        } else if (bArr.length == i) {
            return bArr;
        } else {
            Object obj = new byte[i];
            System.arraycopy(bArr, 0, obj, 0, bArr.length);
            return obj;
        }
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.elements = new ArrayList(1);
        while (dNSInput.remaining() != 0) {
            int readU16 = dNSInput.readU16();
            int readU8 = dNSInput.readU8();
            int readU82 = dNSInput.readU8();
            boolean z = (readU82 & Flags.FLAG8) != 0;
            Object readByteArray = dNSInput.readByteArray(readU82 & -129);
            if (validatePrefixLength(readU16, readU8)) {
                Object element;
                if (readU16 == 1 || readU16 == 2) {
                    element = new Element(z, InetAddress.getByAddress(parseAddress(readByteArray, Address.addressLength(readU16))), readU8);
                } else {
                    element = new Element(z, readByteArray, readU8, null);
                }
                this.elements.add(element);
            } else {
                throw new WireParseException("invalid prefix length");
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void rdataFromString(org.xbill.DNS.Tokenizer r8, org.xbill.DNS.Name r9) throws java.io.IOException {
        /*
        r7 = this;
        r2 = 0;
        r1 = 1;
        r0 = new java.util.ArrayList;
        r0.<init>(r1);
        r7.elements = r0;
    L_0x0009:
        r0 = r8.get();
        r3 = r0.isString();
        if (r3 != 0) goto L_0x0017;
    L_0x0013:
        r8.unget();
        return;
    L_0x0017:
        r4 = r0.value;
        r0 = "!";
        r0 = r4.startsWith(r0);
        if (r0 == 0) goto L_0x00b0;
    L_0x0021:
        r0 = r1;
        r3 = r1;
    L_0x0023:
        r5 = 58;
        r5 = r4.indexOf(r5, r0);
        if (r5 >= 0) goto L_0x0032;
    L_0x002b:
        r0 = "invalid address prefix element";
        r0 = r8.exception(r0);
        throw r0;
    L_0x0032:
        r6 = 47;
        r6 = r4.indexOf(r6, r5);
        if (r6 >= 0) goto L_0x0041;
    L_0x003a:
        r0 = "invalid address prefix element";
        r0 = r8.exception(r0);
        throw r0;
    L_0x0041:
        r0 = r4.substring(r0, r5);
        r5 = r5 + 1;
        r5 = r4.substring(r5, r6);
        r6 = r6 + 1;
        r4 = r4.substring(r6);
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x0061 }
        if (r0 == r1) goto L_0x0069;
    L_0x0057:
        r6 = 2;
        if (r0 == r6) goto L_0x0069;
    L_0x005a:
        r0 = "unknown family";
        r0 = r8.exception(r0);
        throw r0;
    L_0x0061:
        r0 = move-exception;
        r0 = "invalid family";
        r0 = r8.exception(r0);
        throw r0;
    L_0x0069:
        r4 = java.lang.Integer.parseInt(r4);	 Catch:{ NumberFormatException -> 0x007a }
        r6 = validatePrefixLength(r0, r4);
        if (r6 != 0) goto L_0x0082;
    L_0x0073:
        r0 = "invalid prefix length";
        r0 = r8.exception(r0);
        throw r0;
    L_0x007a:
        r0 = move-exception;
        r0 = "invalid prefix length";
        r0 = r8.exception(r0);
        throw r0;
    L_0x0082:
        r0 = org.xbill.DNS.Address.toByteArray(r5, r0);
        if (r0 != 0) goto L_0x00a0;
    L_0x0088:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "invalid IP address ";
        r0 = r0.append(r1);
        r0 = r0.append(r5);
        r0 = r0.toString();
        r0 = r8.exception(r0);
        throw r0;
    L_0x00a0:
        r0 = java.net.InetAddress.getByAddress(r0);
        r5 = r7.elements;
        r6 = new org.xbill.DNS.APLRecord$Element;
        r6.<init>(r3, r0, r4);
        r5.add(r6);
        goto L_0x0009;
    L_0x00b0:
        r0 = r2;
        r3 = r2;
        goto L_0x0023;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.APLRecord.rdataFromString(org.xbill.DNS.Tokenizer, org.xbill.DNS.Name):void");
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = this.elements.iterator();
        while (it.hasNext()) {
            stringBuffer.append((Element) it.next());
            if (it.hasNext()) {
                stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            }
        }
        return stringBuffer.toString();
    }

    public List getElements() {
        return this.elements;
    }

    private static int addressLength(byte[] bArr) {
        for (int length = bArr.length - 1; length >= 0; length--) {
            if (bArr[length] != null) {
                return length + 1;
            }
        }
        return 0;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        for (Element element : this.elements) {
            byte[] address;
            int addressLength;
            int i;
            if (element.family == 1 || element.family == 2) {
                address = ((InetAddress) element.address).getAddress();
                addressLength = addressLength(address);
            } else {
                address = (byte[]) element.address;
                addressLength = address.length;
            }
            if (element.negative) {
                i = addressLength | Flags.FLAG8;
            } else {
                i = addressLength;
            }
            dNSOutput.writeU16(element.family);
            dNSOutput.writeU8(element.prefixLength);
            dNSOutput.writeU8(i);
            dNSOutput.writeByteArray(address, 0, addressLength);
        }
    }
}
