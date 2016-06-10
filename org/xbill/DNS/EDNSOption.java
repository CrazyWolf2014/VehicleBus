package org.xbill.DNS;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import java.io.IOException;
import java.util.Arrays;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;

public abstract class EDNSOption {
    private final int code;

    public static class Code {
        public static final int CLIENT_SUBNET = 20730;
        public static final int NSID = 3;
        private static Mnemonic codes;

        private Code() {
        }

        static {
            codes = new Mnemonic("EDNS Option Codes", 2);
            codes.setMaximum(InBandBytestreamManager.MAXIMUM_BLOCK_SIZE);
            codes.setPrefix("CODE");
            codes.setNumericAllowed(true);
            codes.add(NSID, "NSID");
            codes.add(CLIENT_SUBNET, "CLIENT_SUBNET");
        }

        public static String string(int i) {
            return codes.getText(i);
        }

        public static int value(String str) {
            return codes.getValue(str);
        }
    }

    abstract void optionFromWire(DNSInput dNSInput) throws IOException;

    abstract String optionToString();

    abstract void optionToWire(DNSOutput dNSOutput);

    public EDNSOption(int i) {
        this.code = Record.checkU16("code", i);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{");
        stringBuffer.append(Code.string(this.code));
        stringBuffer.append(": ");
        stringBuffer.append(optionToString());
        stringBuffer.append("}");
        return stringBuffer.toString();
    }

    public int getCode() {
        return this.code;
    }

    byte[] getData() {
        DNSOutput dNSOutput = new DNSOutput();
        optionToWire(dNSOutput);
        return dNSOutput.toByteArray();
    }

    static EDNSOption fromWire(DNSInput dNSInput) throws IOException {
        int readU16 = dNSInput.readU16();
        int readU162 = dNSInput.readU16();
        if (dNSInput.remaining() < readU162) {
            throw new WireParseException("truncated option");
        }
        EDNSOption nSIDOption;
        int saveActive = dNSInput.saveActive();
        dNSInput.setActive(readU162);
        switch (readU16) {
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                nSIDOption = new NSIDOption();
                break;
            case Code.CLIENT_SUBNET /*20730*/:
                nSIDOption = new ClientSubnetOption();
                break;
            default:
                nSIDOption = new GenericEDNSOption(readU16);
                break;
        }
        nSIDOption.optionFromWire(dNSInput);
        dNSInput.restoreActive(saveActive);
        return nSIDOption;
    }

    public static EDNSOption fromWire(byte[] bArr) throws IOException {
        return fromWire(new DNSInput(bArr));
    }

    void toWire(DNSOutput dNSOutput) {
        dNSOutput.writeU16(this.code);
        int current = dNSOutput.current();
        dNSOutput.writeU16(0);
        optionToWire(dNSOutput);
        dNSOutput.writeU16At((dNSOutput.current() - current) - 2, current);
    }

    public byte[] toWire() throws IOException {
        DNSOutput dNSOutput = new DNSOutput();
        toWire(dNSOutput);
        return dNSOutput.toByteArray();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof EDNSOption)) {
            return false;
        }
        EDNSOption eDNSOption = (EDNSOption) obj;
        if (this.code == eDNSOption.code) {
            return Arrays.equals(getData(), eDNSOption.getData());
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        byte[] data = getData();
        int i2 = 0;
        while (i < data.length) {
            i2 += (i2 << 3) + (data[i] & KEYRecord.PROTOCOL_ANY);
            i++;
        }
        return i2;
    }
}
