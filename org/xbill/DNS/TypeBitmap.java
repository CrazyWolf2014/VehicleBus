package org.xbill.DNS;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;
import org.xbill.DNS.Tokenizer.Token;

final class TypeBitmap implements Serializable {
    private static final long serialVersionUID = -125354057735389003L;
    private TreeSet types;

    private TypeBitmap() {
        this.types = new TreeSet();
    }

    public TypeBitmap(int[] iArr) {
        this();
        for (int i = 0; i < iArr.length; i++) {
            Type.check(iArr[i]);
            this.types.add(new Integer(iArr[i]));
        }
    }

    public TypeBitmap(DNSInput dNSInput) throws WireParseException {
        this();
        while (dNSInput.remaining() > 0) {
            if (dNSInput.remaining() < 2) {
                throw new WireParseException("invalid bitmap descriptor");
            }
            int readU8 = dNSInput.readU8();
            if (readU8 < -1) {
                throw new WireParseException("invalid ordering");
            }
            int readU82 = dNSInput.readU8();
            if (readU82 > dNSInput.remaining()) {
                throw new WireParseException("invalid bitmap");
            }
            for (int i = 0; i < readU82; i++) {
                int readU83 = dNSInput.readU8();
                if (readU83 != 0) {
                    for (int i2 = 0; i2 < 8; i2++) {
                        if (((1 << (7 - i2)) & readU83) != 0) {
                            this.types.add(Mnemonic.toInteger(((readU8 * KEYRecord.OWNER_ZONE) + (i * 8)) + i2));
                        }
                    }
                }
            }
        }
    }

    public TypeBitmap(Tokenizer tokenizer) throws IOException {
        Token token;
        this();
        while (true) {
            token = tokenizer.get();
            if (token.isString()) {
                int value = Type.value(token.value);
                if (value < 0) {
                    break;
                }
                this.types.add(Mnemonic.toInteger(value));
            } else {
                tokenizer.unget();
                return;
            }
        }
        throw tokenizer.exception("Invalid type: " + token.value);
    }

    public int[] toArray() {
        int[] iArr = new int[this.types.size()];
        Iterator it = this.types.iterator();
        int i = 0;
        while (it.hasNext()) {
            int i2 = i + 1;
            iArr[i] = ((Integer) it.next()).intValue();
            i = i2;
        }
        return iArr;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = this.types.iterator();
        while (it.hasNext()) {
            stringBuffer.append(Type.string(((Integer) it.next()).intValue()));
            if (it.hasNext()) {
                stringBuffer.append(' ');
            }
        }
        return stringBuffer.toString();
    }

    private static void mapToWire(DNSOutput dNSOutput, TreeSet treeSet, int i) {
        int intValue = ((((Integer) treeSet.last()).intValue() & KEYRecord.PROTOCOL_ANY) / 8) + 1;
        int[] iArr = new int[intValue];
        dNSOutput.writeU8(i);
        dNSOutput.writeU8(intValue);
        Iterator it = treeSet.iterator();
        while (it.hasNext()) {
            int intValue2 = ((Integer) it.next()).intValue();
            int i2 = (intValue2 & KEYRecord.PROTOCOL_ANY) / 8;
            iArr[i2] = (1 << (7 - (intValue2 % 8))) | iArr[i2];
        }
        for (intValue2 = 0; intValue2 < intValue; intValue2++) {
            dNSOutput.writeU8(iArr[intValue2]);
        }
    }

    public void toWire(DNSOutput dNSOutput) {
        if (this.types.size() != 0) {
            TreeSet treeSet = new TreeSet();
            Iterator it = this.types.iterator();
            int i = -1;
            while (it.hasNext()) {
                int intValue = ((Integer) it.next()).intValue();
                int i2 = intValue >> 8;
                if (i2 == i) {
                    i2 = i;
                } else if (treeSet.size() > 0) {
                    mapToWire(dNSOutput, treeSet, i);
                    treeSet.clear();
                }
                treeSet.add(new Integer(intValue));
                i = i2;
            }
            mapToWire(dNSOutput, treeSet, i);
        }
    }

    public boolean empty() {
        return this.types.isEmpty();
    }

    public boolean contains(int i) {
        return this.types.contains(Mnemonic.toInteger(i));
    }
}
