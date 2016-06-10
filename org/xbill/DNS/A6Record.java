package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord.Flags;

public class A6Record extends Record {
    private static final long serialVersionUID = -8815026887337346789L;
    private Name prefix;
    private int prefixBits;
    private InetAddress suffix;

    A6Record() {
    }

    Record getObject() {
        return new A6Record();
    }

    public A6Record(Name name, int i, long j, int i2, InetAddress inetAddress, Name name2) {
        super(name, 38, i, j);
        this.prefixBits = Record.checkU8("prefixBits", i2);
        if (inetAddress == null || Address.familyOf(inetAddress) == 2) {
            this.suffix = inetAddress;
            if (name2 != null) {
                this.prefix = Record.checkName("prefix", name2);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("invalid IPv6 address");
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.prefixBits = dNSInput.readU8();
        int i = ((128 - this.prefixBits) + 7) / 8;
        if (this.prefixBits < Flags.FLAG8) {
            byte[] bArr = new byte[16];
            dNSInput.readByteArray(bArr, 16 - i, i);
            this.suffix = InetAddress.getByAddress(bArr);
        }
        if (this.prefixBits > 0) {
            this.prefix = new Name(dNSInput);
        }
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.prefixBits = tokenizer.getUInt8();
        if (this.prefixBits > Flags.FLAG8) {
            throw tokenizer.exception("prefix bits must be [0..128]");
        }
        if (this.prefixBits < Flags.FLAG8) {
            String string = tokenizer.getString();
            try {
                this.suffix = Address.getByAddress(string, 2);
            } catch (UnknownHostException e) {
                throw tokenizer.exception("invalid IPv6 address: " + string);
            }
        }
        if (this.prefixBits > 0) {
            this.prefix = tokenizer.getName(name);
        }
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.prefixBits);
        if (this.suffix != null) {
            stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            stringBuffer.append(this.suffix.getHostAddress());
        }
        if (this.prefix != null) {
            stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            stringBuffer.append(this.prefix);
        }
        return stringBuffer.toString();
    }

    public int getPrefixBits() {
        return this.prefixBits;
    }

    public InetAddress getSuffix() {
        return this.suffix;
    }

    public Name getPrefix() {
        return this.prefix;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU8(this.prefixBits);
        if (this.suffix != null) {
            int i = ((128 - this.prefixBits) + 7) / 8;
            dNSOutput.writeByteArray(this.suffix.getAddress(), 16 - i, i);
        }
        if (this.prefix != null) {
            this.prefix.toWire(dNSOutput, null, z);
        }
    }
}
