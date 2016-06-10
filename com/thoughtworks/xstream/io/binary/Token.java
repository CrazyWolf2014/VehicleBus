package com.thoughtworks.xstream.io.binary;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.tencent.mm.sdk.platformtools.Util;
import com.thoughtworks.xstream.io.StreamException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Protocol;

public abstract class Token {
    private static final byte ID_EIGHT_BYTES = (byte) 32;
    private static final byte ID_FOUR_BYTES = (byte) 24;
    private static final byte ID_MASK = (byte) 56;
    private static final byte ID_ONE_BYTE = (byte) 8;
    private static final String ID_SPLITTED = "\u0000\u2021\u0000";
    private static final byte ID_TWO_BYTES = (byte) 16;
    private static final int MAX_UTF8_LENGTH = 65535;
    public static final byte TYPE_ATTRIBUTE = (byte) 5;
    public static final byte TYPE_END_NODE = (byte) 4;
    public static final byte TYPE_MAP_ID_TO_VALUE = (byte) 2;
    private static final byte TYPE_MASK = (byte) 7;
    public static final byte TYPE_START_NODE = (byte) 3;
    public static final byte TYPE_VALUE = (byte) 6;
    public static final byte TYPE_VERSION = (byte) 1;
    protected long id;
    private final byte type;
    protected String value;

    public static class Formatter {
        public void write(DataOutput out, Token token) throws IOException {
            byte idType;
            long id = token.getId();
            if (id <= 255) {
                idType = Token.ID_ONE_BYTE;
            } else if (id <= 65535) {
                idType = Token.ID_TWO_BYTES;
            } else if (id <= Util.MAX_32BIT_VALUE) {
                idType = Token.ID_FOUR_BYTES;
            } else {
                idType = Token.ID_EIGHT_BYTES;
            }
            out.write(token.getType() + idType);
            token.writeTo(out, idType);
        }

        public Token read(DataInput in) throws IOException {
            byte nextByte = in.readByte();
            byte idType = (byte) (nextByte & 56);
            Token token = contructToken((byte) (nextByte & 7));
            token.readFrom(in, idType);
            return token;
        }

        private Token contructToken(byte type) {
            switch (type) {
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    return new MapIdToValue();
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    return new StartNode();
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    return new EndNode();
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    return new Attribute();
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    return new Value();
                default:
                    throw new StreamException("Unknown token type");
            }
        }
    }

    public static class Attribute extends Token {
        public Attribute(long id, String value) {
            super(Token.TYPE_ATTRIBUTE);
            this.id = id;
            this.value = value;
        }

        public Attribute() {
            super(Token.TYPE_ATTRIBUTE);
        }

        public void writeTo(DataOutput out, byte idType) throws IOException {
            writeId(out, this.id, idType);
            writeString(out, this.value);
        }

        public void readFrom(DataInput in, byte idType) throws IOException {
            this.id = readId(in, idType);
            this.value = readString(in);
        }
    }

    public static class EndNode extends Token {
        public EndNode() {
            super(Token.TYPE_END_NODE);
        }

        public void writeTo(DataOutput out, byte idType) {
        }

        public void readFrom(DataInput in, byte idType) {
        }
    }

    public static class MapIdToValue extends Token {
        public MapIdToValue(long id, String value) {
            super(Token.TYPE_MAP_ID_TO_VALUE);
            this.id = id;
            this.value = value;
        }

        public MapIdToValue() {
            super(Token.TYPE_MAP_ID_TO_VALUE);
        }

        public void writeTo(DataOutput out, byte idType) throws IOException {
            writeId(out, this.id, idType);
            writeString(out, this.value);
        }

        public void readFrom(DataInput in, byte idType) throws IOException {
            this.id = readId(in, idType);
            this.value = readString(in);
        }
    }

    public static class StartNode extends Token {
        public StartNode(long id) {
            super(Token.TYPE_START_NODE);
            this.id = id;
        }

        public StartNode() {
            super(Token.TYPE_START_NODE);
        }

        public void writeTo(DataOutput out, byte idType) throws IOException {
            writeId(out, this.id, idType);
        }

