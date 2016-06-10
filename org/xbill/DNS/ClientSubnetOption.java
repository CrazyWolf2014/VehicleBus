package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.net.InetAddress;
import org.xbill.DNS.EDNSOption.Code;

public class ClientSubnetOption extends EDNSOption {
    private static final long serialVersionUID = -3868158449890266347L;
    private InetAddress address;
    private int family;
    private int scopeNetmask;
    private int sourceNetmask;

    ClientSubnetOption() {
        super(Code.CLIENT_SUBNET);
    }

    private static int checkMaskLength(String str, int i, int i2) {
        int addressLength = Address.addressLength(i) * 8;
        if (i2 >= 0 && i2 <= addressLength) {
            return i2;
        }
        throw new IllegalArgumentException("\"" + str + "\" " + i2 + " must be in the range " + "[0.." + addressLength + "]");
    }

    public ClientSubnetOption(int i, int i2, InetAddress inetAddress) {
        super(Code.CLIENT_SUBNET);
        this.family = Address.familyOf(inetAddress);
        this.sourceNetmask = checkMaskLength("source netmask", this.family, i);
        this.scopeNetmask = checkMaskLength("scope netmask", this.family, i2);
        this.address = Address.truncate(inetAddress, i);
        if (!inetAddress.equals(this.address)) {
            throw new IllegalArgumentException("source netmask is not valid for address");
        }
    }

    public ClientSubnetOption(int i, InetAddress inetAddress) {
        this(i, 0, inetAddress);
    }

    public int getFamily() {
        return this.family;
    }

    public int getSourceNetmask() {
        return this.sourceNetmask;
    }

    public int getScopeNetmask() {
        return this.scopeNetmask;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    void optionFromWire(DNSInput dNSInput) throws WireParseException {
        this.family = dNSInput.readU16();
        if (this.family == 1 || this.family == 2) {
            this.sourceNetmask = dNSInput.readU8();
            if (this.sourceNetmask > Address.addressLength(this.family) * 8) {
                throw new WireParseException("invalid source netmask");
            }
            this.scopeNetmask = dNSInput.readU8();
            if (this.scopeNetmask > Address.addressLength(this.family) * 8) {
                throw new WireParseException("invalid scope netmask");
            }
            Object readByteArray = dNSInput.readByteArray();
            if (readByteArray.length != (this.sourceNetmask + 7) / 8) {
                throw new WireParseException("invalid address");
            }
            Object obj = new byte[Address.addressLength(this.family)];
            System.arraycopy(readByteArray, 0, obj, 0, readByteArray.length);
            try {
                this.address = InetAddress.getByAddress(obj);
                if (!Address.truncate(this.address, this.sourceNetmask).equals(this.address)) {
                    throw new WireParseException("invalid padding");
                }
                return;
            } catch (Throwable e) {
                throw new WireParseException("invalid address", e);
            }
        }
        throw new WireParseException("unknown address family");
    }

    void optionToWire(DNSOutput dNSOutput) {
        dNSOutput.writeU16(this.family);
        dNSOutput.writeU8(this.sourceNetmask);
        dNSOutput.writeU8(this.scopeNetmask);
        dNSOutput.writeByteArray(this.address.getAddress(), 0, (this.sourceNetmask + 7) / 8);
    }

    String optionToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.address.getHostAddress());
        stringBuffer.append(FilePathGenerator.ANDROID_DIR_SEP);
        stringBuffer.append(this.sourceNetmask);
        stringBuffer.append(", scope netmask ");
        stringBuffer.append(this.scopeNetmask);
        return stringBuffer.toString();
    }
}
