package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.IOException;
import java.util.Random;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;

public class Header implements Cloneable {
    public static final int LENGTH = 12;
    private static Random random;
    private int[] counts;
    private int flags;
    private int id;

    static {
        random = new Random();
    }

    private void init() {
        this.counts = new int[4];
        this.flags = 0;
        this.id = -1;
    }

    public Header(int i) {
        init();
        setID(i);
    }

    public Header() {
        init();
    }

    Header(DNSInput dNSInput) throws IOException {
        this(dNSInput.readU16());
        this.flags = dNSInput.readU16();
        for (int i = 0; i < this.counts.length; i++) {
            this.counts[i] = dNSInput.readU16();
        }
    }

    public Header(byte[] bArr) throws IOException {
        this(new DNSInput(bArr));
    }

    void toWire(DNSOutput dNSOutput) {
        dNSOutput.writeU16(getID());
        dNSOutput.writeU16(this.flags);
        for (int writeU16 : this.counts) {
            dNSOutput.writeU16(writeU16);
        }
    }

    public byte[] toWire() {
        DNSOutput dNSOutput = new DNSOutput();
        toWire(dNSOutput);
        return dNSOutput.toByteArray();
    }

    private static boolean validFlag(int i) {
        return i >= 0 && i <= 15 && Flags.isFlag(i);
    }

    private static void checkFlag(int i) {
        if (!validFlag(i)) {
            throw new IllegalArgumentException("invalid flag bit " + i);
        }
    }

    public void setFlag(int i) {
        checkFlag(i);
        this.flags |= 1 << (15 - i);
    }

    public void unsetFlag(int i) {
        checkFlag(i);
        this.flags &= (1 << (15 - i)) ^ -1;
    }

    public boolean getFlag(int i) {
        checkFlag(i);
        if ((this.flags & (1 << (15 - i))) != 0) {
            return true;
        }
        return false;
    }

    boolean[] getFlags() {
        boolean[] zArr = new boolean[16];
        for (int i = 0; i < zArr.length; i++) {
            if (validFlag(i)) {
                zArr[i] = getFlag(i);
            }
        }
        return zArr;
    }

    public int getID() {
        if (this.id >= 0) {
            return this.id;
        }
        int i;
        synchronized (this) {
            if (this.id < 0) {
                this.id = random.nextInt(InBandBytestreamManager.MAXIMUM_BLOCK_SIZE);
            }
            i = this.id;
        }
        return i;
    }

    public void setID(int i) {
        if (i < 0 || i > InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) {
            throw new IllegalArgumentException("DNS message ID " + i + " is out of range");
        }
        this.id = i;
    }

    public void setRcode(int i) {
        if (i < 0 || i > 15) {
            throw new IllegalArgumentException("DNS Rcode " + i + " is out of range");
        }
        this.flags &= -16;
        this.flags |= i;
    }

    public int getRcode() {
        return this.flags & 15;
    }

    public void setOpcode(int i) {
        if (i < 0 || i > 15) {
            throw new IllegalArgumentException("DNS Opcode " + i + "is out of range");
        }
        this.flags &= 34815;
        this.flags |= i << 11;
    }

    public int getOpcode() {
        return (this.flags >> 11) & 15;
    }

    void setCount(int i, int i2) {
        if (i2 < 0 || i2 > InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) {
            throw new IllegalArgumentException("DNS section count " + i2 + " is out of range");
        }
        this.counts[i] = i2;
    }

    void incCount(int i) {
        if (this.counts[i] == 65535) {
            throw new IllegalStateException("DNS section count cannot be incremented");
        }
        int[] iArr = this.counts;
        iArr[i] = iArr[i] + 1;
    }

    void decCount(int i) {
        if (this.counts[i] == 0) {
            throw new IllegalStateException("DNS section count cannot be decremented");
        }
        int[] iArr = this.counts;
        iArr[i] = iArr[i] - 1;
    }

    public int getCount(int i) {
        return this.counts[i];
    }

    public String printFlags() {
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (i < 16) {
            if (validFlag(i) && getFlag(i)) {
                stringBuffer.append(Flags.string(i));
                stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            }
            i++;
        }
        return stringBuffer.toString();
    }

    String toStringWithRcode(int i) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(";; ->>HEADER<<- ");
        stringBuffer.append("opcode: " + Opcode.string(getOpcode()));
        stringBuffer.append(", status: " + Rcode.string(i));
        stringBuffer.append(", id: " + getID());
        stringBuffer.append(SpecilApiUtil.LINE_SEP);
        stringBuffer.append(";; flags: " + printFlags());
        stringBuffer.append("; ");
        for (int i2 = 0; i2 < 4; i2++) {
            stringBuffer.append(Section.string(i2) + ": " + getCount(i2) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return toStringWithRcode(getRcode());
    }

    public Object clone() {
        Header header = new Header();
        header.id = this.id;
        header.flags = this.flags;
        System.arraycopy(this.counts, 0, header.counts, 0, this.counts.length);
        return header;
    }
}