        public void readFrom(DataInput in, byte idType) throws IOException {
            this.id = readId(in, idType);
        }
    }

    public static class Value extends Token {
        public Value(String value) {
            super(Token.TYPE_VALUE);
            this.value = value;
        }

        public Value() {
            super(Token.TYPE_VALUE);
        }

        public void writeTo(DataOutput out, byte idType) throws IOException {
            writeString(out, this.value);
        }

        public void readFrom(DataInput in, byte idType) throws IOException {
            this.value = readString(in);
        }
    }

    public abstract void readFrom(DataInput dataInput, byte b) throws IOException;

    public abstract void writeTo(DataOutput dataOutput, byte b) throws IOException;

    public Token(byte type) {
        this.id = -1;
        this.type = type;
    }

    public byte getType() {
        return this.type;
    }

    public long getId() {
        return this.id;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return getClass().getName() + " [id=" + this.id + ", value='" + this.value + "']";
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r8) {
        /*
        r7 = this;
        r1 = 1;
        r2 = 0;
        if (r7 != r8) goto L_0x0006;
    L_0x0004:
        r2 = r1;
    L_0x0005:
        return r2;
    L_0x0006:
        if (r8 == 0) goto L_0x0005;
    L_0x0008:
        r3 = r7.getClass();
        r4 = r8.getClass();
        if (r3 != r4) goto L_0x0005;
    L_0x0012:
        r0 = r8;
        r0 = (com.thoughtworks.xstream.io.binary.Token) r0;
        r3 = r7.id;
        r5 = r0.id;
        r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r3 != 0) goto L_0x0005;
    L_0x001d:
        r3 = r7.type;
        r4 = r0.type;
        if (r3 != r4) goto L_0x0005;
    L_0x0023:
        r3 = r7.value;
        if (r3 == 0) goto L_0x0034;
    L_0x0027:
        r3 = r7.value;
        r4 = r0.value;
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x0032;
    L_0x0031:
        r1 = r2;
    L_0x0032:
        r2 = r1;
        goto L_0x0005;
    L_0x0034:
        r3 = r0.value;
        if (r3 != 0) goto L_0x0031;
    L_0x0038:
        goto L_0x0032;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.thoughtworks.xstream.io.binary.Token.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return (((this.type * 29) + ((int) (this.id ^ (this.id >>> 32)))) * 29) + (this.value != null ? this.value.hashCode() : 0);
    }

    protected void writeId(DataOutput out, long id, byte idType) throws IOException {
        if (id < 0) {
            throw new IOException("id must not be negative " + id);
        }
        switch (idType) {
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                out.writeByte(((byte) ((int) id)) - 128);
            case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                out.writeShort(((short) ((int) id)) - 32768);
            case Protocol.TRUNK_2 /*24*/:
                out.writeInt(((int) id) - Integer.MIN_VALUE);
            case Protocol.MERIT_INP /*32*/:
                out.writeLong(Long.MIN_VALUE + id);
            default:
                throw new Error("Unknown idType " + idType);
        }
    }

    protected void writeString(DataOutput out, String string) throws IOException {
        byte[] bytes = string.length() > 16383 ? string.getBytes("utf-8") : new byte[0];
        if (bytes.length <= MAX_UTF8_LENGTH) {
            out.writeUTF(string);
            return;
        }
        out.writeUTF(ID_SPLITTED);
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    protected long readId(DataInput in, byte idType) throws IOException {
        switch (idType) {
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return (long) (in.readByte() + Flags.FLAG8);
            case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                return (long) (in.readShort() - -32768);
            case Protocol.TRUNK_2 /*24*/:
                return (long) (in.readInt() - Integer.MIN_VALUE);
            case Protocol.MERIT_INP /*32*/:
                return in.readLong() - Long.MIN_VALUE;
            default:
                throw new Error("Unknown idType " + idType);
        }
    }

    protected String readString(DataInput in) throws IOException {
        String string = in.readUTF();
        if (!ID_SPLITTED.equals(string)) {
            return string;
        }
        byte[] bytes = new byte[in.readInt()];
        in.readFully(bytes);
        return new String(bytes, "utf-8");
    }
}
