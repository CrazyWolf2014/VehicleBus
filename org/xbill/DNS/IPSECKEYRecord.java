package org.xbill.DNS;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.utils.base64;

public class IPSECKEYRecord extends Record {
    private static final long serialVersionUID = 3050449702765909687L;
    private int algorithmType;
    private Object gateway;
    private int gatewayType;
    private byte[] key;
    private int precedence;

    public static class Algorithm {
        public static final int DSA = 1;
        public static final int RSA = 2;

        private Algorithm() {
        }
    }

    public static class Gateway {
        public static final int IPv4 = 1;
        public static final int IPv6 = 2;
        public static final int Name = 3;
        public static final int None = 0;

        private Gateway() {
        }
    }

    IPSECKEYRecord() {
    }

    Record getObject() {
        return new IPSECKEYRecord();
    }

    public IPSECKEYRecord(Name name, int i, long j, int i2, int i3, int i4, Object obj, byte[] bArr) {
        super(name, 45, i, j);
        this.precedence = Record.checkU8("precedence", i2);
        this.gatewayType = Record.checkU8("gatewayType", i3);
        this.algorithmType = Record.checkU8("algorithmType", i4);
        switch (i3) {
            case KEYRecord.OWNER_USER /*0*/:
                this.gateway = null;
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                if (obj instanceof InetAddress) {
                    this.gateway = obj;
                    break;
                }
                throw new IllegalArgumentException("\"gateway\" must be an IPv4 address");
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (obj instanceof Inet6Address) {
                    this.gateway = obj;
                    break;
                }
                throw new IllegalArgumentException("\"gateway\" must be an IPv6 address");
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                if (obj instanceof Name) {
                    this.gateway = Record.checkName("gateway", (Name) obj);
                    break;
                }
                throw new IllegalArgumentException("\"gateway\" must be a DNS name");
            default:
                throw new IllegalArgumentException("\"gatewayType\" must be between 0 and 3");
        }
        this.key = bArr;
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.precedence = dNSInput.readU8();
        this.gatewayType = dNSInput.readU8();
        this.algorithmType = dNSInput.readU8();
        switch (this.gatewayType) {
            case KEYRecord.OWNER_USER /*0*/:
                this.gateway = null;
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.gateway = InetAddress.getByAddress(dNSInput.readByteArray(4));
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                this.gateway = InetAddress.getByAddress(dNSInput.readByteArray(16));
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                this.gateway = new Name(dNSInput);
                break;
            default:
                throw new WireParseException("invalid gateway type");
        }
        if (dNSInput.remaining() > 0) {
            this.key = dNSInput.readByteArray();
        }
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.precedence = tokenizer.getUInt8();
        this.gatewayType = tokenizer.getUInt8();
        this.algorithmType = tokenizer.getUInt8();
        switch (this.gatewayType) {
            case KEYRecord.OWNER_USER /*0*/:
                if (tokenizer.getString().equals(".")) {
                    this.gateway = null;
                    break;
                }
                throw new TextParseException("invalid gateway format");
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.gateway = tokenizer.getAddress(1);
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                this.gateway = tokenizer.getAddress(2);
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                this.gateway = tokenizer.getName(name);
                break;
            default:
                throw new WireParseException("invalid gateway type");
        }
        this.key = tokenizer.getBase64(false);
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.precedence);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.gatewayType);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.algorithmType);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        switch (this.gatewayType) {
            case KEYRecord.OWNER_USER /*0*/:
                stringBuffer.append(".");
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                stringBuffer.append(((InetAddress) this.gateway).getHostAddress());
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                stringBuffer.append(this.gateway);
                break;
        }
        if (this.key != null) {
            stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            stringBuffer.append(base64.toString(this.key));
        }
        return stringBuffer.toString();
    }

    public int getPrecedence() {
        return this.precedence;
    }

    public int getGatewayType() {
        return this.gatewayType;
    }

    public int getAlgorithmType() {
        return this.algorithmType;
    }

    public Object getGateway() {
        return this.gateway;
    }

    public byte[] getKey() {
        return this.key;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU8(this.precedence);
        dNSOutput.writeU8(this.gatewayType);
        dNSOutput.writeU8(this.algorithmType);
        switch (this.gatewayType) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                dNSOutput.writeByteArray(((InetAddress) this.gateway).getAddress());
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                ((Name) this.gateway).toWire(dNSOutput, null, z);
                break;
        }
        if (this.key != null) {
            dNSOutput.writeByteArray(this.key);
        }
    }
}
