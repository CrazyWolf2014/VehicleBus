package com.google.zxing.qrcode.decoder;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import org.xbill.DNS.KEYRecord;

public final class Mode {
    public static final Mode ALPHANUMERIC;
    public static final Mode BYTE;
    public static final Mode ECI;
    public static final Mode FNC1_FIRST_POSITION;
    public static final Mode FNC1_SECOND_POSITION;
    public static final Mode KANJI;
    public static final Mode NUMERIC;
    public static final Mode STRUCTURED_APPEND;
    public static final Mode TERMINATOR;
    private final int bits;
    private final int[] characterCountBitsForVersions;
    private final String name;

    static {
        TERMINATOR = new Mode(new int[]{0, 0, 0}, 0, "TERMINATOR");
        NUMERIC = new Mode(new int[]{10, 12, 14}, 1, "NUMERIC");
        ALPHANUMERIC = new Mode(new int[]{9, 11, 13}, 2, "ALPHANUMERIC");
        STRUCTURED_APPEND = new Mode(new int[]{0, 0, 0}, 3, "STRUCTURED_APPEND");
        BYTE = new Mode(new int[]{8, 16, 16}, 4, "BYTE");
        ECI = new Mode(null, 7, "ECI");
        KANJI = new Mode(new int[]{8, 10, 12}, 8, "KANJI");
        FNC1_FIRST_POSITION = new Mode(null, 5, "FNC1_FIRST_POSITION");
        FNC1_SECOND_POSITION = new Mode(null, 9, "FNC1_SECOND_POSITION");
    }

    private Mode(int[] iArr, int i, String str) {
        this.characterCountBitsForVersions = iArr;
        this.bits = i;
        this.name = str;
    }

    public static Mode forBits(int i) {
        switch (i) {
            case KEYRecord.OWNER_USER /*0*/:
                return TERMINATOR;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return NUMERIC;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return ALPHANUMERIC;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return STRUCTURED_APPEND;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return BYTE;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                return FNC1_FIRST_POSITION;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                return ECI;
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return KANJI;
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                return FNC1_SECOND_POSITION;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getBits() {
        return this.bits;
    }

    public int getCharacterCountBits(Version version) {
        if (this.characterCountBitsForVersions == null) {
            throw new IllegalArgumentException("Character count doesn't apply to this mode");
        }
        int versionNumber = version.getVersionNumber();
        versionNumber = versionNumber <= 9 ? 0 : versionNumber <= 26 ? 1 : 2;
        return this.characterCountBitsForVersions[versionNumber];
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}
